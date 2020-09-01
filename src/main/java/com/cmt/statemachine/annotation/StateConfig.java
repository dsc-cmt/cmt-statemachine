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

    /**
     * 是否启用状态描述字段绘图
     * 注：状态描述字段由上述 descField 指定，仅支持 String 类型
     */
    boolean enableDesc() default false;
}
