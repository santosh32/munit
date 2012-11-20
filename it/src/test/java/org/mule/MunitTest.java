package org.mule;


import org.mule.munit.runner.java.AbstractMuleSuite;

public class MunitTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mail-server-test.xml";
    }
  }
