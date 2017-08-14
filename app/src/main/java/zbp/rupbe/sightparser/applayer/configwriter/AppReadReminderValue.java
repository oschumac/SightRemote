package zbp.rupbe.sightparser.applayer.configwriter;

import android.os.Parcel;

import zbp.rupbe.sightparser.applayer.CRCAppLayerMessage;
import zbp.rupbe.sightparser.applayer.configwriter.blocks.ConfigBlocks;
import zbp.rupbe.sightparser.applayer.configwriter.blocks.ReminderValueBlock;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public class AppReadReminderValue extends CRCAppLayerMessage {

    private short blockID;
    private ReminderValueBlock parameterBlock;

    @Override
    public Class getMessageClass() {
        return getClass();
    }

    @Override
    public Service getService() {
        return Service.CONFIGWRITER;
    }

    @Override
    public short getCommand() {
        return 0x2194;
    }

    @Override
    protected boolean inCRC() {
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(blockID);
        byte[] serial = parameterBlock == null ? new byte[0] : parameterBlock.serialize(ConfigBlocks.BLOCKS.get(blockID)).getBytes();
        dest.writeInt(serial.length);
        dest.writeByteArray(serial);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        blockID = (short) in.readInt();
        int length = in.readInt();
        if (length != 0) {
            byte[] serial = new byte[length];
            in.readByteArray(serial);
            ByteBuf byteBuf = new ByteBuf(serial.length);
            parameterBlock = (ReminderValueBlock) ParameterBlock.deserialize(ConfigBlocks.BLOCKS.get(blockID), byteBuf);
            parameterBlock.setBlockID(blockID);
        }
    }

    @Override
    protected void parseData(Pipeline pipeline, ByteBuf data) {
        blockID = data.readShort();
        parameterBlock = (ReminderValueBlock) ParameterBlock.deserialize(ConfigBlocks.BLOCKS.get(blockID), data);
    }

    public ReminderValueBlock getParameterBlock() {
        return parameterBlock;
    }

    public void setBlockID(ReminderValueBlock.BlockID blockID) {
        this.blockID = blockID.getValue();
    }

    public short getBlockID() {
        return blockID;
    }
}
