package org.mule.munit;


import org.mule.munit.runner.java.AbstractMuleSuite;

public class MClientTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mclient-test.xml";
    }
  }
