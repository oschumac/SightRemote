package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class VerifyDisplayRequest extends AuthLayerMessage {

    @Override
    byte getCommand() {
        return 0x12;
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
