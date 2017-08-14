package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.AcousticMode;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class ActiveAcousticModeBlock extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.ENUM)
    private AcousticMode acousticMode;

    public AcousticMode getAcousticMode() {
        return acousticMode;
    }

    public void setAcousticMode(AcousticMode acousticMode) {
        this.acousticMode = acousticMode;
    }

    @Override
    public short getBlockID() {
        return 0x4679;
    }
}
