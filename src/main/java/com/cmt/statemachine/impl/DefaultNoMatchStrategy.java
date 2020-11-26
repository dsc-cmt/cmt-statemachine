package com.cmt.statemachine.impl;

import com.cmt.statemachine.NoMatchStrategy;

/**
 * Default no match transition strategy
 * @param <S>
 * @param <E>
 */
public class DefaultNoMatchStrategy<S, E> implements NoMatchStrategy<S, E> {
    @Override
    public void process(S state, E event) {
        Debugger.debug("There is no appropriate Transition for " + event);
    }
}
