package com.cmt.statemachine.impl;

import com.cmt.statemachine.State;
import com.cmt.statemachine.StateMachine;
import com.cmt.statemachine.Transition;
import com.cmt.statemachine.Visitor;

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
     *
     * You have to add "[*] -> initialState" to mark it as a state machine diagram.
     * otherwise it will be recognized as a sequence diagram.
     *
     * @param visitable the element to be visited.
     */
    @Override
    public void visitOnEntry(StateMachine<?, ?> visitable) {
        plantUMLStatements = new ArrayList<>();
        System.out.println("@startuml");
        plantUMLStatements.add("@startuml");
    }

    @Override
    public void visitOnExit(StateMachine<?, ?> visitable) {
        System.out.println("@enduml");
        plantUMLStatements.add("@enduml");
        //生成 plantuml.txt 文件
        plantUMLStatements.forEach(statement->{
            File file = new File("plantuml.txt");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file, true);
                PrintStream out = new PrintStream(fos);
                String str = statement +"\r\n";
                out.print(str);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void visitOnEntry(State<?, ?> state) {
        for(Transition transition: state.getTransitions()){
            System.out.println(transition.getSource().getId()+" --> "+transition.getTarget().getId()+" : "+transition.getEvent());
            plantUMLStatements.add(transition.getSource().getId()+" --> "+transition.getTarget().getId()+" : "+transition.getEvent());
        }
    }

    @Override
    public void visitOnExit(State<?, ?> state) {
    }
}
