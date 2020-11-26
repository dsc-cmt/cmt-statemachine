package com.cmt.statemachine;

/**
 * No match transition process strategy
 *
 * @author yonghuang
 */
public interface NoMatchStrategy<S, E> {
    /**
     * process logic
     * @param state
     * @param event
     */
    void process(S state, E event);
}
