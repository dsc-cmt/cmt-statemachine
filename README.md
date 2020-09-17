[![Build Status](https://travis-ci.com/dsc-cmt/cmt-statemachine.svg?branch=master)](https://travis-ci.com/github/dsc-cmt/cmt-statemachine)
[![codecov](https://codecov.io/gh/dsc-cmt/cmt-statemachine/branch/master/graph/badge.svg)](https://codecov.io/gh/dsc-cmt/cmt-statemachine)

**cmt-statemachine**【_草帽_】，是一个基于 [cola-statemachine](https://github.com/alibaba/COLA/tree/master/cola-statemachine) 的优化版状态机。

**cmt-statemachine**新增特性：

+ 1.支持if/elseif多条件判断
+ 2.触发事件的接口支持返回业务对象
+ 3.触发事件的接口支持传递任意类型的入参
+ 4.触发事件的接口支持分别传入条件参数和执行参数

## 如何使用cmt-statemachine
### 第一步 引入依赖
```xml
<dependency>
    <groupId>com.github.dsc-cmt</groupId>
    <artifactId>cmt-statemachine</artifactId>
    <version>1.3.1</version>
</dependency>

```
### 第二步 定义自己的状态与事件
```java
static enum States {
        STATE1, STATE2, STATE3, STATE4
    }

    static enum Events {
        EVENT1, EVENT2, EVENT3, EVENT4, INTERNAL_EVENT
    }
```
### 第三步 通过工厂构造状态机
```java
@Configuration
public class StateMachineFactory {
    private final String MACHINE_ID="custom";
    @Bean
    public StateMachine stateMachine(){
        StateMachineBuilder<States, Events> builder = StateMachineBuilderFactory.create();
                builder.initialState(States.STATE1)
                        .externalTransition()
                        .from(States.STATE1)
                        .to(States.STATE2)
                        .on(Events.EVENT1)
                        .when(checkCondition())
                        .perform(doAction());
        
         return stateMachine = builder.build(MACHINE_ID);
    }
}
        
```
### 第四步 触发事件
有三种的事件触发方式 适用于不同场景
```java
@Autowired
private StateMachine stateMachine;
//1.仅获取下一个状态
States next=stateMachine.fireEvent(States.STATE1, Operations.OPER1, new Req());
//2.获取perform执行结果且不含条件,适用于不含when参数或者when条件参数与perform参数相同的情况
 T t=stateMachine.fireEventWithResult(States.STATE1, Operations.OPER1, new Req());
//3.获取perform执行结果包含条件参数,适用于when条件参数与perform参数不一致的情况
 T t=stateMachine.fireEventWithResult(States.STATE1, Operations.OPER1, new Cond(), new Req());
```
### com.cmt.statemachine.Action.execute方法的参数,可以实现stateAware接口或者继承DefaultStateAwareImpl<>,来获取下一个状态
```java
class Req implements StateAware<States>{
    private States next;
    ...
     @Override
        public void setNextState(States states) {
            this.next=states;
        }
}

class Req extends DefaultStateAwareImpl<States>{
  
}
```
## 如何根据配置的状态机生成 PlantUML 状态图
### 执行 StateMachine 接口的 generatePlantUML 方法，得到 plantuml.txt 文件:
```java
public interface StateMachine<S, E> extends Visitable {
    ...
    /**
     * generate plantuml.txt which is used to generate the state diagram.
     */
    void generatePlantUML();
}
```
可以参考 StateDiagramTest.testPlantUML()。   
备注：  
1、[PlantUML](https://plantuml.com/zh/starting) 要绘制状态图, 需要安装 [Graphviz](https://www.graphviz.org/download/) ；   
2、InterlliJ IDEA 中有一款 PlantUML Integration 插件，可以配合 Graphviz 编辑生成 PlantUML 图。

## 项目引入了 graphviz-java 支持生成简易的状态图图片
```java
public interface StateMachine<S, E> extends Visitable {
    ...
    /**
     * generate state diagram.
     */
    void generateStateDiagram();
}
```
可以参考 StateDiagramTest.testGenerateStateDiagram()。
