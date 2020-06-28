package com.cmt.statemachine.builder;

import com.cmt.statemachine.Action;
import com.cmt.statemachine.Condition;
import com.cmt.statemachine.State;
import com.cmt.statemachine.Transition;
import com.cmt.statemachine.impl.StateHelper;
import com.cmt.statemachine.impl.TransitionType;

import java.util.Map;

/**
 * TransitionBuilderImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:20 PM
 */
class TransitionBuilderImpl<S,E> implements ExternalTransitionBuilder<S,E>, InternalTransitionBuilder<S,E>, From<S,E>, On<S,E>, To<S,E> {

    final Map<S, State<S, E>> stateMap;

    private State<S, E> source;

    protected State<S, E> target;

    private Transition<S, E> transition;

    final TransitionType transitionType;

    public TransitionBuilderImpl(Map<S, State<S, E>> stateMap, TransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }

    @Override
    public From<S, E> from(S stateId) {
        source = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E> to(S stateId) {
        target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E> within(S stateId) {
        source = target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public <C> When<S, E> when(Condition<C> condition) {
        transition.setCondition(condition);
        return this;
    }

    @Override
    public On<S, E> on(E event) {
        transition = source.addTransition(event, target, transitionType);
        return this;
    }

    @Override
    public <C,T> void perform(Action<C,T> action) {
        transition.setAction(action);
    }


}
