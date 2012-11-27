package org.mule.java;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.common.mocking.SpyProcess;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class MpMockingJavaTest extends FunctionalMunitSuite {
    @Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }
    
    @Test
    public void testMockMp() throws Exception {
        whenMessageProcessor("echo-component").theReturn(muleMessageWithPayload("expectedPayload"));

        MuleEvent eventResult = runFlow("echoFlow", testEvent("anotherString"));

        assertEquals("expectedPayload", eventResult.getMessage().getPayload());

    }
    
    @Test
    public void testMockWithoutChangingPayload() throws Exception {
        whenMessageProcessor("create-group").ofNamespace("jira").theReturnSameEvent();

        MuleEvent eventResult = runFlow("callingJira", testEvent(" Hello world!"));

        assertEquals(" Hello world!", eventResult.getMessage().getPayload());
    }

    @Test
    public void testMpWithParameters() throws Exception {
        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .withAttributes(attributes())
                .theReturn(muleMessageWithPayload("expectedPayload"));

        MuleEvent eventResult = runFlow("callingJira", testEvent("anotherString"));

        verifyCallOfMessageProcessor("create-group")
                .ofNamespace("jira").atLeast(1);

        verifyCallOfMessageProcessor("create-group")
                .ofNamespace("jira").times(1);

        assertEquals("expectedPayload", eventResult.getMessage().getPayload());

    }
    
    @Test
    public void demoTest() throws Exception {
        expectOutboundEndpointWithAddress("jdbc://lookupJob")
                .toReturn(muleMessageWithPayload(jdbcPayload()));


        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .withAttributes(anyAttributes())
                .theReturn(muleMessageWithPayload("createGroupResult"));

        MuleEvent eventResult = runFlow("main", testEvent(" Hello world!]"));

         assertEquals("createGroupResult", eventResult.getMessage().getPayload());
         assertEquals("someGroup", eventResult.getMessage().getInvocationProperty("job"));
    }


    @Test
    public void testWithSpy() throws Exception {
        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .withAttributes(attributes())
                .theReturn(muleMessageWithPayload("expectedPayload"));

        spyMessageProcessor("create-group")
                .ofNamespace("jira")
                .running(beforeCallSpy(), afterCallSpy());


        MuleEvent eventResult = runFlow("callingJira", testEvent("anotherString"));

        verifyCallOfMessageProcessor("create-group")
                .ofNamespace("jira").atLeast(1);

        verifyCallOfMessageProcessor("create-group")
                .ofNamespace("jira").times(1);

        assertEquals("expectedPayload", eventResult.getMessage().getPayload());

    }


    @Test(expected = Exception.class)
    public void testingMockTrowsException() throws Exception {
        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .thenThrow(new Exception());

        runFlow("callingJira", testEvent("anotherString"));
    }

    private List<Map<String, String>> jdbcPayload() {
        List<Map<String,String>> resultOfJdbc = new ArrayList<Map<String,String>>();
        Map<String,String> r = new HashMap<String,String>();
        r.put("jobtitle", "someGroup");
        resultOfJdbc.add(r);
        return resultOfJdbc;
    }

    private ArrayList<SpyProcess> afterCallSpy() {
        ArrayList<SpyProcess> spyProcesses = new ArrayList<SpyProcess>();
        spyProcesses.add(new AfterSpy());
        return spyProcesses;
    }

    private ArrayList<SpyProcess> beforeCallSpy() {
        ArrayList<SpyProcess> spyProcesses = new ArrayList<SpyProcess>();
        spyProcesses.add(new BeforeSpy());
        return spyProcesses;
    }

    private HashMap<String, Object> attributes() {
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("groupName", "someGroupName");
        attributes.put("userName", anyString());
        return attributes;
    }

    private HashMap<String, Object> anyAttributes() {
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("groupName", anyString());
        attributes.put("userName", anyString());
        return attributes;
    }

    private class BeforeSpy implements SpyProcess{
        @Override
        public void spy(MuleEvent event) throws MuleException {
            assertEquals("anotherString", event.getMessage().getPayload());
        }
    }

    private class AfterSpy implements SpyProcess{

        @Override
        public void spy(MuleEvent event) throws MuleException {
            assertEquals("expectedPayload", event.getMessage().getPayload());
        }
    }
}
