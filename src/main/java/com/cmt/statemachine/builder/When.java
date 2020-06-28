package com.cmt.statemachine.builder;

import com.cmt.statemachine.Action;

/**
 * When
 *
 * @author Frank Zhang
 * @date 2020-02-07 9:33 PM
 */
public interface When<S, E>{
    /**
     * Define action to be performed during transition
     *
     * @param action performed action
     */
    <C,T> void perform(Action<C,T> action);
}
