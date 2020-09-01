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
    public static <S> Object getStateDesc(S s) {
        Class clazz = s.getClass();
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
        }
        return null;
    }

    /**
     * 是否使用状态类状态描述字段绘图
     *
     * @param s 状态类
     * @return
     */
    public static <S> boolean getEnableDesc(S s) {
        Class clazz = s.getClass();
        if (!clazz.isAnnotationPresent(StateConfig.class)) {
            return false;
        }
        StateConfig stateConfig = (StateConfig) clazz.getAnnotation(StateConfig.class);
        return stateConfig.enableDesc();
    }
}
