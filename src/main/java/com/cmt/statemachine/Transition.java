package com.cmt.statemachine;

import com.cmt.statemachine.impl.TransitionType;

/**
 * {@code Transition} is something what a state machine associates with a state
 * changes.
 *
 * @author Frank Zhang
 *
 * @param <S> the type of state
 * @param <E> the type of event
 *
 * @date 2020-02-07 2:20 PM
 */
public interface Transition<S, E>{
    /**
     * Gets the source state of this transition.
     *
     * @return the source state
     */
    State<S,E> getSource();

    void setSource(State<S, E> state);

    E getEvent();

    void setEvent(E event);

    void setType(TransitionType type);
    /**
     * Gets the target state of this transition.
     *
     * @return the target state
     */
    State<S,E> getTarget();

    void setTarget(State<S, E> state);

    /**
     * Gets the guard of this transition.
     *
     * @return the guard
     */
    <C> Condition<C> getCondition();

    <C> void setCondition(Condition<C> condition);

    <C,T> Action<C,T> getAction();

    <C,T> void setAction(Action<C,T> action);

    /**
     * Do transition from source state to target state.
     *
     * @return the target state
     */

    <C,T>  State<S,E> transit(C request);

    <T, C> T transitWithResult(C request);

    /**
     * Verify transition correctness
     */
    void verify();

    <T,C,R> T transitWithResult(C cond, R request);
}
