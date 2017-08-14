package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum DeliverVelocity implements DescriptorEnum {

    VERY_SLOW((short) 0x1F00),
    SLOW((short) 0xE300),
    MODERATE((short) 0xFC00),
    STANDARD((short) 0x0253);

    private short value;

    DeliverVelocity(short value) {
        this.value = value;
    }

    @Override
    public short getValue() {
        return value;
    }
}
