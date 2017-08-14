package zbp.rupbe.sightparser.applayer.statusreader;

import android.os.Parcel;

import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public class AppBatteryAmount extends AppLayerMessage {

    private int batteryAmount;

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
        return 0x2503;
    }

    @Override
    protected void parse(Pipeline pipeline, ByteBuf data) {
        data.shift(2); //Unkown enum
        batteryAmount = data.readShortLE();
        data.shift(2); //Unkown enum
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(batteryAmount);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        batteryAmount = in.readInt();
    }

    public int getBatteryAmount() {
        return batteryAmount;
    }
}
