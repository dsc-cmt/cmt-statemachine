package com.cmt.statemachine.util;

import com.cmt.statemachine.annotation.StateConfig;

import java.lang.reflect.Field;

/**
 * @author dingchenchen
 * @since 2020/8/31
 */
public class StateUtil {

    /**
     * 获取状态类的描述字段对象
     *
     * @param s 状态类
     * @return 状态类的描述字段
     */
    public static <S> Object getStateDescField(S s) {
        Class clazz = null;
        if (s instanceof Enum) {
            clazz = ((Enum) s).getDeclaringClass();
        } else {
            clazz = s.getClass();
        }
        if (!clazz.isAnnotationPresent(StateConfig.class)) {
            return null;
        }
        StateConfig stateConfig = (StateConfig) clazz.getAnnotation(StateConfig.class);
        String descField = stateConfig.descField();
        try {
            Field field = clazz.getDeclaredField(descField);
            field.setAccessible(true);
            return field.get(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
