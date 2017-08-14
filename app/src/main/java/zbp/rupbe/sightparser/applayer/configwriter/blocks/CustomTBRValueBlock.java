package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public class CustomTBRValueBlock extends ParameterBlock {

    private BlockID customTBRBlockID;

    @BlockParameter(order = 0, parameterType = BlockParameterType.SHORT_LE)
    private short percentage;
    @BlockParameter(order = 0, parameterType = BlockParameterType.SHORT_LE)
    private short duration;
    @BlockParameter(order = 0, parameterType = BlockParameterType.BOOLEAN)
    private boolean configured;

    @Override
    public short getBlockID() {
        return customTBRBlockID.getValue();
    }

    public void setCustomTBRBlockID(BlockID customTBRBlockID) {
        this.customTBRBlockID = customTBRBlockID;
    }

    public BlockID getCustomTBRBlockID() {
        return customTBRBlockID;
    }

    @Override
    public void setBlockID(short blockID) {
        customTBRBlockID = BlockID.getBlockID(blockID);
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = (short) percentage;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = (short) duration;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public enum BlockID {

        CUSTOM_TBR_1((short) 0x561E),
        CUSTOM_TBR_2((short) 0xAA1E),
        CUSTOM_TBR_3((short) 0xB51E),
        CUSTOM_TBR_4((short) 0x4B28),
        CUSTOM_TBR_5((short) 0x5428);

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
