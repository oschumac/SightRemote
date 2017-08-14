package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;

import org.spongycastle.crypto.encodings.OAEPEncoding;
import org.spongycastle.crypto.engines.RSAEngine;
import zbp.rupbe.sightparser.crypto.CRC;
import zbp.rupbe.sightparser.crypto.Cryptograph;
import zbp.rupbe.sightparser.crypto.KeyPair;
import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class KeyResponse extends CRCAuthLayerMessage {

    private byte[] randomData;
    private byte[] preMasterSecret;

    @Override
    byte getCommand() {
        return 0x11;
    }

    @Override
    public void parse(ByteBuf byteBuf) {
        randomData = byteBuf.readBytes(28);
        byteBuf.shift(4); //Date
        preMasterSecret = byteBuf.readBytes(256);
    }

    public byte[] getRandomData() {
        return randomData;
    }

    public byte[] getPreMasterSecret() {
        return preMasterSecret;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByteArray(randomData);
        dest.writeByteArray(preMasterSecret);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        randomData = new byte[28];
        preMasterSecret = new byte[256];
        in.readByteArray(randomData);
        in.readByteArray(preMasterSecret);
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
