package com.cmt.statemachine.impl;

import com.cmt.statemachine.State;
import com.cmt.statemachine.StateMachine;
import com.cmt.statemachine.Transition;
import com.cmt.statemachine.Visitor;
import com.cmt.statemachine.util.EventUtil;
import com.cmt.statemachine.util.StateUtil;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

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

    private S initialState;

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
    public S getInitialState() {
        return initialState;
    }

    @Override
    public void generateStateDiagram() {
        HashMap<String,Node> stateNodeMap = new HashMap<>(16);
        stateMap.keySet().forEach(s->{
            stateNodeMap.put(getStateDesc(s), node(getStateDesc(s)).with(Color.BLUE));
        });

        List<Node> nodeList = new ArrayList<>();
        stateMap.keySet().stream().forEach(s -> {
            Node sNode = stateNodeMap.get(getStateDesc(s));
            State<S,E> sStateImpl = stateMap.get(s);
            Collection<Transition<S,E>> sTransitions = sStateImpl.getTransitions();
            sTransitions.forEach(
                    transition -> {
                        nodeList.add(
                                sNode.link(to(stateNodeMap.get(getStateDesc(transition.getTarget().getId()))).with(Style.BOLD,Label.of(EventUtil.getEventDesc(transition.getEvent())), Color.GREEN)));
                    }
            );
        });
        Graph graph = graph("StateDiagram").directed().with(nodeList);
        try {
            Graphviz.fromGraph(graph).width(900).render(Format.PNG).toFile(new File("StateDiagram"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 状态描述字符串
     * @param s 状态
     * @return 如果状态指定了描述字段，则返回状态描述字段值，见 @StateConfig;
     *         反之，则返回 s.toString()
     */
    private String getStateDesc(S s){
        Object obj = StateUtil.getStateDescField(s);
        if (Objects.nonNull(obj) && obj instanceof String) {
            return obj.toString();
        }
        return s.toString();
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * Verify a state machine model.
     */
    public void verify() {
        if (initialState == null) {
            throw new StateMachineException("The state machine with id ["+machineId+"] has not set initial state");
        }
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setInitialState(S initialState) {
        this.initialState = initialState;
    }
}
