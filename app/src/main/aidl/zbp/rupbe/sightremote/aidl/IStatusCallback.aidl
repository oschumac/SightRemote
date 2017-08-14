package zbp.rupbe.sightremote.aidl;

import zbp.rupbe.sightparser.Status;

interface IStatusCallback {
    void onStatus(in Status status);
}