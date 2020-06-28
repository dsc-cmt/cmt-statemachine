package com.cmt.statemachine;

import com.cmt.statemachine.impl.StateMachineException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StateMachineFactory
 *
 * @author Frank Zhang
 * @date 2020-02-08 10:21 PM
 */
public class StateMachineFactory {
    /** Map<machineId, StateMachine> */
    static Map<String , StateMachine> stateMachineMap = new ConcurrentHashMap<>();

    public <S, E> void register(StateMachine<S, E> stateMachine){
        String machineId = stateMachine.getMachineId();
        if(stateMachineMap.get(machineId) != null){
            throw new StateMachineException("The state machine with id ["+machineId+"] is already built, no need to build again");
        }
        stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
    }

    public <S, E> StateMachine<S, E> get(String machineId){
        StateMachine stateMachine = stateMachineMap.get(machineId);
        if(stateMachine == null){
            throw new StateMachineException("There is no stateMachine instance for "+machineId+", please build it first");
        }
        return stateMachine;
    }
}
