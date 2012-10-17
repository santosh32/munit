package org.mule.munit.mp;

import java.util.Map;

public class MpBehavior {
    private String name;
    private String namespace;
    private Map<String,Object> parameters;
    private Object returnValue;

    public MpBehavior(String name, String namespace, Map<String, Object> parameters, Object returnValue) {
        this.name = name;
        this.namespace = namespace;
        this.parameters = parameters;
        this.returnValue = returnValue;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
