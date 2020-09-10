package com.cmt.statemachine.impl;

import com.cmt.statemachine.*;

/**
 * TransitionImpl。
 *
 * This should be designed to be immutable, so that there is no thread-safe risk
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:32 PM
 */
public class TransitionImpl<S,E> implements Transition<S,E> {

    private State<S, E> source;

    private State<S, E> target;

    private E event;

    private Condition<?> condition;

    private String conditionDesc;

    private Action<?,?> action;

    private TransitionType type = TransitionType.EXTERNAL;

    @Override
    public State<S, E> getSource() {
        return source;
    }

    @Override
    public void setSource(State<S, E> state) {
        this.source = state;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void setType(TransitionType type) {
        this.type = type;
    }

    @Override
    public State<S, E> getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(State<S, E> target) {
        this.target = target;
    }

    @Override
    public Condition<?> getCondition() {
        return this.condition;
    }

    @Override
    public <C> void setCondition(Condition<C> condition) {
        this.condition = condition;
    }

    @Override
    public <C> void setCondition(Condition<C> condition, String conditionDesc) {
        this.condition = condition;
        this.conditionDesc = conditionDesc;
    }

    @Override
    public <C,T> Action<C,T> getAction() {
        return (Action<C,T>) this.action;
    }

    @Override
    public <C,T> void setAction(Action<C,T> action) {
        this.action = action;
    }

    @Override
    public <C,T> State<S, E> transit(C request) {
        Debugger.debug("Do transition: "+this);
        this.verify();

        Condition<C> cond = (Condition<C>) condition;
        Action<C,T> ac = (Action<C,T>) action;
        if(cond == null || cond.isSatisfied(request)){
            setNextState(target,request);
            if(ac != null){
                ac.execute(request);
            }
            return target;
        }

        Debugger.debug("Condition is not satisfied, stay at the "+source+" state ");
        setNextState(source,request);
        return source;
    }

    @Override
    public <T, C> T transitWithResult(C request) {
        Debugger.debug("Do transition: "+this);
        this.verify();
        Condition<C> cond = (Condition<C>) condition;
        Action<C,T> ac = (Action<C,T>) action;
        return nullIfNotSatisfied(cond,request,ac,request);
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() +", "+type+"]->" + target;
    }

    @Override
    public boolean equals(Object anObject){
        if(anObject instanceof Transition){
            Transition other = (Transition)anObject;
            if(this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void verify() {
        if(type== TransitionType.INTERNAL && source != target) {
            throw new StateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }

    @Override
    public <T, C, R> T transitWithResult(C cond, R request) {
        Debugger.debug("Do transition: "+this);
        this.verify();
        Condition<C> func = (Condition<C>) condition;
        Action<R,T> ac = (Action<R,T>) action;
        return nullIfNotSatisfied(func,cond,ac,request);
    }

    @Override
    public String getConditionDesc() {
        return conditionDesc;
    }

    private <T, C, R> T nullIfNotSatisfied(Condition<C> func, C cond, Action<R,T> ac, R request) {
        if(func == null || func.isSatisfied(cond)){
            T t = null;
            setNextState(target,request);
            if(ac != null){
                t = ac.execute(request);
            }
            return t;
        }
        setNextState(source,request);
        return null;
    }

    private <C> void setNextState(State<S, E> s, C request){
        if(request instanceof StateAware){
            ((StateAware) request).setNextState(s.getId());
        }
    }
}
