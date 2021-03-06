package com.cmt.statemachine.test;

import com.cmt.statemachine.Action;
import com.cmt.statemachine.Condition;
import com.cmt.statemachine.StateMachine;
import com.cmt.statemachine.StateMachineFactory;
import com.cmt.statemachine.annotation.EventConfig;
import com.cmt.statemachine.annotation.StateConfig;
import com.cmt.statemachine.builder.StateMachineBuilder;
import com.cmt.statemachine.builder.StateMachineBuilderFactory;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author dingchenchen
 * @since 2020/8/31
 */
public class AnnotationTest {

    @StateConfig(descField = "desc")
    static enum States {
        STATE1(1, "状态1"),
        STATE2(2, "状态2"),
        STATE3(3, "状态3"),
        STATE4(4, "状态4");

        private Integer code;
        private String desc;

        States(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    static enum Events {
        @EventConfig("事件1")
        EVENT1,
        @EventConfig("事件2")
        EVENT2,
        @EventConfig("事件3")
        EVENT3,
        @EventConfig("事件4")
        EVENT4,
        @EventConfig("状态自流转事件")
        INTERNAL_EVENT
    }

    @Test
    @Ignore
    public void testPlantUML(){
        StateMachine<StateMachineTest.States, StateMachineTest.Events> stateMachine = buildStateMachine("testExternalInternalNormal").get("testExternalInternalNormal");
        stateMachine.generatePlantUML();
    }

    private StateMachineFactory buildStateMachine(String machineId) {
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
        builder.initialState(States.STATE1)
                .externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        builder.internalTransition()
                .within(States.STATE2)
                .on(Events.INTERNAL_EVENT)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(States.STATE2)
                .to(States.STATE1)
                .on(Events.EVENT2)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE3)
                .on(Events.EVENT3)
                .when(Optype::isLoanFirst)
                .perform(doAction());

        builder.externalTransitions()
                .fromAmong(States.STATE1, States.STATE2, States.STATE3)
                .to(States.STATE4)
                .on(Events.EVENT4)
                .when(checkCondition(),"条件为true")
                .perform(doAction());


        StateMachineFactory factory = new StateMachineFactory();
        factory.register(builder.build(machineId));
        StateMachine<StateMachineTest.States, Events> stateMachine = factory.get(machineId);
        stateMachine.showStateMachine();
        return factory;
    }

    private Condition<StateMachineTest.Context> checkCondition() {
        return (ctx) -> {return true;};
    }


    private Action<StateMachineTest.Context,String> doAction() {
        return new Action<StateMachineTest.Context,String>() {
            @Override
            public String execute(StateMachineTest.Context ctx) {
                return null;
            }
        };

    }

    public static class Optype{
        public static boolean isLoanFirst(Integer code){
            return code == 1;
        }
    }
}
