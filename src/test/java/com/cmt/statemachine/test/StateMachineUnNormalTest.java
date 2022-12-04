package com.cmt.statemachine.test;

import com.cmt.statemachine.Action;
import com.cmt.statemachine.Condition;
import com.cmt.statemachine.StateMachine;
import com.cmt.statemachine.builder.StateMachineBuilder;
import com.cmt.statemachine.builder.StateMachineBuilderFactory;
import com.cmt.statemachine.impl.StateMachineException;
import org.junit.Assert;
import org.junit.Test;

/**
 * StateMachineUnNormalTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 5:52 PM
 */
public class StateMachineUnNormalTest {

    @Test
    public void testConditionNotMeet(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events> builder = StateMachineBuilderFactory.create();
        builder.initialState(StateMachineTest.States.STATE1)
                .externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkConditionFalse())
                .perform(doAction());

        StateMachine<StateMachineTest.States, StateMachineTest.Events> stateMachine = builder.build("NotMeetConditionMachine");
        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new StateMachineTest.Context());
        Assert.assertEquals(StateMachineTest.States.STATE1,target);
    }


    @Test(expected = StateMachineException.class)
    public void testDuplicatedTransition(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        StateMachine<StateMachineTest.States, StateMachineTest.Events> stateMachine = builder.build("DuplicatedTransitionMachine");
        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new StateMachineTest.Context());
        Assert.assertEquals(StateMachineTest.States.STATE2, target);
    }

   /* @Test(expected = StateMachineException.class)
    public void testDuplicateMachine(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        builder.build("DuplicatedMachine");
        builder.build("DuplicatedMachine");
    }*/

    @Test
    public void testConditionNotSatisfied(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events> builder = StateMachineBuilderFactory.create();
        builder.initialState(StateMachineTest.States.STATE1)
                .externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        StateMachine<StateMachineTest.States, StateMachineTest.Events> stateMachine = builder.build("id");
        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT3, new StateMachineTest.Context());
        Assert.assertEquals(StateMachineTest.States.STATE1, target);
    }

    @Test
    public void testConditionNotSatisfiedNoResult(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events> builder = StateMachineBuilderFactory.create();
        builder.initialState(StateMachineTest.States.STATE1)
                .externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        StateMachine<StateMachineTest.States, StateMachineTest.Events> stateMachine = builder.build("id");
        String result = stateMachine.fireEventWithResult(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT3, new StateMachineTest.Context());
        Assert.assertNull(result);
    }

    @Test(expected = StateMachineException.class)
    public void testNoInitialState(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        StateMachine<StateMachineTest.States, StateMachineTest.Events> stateMachine = builder.build("id");
        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new StateMachineTest.Context());
        Assert.assertEquals(StateMachineTest.States.STATE2, target);
    }

    private Condition<StateMachineTest.Context> checkCondition() {
        return (ctx) -> {return true;};
    }

    private Condition<StateMachineTest.Context> checkConditionFalse() {
        return (ctx) -> {return false;};
    }

    private Action<StateMachineTest.Context,String> doAction() {
        return new Action<StateMachineTest.Context,String>() {
            @Override
            public String execute(StateMachineTest.Context ctx) {
//                System.out.println(ctx.operator+" is operating "+ctx.entityId+"from:"+from+" to:"+to+" on:"+event);
                return "success";
            }
        };

//                (from, to, event, ctx)-> {
//            System.out.println(ctx.operator+" is operating "+ctx.entityId+"from:"+from+" to:"+to+" on:"+event);
//        };
    }
}
