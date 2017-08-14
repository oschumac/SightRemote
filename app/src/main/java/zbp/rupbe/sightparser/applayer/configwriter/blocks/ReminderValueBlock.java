package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.Melody;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public class ReminderValueBlock extends ParameterBlock {

    private BlockID blockID;

    @BlockParameter(order = 0, parameterType = BlockParameterType.SHORT_LE)
    private short time;
    @BlockParameter(order = 1, parameterType = BlockParameterType.ENUM)
    private Melody melody;

    public void setTime(int hour, int minute) {
        time = (short) (hour * 60 + minute);
    }

    public int getHour() {
        return (time - time % 60) / 60;
    }

    public int getMinute() {
        return time % 60;
    }

    public Melody getMelody() {
        return melody;
    }

    public void setMelody(Melody melody) {
        this.melody = melody;
    }

    @Override
    public short getBlockID() {
        return blockID.getValue();
    }

    @Override
    public void setBlockID(short blockID) {
        this.blockID = BlockID.getBlockID(blockID);
    }

    public void setBlockID(BlockID blockID) {
        this.blockID = blockID;
    }

    public enum BlockID {
        DELIVER_BOLUS_REMINDER_1((short) 0xBB54),
        DELIVER_BOLUS_REMINDER_2((short) 0x6257),
        DELIVER_BOLUS_REMINDER_3((short) 0x7D57),
        DELIVER_BOLUS_REMINDER_4((short) 0x8157),
        DELIVER_BOLUS_REMINDER_5((short) 0x9E57),
        MISSES_BOLUS_REMINDER_1((short) 0x6061),
        MISSES_BOLUS_REMINDER_2((short) 0x7F61),
        MISSES_BOLUS_REMINDER_3((short) 0x8361),
        MISSES_BOLUS_REMINDER_4((short) 0x9C61),
        MISSES_BOLUS_REMINDER_5((short) 0x4562),
        ALARM_CLOCK_REMINDER_1((short) 0x5A62),
        ALARM_CLOCK_REMINDER_2((short) 0xA662),
        ALARM_CLOCK_REMINDER_3((short) 0xB962),
        ALARM_CLOCK_REMINDER_4((short) 0x2A64),
        ALARM_CLOCK_REMINDER_5((short) 0x3564);

        private short value;

        BlockID(short value) {
            this.value = value;
        }

        public short getValue() {
            return value;
        }

        public static BlockID getBlockID(short value) {
            for (BlockID blockID : values()) if (blockID.getValue() == value) return blockID;
            return null;
        }
    }
}
