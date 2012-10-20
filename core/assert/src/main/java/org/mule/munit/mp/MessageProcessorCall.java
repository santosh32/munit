package org.mule.munit.mp;


import java.util.Map;

public class MessageProcessorCall {
    private String name;
    private String namespace;
    private Map<String,Object> attributes;

    public MessageProcessorCall(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
