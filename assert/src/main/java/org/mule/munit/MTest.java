package org.mule.munit;

public class MTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return System.getProperty("munit.resource");
    }
}
