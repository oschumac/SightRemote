package zbp.rupbe.sightparser.applayer.connection;

import android.os.Parcel;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 04.07.2017.
 */
public class AppBind extends AppLayerMessage {

    private byte[] modelNumber;

    @Override
    public Service getService() {
        return Service.CONNECTION;
    }

    @Override
    protected byte[] getData() {
        return Hex.decode("3438310000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
    }

    @Override
    protected void parse(Pipeline pipeline, ByteBuf data) {
        modelNumber = data.readBytesLE(16);
    }

    @Override
    public short getCommand() {
        return (short) 0xCDF3;
    }

    public byte[] getModelNumber() {
        return modelNumber;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(modelNumber.length);
        dest.writeByteArray(modelNumber);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        modelNumber = new byte[in.readInt()];
        in.readByteArray(modelNumber);
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
