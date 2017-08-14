package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;
import android.util.Log;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.Message;
import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class DataMessage extends AuthLayerMessage {

    private byte[] data;

    @Override
    byte getCommand() {
        return 0x03;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    void parse(ByteBuf byteBuf) {
        data = byteBuf.getBytes();
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(data.length);
        dest.writeByteArray(data);
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
