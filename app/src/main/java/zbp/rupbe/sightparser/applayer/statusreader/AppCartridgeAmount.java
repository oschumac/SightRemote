package zbp.rupbe.sightparser.applayer.statusreader;

import android.os.Parcel;

import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public class AppCartridgeAmount extends AppLayerMessage {

    private float cartridgeAmount;

    @Override
    public Class getMessageClass() {
        return getClass();
    }

    @Override
    public Service getService() {
        return Service.STATUSREADER;
    }

    @Override
    public short getCommand() {
        return 0x3A03;
    }

    @Override
    protected void parse(Pipeline pipeline, ByteBuf data) {
        data.shift(2); //Unknown value
        data.shift(2); //Unknown enum
        data.shift(2); //Unknown enum
        cartridgeAmount = ((float) data.readShortLE()) / 100F;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(cartridgeAmount);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        cartridgeAmount = in.readFloat();
    }

    public float getCartridgeAmount() {
        return cartridgeAmount;
    }
}
