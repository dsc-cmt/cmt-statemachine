package com.cmt.statemachine;

/**
 * Condition
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:50 PM
 */
@FunctionalInterface
public interface Condition<R> {

    /**
     * Conditions for transition
     * @param context context object
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(R context);
}