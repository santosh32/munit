package org.mule.munit;


import org.mule.munit.runner.java.AbstractMuleSuite;

public class MailServerTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mail-server-test.xml";
    }
  }
