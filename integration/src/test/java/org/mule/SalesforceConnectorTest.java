package org.mule;


import org.mule.munit.AbstractMuleSuite;

public class SalesforceConnectorTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mule-config.xml, test-config.xml";
    }
}
