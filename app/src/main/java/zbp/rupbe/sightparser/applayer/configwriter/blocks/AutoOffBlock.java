package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class AutoOffBlock extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.BOOLEAN)
    private boolean enabled;
    @BlockParameter(order = 1, parameterType = BlockParameterType.SHORT_LE)
    private short timeout;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = (short) timeout;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public short getBlockID() {
        return 0x4754;
    }
}
