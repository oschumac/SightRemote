package zbp.rupbe.sightparser.applayer.descriptors;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public enum ReminderStatus implements DescriptorEnum {

    OFF((short) 0x1F00),
    ONCE((short) 0xE300),
    DAILY((short) 0xFC00);

    private short value;

    ReminderStatus(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
