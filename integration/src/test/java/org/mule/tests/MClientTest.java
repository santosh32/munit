package org.mule.tests;

import org.mule.munit.AbstractMuleSuite;

/**
 * <p> Running Tests with transport calls</p>
 *
 * @author Federico, Fernando
 */
public class MClientTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mule-config.xml,  mclient-test-config.xml";
    }
}
