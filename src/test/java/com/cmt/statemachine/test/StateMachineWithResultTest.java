package com.cmt.statemachine.test;

import com.cmt.statemachine.Action;
import com.cmt.statemachine.Condition;
import com.cmt.statemachine.StateMachine;
import com.cmt.statemachine.builder.StateMachineBuilder;
import com.cmt.statemachine.builder.StateMachineBuilderFactory;

import java.util.Objects;
import org.junit.Assert;
import org.junit.Test;

/**
 * StateMachineWithResultTest
 *
 * @author yonghuang
 */
public class StateMachineWithResultTest {
    static String MACHINE_ID = "StateMachineWithResultTest";

    @Test
    public void testExternalNormal() {
        StateMachineBuilder<States, Operations> builder = StateMachineBuilderFactory.create();
        builder.initialState(States.STATE1)
                .externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Operations.OPER1)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(States.STATE3)
                .to(States.STATE4)
                .on(Operations.OPER2)
                .when((request) -> true)
                .perform(new Action<Request, Response>() {
                    @Override
                    public Response execute(Request request) {
                        Response response = new Response();
                        response.ret = "Hello " + request.operator;
                        return response;
                    }
                });

        StateMachine<States, Operations> stateMachine = builder.build(MACHINE_ID);
        String result = stateMachine.fireEventWithResult(States.STATE1, Operations.OPER1, "Hello");
        Assert.assertEquals(result, "Hello World!");

        Response ret = stateMachine.fireEventWithResult(States.STATE3, Operations.OPER2, new Request());
        System.out.println(ret);
        Assert.assertEquals(ret.ret, "Hello frank");
    }

    @Test
    public void testConditionAndRequest() {
        StateMachineBuilder<States, Operations> builder = StateMachineBuilderFactory.create();
        builder.initialState(States.STATE1)
                .externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Operations.OPER1)
                .when(Objects::nonNull)
                .perform(doAction());
        StateMachine<States, Operations> stateMachine = builder.build(MACHINE_ID);
        String result = stateMachine.fireEventWithResult(States.STATE1, Operations.OPER1, null, null);
        Assert.assertNull(result);
        result = stateMachine.fireEventWithResult(States.STATE1, Operations.OPER1, "", null);
        Assert.assertEquals(result, "Hello World!");
    }

    private Condition<String> checkCondition() {
        return (request) -> {
            return true;
        };
    }

    private Action<String, String> doAction() {
        return new Action<String, String>() {
            @Override
            public String execute(String request) {
                return "Hello World!";
            }
        };
    }

    static enum States {
        STATE1, STATE2, STATE3, STATE4
    }

    static enum Operations {
        OPER1, OPER2, OPER3, OPER4
    }

    static class Request {
        String operator = "frank";
        String entityId = "123465";

        String nodeType = "2";
    }

    static class Response {
        String ret;
    }
}
