package zbp.rupbe.sightremote.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import zbp.rupbe.sightremote.activities.MainActivity;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public abstract class BaseFragment extends Fragment {

    public MainActivity getMainActivity() {
        if (getActivity() == null) return null;
        return (MainActivity) getActivity();
    }

    public void sendMessages() {
        update();
    }

    public boolean autoUpdate() {
        return false;
    }

    public void update() {

    }
}
