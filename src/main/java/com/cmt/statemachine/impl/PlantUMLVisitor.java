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
import java.util.Objects;

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

    @Override
    public void visitOnEntry(StateMachine<?, ?> visitable) {
        plantUMLStatements = new ArrayList<>();
        plantUMLStatements.add("@startuml");
    }

    /**
     * You have to add "[*] -> initialState" to mark it as a state machine diagram.
     * otherwise it will be recognized as a sequence diagram.
     *
     * @param visitable the element to be visited.
     */
    @Override
    public void visitOnExit(StateMachine<?, ?> visitable) {
        // add "[*] -> initialState"
        addInitStatement(visitable);
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
    private void addInitStatement(StateMachine<?, ?> visitable) {
        // 添加上PlantUML所需的开始状态描述语法：[*] --> initialState
        String initStatement = "[*] --> " + visitable.getInitialState();
        plantUMLStatements.add(initStatement);
    }

    @Override
    public void visitOnEntry(State<?, ?> state) {
        for (Transition transition : state.getTransitions()) {
            String sourceState = transition.getSource().getId().toString();
            String targetState = transition.getTarget().getId().toString();
            plantUMLStatements.add(sourceState + " --> " + targetState + " : " + EventUtil.getEventDesc(transition.getEvent()));
        }
        Object obj = StateUtil.getStateDescField(state.getId());
        if (Objects.nonNull(obj) && obj instanceof String) {
            String stateDesc = obj.toString();
            plantUMLStatements.add(state.getId().toString() + " : " + stateDesc);
        }
    }

    @Override
    public void visitOnExit(State<?, ?> state) {
    }
}
