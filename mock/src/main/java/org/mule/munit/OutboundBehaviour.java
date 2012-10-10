package org.mule.munit;

import org.mule.api.NestedProcessor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 10/10/12
 * Time: 12:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OutboundBehaviour {
    
    private Object returnValue;
    private List<NestedProcessor> assertions;

    public OutboundBehaviour(Object returnValue, List<NestedProcessor> assertions) {
        this.returnValue = returnValue;
        this.assertions = assertions;
    }


    public Object getReturnValue() {
        return returnValue;
    }

    public List<NestedProcessor> getAssertions() {
        return assertions;
    }
}
