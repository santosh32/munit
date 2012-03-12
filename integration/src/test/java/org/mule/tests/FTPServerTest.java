package org.mule.tests;

import org.mule.munit.AbstractMuleSuite;

/**
 * <p> Run tests that need the FTP server.</p>
 *
 * @author Federico, Fernando
 */
public class FTPServerTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mule-config.xml, ftpserver-test-config.xml";
    }
}
