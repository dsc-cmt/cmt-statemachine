package com.cmt.statemachine;

/**
 * Action<R,T>的参数R若实现了stateAware接口 则可以注入下一个状态
 *
 * @author tuzhenxian
 * @date 20-6-19
 */
public interface StateAware<S> {
    /**
     * 设置下一个状态
     *
     * @param next
     * @return
     */
    void setNextState(S next);
}
