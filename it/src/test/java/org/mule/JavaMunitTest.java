package org.mule;

import org.junit.Ignore;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import static junit.framework.Assert.assertEquals;


public class JavaMunitTest extends FunctionalMunitSuite{

    @Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }

    @Ignore
    @Test
    public void test() throws Exception {
        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .theReturn(muleMessageWithPayload("expected"));

        Object payload = runFlow("callingJira", testEvent("something")).getMessage().getPayload();

        assertEquals("expected", payload);
    }


}
