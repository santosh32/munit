package org.mule.munit;


import org.junit.Ignore;
import org.mule.munit.runner.java.AbstractMuleSuite;

@Ignore
public class MailServerTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mail-server-test.xml";
    }
  }
