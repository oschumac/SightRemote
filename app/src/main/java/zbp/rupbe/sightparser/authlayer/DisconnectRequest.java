package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;

/**
 * Created by Tebbe Ubben on 04.07.2017.
 */
public class DisconnectRequest extends AuthLayerMessage {

    @Override
    byte getCommand() {
        return 0x1B;
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
