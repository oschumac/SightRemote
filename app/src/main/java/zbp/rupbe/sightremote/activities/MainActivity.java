package zbp.rupbe.sightremote.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import zbp.rupbe.sightparser.Status;
import zbp.rupbe.sightremote.R;
import zbp.rupbe.sightremote.aidl.IConnectionService;
import zbp.rupbe.sightremote.aidl.IMessageCallback;
import zbp.rupbe.sightremote.aidl.IStatusCallback;
import zbp.rupbe.sightremote.fragments.BaseFragment;
import zbp.rupbe.sightremote.fragments.StatusFragment;
import zbp.rupbe.sightremote.services.ConnectionService;

/**
 * Created by Tebbe Ubben on 07.07.2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int UPDATE_DELAY = 3000;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private Map<String, IMessageCallback> callbacks = new HashMap<>();
    private Snackbar connectionStatusSnackbar;
    private BaseFragment fragment;
    private Handler handler = new Handler();
    private Runnable autoUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (connectionService != null && connectionService.getStatus() == Status.CONNECTED && fragment != null && fragment.autoUpdate()) fragment.update();
            } catch (RemoteException e) {
            }
            handler.postDelayed(this, UPDATE_DELAY);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!setupRun()) {
            Intent intent = new Intent(this, SetupActivity.class);
            startActivity(intent);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_navigation, R.string.close_navigation);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();

        connectionStatusSnackbar = Snackbar.make(drawerLayout, R.string.connecting, Snackbar.LENGTH_INDEFINITE);

        if (savedInstanceState == null) startFragment(new StatusFragment());
        else fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        handler.postDelayed(autoUpdateRunnable, UPDATE_DELAY);
    }

    private void startFragment(BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        this.fragment = fragment;
        try {
            if (connectionService != null && connectionService.getStatus() == Status.CONNECTED) fragment.sendMessages();
        } catch (RemoteException e) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, ConnectionService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    private boolean setupRun() {
        return getSharedPreferences("zbp.rupbe.sightremote.SETUP", Context.MODE_PRIVATE).getBoolean("SETUPRUN", false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    private IStatusCallback statusCallback = new IStatusCallback() {
        @Override
        public void onStatus(final Status status) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (status == Status.CONNECTED) {
                        if (connectionStatusSnackbar.isShown()) connectionStatusSnackbar.dismiss();
                        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        fragment.sendMessages();
                    } else if (status == Status.DICONNECTED) {
                        if (connectionStatusSnackbar.isShown()) connectionStatusSnackbar.dismiss();
                        connectionStatusSnackbar = Snackbar.make(drawerLayout, R.string.disconnected, Snackbar.LENGTH_INDEFINITE);
                        connectionStatusSnackbar.setAction(R.string.connect, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    connectionService.connect(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("DEVICEMAC", null));
                                } catch (RemoteException e) {
                                }
                            }
                        });
                        connectionStatusSnackbar.show();
                    } else if (status == Status.CONNECTING) {
                        if (connectionStatusSnackbar.isShown()) connectionStatusSnackbar.dismiss();
                        connectionStatusSnackbar = Snackbar.make(drawerLayout, R.string.connecting, Snackbar.LENGTH_INDEFINITE);
                        connectionStatusSnackbar.show();
                    }
                }
            });
        }

        @Override
        public IBinder asBinder() {
            return connectionService.asBinder();
        }
    };

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        } else finish();
    }

    private IConnectionService connectionService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            connectionService = IConnectionService.Stub.asInterface(service);
            for (Map.Entry<String, IMessageCallback> entry : callbacks.entrySet()) {
                try {
                    connectionService.registerMessageCallback(entry.getKey(), entry.getValue());
                } catch (RemoteException e) {
                }
            }
            try {
                statusCallback.onStatus(connectionService.getStatus());
                connectionService.registerStatusCallback("zbp.rupbe.sightremote.activites.MainActivity.STATUSCALLBACK", statusCallback);
            } catch (RemoteException e) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            for (String id : callbacks.keySet()) {
                try {
                    connectionService.unregisterMessageCallback(id);
                } catch (RemoteException e) {
                }
            }
            try {
                connectionService.unregisterStatusCallback("zbp.rupbe.sightremote.activites.MainActivity.STATUSCALLBACK");
            } catch (RemoteException e) {
            }
            connectionService = null;
        }
    };

    public void registerMessageCallback(String id, IMessageCallback messageCallback) {
        callbacks.put(id, messageCallback);
        if (connectionService != null) try {
            connectionService.registerMessageCallback(id, messageCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unregisterMessageCallback(String id) {
        callbacks.remove(id);
        if (connectionService != null) try {
            connectionService.unregisterMessageCallback(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public IConnectionService getConnectionService() {
        return connectionService;
    }
}
