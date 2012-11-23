package org.mule.munit;

import org.mule.munit.runner.java.AbstractMuleSuite;

public class EnricherTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "enricher-test.xml";
    }
}