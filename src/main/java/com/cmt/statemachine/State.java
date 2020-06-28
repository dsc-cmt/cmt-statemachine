package com.cmt.statemachine;

import com.cmt.statemachine.impl.TransitionType;

import java.util.Collection;
import java.util.List;

/**
 * State
 *
 * @param <S> the type of state
 * @param <E> the type of event
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:12 PM
 */
public interface State<S,E> extends Visitable{

    /**
     * Gets the state identifier.
     *
     * @return the state identifiers
     */
    S getId();

    /**
     * Add transition to the state
     * @param event the event of the Transition
     * @param target the target of the transition
     * @return
     */
    Transition<S,E> addTransition(E event, State<S, E> target, TransitionType transitionType);

    List<Transition<S,E>> getTransition(E event);

    Collection<Transition<S,E>> getTransitions();

}
