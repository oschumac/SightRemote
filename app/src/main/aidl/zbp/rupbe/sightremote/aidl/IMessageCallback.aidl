package zbp.rupbe.sightremote.aidl;

import zbp.rupbe.sightparser.Message;

interface IMessageCallback {
    void onMessage(in Message message);
    boolean isInbound();
    boolean isOutbound();
    String getMessageClass();
}