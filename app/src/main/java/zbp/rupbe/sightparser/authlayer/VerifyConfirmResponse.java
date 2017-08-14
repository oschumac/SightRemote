package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;
import android.util.Pair;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

import java.util.Arrays;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class VerifyConfirmResponse extends AuthLayerMessage {

    private PairingStatus pairingStatus;

    @Override
    byte getCommand() {
        return 0x1E;
    }

    @Override
    public void parse(ByteBuf byteBuf) {
        byte[] bytes = byteBuf.readBytes(2);
        if (Arrays.equals(PairingStatus.CONFIRMED.getValue(), bytes)) pairingStatus = PairingStatus.CONFIRMED;
        else if (Arrays.equals(PairingStatus.REJECTED.getValue(), bytes)) pairingStatus = PairingStatus.REJECTED;
        else if (Arrays.equals(PairingStatus.PENDING.getValue(), bytes)) pairingStatus = PairingStatus.PENDING;
    }

    public PairingStatus getPairingStatus() {
        return pairingStatus;
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
