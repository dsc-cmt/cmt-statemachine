package com.cmt.statemachine;

/**
 * Condition
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:50 PM
 */
public interface Condition<R> {

    /**
     * @param context context object
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(R context);

    default String name(){
        return this.getClass().getSimpleName();
    }
}