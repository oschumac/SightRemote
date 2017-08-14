package zbp.rupbe.sightparser.applayer.statusreader;

import android.os.Parcel;

import zbp.rupbe.sightparser.applayer.CRCAppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public class AppCurrentTBR extends CRCAppLayerMessage {

    private int percentage;
    private int leftoverTime;
    private int initialTime;

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
        return (short) 0xB605;
    }

    @Override
    protected boolean inCRC() {
        return true;
    }

    @Override
    protected void parseData(Pipeline pipeline, ByteBuf data) {
        percentage = data.readShortLE();
        leftoverTime = data.readShortLE();
        initialTime = data.readShortLE();
        data.shift(2); //Unknown enum
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(percentage);
        dest.writeInt(leftoverTime);
        dest.writeInt(initialTime);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        percentage = in.readInt();
        leftoverTime = in.readInt();
        initialTime = in.readInt();
    }

    public int getPercentage() {
        return percentage;
    }

    public int getLeftoverTime() {
        return leftoverTime;
    }

    public int getInitialTime() {
        return initialTime;
    }
}
