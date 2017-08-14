package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class ConnectionRequest extends CRCAuthLayerMessage {

    @Override
    byte getCommand() {
        return 0x09;
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
