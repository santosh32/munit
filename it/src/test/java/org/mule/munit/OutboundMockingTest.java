package org.mule.munit;


import org.mule.munit.runner.java.AbstractMuleSuite;

public class OutboundMockingTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "outbound-mocking-test.xml";
    }
  }
