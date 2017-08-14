package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class InfusionSetBlock extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.SHORT_LE)
    private short catheterFillAmount;
    @BlockParameter(order = 1, parameterType = BlockParameterType.SHORT_LE)
    private short tubingFillAmount;

    public float getCatheterFillAmount() {
        return ((float) catheterFillAmount) / 100F;
    }

    public void setCatheterFillAmount(float catheterFillAmount) {
        this.catheterFillAmount = (short) (catheterFillAmount * 100);
    }

    public short getTubingFillAmount() {
        return (short) (((float) tubingFillAmount) / 100F);
    }

    public void setTubingFillAmount(float tubingFillAmount) {
        this.tubingFillAmount = (short) (tubingFillAmount * 100);
    }

    @Override
    public short getBlockID() {
        return 0x297F;
    }
}
