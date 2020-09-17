package com.cmt.statemachine.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件枚举配置
 * 暂时只支持枚举类型
 * @author dingchenchen
 * @since 2020/9/2
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventConfig {

    /**
     * 事件描述
     * 注：生成 plant uml 语法文件和绘图会使用描述字符串
     */
    String value() default "";
}
