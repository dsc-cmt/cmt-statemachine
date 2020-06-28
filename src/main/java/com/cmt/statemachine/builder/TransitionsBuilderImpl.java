package com.cmt.statemachine.builder;

import com.cmt.statemachine.Action;
import com.cmt.statemachine.Condition;
import com.cmt.statemachine.State;
import com.cmt.statemachine.Transition;
import com.cmt.statemachine.impl.StateHelper;
import com.cmt.statemachine.impl.TransitionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TransitionsBuilderImpl
 *
 * @author Frank Zhang
 * @date 2020-02-08 7:43 PM
 */
public class TransitionsBuilderImpl<S,E,C> extends TransitionBuilderImpl<S,E> implements ExternalTransitionsBuilder<S,E> {
    /**
     * This is for fromAmong where multiple sources can be configured to point to one target
     */
    private List<State<S, E>> sources = new ArrayList<>();

    private List<Transition<S, E>> transitions = new ArrayList<>();

    public TransitionsBuilderImpl(Map<S, State<S, E>> stateMap, TransitionType transitionType) {
        super(stateMap, transitionType);
    }

    @Override
    public From<S, E> fromAmong(S... stateIds) {
        for(S stateId : stateIds) {
            sources.add(StateHelper.getState(super.stateMap, stateId));
        }
        return this;
    }

    @Override
    public On<S, E> on(E event) {
        for(State source : sources) {
            Transition transition = source.addTransition(event, super.target, super.transitionType);
            transitions.add(transition);
        }
        return this;
    }

    @Override
    public <C> When<S, E> when(Condition<C> condition) {
        for(Transition transition : transitions){
            transition.setCondition(condition);
        }
        return this;
    }

    @Override
    public <C,T> void perform(Action<C,T> action) {
        for(Transition transition : transitions){
            transition.setAction(action);
        }
    }
}
