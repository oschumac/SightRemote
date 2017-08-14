package zbp.rupbe.sightparser.applayer.statusreader;

import android.os.Parcel;

import zbp.rupbe.sightparser.applayer.CRCAppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public class AppCurrentBasal extends CRCAppLayerMessage {

    private String currentBasalName;
    private float currentBasalAmount;

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
        return (short) 0xA905;
    }

    @Override
    protected boolean inCRC() {
        return true;
    }

    @Override
    protected  void parseData(Pipeline pipeline, ByteBuf data) {
        data.shift(2); //Unknown enum
        currentBasalName = data.readUTF8(62);
        currentBasalAmount = ((float) data.readShortLE()) / 100F;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(currentBasalName);
        dest.writeFloat(currentBasalAmount);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        currentBasalName = in.readString();
        currentBasalAmount = in.readFloat();
    }

    public String getCurrentBasalName() {
        return currentBasalName;
    }

    public float getCurrentBasalAmount() {
        return currentBasalAmount;
    }
}
