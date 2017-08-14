package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class FactoryInfusionSetLimit extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.SHORT_LE)
    private short maximumCatheterAmount;
    @BlockParameter(order = 1, parameterType = BlockParameterType.SHORT_LE)
    private short maximumTubingAmount;

    public float getMaximumCatheterAmount() {
        return ((float) maximumCatheterAmount) / 100F;
    }

    public void setMaximumCatheterAmount(float maximumCatheterAmount) {
        this.maximumCatheterAmount = (short) (maximumCatheterAmount * 100);
    }

    public float getMaximumTubingAmount() {
        return ((float) maximumTubingAmount) / 100F;
    }

    public void setMaximumTubingAmount(short maximumTubingAmount) {
        this.maximumTubingAmount = (short) (maximumTubingAmount * 100);
    }

    @Override
    public short getBlockID() {
        return (short) 0xB728;
    }
}
