package com.cmt.statemachine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 状态配置注解
 * @author dingchenchen
 * @since 2020/8/31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StateConfig {

    /**
     * 指定状态类 S 中的描述字段
     */
    String descField() default "";
}
