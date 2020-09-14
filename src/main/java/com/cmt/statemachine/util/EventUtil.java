package com.cmt.statemachine.util;

import com.cmt.statemachine.annotation.EventConfig;

import java.lang.reflect.Field;

/**
 * @author dingchenchen
 * @since 2020/9/2
 */
public class EventUtil {

    /**
     * 获取事件描述
     */
    public static <E> String getEventDesc(E e) {
        if (!(e instanceof Enum)) {
            return e.toString();
        }
        Class clazz = e.getClass();
        String fieldName = ((Enum) e).name();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (field.isAnnotationPresent(EventConfig.class)) {
                EventConfig eventConfig = field.getAnnotation(EventConfig.class);
                return eventConfig.desc();
            }
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
        return e.toString();
    }
}
