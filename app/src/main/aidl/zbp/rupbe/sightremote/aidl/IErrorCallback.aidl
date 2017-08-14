package zbp.rupbe.sightremote.aidl;

import zbp.rupbe.sightparser.InsightError;

interface IErrorCallback {
    void onError(in InsightError error);
}