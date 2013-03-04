package org.mule.munit;

import org.mule.munit.runner.java.AbstractMuleSuite;

/**
 * Created with IntelliJ IDEA.
 * User: fernandofederico
 * Date: 3/4/13
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class PollTest extends AbstractMuleSuite {
    @Override
    public String getConfigResources() {
        return "poll-config-test.xml";
    }
}
