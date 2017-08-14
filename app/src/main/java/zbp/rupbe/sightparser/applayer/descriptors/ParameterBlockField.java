package zbp.rupbe.sightparser.applayer.descriptors;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public class ParameterBlockField implements Comparable<ParameterBlockField> {

    private Field field;
    private int order;
    private BlockParameterType blockParameterType;
    private int arrayLength;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public BlockParameterType getBlockParameterType() {
        return blockParameterType;
    }

    public void setBlockParameterType(BlockParameterType blockParameterType) {
        this.blockParameterType = blockParameterType;
    }

    public int getArrayLength() {
        return arrayLength;
    }

    public void setArrayLength(int arrayLength) {
        this.arrayLength = arrayLength;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(@NonNull ParameterBlockField o) {
        return ((Integer) order).compareTo(o.getOrder());
    }
}
