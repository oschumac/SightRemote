package zbp.rupbe.sightparser.applayer.statusreader;

import android.os.Parcel;

import zbp.rupbe.sightparser.applayer.CRCAppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.applayer.descriptors.ActiveBolus;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public class AppActiveBoluses extends CRCAppLayerMessage {

    private ActiveBolus bolus1;
    private ActiveBolus bolus2;
    private ActiveBolus bolus3;


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
        return 0x6F06;
    }

    @Override
    protected boolean inCRC() {
        return true;
    }

    @Override
    protected void parseData(Pipeline pipeline, ByteBuf data) {
        bolus1 = ActiveBolus.parse(data);
        bolus2 = ActiveBolus.parse(data);
        bolus3 = ActiveBolus.parse(data);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(bolus1, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(bolus2, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(bolus3, PARCELABLE_WRITE_RETURN_VALUE);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        bolus1 = in.readParcelable(ActiveBolus.class.getClassLoader());
        bolus2 = in.readParcelable(ActiveBolus.class.getClassLoader());
        bolus3 = in.readParcelable(ActiveBolus.class.getClassLoader());
    }

    public ActiveBolus getBolus1() {
        return bolus1;
    }

    public ActiveBolus getBolus2() {
        return bolus2;
    }

    public ActiveBolus getBolus3() {
        return bolus3;
    }
}
