package io.github.sanchert.namelessgf;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoName {
    String prefix() default "";
    boolean autoIncrement() default true;
}
