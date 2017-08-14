package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum TimeFormat implements DescriptorEnum {

    FORMAT_24H((short) 0x1F00),
    FORMAT_12H((short) 0xE300);

    private short value;

    TimeFormat(short value) {
        this.value = value;
    }

    @Override
    public short getValue() {
        return value;
    }
}
