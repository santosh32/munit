package org.mule.java;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mule.munit.mail.MailServer;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import static junit.framework.Assert.assertFalse;

        @Ignore
public class MailServerTest extends FunctionalMunitSuite{

    private static MailServer mailServer;

    @BeforeClass
    public static void startServer(){
        mailServer = new MailServer();
        mailServer.start();
    }


    @AfterClass
    public static void stopServer(){
        mailServer.stop();
    }

    @Override
    protected boolean mockConnectors() {
        return false;
    }

    @Override
    protected boolean mockInboundEndpoints() {
        return false;
    }

    @Override
    protected String getConfigResources() {
        return "mail-server-config.xml";
    }

    @Test
    @Ignore
    public void testingServerCall() throws Exception {
        runFlow("NotificationOfError", testEvent("hello"));

        assertFalse(mailServer.getReceivedMessages().isEmpty());
    }
}
