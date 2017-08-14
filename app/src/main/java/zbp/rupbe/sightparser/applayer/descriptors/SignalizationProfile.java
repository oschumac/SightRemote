package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public enum SignalizationProfile implements DescriptorEnum {

    NORMAL((short) 0x1F00),
    SILENT((short) 0xE300),
    MEETING((short) 0xFC00),
    LOUD((short) 0x2503);

    private short value;

    SignalizationProfile(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
