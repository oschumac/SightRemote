package zbp.rupbe.sightparser.applayer.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tebbe Ubben on 30.06.2017.
 */
public enum Service implements Parcelable {

    CONNECTION((byte) 0x00, (short) 0, null),
    STATUSREADER((byte) 0x0F, (short) 0x0100, null),
    CONFIGREADER((byte) 0x33, (short) 0x0200, null),
    CONFIGWRITER((byte) 0x55, (short) 0x0200, "u+5Fhz6Gw4j1Kkas"),
    INSULINCONTROL((byte) 0x66, (short) 0x0100, null); //SERVICE PASSWORD UNKNOWN

    private byte serviceByte;
    private short serviceVersion;
    private String servicePassword;

    Service(byte serviceByte, short serviceVersion, String servicePassword) {
        this.serviceByte = serviceByte;
        this.serviceVersion = serviceVersion;
        this.servicePassword = servicePassword;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return valueOf(in.readString());
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public byte getServiceID() {
        return serviceByte;
    }

    public short getServiceVersion() {
        return serviceVersion;
    }

    public String getServicePassword() {
        return servicePassword;
    }

    public static Service getService(byte id) {
        for (Service service : values()) if (service.getServiceID() == id) return service;
        return null;
    }
}
