package zbp.rupbe.sightremote.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import zbp.rupbe.sightparser.InsightError;
import zbp.rupbe.sightparser.Message;
import zbp.rupbe.sightparser.Status;
import zbp.rupbe.sightremote.R;
import zbp.rupbe.sightremote.adapters.BluetoothDeviceAdapter;
import zbp.rupbe.sightremote.aidl.IConnectionService;
import zbp.rupbe.sightremote.aidl.IErrorCallback;
import zbp.rupbe.sightremote.aidl.IMessageCallback;
import zbp.rupbe.sightremote.aidl.IStatusCallback;
import zbp.rupbe.sightremote.services.ConnectionService;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewFlipper viewFlipper;
    private CheckBox agreement;
    private RecyclerView deviceList;
    private ImageView connectingAnimation;
    private TextView status;
    private TextView errorMessage;
    private Button back;
    private Button next;
    private Button custom;
    private ImageView confirmTutorial;

    private AnimatedVectorDrawableCompat confirmTutorialDrawable;
    private AnimatedVectorDrawableCompat connectingAnimationDrawable;

    private boolean confirmTutorialStarted;
    private boolean connectingAnimationStarted;

    private ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private BluetoothDeviceAdapter bluetoothDeviceAdapter;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private IConnectionService iConnectionService;

    private String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        agreement = (CheckBox) findViewById(R.id.agreement);
        deviceList = (RecyclerView) findViewById(R.id.device_list);
        connectingAnimation = (ImageView) findViewById(R.id.connecting_anim);
        status = (TextView) findViewById(R.id.status);
        errorMessage = (TextView) findViewById(R.id.error_message);
        back = (Button) findViewById(R.id.back);
        next = (Button) findViewById(R.id.next);
        custom = (Button) findViewById(R.id.custom);
        confirmTutorial = (ImageView) findViewById(R.id.confirm_tutorial);


        back.setOnClickListener(this);
        next.setOnClickListener(this);
        custom.setOnClickListener(this);
        agreement.setOnClickListener(this);

        confirmTutorialDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.confirm_anim);
        confirmTutorialDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                if (confirmTutorialStarted) confirmTutorialDrawable.start();
            }
        });
        confirmTutorial.setImageDrawable(confirmTutorialDrawable);

        connectingAnimationDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.connecting_anim);
        connectingAnimationDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                if (connectingAnimationStarted) connectingAnimationDrawable.start();
            }
        });
        connectingAnimation.setImageDrawable(connectingAnimationDrawable);

        deviceList.setLayoutManager(new LinearLayoutManager(this));
        bluetoothDeviceAdapter = new BluetoothDeviceAdapter(bluetoothDevices);
        bluetoothDeviceAdapter.setOnClickListener(deviceListListener);
        deviceList.setAdapter(bluetoothDeviceAdapter);

        IntentFilter receiverFilter = new IntentFilter();
        receiverFilter.addAction(BluetoothDevice.ACTION_FOUND);
        receiverFilter.addAction(BluetoothDevice.ACTION_UUID);
        receiverFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        receiverFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, receiverFilter);

        showView(0, false, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ConnectionService.class);
        intent.setAction(IConnectionService.class.getName());
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    private View.OnClickListener deviceListListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = deviceList.getChildAdapterPosition(v);
            status.setText(R.string.connecting);
            showView(3, false, true);
            try {
                if (bluetoothAdapter.isDiscovering()) bluetoothAdapter.cancelDiscovery();
                BluetoothDevice device = bluetoothDevices.get(pos);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    BluetoothDevice.class.getMethod("removeBond").invoke(device);
                }
                connectionService.establishPairing(mac = device.getAddress());
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v == back) {
            int current = viewFlipper.getDisplayedChild();
            if (current == 1) showView(0, true, true);
            else if (current == 2) showView(1, true, true);
            else if (current == 3) {
                showView(1, true, true);
                cancelPairing();
            }
            else if (current == 4) {
                showView(1, true, true);
                cancelPairing();
            }
            else if (current == 5) showView(1, true, true);
        } else if (v == next) {
            int current = viewFlipper.getDisplayedChild();
            if (current == 6) {
                finish();
                return;
            }
            if (current == 0) showView(1, false, true);
            else if (current == 1) showView(2, false, true);
        } else if (v == custom) {
            startDiscovery();
            bluetoothDevices.clear();
            bluetoothDeviceAdapter.notifyDataSetChanged();
        } else if (v == agreement) {
            next.setEnabled(agreement.isChecked());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void stopConfirmTutorial() {
        confirmTutorialStarted = false;
        confirmTutorialDrawable.stop();
    }

    private void startConfirmTutorial() {
        confirmTutorialStarted = true;
        confirmTutorialDrawable.start();
    }

    private void startConnectingAnimation() {
        connectingAnimationStarted = true;
        connectingAnimationDrawable.start();
    }

    private void stopConnectingAnimation() {
        connectingAnimationStarted = false;
        connectingAnimationDrawable.stop();
    }

    private void showView(int position, boolean backAnim, boolean anim) {
        if (position == viewFlipper.getDisplayedChild()) return;
        if (anim) {
            if (position == 2) startDiscovery();
            else if (bluetoothAdapter.isDiscovering()) bluetoothAdapter.cancelDiscovery();
        }
        if (position == 3) startConnectingAnimation();
        else stopConnectingAnimation();
        if (position == 4) startConfirmTutorial();
        else stopConfirmTutorial();
        if (position == 0) {
            back.setVisibility(View.GONE);
            custom.setVisibility(View.GONE);
            next.setEnabled(agreement.isChecked());
            next.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            back.setVisibility(View.VISIBLE);
            custom.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        } else if (position == 2) {
            back.setVisibility(View.VISIBLE);
            custom.setText(R.string.searching);
            custom.setEnabled(false);
            custom.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);
            bluetoothDevices.clear();
            bluetoothDeviceAdapter.notifyDataSetChanged();
        } else if (position == 3) {
            back.setVisibility(View.VISIBLE);
            custom.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        } else if (position == 4) {
            back.setVisibility(View.VISIBLE);
            custom.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        } else if (position == 5) {
            back.setVisibility(View.VISIBLE);
            custom.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        } else if (position == 6) {
            back.setVisibility(View.GONE);
            custom.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
            next.setEnabled(true);
            next.setText(R.string.exit);
        }
        if (anim) {
            if (backAnim) {
                viewFlipper.setInAnimation(this, R.anim.slide_in_to_right);
                viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);
            } else {
                viewFlipper.setInAnimation(this, R.anim.slide_in_to_left);
                viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
            }
        }
        viewFlipper.setDisplayedChild(position);
    }

    private void cancelPairing() {
        try {
            connectionService.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static final int PERMISSION_REQUEST_ID = 41785;

    private void startDiscovery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this).setTitle(R.string.location_permission_required)
                    .setMessage(R.string.location_permission_required_desc)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SetupActivity.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_ID);
                        }
                    }).show();
        } else {
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_ID && grantResults[0] == PackageManager.PERMISSION_GRANTED) bluetoothAdapter.startDiscovery();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                custom.setText(R.string.searching);
                custom.setEnabled(false);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                custom.setText(R.string.retry);
                custom.setEnabled(true);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device == null) return;
                if (device.getName().startsWith("PUMP32") && !bluetoothDevices.contains(device)) {
                    bluetoothDevices.add(device);
                    bluetoothDeviceAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private IErrorCallback errorCallback = new IErrorCallback() {
        @Override
        public void onError(final InsightError error) throws RemoteException {
            cancelPairing();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String displayMessage = error.getErrorType().name();
                    if (error.getErrorMessage() != null) displayMessage += " (" + error.getErrorMessage() + ")";
                    errorMessage.setText(displayMessage);
                    showView(5, false, true);
                }
            });
        }

        @Override
        public IBinder asBinder() {
            return connectionService.asBinder();
        }
    };

    private IStatusCallback statusCallback = new IStatusCallback() {
        @Override
        public void onStatus(final Status status1) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (status1 == Status.CONFIRMING) showView(4, false, true);
                    else if (status1 == Status.BIND) {
                        showView(3, false, true);
                        status.setText(R.string.binding);
                    }
                    else if (status1 == Status.PAIRING_ESTABLISHED) {
                        showView(6, false, true);
                        getSharedPreferences("zbp.rupbe.sightremote.SETUP", Context.MODE_PRIVATE).edit().putBoolean("SETUPRUN", true).commit();
                        PreferenceManager.getDefaultSharedPreferences(SetupActivity.this).edit().putString("DEVICEMAC", mac).commit();
                    }
                    else if (status1 == Status.DICONNECTED) {
                        cancelPairing();
                        errorMessage.setText(R.string.disconnected);
                        showView(5, false, true);
                    }
                    else if (status1 == Status.KEY_EXCHANGE) status.setText(R.string.key_exchange);
                }
            });
        }

        @Override
        public IBinder asBinder() {
            return connectionService.asBinder();
        }
    };

    private IConnectionService connectionService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            connectionService = IConnectionService.Stub.asInterface(service);
            try {
                connectionService.registerErrorCallback("zbp.rupbe.sightremote.SetupActivity.ERRORCALLBACK", errorCallback);
                connectionService.registerStatusCallback("zbp.rupbe.sightremote.SetupActivity.STATUSCALLBACK", statusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                connectionService.unregisterErrorCallback("zbp.rupbe.sightremote.SetupActivity.ERRORCALLBACK");
                connectionService.unregisterErrorCallback("zbp.rupbe.sightremote.SetupActivity.STATUSCALLBACK");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
}
