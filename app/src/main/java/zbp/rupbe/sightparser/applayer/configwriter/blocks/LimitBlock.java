package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public class LimitBlock extends ParameterBlock {

    private BlockID limitBlockID;

    @BlockParameter(order = 0, parameterType = BlockParameterType.SHORT_LE)
    private short amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = (short) amount;
    }

    public BlockID getLimitBlockID() {
        return limitBlockID;
    }

    public void setLimitBlockID(BlockID limitBlockID) {
        this.limitBlockID = limitBlockID;
    }

    @Override
    public short getBlockID() {
        return limitBlockID.getValue();
    }


    public enum BlockID {
        FACTORY_MINIMUM_BOLUS_AMOUNT((short) 0x17EB),
        FACTORY_MAXIMUM_BOLUS_AMOUNT((short) 0x06A1),
        FACTORY_MINIMUM_BASAL_RATE_AMOUNT((short) 0xEBEB),
        FACTORY_MAXIMUM_BASAL_RATE_AMOUNT((short) 0x19A1),
        FACTORY_CARTRIDGE_WARNING_AMOUNT((short) 0xFAA1),
        USER_MAXIMUM_BOLUS_AMOUNT((short) 0x1F00);

        private short value;

        BlockID(short value) {
            this.value = value;
        }

        public short getValue() {
            return value;
        }

        public static BlockID getLimitBlockID(short value) {
            for (BlockID blockID : values()) if (blockID.getValue() == value) return blockID;
            return null;
        }
    }
}
