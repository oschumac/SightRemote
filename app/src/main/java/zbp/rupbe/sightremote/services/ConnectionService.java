package zbp.rupbe.sightremote.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import zbp.rupbe.sightparser.ErrorType;
import zbp.rupbe.sightparser.InsightError;
import zbp.rupbe.sightparser.InsightPump;
import zbp.rupbe.sightparser.Message;
import zbp.rupbe.sightparser.Status;
import zbp.rupbe.sightparser.applayer.connection.AppDisconnect;
import zbp.rupbe.sightparser.applayer.statusreader.AppPumpStatus;
import zbp.rupbe.sightparser.authlayer.DisconnectRequest;
import zbp.rupbe.sightparser.pipeline.DelayCallback;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.InboundHandler;
import zbp.rupbe.sightparser.pipeline.handler.OutboundHandler;
import zbp.rupbe.sightremote.DataStorageImplementation;
import zbp.rupbe.sightremote.aidl.IConnectionService;
import zbp.rupbe.sightremote.aidl.IErrorCallback;
import zbp.rupbe.sightremote.aidl.IMessageCallback;
import zbp.rupbe.sightremote.aidl.IStatusCallback;

/**
 * Created by Tebbe Ubben on 07.07.2017.
 */

public class ConnectionService extends Service {

    private static final int synDelay = 5000;

    private Map<String, IErrorCallback> errorCallbacks = new HashMap<>();
    private Map<String, IStatusCallback> statusCallbacks = new HashMap<>();
    private Map<String, IMessageCallback> messageCallbacks = new HashMap<>();

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;

    private InsightPump insightPump;
    private DataStorageImplementation dataStorageImplementation = DataStorageImplementation.getInstance();
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean disconnect;
    private Status status = Status.DICONNECTED;

    private ExecutorThread executorThread;

    private Timer timer;

    private boolean autoReconnect;
    private final Handler handler = new Handler();
    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (executorThread == null) {
                executorThread = new ExecutorThread(PreferenceManager.getDefaultSharedPreferences(ConnectionService.this).getString("DEVICEMAC", null), false);
                executorThread.start();
            }
        }
    };
    private Runnable synRunnable = new Runnable() {
        @Override
        public void run() {
            if (executorThread != null && status == Status.CONNECTED) {
                executorThread.addSendObject(new AppPumpStatus());
            }
            handler.postDelayed(this, synDelay);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains("DEVICEMAC")) try {
            binder.connect(sharedPreferences.getString("DEVICEMAC", null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    private OutboundHandler statusCallback = new OutboundHandler<Status>() {
        @Override
        public void send(Pipeline pipeline, Status data) {
            if (data == Status.PAIRING_ESTABLISHED) autoReconnect = true;
            status = data;
            for (IStatusCallback statusCallback: statusCallbacks.values()) {
                try {
                    statusCallback.onStatus(data);
                } catch (RemoteException e) {
                }
            }
        }
    };

    private OutboundHandler errorCallback = new OutboundHandler<InsightError>() {
        @Override
        public void send(Pipeline pipeline, InsightError data) {
            if (data.getErrorType() == ErrorType.BROKEN_PIPE) try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (IErrorCallback errorCallback: errorCallbacks.values()) {
                try {
                    errorCallback.onError(data);
                } catch (RemoteException e) {
                }
            }
        }
    };

    private OutboundHandler outMessageCallback = new OutboundHandler<Message>() {
        @Override
        public void send(Pipeline pipeline, Message data) {
            for (IMessageCallback messageCallback : messageCallbacks.values()) {
                try {
                    if (!messageCallback.isOutbound()) continue;
                    if (Class.forName(messageCallback.getMessageClass()).isAssignableFrom(data.getClass())) messageCallback.onMessage(data);
                } catch (RemoteException e) {
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private InboundHandler inMessageCallback = new InboundHandler<Message>() {
        @Override
        public void receive(Pipeline pipeline, Message data) {
            for (IMessageCallback messageCallback : messageCallbacks.values()) {
                try {
                    if (!messageCallback.isInbound()) continue;
                    if (Class.forName(messageCallback.getMessageClass()).isAssignableFrom(data.getClass())) messageCallback.onMessage(data);
                } catch (RemoteException e) {
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private class ExecutorThread extends Thread {

        private List<Object> send = new ArrayList<>();

        private String mac;
        private boolean pairing;

        ExecutorThread(String mac, boolean pairing) {
            this.mac = mac;
            this.pairing = pairing;
        }

        @Override
        public void run() {
            try {
                bluetoothDevice = bluetoothAdapter.getRemoteDevice(mac);
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
                timer = new Timer();
                if (pairing) dataStorageImplementation.clear();
                insightPump = new InsightPump(dataStorageImplementation, delayCallback);
                insightPump.addHandlerFirst(statusCallback);
                insightPump.addHandlerFirst(errorCallback);
                insightPump.addHandlerFirst(inMessageCallback);
                insightPump.addHandlerFirst(outMessageCallback);
                statusCallback.send(null, Status.CONNECTING);
                bluetoothSocket.connect();
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
                if (pairing) insightPump.establishPairing(outputStream);
                else insightPump.connect(outputStream);
                handler.postDelayed(synRunnable, synDelay);
                while (bluetoothSocket.isConnected()) {
                    if (inputStream.available() > 0) {
                        byte[] bytes = new byte[inputStream.available()];
                        inputStream.read(bytes);
                        insightPump.parseBytes(bytes);
                    }
                    if (send.size() > 0) {
                        List<Object> toSend = new ArrayList<>(send);
                        send.clear();
                        for (Object object : toSend) insightPump.getPipeline().send(object);
                    }
                }
                timer.cancel();
                handler.removeCallbacksAndMessages(synRunnable);
            } catch (IOException e) {
                errorCallback.send(null, new InsightError(ErrorType.CANT_CONNECT, null));
                e.printStackTrace();
            } finally {
                bluetoothDevice = null;
                bluetoothSocket = null;
                timer = null;
                insightPump = null;
                inputStream = null;
                outputStream = null;
                executorThread = null;
                disconnect = false;
                if (!disconnect) statusCallback.send(null, Status.DICONNECTED);
                if (autoReconnect) handler.postDelayed(reconnectRunnable, 15000);
            }
        }

        public synchronized void addSendObject(Object object) {
            send.add(object);
        }
    };

    private void disconnect() {
        handler.removeCallbacksAndMessages(reconnectRunnable);
        autoReconnect = false;
        if (executorThread != null) {
            disconnect = true;
            executorThread.addSendObject(new AppDisconnect());
            executorThread.addSendObject(new DisconnectRequest());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (bluetoothSocket != null) bluetoothSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 1000);
        }
    }

    private final IConnectionService.Stub binder = new IConnectionService.Stub() {
        @Override
        public synchronized void establishPairing(String mac) throws RemoteException {
            executorThread = new ExecutorThread(mac, true);
            executorThread.start();
        }

        @Override
        public void connect(String mac) throws RemoteException {
            if (executorThread == null) {
                autoReconnect = true;
                handler.removeCallbacksAndMessages(reconnectRunnable);
                executorThread = new ExecutorThread(mac, false);
                executorThread.start();
            }
        }

        @Override
        public void disconnect() throws RemoteException {
            ConnectionService.this.disconnect();
        }

        @Override
        public void registerErrorCallback(String id, IErrorCallback errorCallback) throws RemoteException {
            errorCallbacks.put(id, errorCallback);
        }

        @Override
        public void registerStatusCallback(String id, IStatusCallback statusCallback) throws RemoteException {
            statusCallbacks.put(id, statusCallback);
        }

        @Override
        public void unregisterErrorCallback(String id) throws RemoteException {
            errorCallbacks.remove(id);
        }

        @Override
        public void registerMessageCallback(String id, IMessageCallback callback) throws RemoteException {
            messageCallbacks.put(id, callback);
        }

        @Override
        public void unregisterMessageCallback(String id) throws RemoteException {
            messageCallbacks.remove(id);
        }

        @Override
        public  void send(Message message) throws RemoteException {
            if (executorThread != null) executorThread.addSendObject(message);
        }

        @Override
        public Status getStatus() throws RemoteException {
            return status;
        }

        @Override
        public void unregisterStatusCallback(String id) throws RemoteException {
            statusCallbacks.remove(id);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    private DelayCallback delayCallback =  new DelayCallback() {
        @Override
        public void runDelayed(int milliSeconds, final Runnable runnable) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runnable.run();
                }
            }, milliSeconds);
        }
    };
}
