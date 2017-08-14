package zbp.rupbe.sightparser.authlayer;

import android.os.Parcel;

import org.spongycastle.util.encoders.Hex;
import zbp.rupbe.sightparser.pipeline.ByteBuf;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class KeyRequest extends CRCAuthLayerMessage {

    private byte[] randomBytes;
    private byte[] preMasterKey;

    @Override
    byte getCommand() {
        return 0x0C;
    }

    @Override
    byte[] getData() {
        ByteBuf byteBuf = new ByteBuf(288);
        byteBuf.putBytes(randomBytes);
        translateDate(byteBuf);
        byteBuf.putBytes(preMasterKey);
        return byteBuf.getBytes();
    }

    private void translateDate(ByteBuf byteBuf) {
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        byteBuf.putIntLE((year % 100 & 0x3f) << 26 | (month & 0x0f) << 22 | (day & 0x1f) << 17 | (hour & 0x1f) << 12 | (minute & 0x3f) << 6 | (second & 0x3f));
}

    public void setRandomBytes(byte[] randomBytes) {
        this.randomBytes = randomBytes;
    }

    public void setPreMasterKey(byte[] preMasterKey) {
        this.preMasterKey = preMasterKey;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByteArray(randomBytes);
        dest.writeByteArray(preMasterKey);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        randomBytes = new byte[28];
        preMasterKey = new byte[256];
        in.readByteArray(randomBytes);
        in.readByteArray(preMasterKey);
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
