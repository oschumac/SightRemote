package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public enum BolusStepSize implements DescriptorEnum {

    STEP_0_10U((short) 0x0A00),
    STEP_0_20U((short) 0x1400),
    STEP_0_50U((short) 0x3200),
    STEP_1_00U((short) 0x6400),
    STEP_2_00U((short) 0xC800);

    private short value;

    BolusStepSize(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
