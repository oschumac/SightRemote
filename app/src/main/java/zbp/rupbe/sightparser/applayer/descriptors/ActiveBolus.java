package zbp.rupbe.sightparser.applayer.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public class ActiveBolus implements Parcelable {

    private short bolusID;
    private BolusType bolusType;
    private float initialAmount;
    private float leftoverAmount;
    private int delay;

    public static final Creator<ActiveBolus> CREATOR = new Creator<ActiveBolus>() {
        @Override
        public ActiveBolus createFromParcel(Parcel in) {
            ActiveBolus activeBolus = new ActiveBolus();
            activeBolus.bolusID = (short) in.readInt();
            activeBolus.bolusType = in.readParcelable(BolusType.class.getClassLoader());
            activeBolus.initialAmount = in.readFloat();
            activeBolus.leftoverAmount = in.readFloat();
            activeBolus.delay = in.readInt();
            return activeBolus;
        }

        @Override
        public ActiveBolus[] newArray(int size) {
            return new ActiveBolus[size];
        }
    };

    public static ActiveBolus parse(ByteBuf byteBuf) {
        ActiveBolus activeBolus = new ActiveBolus();
        activeBolus.bolusID = byteBuf.readShort();
        activeBolus.bolusType = BolusType.getBolusType(byteBuf.readShort());
        byteBuf.shift(2); //Unknown enum
        byteBuf.shift(2); //Unknown value
        activeBolus.initialAmount = ((float) byteBuf.readShortLE()) / 100F;
        activeBolus.leftoverAmount = ((float) byteBuf.readShortLE()) / 100F;
        activeBolus.delay = byteBuf.readShortLE();
        return activeBolus;
    }

    public short getBolusID() {
        return bolusID;
    }

    public BolusType getBolusType() {
        return bolusType;
    }

    public float getInitialAmount() {
        return initialAmount;
    }

    public float getLeftoverAmount() {
        return leftoverAmount;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bolusID);
        dest.writeParcelable(bolusType, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeFloat(initialAmount);
        dest.writeFloat(leftoverAmount);
        dest.writeInt(delay);
    }
}
