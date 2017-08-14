package zbp.rupbe.sightremote.aidl;

import zbp.rupbe.sightremote.aidl.IMessageCallback;
import zbp.rupbe.sightremote.aidl.IStatusCallback;
import zbp.rupbe.sightremote.aidl.IErrorCallback;
import zbp.rupbe.sightparser.Message;
import zbp.rupbe.sightparser.Status;

interface IConnectionService {
    void establishPairing(String mac);
    void connect(String mac);
    void disconnect();
    void registerStatusCallback(String id, IStatusCallback callback);
    void unregisterStatusCallback(String id);
    void registerErrorCallback(String id, IErrorCallback callback);
    void unregisterErrorCallback(String id);
    void registerMessageCallback(String id, IMessageCallback callback);
    void unregisterMessageCallback(String id);
    void send(in Message message);
    Status getStatus();
}