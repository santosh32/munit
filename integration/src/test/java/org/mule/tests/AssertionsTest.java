package org.mule.tests;

import org.mule.munit.AbstractMuleSuite;

/**
 * <p>Testing simple Scenarios with Munit</p>
 *
 * @author Federico, Fernando
 */
public class AssertionsTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mule-config.xml, assertions-test-config.xml";
    }
}
