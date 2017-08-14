package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class CartridgeAmountWarningBlock extends ParameterBlock {

    @BlockParameter(order = 1, parameterType = BlockParameterType.BOOLEAN)
    private boolean automatic;
    @BlockParameter(order = 1, parameterType = BlockParameterType.SHORT_LE)
    private short amount;

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    public float getAmount() {
        return ((float) amount) / 100F;
    }

    public void setAmount(float amount) {
        this.amount = (short) (amount * 100);
    }

    @Override
    public short getBlockID() {
        return 0x3676;
    }
}
