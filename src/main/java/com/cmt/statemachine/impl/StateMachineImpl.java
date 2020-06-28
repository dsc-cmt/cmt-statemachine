package com.cmt.statemachine.impl;

import com.cmt.statemachine.State;
import com.cmt.statemachine.StateMachine;
import com.cmt.statemachine.Transition;
import com.cmt.statemachine.Visitor;

import java.util.List;
import java.util.Map;

/**
 * For performance consideration,
 * The state machine is made "stateless" on purpose.
 * Once it's built, it can be shared by multi-thread
 *
 * One side effect is since the state machine is stateless, we can not get current state from State Machine.
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:40 PM
 */
public class StateMachineImpl<S,E> implements StateMachine<S, E> {

    private String machineId;

    private final Map<S, State<S,E>> stateMap;

    private boolean ready;

    public StateMachineImpl(Map<S, State< S, E>> stateMap){
        this.stateMap = stateMap;
    }

    @Override
    public <C> S fireEvent(S sourceStateId, E event, C request){
        isReady();
        Transition<S,E> transition = routeTransition(sourceStateId, event, request);

        if (transition == null) {
            Debugger.debug("There is no appropriate Transition for " + event);
            return sourceStateId;
        }

        return transition.transit(request).getId();
    }

    @Override
    public <T, R> T fireEventWithResult(S sourceStateId, E event, R request) {
        isReady();
        Transition<S,E> transition = routeTransition(sourceStateId, event, request);

        if (transition == null) {
            Debugger.debug("There is no appropriate Transition for " + event);
            return null;
        }

       return transition.transitWithResult(request);
    }

    @Override
    public <T, C, R> T fireEventWithResult(S sourceStateId, E event, C cond, R request) {
        this.isReady();
        Transition<S, E> transition = this.routeTransition(sourceStateId, event, cond);
        if (transition == null) {
            Debugger.debug("There is no appropriate Transition for " + event);
            return null;
        } else {
            return transition.transitWithResult(cond,request);
        }
    }

    private <R> Transition<S,E> routeTransition(S sourceStateId, E event, R request) {
        State sourceState = getState(sourceStateId);

        List<Transition<S,E>> transitions = sourceState.getTransition(event);

        if (transitions == null || transitions.size() == 0) {
            return null;
        }

        Transition<S,E> transit = null;
        for (Transition<S,E> transition: transitions) {
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(request)) {
                transit = transition;
                break;
            }
        }

        return transit;
    }

    private State getState(S currentStateId) {
        State state = StateHelper.getState(stateMap, currentStateId);
        if(state == null){
            showStateMachine();
            throw new StateMachineException(currentStateId + " is not found, please check state machine");
        }
        return state;
    }

    private void isReady() {
        if(!ready){
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitOnEntry(this);
        for(State state: stateMap.values()){
            state.accept(visitor);
        }
        visitor.visitOnExit(this);
    }

    @Override
    public void showStateMachine() {
        SysOutVisitor sysOutVisitor = new SysOutVisitor();
        accept(sysOutVisitor);
    }

    @Override
    public void generatePlantUML(){
        PlantUMLVisitor plantUMLVisitor = new PlantUMLVisitor();
        accept(plantUMLVisitor);
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
