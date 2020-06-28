package com.cmt.statemachine.builder;

/**
 * StateMachineBuilderFactory
 *
 * @author Frank Zhang
 * @date 2020-02-08 12:33 PM
 */
public class StateMachineBuilderFactory {
    public static <S, E> StateMachineBuilder<S, E> create(){
        return new StateMachineBuilderImpl<>();
    }
}
