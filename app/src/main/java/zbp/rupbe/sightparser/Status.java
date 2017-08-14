package zbp.rupbe.sightparser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tebbe Ubben on 30.06.2017.
 */
public enum Status implements Parcelable {

    CONNECTING,
    KEY_EXCHANGE,
    CONFIRMING,
    BIND,
    PAIRING_ESTABLISHED,
    DICONNECTED,
    CONNECTED;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return valueOf(in.readString());
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
}
