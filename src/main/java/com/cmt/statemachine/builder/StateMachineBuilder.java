package com.cmt.statemachine.builder;

import com.cmt.statemachine.StateMachine;

/**
 * StateMachineBuilder
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:32 PM
 */
public interface StateMachineBuilder<S, E> {
    /**
     * Builder for one transition
     * @return External transition builder
     */
    ExternalTransitionBuilder<S, E> externalTransition();

    /**
     * Builder for multiple transitions
     * @return External transition builder
     */
    ExternalTransitionsBuilder<S, E> externalTransitions();

    /**
     * Start to build internal transition
     * @return Internal transition builder
     */
    InternalTransitionBuilder<S, E> internalTransition();

    StateMachine<S,E> build(String machineId);

    /**
     * Specify a initial state
     *
     * @param initial the initial state
     * @return StateMachineBuilder
     */
    StateMachineBuilder<S, E> initialState(S initial);
}
