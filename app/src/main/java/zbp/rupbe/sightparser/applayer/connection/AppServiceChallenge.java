package zbp.rupbe.sightparser.applayer.connection;

import android.os.Parcel;

import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public class AppServiceChallenge extends AppLayerMessage {

    private byte service;
    private byte[] randomData;
    private short version;

    @Override
    protected byte[] getData() {
        ByteBuf byteBuf = new ByteBuf(19);
        byteBuf.putByte(service);
        byteBuf.putShort(version);
        byteBuf.putBytes(randomData);
        return byteBuf.getBytes();
    }

    @Override
    public Service getService() {
        return Service.CONNECTION;
    }

    @Override
    public short getCommand() {
        return (short) 0xD2F3;
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }

    @Override
    protected void parse(Pipeline pipeline, ByteBuf data) {
        randomData = data.readBytes(16);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        service = in.readByte();
        randomData = new byte[16];
        in.readByteArray(randomData);
        version = (short) in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(service);
        dest.writeByteArray(randomData);
        dest.writeInt(version);
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public void setService(byte service) {
        this.service = service;
    }

    public byte[] getRandomData() {
        return randomData;
    }
}
