package com.cmt.statemachine;

/**
 * @author tuzhenxian
 * @date 20-6-23
 */
public class Request<C,P> {
    /**
     * condition
     */
    private C condition;
    /**
     * request
     */
    private P parameter;

    public void setCondition(C condition) {
        this.condition = condition;
    }

    public void setParameter(P parameter) {
        this.parameter = parameter;
    }

    private Request() {
    }

    public Request with(C condition,P parameter){
        Request r=new Request<>();
        r.setCondition(condition);
        r.setParameter(parameter);
        return r;
    }

    public Request with(P parameter){
        Request r=new Request<>();
        r.setParameter(parameter);
        return r;
    }
}
