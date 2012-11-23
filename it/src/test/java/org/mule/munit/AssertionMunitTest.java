package org.mule.munit;


import org.mule.munit.runner.java.AbstractMuleSuite;

public class AssertionMunitTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "assertion-munit-test.xml";
    }
}
