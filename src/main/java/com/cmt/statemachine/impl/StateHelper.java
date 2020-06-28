package com.cmt.statemachine.impl;

import com.cmt.statemachine.State;

import java.util.Map;

/**
 * StateHelper
 *
 * @author Frank Zhang
 * @date 2020-02-08 4:23 PM
 */
public class StateHelper {
    public static <S, E> State<S, E> getState(Map<S, State<S, E>> stateMap, S stateId){
        State<S, E> state = stateMap.get(stateId);
        if (state == null) {
            state = new StateImpl<>(stateId);
            stateMap.put(stateId, state);
        }
        return state;
    }
}
