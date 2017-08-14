package zbp.rupbe.sightparser.applayer.descriptors;

import java.lang.reflect.Method;

import zbp.rupbe.sightparser.pipeline.ByteBuf;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public enum BlockParameterType {

    BYTE("readByte", "putByte", 1),
    SHORT("readShort", "putShort", 2),
    INTEGER("readInt", "putInt", 4),
    FLOAT("readFloat", "putDouble", 4),
    DOUBLE("readDouble", "putDouble", 8),
    LONG("readLong", "putLOng", 8),
    BYTES("readBytes", "putBytes", true),

    SHORT_LE("readShortLE", "putShortLE", 2),
    INTEGER_LE("readIntLE", "putIntLE", 4),
    FLOAT_LE("readFloatLE", "putFloatLE", 4),
    DOUBLE_LE("readDoubleLE", "putDoubleLE", 8),
    LONG_LE("readLongLE", "putLongLE", 8),
    BYTES_LE("readBytesLE", "putBytesLE", true),

    BOOLEAN("readBoolean", "putBoolean", 2),
    ENUM("readEnum", "putEnum", 2, Class.class);

    private Method byteBufReadMethod;
    private Method byteBufPutMethod;
    private boolean isArray = false;
    private int valueSize = 0;

    BlockParameterType(String readMethod, String putMethod, int valueSize, Class arg) {
        try {
            byteBufReadMethod = ByteBuf.class.getMethod(readMethod, arg);
            byteBufPutMethod = ByteBuf.class.getMethod(putMethod);
            this.valueSize = valueSize;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    BlockParameterType(String readMethod, String putMethod, int valueSize) {
        this(readMethod, putMethod, 0, null);
    }

    BlockParameterType(String readMethod, String putMethod, boolean isArray) {
        this(readMethod, putMethod, 0, isArray ? Integer.class : null);
        isArray = true;
    }

    public boolean isArray() {
        return isArray;
    }

    public Method getReadMethod() {
        return byteBufReadMethod;
    }

    public Method getPutMethod() {
        return byteBufPutMethod;
    }

    public int getValueSize() {
        return valueSize;
    }
}
