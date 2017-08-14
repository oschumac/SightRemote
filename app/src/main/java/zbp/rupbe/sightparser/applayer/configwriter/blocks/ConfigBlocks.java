package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import java.util.HashMap;
import java.util.Map;

import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public abstract class ConfigBlocks {

    public static final Map<Short, Class<? extends ParameterBlock>> BLOCKS = new HashMap<>();

    static {
        BLOCKS.put((short) 0x4697, ActiveAcousticModeBlock.class);
        BLOCKS.put((short) 0x901D, ActiveBasalRateProfileBlock.class);
        BLOCKS.put((short) 0x9791, ActiveSignalisationProfileBlock.class);
        BLOCKS.put((short) 0x4754, AutoOffBlock.class);
        BLOCKS.put((short) 0xC518, BlindBolusStepSizeBlock.class);
        BLOCKS.put((short) 0x3676, CartridgeAmountWarningBlock.class);
        BLOCKS.put((short) 0xC963, ChangeInfusionSetReminderValueBlock.class);
        BLOCKS.put((short) 0x7C7A, DeliverVelocityBlock.class);
        BLOCKS.put((short) 0xEC67, DisplayOrientationBlock.class);
        BLOCKS.put((short) 0xB728, FactoryInfusionSetLimit.class);
        BLOCKS.put((short) 0x297F, InfusionSetBlock.class);
        BLOCKS.put((short) 0xF367, ScreenBackgroundColor.class);
        BLOCKS.put((short) 0xEC67, ScreenOrientationBlock.class);
        BLOCKS.put((short) 0x5854, SignalisationProfileBlock.class);
        BLOCKS.put((short) 0x637A, TimeFormatBlock.class);
        for (BasalRateProfileBlock.BlockID id : BasalRateProfileBlock.BlockID.values()) BLOCKS.put(id.getValue(), BasalRateProfileBlock.class);
        for (CustomBolusAmountBlock.BolusID id : CustomBolusAmountBlock.BolusID.values()) BLOCKS.put(id.getValue(), CustomBolusAmountBlock.class);
        for (CustomTBRValueBlock.BlockID id : CustomTBRValueBlock.BlockID.values()) BLOCKS.put(id.getValue(), CustomTBRValueBlock.class);
        for (LimitBlock.BlockID id : LimitBlock.BlockID.values()) BLOCKS.put(id.getValue(), LimitBlock.class);
        for (NameBlock.BlockID id : NameBlock.BlockID.values()) BLOCKS.put(id.getValue(), NameBlock.class);
        for (ReminderStatusBlock.BlockID id : ReminderStatusBlock.BlockID.values()) BLOCKS.put(id.getValue(), ReminderStatusBlock.class);
        for (ReminderValueBlock.BlockID id : ReminderValueBlock.BlockID.values()) BLOCKS.put(id.getValue(), ReminderValueBlock.class);
    }

}
