package com.cmt.statemachine.builder;

/**
 * From
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:13 PM
 */
public interface From<S, E> {
    /**
     * Build transition target state and return to clause builder
     * @param stateId id of state
     * @return To clause builder
     */
    To<S, E> to(S stateId);

}
