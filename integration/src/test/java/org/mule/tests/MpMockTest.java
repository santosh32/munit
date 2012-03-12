package org.mule.tests;

import org.mule.munit.AbstractMuleSuite;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 3/12/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class MpMockTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "mpmock-test-config.xml";
    }
}
