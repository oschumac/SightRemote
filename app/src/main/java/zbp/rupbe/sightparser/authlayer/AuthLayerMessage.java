package zbp.rupbe.sightparser.authlayer;

import android.util.Log;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.ErrorType;
import zbp.rupbe.sightparser.InsightError;
import zbp.rupbe.sightparser.Message;
import zbp.rupbe.sightparser.crypto.CRC;
import zbp.rupbe.sightparser.crypto.Cryptograph;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public abstract class AuthLayerMessage extends Message {

    public static final byte[] MAGIC_HEADER = new byte[] {(byte) 0x88, (byte) 0xCC, (byte) 0xEE, (byte) 0xFF};
    static final byte VERSION = 0x20;

    private static final Map<Byte, Class<? extends AuthLayerMessage>> TYPES = new HashMap<>();

    static {
        TYPES.put((byte) 0x09, ConnectionRequest.class);
        TYPES.put((byte) 0x0A, ConnectionResponse.class);
        TYPES.put((byte) 0x0C, KeyRequest.class);
        TYPES.put((byte) 0x11, KeyResponse.class);
        TYPES.put((byte) 0x12, VerifyDisplayRequest.class);
        TYPES.put((byte) 0x14, VerifyDisplayResponse.class);
        TYPES.put((byte) 0x0E, VerifyConfirmRequest.class);
        TYPES.put((byte) 0x1E, VerifyConfirmResponse.class);
        TYPES.put((byte) 0x17, SynRequest.class);
        TYPES.put((byte) 0x18, SynAckResponse.class);
        TYPES.put((byte) 0x03, DataMessage.class);
        TYPES.put((byte) 0x06, ErrorMessage.class);
        TYPES.put((byte) 0x1B, DisconnectRequest.class);
    }

    abstract byte getCommand();

    byte[] getData() {
        return new byte[0];
    }

    public ByteBuf serialize(BigInteger nonce, int commID, byte[] key) {
        byte[] data = getData();
        byte[] nonceBytes = getNonceBytes(nonce);
        byte[] dataEncrypted = encryptDecrypt(data, key, nonceBytes);
        short dataLength = (short) dataEncrypted.length;
        short length = (short) (29 + dataLength);
        ByteBuf byteBuf = new ByteBuf(length + 8);
        byteBuf.putBytes(MAGIC_HEADER);
        byteBuf.putShortLE(length);
        byteBuf.putShortLE((short) ~length);
        byteBuf.putByte(VERSION);
        byteBuf.putByte(getCommand());
        byteBuf.putShortLE(dataLength);
        byteBuf.putIntLE(commID);
        byteBuf.putBytes(nonceBytes);
        byteBuf.putBytes(dataEncrypted);
        byteBuf.putBytes(calculateTrailer(byteBuf.getBytes(8, 21), byteBuf.getBytes(16, 13), data, key));
        return byteBuf;
    }

    public static void deserialize(ByteBuf data, Pipeline pipeline) throws IllegalAccessException, InstantiationException {
        data.shift(4); //MAGIC HEADER
        int packetLength = data.readShortLE();
        int packetLengthXOR = ~data.readShortLE();
        byte[] crcContent = data.getBytes(packetLength - 10);
        byte[] header = data.getBytes(21);
        byte version = data.readByte();
        byte command = data.readByte();
        int dataLength = data.readShortLE();
        int commID = data.readIntLE();
        byte[] nonceTrailer = data.getBytes(13);
        byte[] nonce = data.readBytesLE(13);
        byte[] payload = data.readBytes(dataLength);
        byte[] trailer = data.readBytes(8);
        Class clazz = TYPES.get(command);
        boolean crcPacket = CRCAuthLayerMessage.class.isAssignableFrom(clazz);
        BigInteger nonceInt = new BigInteger(nonce);
        if (KeyResponse.class.isAssignableFrom(clazz)) {
            pipeline.setCommID(commID);
        }
        if (packetLength != packetLengthXOR) {
            pipeline.send(new InsightError(ErrorType.PACKET_LENGTH_XOR_WRONG, packetLength + "," + packetLengthXOR));
        } else if (version != VERSION) {
            pipeline.send(new InsightError(ErrorType.INVALID_AUTH_VERSION, version + "," + VERSION));
        } else if(pipeline.getLastNonceReceived().intValue() != 0 & nonceInt.compareTo(pipeline.getLastNonceReceived()) != 1) {
            pipeline.send(new InsightError(ErrorType.NONCE_ERROR, Hex.toHexString(nonce) + "," + Hex.toHexString(pipeline.getLastNonceReceived().toByteArray())));
        } else if (!ConnectionResponse.class.isAssignableFrom(clazz) && pipeline.getCommID() != commID) {
            pipeline.send(new InsightError(ErrorType.COMMM_ID_ERROR, commID + "," + pipeline.getCommID()));
        } else {
            boolean integrityVerified;
            if (crcPacket) {
                byte[] crcBytes = new byte[2];
                byte[] payloadWithoutCRC = new byte[dataLength - 2];
                System.arraycopy(payload, dataLength - 2, crcBytes, 0, 2);
                System.arraycopy(payload, 0, payloadWithoutCRC, 0, dataLength - 2);
                payload = payloadWithoutCRC;
                short crc = (short) (crcBytes[0] & 0xFF |
                        crcBytes[1]  << 8);
                short calculatedCRC = (short) CRC.calculateCRC(crcContent);
                integrityVerified = crc == calculatedCRC;
                if (!integrityVerified) {
                    pipeline.send(new InsightError(ErrorType.COULD_NOT_VERIFY_CRC, crc + "," + calculatedCRC));
                }
            } else {
                payload = Cryptograph.encryptDataCTR(payload, pipeline.getDerivedKeys().getIncomingKey(), nonceTrailer);
                byte[] calculatedTrailer = calculateTrailer(header, nonceTrailer, payload, pipeline.getDerivedKeys().getIncomingKey());
                integrityVerified = Arrays.equals(trailer, calculatedTrailer);
                if (!integrityVerified) {
                    pipeline.send(new InsightError(ErrorType.COULD_NOT_VERIFY_INTEGRITY, Hex.toHexString(trailer) + "," + Hex.toHexString(calculatedTrailer)));
                }
            }
            if (integrityVerified) {
                AuthLayerMessage message = (AuthLayerMessage) clazz.newInstance();
                ByteBuf byteBuf = new ByteBuf(payload.length);
                byteBuf.putBytes(payload);
                message.parse(byteBuf);
                pipeline.receive(message);
                pipeline.setLastNonceReceived(nonceInt);
            }
        }
    }

    private static byte[] calculateTrailer(byte[] header, byte[] nonce, byte[] payload, byte[] key) {
        return Cryptograph.produceCCMTag(nonce, payload, header, key);
    }

    private static byte[] encryptDecrypt(byte[] data, byte[] key, byte[] nonce) {
        return Cryptograph.encryptDataCTR(data, key, nonce);
    }

    static byte[] getNonceBytes(BigInteger nonce) {
        byte[] bytes = nonce.toByteArray();
        ByteBuf byteBuf = new ByteBuf(13);
        byteBuf.putBytesLE(bytes);
        byteBuf.putBytes((byte) 0x00, 13 - bytes.length);
        return byteBuf.getBytes();
    }

    void parse(ByteBuf byteBuf) {

    }

}
