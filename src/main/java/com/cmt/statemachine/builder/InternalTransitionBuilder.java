package com.cmt.statemachine.builder;

/**
 * InternalTransitionBuilder
 *
 * @author Frank Zhang
 * @date 2020-02-07 9:39 PM
 */
public interface InternalTransitionBuilder <S, E> {
    /**
     * Build a internal transition
     * @param stateId id of transition
     * @return To clause builder
     */
    To<S, E> within(S stateId);
}
