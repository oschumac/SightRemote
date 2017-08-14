package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum CustomBolusType implements DescriptorEnum {
    STANDARD((short) 0x1F00),
    EXTENDED((short) 0xE300),
    MULTIWAVE((short) 0xFC00);

    private short value;

    CustomBolusType(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
