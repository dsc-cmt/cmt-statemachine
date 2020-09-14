package com.cmt.statemachine.builder;

import com.cmt.statemachine.Condition;

/**
 * On
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:14 PM
 */
public interface On<S, E> extends When<S, E>{
    /**
     * Add condition for the transition
     * @param condition transition condition
     * @return When clause builder
     */
    <R> When<S, E> when(Condition<R> condition);

    /**
     * Add condition for the transition
     * @param condition transition condition
     * @param desc Description of transition condition
     * @return When clause builder
     */
    <R> When<S, E> when(Condition<R> condition, String desc);
}
