package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import java.io.UnsupportedEncodingException;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;
import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public class NameBlock extends ParameterBlock {

    private BlockID blockID;

    @BlockParameter(order = 0, parameterType = BlockParameterType.BYTES, arrayLength = 42)
    private byte[] name;

    @Override
    public short getBlockID() {
        return blockID.getValue();
    }

    @Override
    public void setBlockID(short blockID) {
        this.blockID = BlockID.getNameBlockID(blockID);
    }

    public String getName() {
        try {
            return new String(name, "UTF-8").replace(Character.toString((char) 0), "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setName(String name) {
        try {
            if (name.length() > 42) name = name.substring(0, 41);
            ByteBuf byteBuf = new ByteBuf(42);
            for (byte b : name.getBytes("UTF-8")) byteBuf.putShortLE(b);
            byteBuf.putBytes((byte) 0, 42 - byteBuf.size());
            this.name = byteBuf.getBytes();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setBlockID(BlockID blockID) {
        this.blockID = blockID;
    }

    public enum BlockID {
        CUSTOM_BOLUS_1((short) 0x43C3),
        CUSTOM_BOLUS_2((short) 0x5CC3),
        CUSTOM_BOLUS_3((short) 0xA0C3),
        CUSTOM_BOLUS_4((short) 0xBFC3),
        CUSTOM_BOLUS_5((short) 0x2CC5),
        CUSTOM_BOLUS_6((short) 0x33C5),
        CUSTOM_BOLUS_7((short) 0xC5C5),
        CUSTOM_BOLUS_8((short) 0xD0C5),
        CUSTOM_BOLUS_9((short) 0x09C6),
        CUSTOM_BOLUS_10((short) 0x16C6),
        DELIVER_BOLUS_REMINDER_1((short) 0xB0A4),
        DELIVER_BOLUS_REMINDER_2((short) 0x69A7),
        DELIVER_BOLUS_REMINDER_3((short) 0x76A7),
        DELIVER_BOLUS_REMINDER_4((short) 0x8AA7),
        DELIVER_BOLUS_REMINDER_5((short) 0x95A7),
        MISSED_BOLUS_REMINDER_1((short) 0x20B9),
        MISSED_BOLUS_REMINDER_2((short) 0x3FB9),
        MISSED_BOLUS_REMINDER_3((short) 0xC3B9),
        MISSED_BOLUS_REMINDER_4((short) 0xDCB9),
        MISSED_BOLUS_REMINDER_5((short) 0x05BA),
        ALARM_CLOCK_BOLUS_REMINDER_1((short) 0x1ABA),
        ALARM_CLOCK_BOLUS_REMINDER_2((short) 0xE6BA),
        ALARM_CLOCK_BOLUS_REMINDER_3((short) 0xF9BA),
        ALARM_CLOCK_BOLUS_REMINDER_4((short) 0x6ABC),
        ALARM_CLOCK_BOLUS_REMINDER_5((short) 0x75BC),
        CUSTOM_TBR_1((short) 0xB3BF),
        CUSTOM_TBR_2((short) 0x66C0),
        CUSTOM_TBR_3((short) 0x79C0),
        CUSTOM_TBR_4((short) 0x85C0),
        CUSTOM_TBR_5((short) 0x9AC0),
        BASAL_RATE_PROFILE_1((short) 0x89BC),
        BASAL_RATE_PROFILE_2((short) 0x96BC),
        BASAL_RATE_PROFILE_3((short) 0x4FBF),
        BASAL_RATE_PROFILE_4((short) 0x50BF),
        BASAL_RATE_PROFILE_5((short) 0xACBF);

        private short value;

        BlockID(short blockID) {
            this.value = blockID;
        }

        public short getValue() {
            return value;
        }

        public static BlockID getNameBlockID(short value) {
            for (BlockID blockID : values()) if (blockID.getValue() == value) return blockID;
            return null;
        }
    }
}
