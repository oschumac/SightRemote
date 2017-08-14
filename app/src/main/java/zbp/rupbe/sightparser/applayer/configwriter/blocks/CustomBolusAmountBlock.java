package zbp.rupbe.sightparser.applayer.configwriter.blocks;

import zbp.rupbe.sightparser.applayer.descriptors.BlockParameter;
import zbp.rupbe.sightparser.applayer.descriptors.BlockParameterType;
import zbp.rupbe.sightparser.applayer.descriptors.CustomBolusType;
import zbp.rupbe.sightparser.applayer.descriptors.ParameterBlock;

/**
 * Created by Tebbe Ubben on 13.07.2017.
 */

public class CustomBolusAmountBlock extends ParameterBlock {

    private BolusID bolusID;

    @BlockParameter(order = 0, parameterType = BlockParameterType.ENUM)
    private CustomBolusType bolusType;
    @BlockParameter(order = 1, parameterType = BlockParameterType.SHORT_LE)
    private short amount1;
    @BlockParameter(order = 2, parameterType = BlockParameterType.SHORT_LE)
    private short amount2;
    @BlockParameter(order = 3, parameterType = BlockParameterType.SHORT_LE)
    private short duration1;
    @BlockParameter(order = 4, parameterType = BlockParameterType.SHORT_LE)
    private short duration2;
    @BlockParameter(order = 5, parameterType = BlockParameterType.BOOLEAN)
    private boolean configured;

    @Override
    public short getBlockID() {
        return bolusID.getValue();
    }

    @Override
    public void setBlockID(short blockID) {
        bolusID = BolusID.getBolusID(blockID);
    }

    public BolusID getBolusID() {
        return bolusID;
    }

    public void setBolusID(BolusID bolusID) {
        this.bolusID = bolusID;
    }

    public CustomBolusType getBolusType() {
        return bolusType;
    }

    public void setBolusType(CustomBolusType bolusType) {
        this.bolusType = bolusType;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public float getAmount() {
        return ((float) amount1) / 100F;
    }

    public float getDelayedAmount() {
        switch (bolusType) {
            case EXTENDED:
                return ((float) amount1) / 100F;
            case MULTIWAVE:
                return ((float) amount2) / 100F;
        }
        return 0;
    }

    public int getDuration() {
        switch (bolusType) {
            case EXTENDED:
                return duration1;
            case MULTIWAVE:
                return duration2;
        }
        return 0;
    }

    public void setAmount(float amount) {
        amount1 = (short) (amount * 100);
    }

    public void setDelayedAmount(float amount) {
        switch (bolusType) {
            case EXTENDED:
                amount1 = (short) (amount * 100);
                break;
            case MULTIWAVE:
                amount2 = (short) (amount * 100);
                break;
        }
    }

    public void setDuration(int duration) {
        switch (bolusType) {
            case EXTENDED:
                duration1 = (short) duration;
                break;
            case MULTIWAVE:
                duration2 = (short) duration;
                break;
        }
    }

    public enum BolusID {
        CUSTOM_BOLUS_1((short) 0x3A03),
        CUSTOM_BOLUS_2((short) 0xC603),
        CUSTOM_BOLUS_3((short) 0xD903),
        CUSTOM_BOLUS_4((short) 0x5A05),
        CUSTOM_BOLUS_5((short) 0x5505),
        CUSTOM_BOLUS_6((short) 0xA905),
        CUSTOM_BOLUS_7((short) 0xB605),
        CUSTOM_BOLUS_8((short) 0x6F06),
        CUSTOM_BOLUS_9((short) 0x7006),
        CUSTOM_BOLUS_10((short) 0x8C06);

        private short value;

        BolusID(short blockID) {
            this.value = blockID;
        }

        public short getValue() {
            return value;
        }

        public static BolusID getBolusID(short value) {
            for (BolusID bolusID : values()) if (bolusID.getValue() == value) return bolusID;
            return null;
        }
    }
}
