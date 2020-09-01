package com.cmt.statemachine.test;

import com.cmt.statemachine.Action;
import com.cmt.statemachine.Condition;
import com.cmt.statemachine.StateMachine;
import com.cmt.statemachine.StateMachineFactory;
import com.cmt.statemachine.annotation.StateConfig;
import com.cmt.statemachine.builder.StateMachineBuilder;
import com.cmt.statemachine.builder.StateMachineBuilderFactory;
import com.cmt.statemachine.util.StateUtil;
import org.junit.Test;

/**
 * @author dingchenchen
 * @since 2020/8/31
 */
public class StateConfigAnnotationTest {

    @StateConfig(descField = "desc", enableDesc = true)
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
        EVENT1, EVENT2, EVENT3, EVENT4, INTERNAL_EVENT
    }

    @Test
    public void testStateConfigAnnotation() {
        System.out.println(StateUtil.getEnableDesc(States.STATE2));
    }

    @Test
    public void testExternalInternalNormal(){
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
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransitions()
                .fromAmong(States.STATE1, States.STATE2, States.STATE3)
                .to(States.STATE4)
                .on(Events.EVENT4)
                .when(checkCondition())
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
}
