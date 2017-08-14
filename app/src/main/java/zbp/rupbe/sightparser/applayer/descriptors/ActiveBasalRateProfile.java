package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum ActiveBasalRateProfile implements DescriptorEnum {

    BASAL_RATE_PROFILE_1((short) 0x1F00),
    BASAL_RATE_PROFILE_2((short) 0xE300),
    BASAL_RATE_PROFILE_3((short) 0xFC00),
    BASAL_RATE_PROFILE_4((short) 0x2503),
    BASAL_RATE_PROFILE_5((short) 0xC603);

    private short value;

    ActiveBasalRateProfile(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
