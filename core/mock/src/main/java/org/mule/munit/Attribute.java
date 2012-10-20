package org.mule.munit;


import org.mule.api.annotations.Configurable;

public class Attribute {
    /**
     * <p>The name of the attribute of the message processor</p>
     */
    @Configurable
    private String name;

    /**
     * <p>The object that need to match (can be a matcher expression)</p>
     */
    @Configurable
    private Object whereValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getWhereValue() {
        return whereValue;
    }

    public void setWhereValue(Object whereValue) {
        this.whereValue = whereValue;
    }
}
