package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public enum Melody implements DescriptorEnum {

    MELODY_1((short) 0x1F00),
    MELODY_2((short) 0xE300),
    MELODY_3((short) 0xFC00),
    MELODY_4((short) 0x2503),
    MELODY_5((short) 0x3A03),
    MELODY_6((short) 0xC603),
    MELODY_7((short) 0xD903),
    MELODY_8((short) 0x4A05),
    MELODY_9((short) 0x5505),
    MELODY_10((short) 0xA905);

    private short value;

    Melody(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
