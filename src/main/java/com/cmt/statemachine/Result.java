package com.cmt.statemachine;

/**
 * 状态执行结果
 *
 * @author yonghuang
 */
public class Result<S, T> {
    private S state;
    private T t;

    public S getState() {
        return state;
    }

    public void setState(S state) {
        this.state = state;
    }

    public T getValue() {
        return t;
    }

    public void setValue(T t) {
        this.t = t;
    }
}
