package zbp.rupbe.sightparser.applayer.descriptors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BlockParameter {
    int order();
    BlockParameterType parameterType();
    int arrayLength() default 0;
}
