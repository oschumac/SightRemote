package zbp.rupbe.sightparser.applayer.statusreader;

import android.os.Parcel;

import zbp.rupbe.sightparser.applayer.CRCAppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.PumpStatus;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public class AppPumpStatus extends CRCAppLayerMessage {

    private PumpStatus pumpStatus;

    @Override
    protected void parseData(Pipeline pipeline, ByteBuf data) {
        pumpStatus = PumpStatus.getPumpStatus(data.readShort());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(pumpStatus, PARCELABLE_WRITE_RETURN_VALUE);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        pumpStatus = in.readParcelable(PumpStatus.class.getClassLoader());
    }

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
        return (short) 0xFC00 ;
    }

    public PumpStatus getPumpStatus() {
        return pumpStatus;
    }

    @Override
    protected boolean inCRC() {
        return true;
    }
}
