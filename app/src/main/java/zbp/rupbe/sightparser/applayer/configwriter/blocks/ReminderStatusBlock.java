package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;
import zbp.rupbe.sightparser.applayer.descriptors.ReminderStatus;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class ReminderStatusBlock extends ParameterBlock {

    private BlockID reminderStatusBlockID;

    @BlockParameter(order = 0, parameterType = BlockParameterType.ENUM)
    private ReminderStatus reminderStatus;

    public void setReminderStatus(ReminderStatus reminderStatus) {
        this.reminderStatus = reminderStatus;
    }

    public ReminderStatus getReminderStatus() {
        return reminderStatus;
    }

    @Override
    public short getBlockID() {
        return reminderStatusBlockID.getValue();
    }

    @Override
    public void setBlockID(short blockID) {
        this.reminderStatusBlockID = BlockID.getBlockID(blockID);
    }

    public BlockID getReminderStatusBlockID() {
        return reminderStatusBlockID;
    }

    public void setReminderStatusBlockID(BlockID reminderStatusBlockID) {
        this.reminderStatusBlockID = reminderStatusBlockID;
    }

    public enum BlockID {
        CHANGE_INFUSION_SET_REMINDER((short) 0x2618),
        ALARM_CLOCK_REMINDER_1((short) 0xB605),
        ALARM_CLOCK_REMINDER_2((short) 0x6F05),
        ALARM_CLOCK_REMINDER_3((short) 0x7006),
        ALARM_CLOCK_REMINDER_4((short) 0x8C06),
        ALARM_CLOCK_REMINDER_5((short) 0x9306),
        BOLUS_DELIVERY_REMINDER_1((short) 0x1F00),
        BOLUS_DELIVERY_REMINDER_2((short) 0xE300),
        BOLUS_DELIVERY_REMINDER_3((short) 0xFC00),
        BOLUS_DELIVERY_REMINDER_4((short) 0x2503),
        BOLUS_DELIVERY_REMINDER_5((short) 0x3A03),
        MISSES_BOLUS_REMINDER_1((short) 0xC603),
        MISSES_BOLUS_REMINDER_2((short) 0xD903),
        MISSES_BOLUS_REMINDER_3((short) 0x4A05),
        MISSES_BOLUS_REMINDER_4((short) 0x5505),
        MISSES_BOLUS_REMINDER_5((short) 0xA905),
        SIGNALIZATION_PROFILE((short) 0x6B91);

        private short value;

        BlockID(short blockID) {
            this.value = blockID;
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
