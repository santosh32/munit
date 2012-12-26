package org.mule.munit.runner.mule;


import org.junit.Before;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.output.TestOutputHandler;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MunitTestTest {
    private MunitFlow before;
    private MunitFlow after;
    private MunitTestFlow testFlow;
    private TestOutputHandler handler;
    private MuleEvent muleEvent;

    @Before
    public void setUpMocks(){
        before = mock(MunitFlow.class);
        after = mock(MunitFlow.class);
        testFlow = mock(MunitTestFlow.class);
        handler = mock(TestOutputHandler.class);
        muleEvent = mock(MuleEvent.class);

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

        when(testFlow.process(muleEvent)).thenThrow(new DefaultMuleException("Error"));
        TestResult testResult = test.run();

        verify(testFlow,times(1)).process(muleEvent);
        verify(before,times(1)).process(muleEvent);
        verify(after,times(1)).process(muleEvent);

        assertFalse(testResult.hasSucceeded());
        assertEquals("Error", testResult.getError().getShortMessage());
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
