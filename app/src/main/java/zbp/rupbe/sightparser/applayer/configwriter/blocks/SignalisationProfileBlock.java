package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BeepVolume;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.Feedback;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 14.07.2017.
 */

public class SignalisationProfileBlock extends ParameterBlock {

    @BlockParameter(order = 0, parameterType = BlockParameterType.ENUM)
    private BeepVolume normalBeepVolume;
    @BlockParameter(order = 1, parameterType = BlockParameterType.ENUM)
    private BeepVolume silentBeepVolume;
    @BlockParameter(order = 2, parameterType = BlockParameterType.ENUM)
    private BeepVolume meetingBeepVolume;
    @BlockParameter(order = 3, parameterType = BlockParameterType.ENUM)
    private BeepVolume loudBeepVolume;

    @BlockParameter(order = 4, parameterType = BlockParameterType.ENUM)
    private Feedback normalFeedback;
    @BlockParameter(order = 5, parameterType = BlockParameterType.ENUM)
    private Feedback silentFeedback;
    @BlockParameter(order = 6, parameterType = BlockParameterType.ENUM)
    private Feedback meetingFeedback;
    @BlockParameter(order = 7, parameterType = BlockParameterType.ENUM)
    private Feedback loudFeedback;

    @BlockParameter(order = 8, parameterType = BlockParameterType.SHORT_LE)
    private short snoozingStartTime;
    @BlockParameter(order = 9, parameterType = BlockParameterType.SHORT_LE)
    private short snoozingDuration;

    public void setSnoozingStartTime(int hour, int minute) {
        this.snoozingStartTime = (short) (hour * 60 + minute);
    }

    public int getSnoozingStartTimeHour() {
        return (snoozingStartTime - snoozingStartTime % 60) / 60;
    }

    public int getSnoozingStartTimeMinute() {
        return snoozingStartTime % 60;
    }

    public void setSnoozingDuration(int hours, int minutes) {
        snoozingDuration = (short) (hours * 60 + minutes);
    }

    public int getSnoozingDurationHours() {
        return (snoozingDuration - snoozingDuration % 60) / 60;
    }

    public int getSnoozingDurationMinutes() {
        return snoozingDuration % 60;
    }

    public BeepVolume getNormalBeepVolume() {
        return normalBeepVolume;
    }

    public void setNormalBeepVolume(BeepVolume normalBeepVolume) {
        this.normalBeepVolume = normalBeepVolume;
    }

    public BeepVolume getSilentBeepVolume() {
        return silentBeepVolume;
    }

    public void setSilentBeepVolume(BeepVolume silentBeepVolume) {
        this.silentBeepVolume = silentBeepVolume;
    }

    public BeepVolume getMeetingBeepVolume() {
        return meetingBeepVolume;
    }

    public void setMeetingBeepVolume(BeepVolume meetingBeepVolume) {
        this.meetingBeepVolume = meetingBeepVolume;
    }

    public BeepVolume getLoudBeepVolume() {
        return loudBeepVolume;
    }

    public void setLoudBeepVolume(BeepVolume loudBeepVolume) {
        this.loudBeepVolume = loudBeepVolume;
    }

    public Feedback getNormalFeedback() {
        return normalFeedback;
    }

    public void setNormalFeedback(Feedback normalFeedback) {
        this.normalFeedback = normalFeedback;
    }

    public Feedback getSilentFeedback() {
        return silentFeedback;
    }

    public void setSilentFeedback(Feedback silentFeedback) {
        this.silentFeedback = silentFeedback;
    }

    public Feedback getMeetingFeedback() {
        return meetingFeedback;
    }

    public void setMeetingFeedback(Feedback meetingFeedback) {
        this.meetingFeedback = meetingFeedback;
    }

    public Feedback getLoudFeedback() {
        return loudFeedback;
    }

    public void setLoudFeedback(Feedback loudFeedback) {
        this.loudFeedback = loudFeedback;
    }

    @Override
    public short getBlockID() {
        return 0x5854;
    }
}
