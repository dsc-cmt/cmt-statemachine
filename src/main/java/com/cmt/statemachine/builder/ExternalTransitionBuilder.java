package com.cmt.statemachine.builder;

/**
 * ExternalTransitionBuilder
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:11 PM
 */
public interface ExternalTransitionBuilder<S, E> {
    /**
     * Build transition source state.
     * @param stateId id of state
     * @return from clause builder
     */
    From<S, E> from(S stateId);

}
