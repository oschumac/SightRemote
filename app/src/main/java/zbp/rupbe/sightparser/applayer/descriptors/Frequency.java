package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public enum Frequency implements DescriptorEnum {

    ONE_DAY((short) 0x1F00),
    TWO_DAYS((short) 0xE300),
    THREE_DAYS((short) 0xFC00);

    private short value;

    Frequency(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
