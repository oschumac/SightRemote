package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import java.util.ArrayList;
import java.util.List;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;
import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class BasalRateProfileBlock extends ParameterBlock {

    private BlockID basalRateProfileBlockID;

    @BlockParameter(order = 0, parameterType = BlockParameterType.BYTES, arrayLength = 48)
    private byte[] duration;
    @BlockParameter(order = 1, parameterType = BlockParameterType.BYTES, arrayLength = 48)
    private byte[] rate;

    public void setDuration(List<Integer> duration) {
        ByteBuf byteBuf = new ByteBuf(48);
        for (int d : duration) byteBuf.putShortLE((short) d);
        this.duration = byteBuf.getBytes();
    }

    public List<Integer> getDuration() {
        List<Integer> duration = new ArrayList<>();
        ByteBuf byteBuf = new ByteBuf(48);
        byteBuf.putBytes(this.duration);
        while (byteBuf.size() > 0) duration.add((int) byteBuf.readShortLE());
        return duration;
    }

    public void setRate(List<Integer> rate) {
        ByteBuf byteBuf = new ByteBuf(48);
        for (int d : rate) byteBuf.putShortLE((short) d);
        this.rate = byteBuf.getBytes();
    }

    public List<Integer> getRate() {
        List<Integer> rate = new ArrayList<>();
        ByteBuf byteBuf = new ByteBuf(48);
        byteBuf.putBytes(this.rate);
        while (byteBuf.size() > 0) rate.add((int) byteBuf.readShortLE());
        return rate;
    }

    public BlockID getBasalRateProfileBlockID() {
        return basalRateProfileBlockID;
    }

    public void setBasalRateProfileBlockID(BlockID basalRateProfileBlockID) {
        this.basalRateProfileBlockID = basalRateProfileBlockID;
    }

    @Override
    public short getBlockID() {
        return basalRateProfileBlockID.getValue();
    }

    public enum BlockID {
        BASAL_RATE_PROFILE_1((short) 0xE01B),
        BASAL_RATE_PROFILE_2((short) 0xFF1B),
        BASAL_RATE_PROFILE_3((short) 0x6C1D),
        BASAL_RATE_PROFILE_4((short) 0x731D),
        BASAL_RATE_PROFILE_5((short) 0x8F1D);

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
