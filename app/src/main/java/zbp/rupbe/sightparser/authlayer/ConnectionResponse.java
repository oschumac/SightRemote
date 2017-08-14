package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class ConnectionResponse extends CRCAuthLayerMessage {

    @Override
    byte getCommand() {
        return 0x0A;
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
