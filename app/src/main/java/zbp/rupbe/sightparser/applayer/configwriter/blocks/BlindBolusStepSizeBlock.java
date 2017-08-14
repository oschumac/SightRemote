package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.BolusStepSize;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public class BlindBolusStepSizeBlock extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.ENUM)
    private BolusStepSize stepSize;

    public BolusStepSize getStepSize() {
        return stepSize;
    }

    public void setStepSize(BolusStepSize stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    public short getBlockID() {
        return (short) 0xC518;
    }
}
