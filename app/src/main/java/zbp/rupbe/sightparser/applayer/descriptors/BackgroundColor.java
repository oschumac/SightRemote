package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum BackgroundColor implements DescriptorEnum {

    DARK((short) 0x1F00),
    LIGHT((short) 0xE300);

    private short value;

    BackgroundColor(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
