package com.cmt.statemachine.test;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static guru.nidi.graphviz.attribute.Label.Justification.LEFT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

/**
 * @author dingchenchen
 * @since 2020/8/9
 */
public class GraphvizJavaTest {
    public static void main(String[] args) {
        Node
                main = node("main"),
                init = node(Label.markdown("**_init_**")),
                execute = node("execute"),
                compare = node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0)),
                mkString = node("mkString").with(Label.lines(LEFT, "make", "a", "multi-line")),
                printf = node("printf");

        List<Node> nodeList = new ArrayList<>();
        nodeList.add(execute.link(
                graph().with(mkString, printf),
                to(compare).with(Color.RED)));
        nodeList.add(
                init.link(mkString)
        );
        nodeList.add(main.link(
                to(node("parse").link(execute)).with(LinkAttr.weight(8))));
        nodeList.add(main.link(
                node("cleanup")));
        nodeList.add(main.link(
                to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED)));
      /*  Graph g = graph("example2").directed().with(
                execute.link(
                        graph().with(mkString, printf),
                        to(compare).with(Color.RED)),
                init.link(mkString),
                main.link(
                        to(node("parse").link(execute)).with(LinkAttr.weight(8)),
                        to(init).with(Style.DOTTED),
                        node("cleanup"),
                        to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED)));*/
        Graph g = graph("example2").directed().with(nodeList);

        try {
            Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("ex2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
