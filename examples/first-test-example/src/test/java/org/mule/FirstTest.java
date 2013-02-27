package org.mule;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import static junit.framework.Assert.assertEquals;


/**
 * Munit test created with JAVA
 */
public class FirstTest extends FunctionalMunitSuite {

    /**
     *
     * @return The location of your MULE config file
     */
    @Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }


    @Test
    public void validateEchoFlow() throws Exception {
        MuleEvent resultEvent = runFlow("echoFlow", testEvent("Hello world!"));

        assertEquals("Hello world!", resultEvent.getMessage().getPayloadAsString());
    }
}
