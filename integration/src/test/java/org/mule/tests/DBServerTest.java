package org.mule.tests;

import org.mule.munit.AbstractMuleSuite;

/**
 * <p> Run tests that need the DB server.</p>
 *
 * @author Federico, Fernando
 */
public class DBServerTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "dbserver-test-config.xml";
    }
}
