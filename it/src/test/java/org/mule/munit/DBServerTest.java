package org.mule.munit;

import org.mule.munit.runner.java.AbstractMuleSuite;

public class DBServerTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "dbserver-test.xml";
    }
  }
