package zbp.rupbe.sightparser.applayer.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public enum BolusType implements Parcelable {

    INSTANT((short) 0xE300),
    EXTENDED((short) 0xFC00),
    MULTIWAVE((short) 0x2503);

    private short value;

    BolusType(short value) {
        this.value = value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BolusType> CREATOR = new Creator<BolusType>() {
        @Override
        public BolusType createFromParcel(Parcel in) {
            return valueOf(in.readString());
        }

        @Override
        public BolusType[] newArray(int size) {
            return new BolusType[size];
        }
    };

    public static BolusType getBolusType(short value) {
        for (BolusType bolusType : values()) if (bolusType.value == value) return bolusType;
        return null;
    }

    public short getValue() {
        return value;
    }
}
