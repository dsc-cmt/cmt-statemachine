package com.cmt.statemachine.impl;

import com.cmt.statemachine.State;
import com.cmt.statemachine.StateMachine;
import com.cmt.statemachine.Transition;
import com.cmt.statemachine.Visitor;
import com.cmt.statemachine.util.EventUtil;
import com.cmt.statemachine.util.StateUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PlantUMLVisitor
 *
 * @author Frank Zhang
 * @date 2020-02-09 7:47 PM
 */
public class PlantUMLVisitor implements Visitor {

    /**
     * 存储构建PlantUML状态图的语句
     */
    private List<String> plantUMLStatements;

    /**
     * Since the state machine is stateless, there is no initial state.
     * <p>
     * You have to add "[*] -> initialState" to mark it as a state machine diagram.
     * otherwise it will be recognized as a sequence diagram.
     *
     * @param visitable the element to be visited.
     */
    @Override
    public void visitOnEntry(StateMachine<?, ?> visitable) {
        plantUMLStatements = new ArrayList<>();
        plantUMLStatements.add("@startuml");
    }

    @Override
    public void visitOnExit(StateMachine<?, ?> visitable) {
        plantUMLStatements.add(getInitStatement(visitable));
        plantUMLStatements.add("@enduml");
        //生成 plantuml.txt 文件
        plantUMLStatements.forEach(statement -> {
            File file = new File("plantuml.txt");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file, true);
                PrintStream out = new PrintStream(fos);
                String str = statement + "\r\n";
                out.print(str);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 拼接uml初始化语句
     *
     * @param visitable
     * @return
     */
    private String getInitStatement(StateMachine<?, ?> visitable) {
        // 添加上PlantUML所需的开始状态描述语法：[*] --> initialState
        boolean enableDesc = StateUtil.getEnableDesc(visitable.getInitialState());
        String initStatement = "[*] --> " + visitable.getInitialState();
        if (enableDesc) {
            Object obj = StateUtil.getStateDesc(visitable.getInitialState());
            if (obj instanceof String) {
                initStatement = "[*] --> " + obj.toString();
            }
        }
        return initStatement;
    }

    @Override
    public void visitOnEntry(State<?, ?> state) {
        boolean enableDesc = StateUtil.getEnableDesc(state.getId());
        for (Transition transition : state.getTransitions()) {
            String sourceState = transition.getSource().getId().toString();
            String targetState = transition.getTarget().getId().toString();
            if (enableDesc) {
                Object obj = StateUtil.getStateDesc(transition.getSource().getId());
                if (obj instanceof String) {
                    sourceState = obj.toString();
                    obj = StateUtil.getStateDesc(transition.getTarget().getId());
                    targetState = obj.toString();
                }
            }
            plantUMLStatements.add(sourceState + " --> " + targetState + " : " + EventUtil.getEventDesc(transition.getEvent()));
        }
    }

    @Override
    public void visitOnExit(State<?, ?> state) {
    }
}
