package org.mule.munit;


import org.mule.munit.runner.java.AbstractMuleSuite;

public class MpMockingTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mp-mocking-test.xml";
    }
  }
