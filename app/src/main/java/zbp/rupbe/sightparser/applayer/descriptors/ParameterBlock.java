package zbp.rupbe.sightparser.applayer.descriptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public abstract class ParameterBlock {

    private static Map<Class<? extends ParameterBlock>, List<ParameterBlockField>> cache = new HashMap<>();

    public abstract short getBlockID();

    public void setBlockID(short blockID) {
    }

    public void afterParse() {
    }

    public static ParameterBlock deserialize(Class<? extends ParameterBlock> clazz, ByteBuf byteBuf) {
        try {
            ParameterBlock parameterBlock = clazz.newInstance();
            List<ParameterBlockField> fields = getConfigBlockFields(parameterBlock.getClass());
            try {
                for (ParameterBlockField blockField : fields) {
                    Field field = blockField.getField();
                    Object value;
                    if (blockField.getBlockParameterType() == BlockParameterType.ENUM) value = blockField.getBlockParameterType().getReadMethod().invoke(byteBuf, field.getClass());
                    else if (blockField.getBlockParameterType().isArray()) value = blockField.getBlockParameterType().getReadMethod().invoke(byteBuf, blockField.getArrayLength());
                    else value = blockField.getBlockParameterType().getReadMethod().invoke(byteBuf);
                    field.set(parameterBlock, value);
                }
                parameterBlock.afterParse();
                return parameterBlock;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public short getRestrictionLevel() {
        return 0x1F00;
    }

    public ByteBuf serialize(Class<? extends ParameterBlock> clazz) {
        try {
            List<ParameterBlockField> fields = getConfigBlockFields(clazz);
            ByteBuf byteBuf = new ByteBuf(calculateSize(fields));
            for (ParameterBlockField parameter : fields) parameter.getBlockParameterType().getPutMethod().invoke(byteBuf, parameter.getField().get(this));
            return byteBuf;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int calculateSize(List<ParameterBlockField> parameters) {
        int size = 0;
        for (ParameterBlockField parameter : parameters) {
            int increment = 0;
            if (parameter.getBlockParameterType() == BlockParameterType.BYTES || parameter.getBlockParameterType() == BlockParameterType.BYTES_LE) increment = parameter.getArrayLength();
            else increment = parameter.getBlockParameterType().getValueSize();
            size += increment;
        }
        return size;
    }

    private static List<ParameterBlockField> getConfigBlockFields(Class<? extends ParameterBlock> clazz) {
        List<ParameterBlockField> fields = cache.get(clazz);
        if (fields == null) {
            fields = new ArrayList<>();
            for (Field field : clazz.getClass().getDeclaredFields()) {
                for (Annotation annotation : field.getAnnotations()) {
                    if (annotation.annotationType() == BlockParameter.class) {
                        field.setAccessible(true);
                        BlockParameter blockParameter = (BlockParameter) annotation;
                        ParameterBlockField blockField = new ParameterBlockField();
                        blockField.setBlockParameterType(blockParameter.parameterType());
                        blockField.setField(field);
                        blockField.setOrder(blockParameter.order());
                        blockField.setArrayLength(blockParameter.arrayLength());
                        fields.add(blockField);
                        break;
                    }
                }
            }
            Collections.shuffle(fields);
            cache.put(clazz, fields);
        }
        return fields;
    }

}
