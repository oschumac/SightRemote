package zbp.rupbe.sightremote;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import zbp.rupbe.sightremote.aidl.IConnectionService;
import zbp.rupbe.sightremote.services.ConnectionService;

/**
 * Created by Tebbe Ubben on 04.07.2017.
 */

public class SightRemote extends Application {

    private static SightRemote instance;

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, ConnectionService.class));
        instance = this;
    }

    public static SightRemote getInstance() {
        return instance;
    }
}
