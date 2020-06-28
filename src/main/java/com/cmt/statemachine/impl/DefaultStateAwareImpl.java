package com.cmt.statemachine.impl;

import com.cmt.statemachine.StateAware;

/**
 * @author tuzhenxian
 * @date 20-6-28
 */
public class DefaultStateAwareImpl<S> implements StateAware<S> {
    private S next;

    @Override
    public void setNextState(S next) {
        this.next = next;
    }

    public S getNext() {
        return next;
    }
}
