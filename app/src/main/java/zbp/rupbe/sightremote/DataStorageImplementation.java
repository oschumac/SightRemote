package zbp.rupbe.sightremote;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import zbp.rupbe.sightparser.DataStorage;

/**
 * Created by Tebbe Ubben on 07.07.2017.
 */

public class DataStorageImplementation implements DataStorage {

    private static DataStorageImplementation instance;

    public static DataStorageImplementation getInstance() {
        if (instance == null) instance = new DataStorageImplementation();
        return instance;
    }

    private DataStorageImplementation() {

    }

    private SharedPreferences sharedPreferences = SightRemote.getInstance().getSharedPreferences("zbp.rupbe.sightremote.DataStorageImplementation", Context.MODE_PRIVATE);

    @Override
    public String get(String key) {
        return sharedPreferences.getString(key, null);
    }

    @Override
    public void set(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    @Override
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public void clear() {
        sharedPreferences.edit().clear().commit();
    }
}
