package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum BeepVolume implements DescriptorEnum {

    VOLUME_0((short) 0x0000),
    VOLUME_1((short) 0x0100),
    VOLUME_2((short) 0x0200),
    VOLUME_3((short) 0x0300),
    VOLUME_4((short) 0x0400),
    VOLUME_5((short) 0x0500);

    private short value;

    BeepVolume(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
