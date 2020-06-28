package com.cmt.statemachine;

/**
 * StateContext
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:49 PM
 */
public interface StateContext<S, E> {
    /**
     * Gets the transition.
     *
     * @return the transition
     */
    Transition<S, E> getTransition();

    /**
     * Gets the state machine.
     *
     * @return the state machine
     */
    StateMachine<S, E> getStateMachine();
}
