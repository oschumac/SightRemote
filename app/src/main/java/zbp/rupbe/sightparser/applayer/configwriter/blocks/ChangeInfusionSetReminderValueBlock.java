package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.Frequency;
import zbp.rupbe.sightparser.applayer.descriptors.Melody;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public class ChangeInfusionSetReminderValueBlock extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.SHORT_LE)
    private short time;
    @BlockParameter(order = 1, parameterType = BlockParameterType.ENUM)
    private Melody melody;
    @BlockParameter(order = 2, parameterType = BlockParameterType.ENUM)
    private Frequency frequency;

    public void setTime(int hour, int minute) {
        time = (short) (hour * 60 + minute);
    }

    public int getHour() {
        return (time - time % 60) / 60;
    }

    public int getMinute() {
        return time % 60;
    }

    public Melody getMelody() {
        return melody;
    }

    public void setMelody(Melody melody) {
        this.melody = melody;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    @Override
    public short getBlockID() {
        return (short) 0xC963;
    }
}
