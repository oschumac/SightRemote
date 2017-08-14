package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public enum PairingStatus implements Parcelable {

    CONFIRMED (new byte[] {(byte) 0x3B, (byte) 0x2E}),
    REJECTED (new byte[] {(byte) 0xAA, (byte) 0x1E}),
    PENDING (new byte[] {(byte) 0x93, (byte) 0x06});

    private byte[] value;

    PairingStatus(byte[] value) {
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

    public static final Creator<PairingStatus> CREATOR = new Creator<PairingStatus>() {
        @Override
        public PairingStatus createFromParcel(Parcel in) {
            return valueOf(in.readString());
        }

        @Override
        public PairingStatus[] newArray(int size) {
            return new PairingStatus[size];
        }
    };

    public byte[] getValue() {
        return value;
    }
}
