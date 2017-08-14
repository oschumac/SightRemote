package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum AcousticMode implements DescriptorEnum {

    STANDARD((short) 0x1F00),
    LOWTONES((short) 0xE300),
    ACOUSTIC((short) 0xFC00);

    private short value;

    AcousticMode(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
