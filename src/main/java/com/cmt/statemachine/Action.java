package com.cmt.statemachine;

/**
 * Generic strategy interface used by a state machine to respond
 * events by executing an {@code Action} with a {@link StateContext}.
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:51 PM
 */
public interface Action<R,T> {

//    /**
//     * Execute action with a {@link StateContext}.
//     *
//     * @param context the state context
//     */
//    void execute(StateContext<S, E> context);

    T execute(R request);

}
