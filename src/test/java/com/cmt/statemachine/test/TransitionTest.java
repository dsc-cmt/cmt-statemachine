package com.cmt.statemachine.test;

import com.cmt.statemachine.State;
import com.cmt.statemachine.Transition;
import com.cmt.statemachine.impl.StateHelper;
import com.cmt.statemachine.impl.TransitionImpl;
import com.cmt.statemachine.impl.TransitionType;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Allen Ding
 * @since 2022/12/3
 */
public class TransitionTest {

    enum States {
        STATE1, STATE2, STATE3, STATE4
    }

    enum Events {
        EVENT1, EVENT2, EVENT3, EVENT4, INTERNAL_EVENT
    }

    @Test
    public void testNoMatchTransit() {
        Map<States, State<States, Events>> stateMap = new HashMap<>();
        Transition<States, Events> newTransition = new TransitionImpl<>();
        newTransition.setSource(StateHelper.getState(stateMap,States.STATE1));
        newTransition.setTarget(StateHelper.getState(stateMap,States.STATE2));
        newTransition.setEvent(Events.EVENT1);
        newTransition.setType(TransitionType.EXTERNAL);
        newTransition.setCondition(context -> false);
        State target = newTransition.transit(new Object(), true);
        Assert.assertEquals(States.STATE1, target.getId());
    }

    @Test
    public void testNoMatchTransitWithResult() {
        Map<States, State<States, Events>> stateMap = new HashMap<>();
        Transition<States, Events> newTransition = new TransitionImpl<>();
        newTransition.setSource(StateHelper.getState(stateMap,States.STATE1));
        newTransition.setTarget(StateHelper.getState(stateMap,States.STATE2));
        newTransition.setEvent(Events.EVENT1);
        newTransition.setType(TransitionType.EXTERNAL);
        newTransition.setCondition(context -> false);
        Object result = newTransition.transitWithResult(new Object(), true);
        Assert.assertNull(result);
    }
}
