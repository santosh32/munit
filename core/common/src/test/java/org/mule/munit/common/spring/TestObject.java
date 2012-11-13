package org.mule.munit.common.spring;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class TestObject {
    private String string1;
    private Integer integer1;

    public TestObject() {
    }

    public TestObject(String string1, Integer integer1) {
        this.string1 = string1;
        this.integer1 = integer1;
    }

    public String getString1() {
        return string1;
    }

    public Integer getInteger1() {
        return integer1;
    }
}
