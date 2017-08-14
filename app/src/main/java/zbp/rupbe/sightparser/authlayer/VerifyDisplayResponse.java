package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class VerifyDisplayResponse extends AuthLayerMessage {

    @Override
    byte getCommand() {
        return 0x14;
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
