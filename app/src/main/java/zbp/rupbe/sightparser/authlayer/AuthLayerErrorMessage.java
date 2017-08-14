package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tebbe Ubben on 04.07.2017.
 */
public enum AuthLayerErrorMessage implements Parcelable {

    UNKNOWN((byte) 0x0000);

    private String errorMessage;
    private byte errorCode;

    AuthLayerErrorMessage(byte errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(errorCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuthLayerErrorMessage> CREATOR = new Creator<AuthLayerErrorMessage>() {
        @Override
        public AuthLayerErrorMessage createFromParcel(Parcel in) {
            return parseErrorMessage(in.readByte());
        }

        @Override
        public AuthLayerErrorMessage[] newArray(int size) {
            return new AuthLayerErrorMessage[size];
        }
    };

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public byte getErrorCode() {
        return errorCode;
    }

    public static AuthLayerErrorMessage parseErrorMessage(byte b) {
        for (AuthLayerErrorMessage errorMessage : values()) {
            if (errorMessage.errorCode == b) return errorMessage;
        }
        return UNKNOWN;
    }
}
