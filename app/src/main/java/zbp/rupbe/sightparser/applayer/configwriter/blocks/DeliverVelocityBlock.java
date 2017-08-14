package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.DeliverVelocity;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class DeliverVelocityBlock extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.ENUM)
    private DeliverVelocity deliverVelocity;

    public DeliverVelocity getDeliverVelocity() {
        return deliverVelocity;
    }

    public void setDeliverVelocity(DeliverVelocity deliverVelocity) {
        this.deliverVelocity = deliverVelocity;
    }

    @Override
    public short getBlockID() {
        return 0x7C7A;
    }
}
