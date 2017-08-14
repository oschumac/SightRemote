package zbp.rupbe.sightparser.applayer.connection;

import android.os.Parcel;

import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public class AppActivateService extends AppLayerMessage {

    private byte service;
    private short version;
    private byte[] servicePassword;

    @Override
    public Service getService() {
        return Service.CONNECTION;
    }

    @Override
    public short getCommand() {
        return (short) 0xF7F0;
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        service = in.readByte();
        version = (short) in.readInt();
        servicePassword = new byte[16];
        in.readByteArray(servicePassword);
    }

    @Override
    protected  void parse(Pipeline pipeline, ByteBuf data) {
        service = data.readByte();
        version = data.readShort();
    }

    @Override
    protected byte[] getData() {
        ByteBuf byteBuf = new ByteBuf(19);
        byteBuf.putByte(service);
        byteBuf.putShort(version);
        byteBuf.putBytes(servicePassword);
        return byteBuf.getBytes();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(service);
        dest.writeInt(version);
        dest.writeByteArray(servicePassword);
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public void setServicePassword(byte[] servicePassword) {
        this.servicePassword = servicePassword;
    }

    public short getVersion() {
        return version;
    }

    public byte getServiceID() {
        return service;
    }

    public void setService(byte service) {
        this.service = service;
    }

    public byte[] getServicePassword() {
        return servicePassword;
    }
}
