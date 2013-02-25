package org.mule.munit.runner.mule;


import org.junit.Before;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.config.MuleProperties;
import org.mule.api.registry.MuleRegistry;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.mp.MessageProcessorCall;
import org.mule.munit.common.mp.MessageProcessorId;
import org.mule.munit.common.mp.MockedMessageProcessorManager;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.output.TestOutputHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MunitTestTest {
    private MunitFlow before = mock(MunitFlow.class);
    private MunitFlow after = mock(MunitFlow.class);
    private MunitTestFlow testFlow = mock(MunitTestFlow.class);
    private TestOutputHandler handler = mock(TestOutputHandler.class);
    private MuleEvent muleEvent= mock(MuleEvent.class);
    private MuleContext muleContext = mock(MuleContext.class);
    private MuleRegistry muleRegistry = mock(MuleRegistry.class);
    private MockEndpointManager endpointManager = mock(MockEndpointManager.class);
    private MockedMessageProcessorManager processorManager = mock(MockedMessageProcessorManager.class);



    @Before
    public void setUpMocks(){
        when(muleEvent.getMuleContext()).thenReturn(muleContext);
        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(endpointManager);
        when(muleRegistry.lookupObject(MockedMessageProcessorManager.ID)).thenReturn(processorManager);
    }

    /**
     * If everything runs ok, then the before, after and test must be called
     */
    @org.junit.Test
    public void testRunSuccessful() throws MuleException {
        MunitTest test = new MockedTest(buildList(before), testFlow, buildList(after), handler);

        TestResult testResult = test.run();

        verify(testFlow,times(1)).process(muleEvent);
        verify(before,times(1)).process(muleEvent);
        verify(after,times(1)).process(muleEvent);

        assertTrue(testResult.hasSucceeded());
    }

    /**
     * If Test has a failure, add it to the result
     */
    @org.junit.Test
    public void testRunWithFailure() throws MuleException {
        MunitTest test = new MockedTest(buildList(before), testFlow, buildList(after), handler);

        when(testFlow.process(muleEvent)).thenThrow(new AssertionError("Error"));
        TestResult testResult = test.run();

        verify(testFlow,times(1)).process(muleEvent);
        verify(before,times(1)).process(muleEvent);
        verify(after,times(1)).process(muleEvent);

        assertFalse(testResult.hasSucceeded());
        assertEquals("Error", testResult.getFailure().getShortMessage());
    }

    /**
     * If Test has an error, add it to the result
     */
    @org.junit.Test
    public void testRunWithError() throws MuleException {
        MunitTest test = new MockedTest(buildList(before), testFlow, buildList(after), handler);

        when(processorManager.getCalls()).thenReturn(createTestCalls());
        when(testFlow.process(muleEvent)).thenThrow(new DefaultMuleException("Error"));

        TestResult testResult = test.run();

        verify(testFlow,times(1)).process(muleEvent);
        verify(before,times(1)).process(muleEvent);
        verify(after,times(1)).process(muleEvent);

        assertFalse(testResult.hasSucceeded());
        assertEquals("Error", testResult.getError().getShortMessage());
        assertTrue(testResult.getError().getFullMessage().contains("namespace2"));
        assertTrue(testResult.getError().getFullMessage().contains("mp2"));
        assertTrue(testResult.getError().getFullMessage().contains("namespace1"));
        assertTrue(testResult.getError().getFullMessage().contains("mp1"));
    }

    private ArrayList<MessageProcessorCall> createTestCalls() {
        ArrayList<MessageProcessorCall> calls = new ArrayList<MessageProcessorCall>();
        calls.add(new MessageProcessorCall(new MessageProcessorId("mp1", "namespace1")));
        calls.add(new MessageProcessorCall(new MessageProcessorId("mp2", "namespace2")));

        return calls;
    }


    private List<MunitFlow> buildList(MunitFlow ... flows) {
        return Arrays.asList(flows);
    }

    private class MockedTest extends MunitTest {
        public MockedTest(List<MunitFlow> before, MunitTestFlow test, List<MunitFlow> after, TestOutputHandler outputHandler) {
            super(before, test, after, outputHandler);
        }

        @Override
        protected MuleEvent muleEvent() {
            return muleEvent;
        }
    }
}
