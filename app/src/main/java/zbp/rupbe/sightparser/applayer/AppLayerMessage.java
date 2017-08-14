package zbp.rupbe.sightparser.applayer;

import android.os.Parcel;
import android.util.Log;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.ErrorType;
import zbp.rupbe.sightparser.InsightError;
import zbp.rupbe.sightparser.Message;
import zbp.rupbe.sightparser.applayer.configwriter.AppCloseWriteSession;
import zbp.rupbe.sightparser.applayer.configwriter.AppOpenWriteSession;
import zbp.rupbe.sightparser.applayer.configwriter.AppReadConfigBlock;
import zbp.rupbe.sightparser.applayer.configwriter.AppReadReminderSnooze;
import zbp.rupbe.sightparser.applayer.configwriter.AppReadReminderValue;
import zbp.rupbe.sightparser.applayer.configwriter.AppWriteConfigBlock;
import zbp.rupbe.sightparser.applayer.configwriter.AppWriteReminderSnooze;
import zbp.rupbe.sightparser.applayer.configwriter.AppWriteReminderValue;
import zbp.rupbe.sightparser.applayer.connection.AppActivateService;
import zbp.rupbe.sightparser.applayer.connection.AppBind;
import zbp.rupbe.sightparser.applayer.connection.AppConnect;
import zbp.rupbe.sightparser.applayer.connection.AppDisconnect;
import zbp.rupbe.sightparser.applayer.connection.AppServiceChallenge;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.applayer.statusreader.AppActiveBoluses;
import zbp.rupbe.sightparser.applayer.statusreader.AppBatteryAmount;
import zbp.rupbe.sightparser.applayer.statusreader.AppCartridgeAmount;
import zbp.rupbe.sightparser.applayer.statusreader.AppCurrentBasal;
import zbp.rupbe.sightparser.applayer.statusreader.AppCurrentTBR;
import zbp.rupbe.sightparser.applayer.statusreader.AppPumpStatus;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tebbe Ubben on 30.06.2017.
 */
public abstract class AppLayerMessage extends Message {

    static final byte VERSION = 0x20;
    private static Map<Byte, Map<Short, Class<? extends AppLayerMessage>>> TYPES = new HashMap<>();

    protected short error;

    static {
        Map<Short, Class<? extends AppLayerMessage>> connectionMessages = new HashMap<>();
        connectionMessages.put((short) 0xCDF3, AppBind.class);
        connectionMessages.put((short) 0x0BF0, AppConnect.class);
        connectionMessages.put((short) 0x14F0, AppDisconnect.class);
        connectionMessages.put((short) 0xD2F3, AppServiceChallenge.class);
        connectionMessages.put((short) 0xF7F0, AppActivateService.class);
        TYPES.put(Service.CONNECTION.getServiceID(), connectionMessages);

        Map<Short, Class<? extends AppLayerMessage>> statusMessages = new HashMap<>();
        statusMessages.put((short) 0xFC00, AppPumpStatus.class);
        statusMessages.put((short) 0xA905, AppCurrentBasal.class);
        statusMessages.put((short) 0x3A03, AppCartridgeAmount.class);
        statusMessages.put((short) 0x2503, AppBatteryAmount.class);
        statusMessages.put((short) 0xB605, AppCurrentTBR.class);
        statusMessages.put((short) 0x6F06, AppActiveBoluses.class);
        TYPES.put(Service.STATUSREADER.getServiceID(), statusMessages);

        Map<Short, Class<? extends AppLayerMessage>> configWriterMessages = new HashMap<>();
        configWriterMessages.put((short) 0x491E, AppOpenWriteSession.class);
        configWriterMessages.put((short) 0xB51E, AppCloseWriteSession.class);
        configWriterMessages.put((short) 0x561E, AppReadConfigBlock.class);
        configWriterMessages.put((short) 0xAA1E, AppWriteConfigBlock.class);
        configWriterMessages.put((short) 0x561E, AppReadReminderSnooze.class);
        configWriterMessages.put((short) 0xDE8F, AppWriteReminderSnooze.class);
        configWriterMessages.put((short) 0x2194, AppReadReminderValue.class);
        configWriterMessages.put((short) 0xB292, AppWriteReminderValue.class);
        TYPES.put(Service.CONFIGWRITER.getServiceID(), configWriterMessages);
    }

    protected byte[] getData() {
        return new byte[0];
    }

    public abstract Service getService();

    public abstract short getCommand();

    public byte[] serialize() {
        byte[] data = getData();
        if (data == null) data = new byte[0];
        ByteBuf byteBuf = new ByteBuf(6 + data.length);
        byteBuf.putByte(VERSION);
        byteBuf.putByte(getService().getServiceID());
        byteBuf.putShort(getCommand());
        byteBuf.putBytes(data);
        return byteBuf.getBytes();
    }

    protected void parse(Pipeline pipeline, ByteBuf data) {
    }

    protected boolean isError() {
        return error != 0x0000;
    }

    public static void deserialize(Pipeline pipeline, ByteBuf byteBuf) throws IllegalAccessException, InstantiationException {
        byte version = byteBuf.readByte();
        byte service = byteBuf.readByte();
        short command = byteBuf.readShort();
        short error = byteBuf.readShort();
        byte[] data = byteBuf.readBytes();
        if (version != VERSION) {
            pipeline.send(new InsightError(ErrorType.INVALID_APP_VERSION, version + "," + VERSION));
            return;
        }
        AppLayerMessage message = TYPES.get(service).get(command).newInstance();
        message.error = error;
        if (message.isError()) {
            pipeline.send(new InsightError(ErrorType.APP_ERROR, message.getClass().getSimpleName() + ": " + Integer.toHexString(error)));
            return;
        }
        ByteBuf dataBuf = new ByteBuf(data.length);
        dataBuf.putBytes(data);
        message.parse(pipeline, dataBuf);
        pipeline.receive(message);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(error);
    }

    @Override
    public void readFromParcel(Parcel in) {
        error = (short) in.readInt();
    }
}
