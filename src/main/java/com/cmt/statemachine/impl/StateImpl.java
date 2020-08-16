package com.cmt.statemachine.impl;

import com.cmt.statemachine.State;
import com.cmt.statemachine.Transition;
import com.cmt.statemachine.Visitor;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Collection;
import java.util.List;

/**
 * StateImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 11:19 PM
 */
public class StateImpl<S, E> implements State<S, E> {
    protected final S stateId;
    /**
     * event和transition 改为 一对多，解决选择伪状态问题
     */
    private ListMultimap<E, Transition<S, E>> transitions = ArrayListMultimap.create();

    StateImpl(S stateId) {
        this.stateId = stateId;
    }

    @Override
    public Transition<S, E> addTransition(E event, State<S, E> target, TransitionType transitionType) {
        Transition<S, E> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);

        Debugger.debug("Begin to add new transition: " + newTransition);
        verify(event, newTransition);
        transitions.put(event, newTransition);
        return newTransition;
    }

    /**
     * Per one source and target state, there is only one transition is allowed
     *
     * @param event
     * @param newTransition
     */
    private void verify(E event, Transition<S, E> newTransition) {
        List<Transition<S, E>> existingTransitions = transitions.get(event);
        for (Transition transition : existingTransitions) {
            if (transition.equals(newTransition)) {
                throw new StateMachineException(transition + " already Exist, you can not add another one");
            }
        }
    }

    @Override
    public List<Transition<S, E>> getTransition(E event) {
        return transitions.get(event);
    }

    @Override
    public Collection<Transition<S, E>> getTransitions() {
        return transitions.values();
    }

    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitOnEntry(this);
        visitor.visitOnExit(this);
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof State) {
            State other = (State) anObject;
            if (this.stateId.equals(other.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return stateId.toString();
    }
}
