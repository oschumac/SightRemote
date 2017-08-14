package zbp.rupbe.sightparser.applayer.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public enum PumpStatus implements Parcelable {

    STARTED((short) 0xE300),
    STOPPED((short) 0x1F00),
    PAUSED((short) 0xFC00);

    PumpStatus(short value) {
        this.value = value;
    }

    private short value;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PumpStatus> CREATOR = new Creator<PumpStatus>() {
        @Override
        public PumpStatus createFromParcel(Parcel in) {
            return PumpStatus.valueOf(in.readString());
        }

        @Override
        public PumpStatus[] newArray(int size) {
            return new PumpStatus[size];
        }
    };

    public short getValue() {
        return value;
    }

    public static PumpStatus getPumpStatus(short value) {
        for (PumpStatus pumpStatus : values()) if (pumpStatus.getValue() == value) return pumpStatus;
        return null;
    }
}
