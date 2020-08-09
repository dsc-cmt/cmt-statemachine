package com.cmt.statemachine;

/**
 * StateMachine
 *
 * @param <S> the type of state
 * @param <E> the type of event
 * @author Frank Zhang
 * @date 2020-02-07 2:13 PM
 */
public interface StateMachine<S, E> extends Visitable {

    /**
     * Send an event {@code E} to the state machine.
     *
     * @param sourceState the source state
     * @param event       the event to send
     * @param request     the user defined request
     * @return the target state
     */
    <C> S fireEvent(S sourceState, E event, C request);

    /**
     * Send an event {@code E} to the state machine and return a result
     *
     * @param sourceState
     * @param event
     * @param request
     * @return return null if there is no suitable transaction else return transaction.transitWithResult
     */
    <T, R> T fireEventWithResult(S sourceState, E event, R request);

    /**
     * Send an event {@code E} to the state machine and return a result
     *
     * @param sourceState
     * @param event
     * @param cond        the user defined condition(used as a parameter in {@link Condition#isSatisfied(java.lang.Object)})
     * @param request     the user defined request(used as a parameter in {@link Action#execute(java.lang.Object)})
     * @return return null if there is no suitable transaction else return transaction.transitWithResult
     */
    <T, C, R> T fireEventWithResult(S sourceState, E event, C cond, R request);

    /**
     * MachineId is the identifier for a State Machine
     *
     * @return
     */
    String getMachineId();

    /**
     * Use visitor pattern to display the structure of the state machine
     */
    void showStateMachine();

    /**
     * generate plantUml state diagram text file.
     */
    void generatePlantUML();

    /**
     * Gets the initial state {@code S}.
     *
     * @return initial state
     */
    S getInitialState();

    /**
     * generate GraphvizJava state diagram.
     */
    void generateStateDiagram();
}
