package zbp.rupbe.sightparser.authlayer;

import zbp.rupbe.sightparser.crypto.CRC;
import zbp.rupbe.sightparser.pipeline.ByteBuf;

import java.math.BigInteger;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public abstract class CRCAuthLayerMessage extends AuthLayerMessage {

    @Override
    public ByteBuf serialize(BigInteger nonce, int commID, byte[] key) {
        byte[] data = getData();
        short dataLength = (short) (data.length + 2);
        short length = (short) (29 + dataLength);
        ByteBuf byteBuf = new ByteBuf(length + 8);
        byteBuf.putBytes(MAGIC_HEADER);
        byteBuf.putShortLE(length);
        byteBuf.putShortLE((short) ~length);
        byteBuf.putByte(VERSION);
        byteBuf.putByte(getCommand());
        byteBuf.putShortLE(dataLength);
        byteBuf.putIntLE(commID);
        byteBuf.putBytes(getNonceBytes(nonce));
        byteBuf.putBytes(data);
        byteBuf.putShortLE((short) CRC.calculateCRC(byteBuf.getBytes(8, length - 10)));
        byteBuf.putBytes((byte) 0x00, 8);
        return byteBuf;
    }

}
