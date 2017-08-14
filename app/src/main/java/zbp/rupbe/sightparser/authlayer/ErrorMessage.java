package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;
import android.os.Parcelable;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 01.07.2017.
 */
public class ErrorMessage extends AuthLayerMessage {

    private AuthLayerErrorMessage errorMessage;

    @Override
    byte getCommand() {
        return 0x06;
    }

    @Override
    byte[] getData() {
        return new byte[] {errorMessage.getErrorCode()};
    }

    @Override
    void parse(ByteBuf byteBuf) {
        this.errorMessage = AuthLayerErrorMessage.parseErrorMessage(byteBuf.readByte());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(errorMessage, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        errorMessage = in.readParcelable(AuthLayerErrorMessage.class.getClassLoader());
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
