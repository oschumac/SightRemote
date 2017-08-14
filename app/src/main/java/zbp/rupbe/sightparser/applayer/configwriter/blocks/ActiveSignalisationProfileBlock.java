package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;
import zbp.rupbe.sightparser.applayer.descriptors.SignalizationProfile;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class ActiveSignalisationProfileBlock extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.ENUM)
    private SignalizationProfile activeProfile;

    public SignalizationProfile getActiveProfile() {
        return activeProfile;
    }

    public void setActiveProfile(SignalizationProfile activeProfile) {
        this.activeProfile = activeProfile;
    }

    @Override
    public short getBlockID() {
        return (short) 0x9791;
    }
}
