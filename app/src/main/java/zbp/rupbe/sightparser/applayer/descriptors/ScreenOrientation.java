package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum ScreenOrientation implements DescriptorEnum {

    NORMAL((short) 0x4B00),
    FLIPPED((short) 0xB400);

    private short value;

    ScreenOrientation(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
