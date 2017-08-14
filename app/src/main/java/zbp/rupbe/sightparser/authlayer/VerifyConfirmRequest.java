package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class VerifyConfirmRequest extends AuthLayerMessage {

    private PairingStatus pairingStatus;

    @Override
    byte getCommand() {
        return 0x0E;
    }

    @Override
    byte[] getData() {
        return pairingStatus.getValue();
    }

    public void setPairingStatus(PairingStatus pairingStatus) {
        this.pairingStatus = pairingStatus;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(pairingStatus, PARCELABLE_WRITE_RETURN_VALUE);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        pairingStatus = in.readParcelable(PairingStatus.class.getClassLoader());
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
