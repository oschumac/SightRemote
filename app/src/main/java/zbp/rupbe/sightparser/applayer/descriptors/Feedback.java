package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum Feedback implements DescriptorEnum {

    VIBRATION_SOUND_SOUND((short) 0x1F00),
    ONLY_SOUND((short) 0xE300),
    ONLY_VIBRATION((short) 0xFC00);

    private short value;

    Feedback(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
