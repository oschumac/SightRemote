package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.ActiveBasalRateProfile;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class ActiveBasalRateProfileBlock extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.ENUM)
    private ActiveBasalRateProfile activeProfile;

    public ActiveBasalRateProfile getActiveProfile() {
        return activeProfile;
    }

    public void setActiveProfile(ActiveBasalRateProfile activeProfile) {
        this.activeProfile = activeProfile;
    }

    @Override
    public short getBlockID() {
        return (short) 0x901D;
    }
}
