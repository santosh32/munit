package org.mule.munit.config;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.MunitAssertion;

import static org.mockito.Mockito.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class RunAssertionMessageProcessorTest {

    private MunitAssertion munitAssertion;
    private MuleEvent muleEvent;
    private MuleEvent responseMuleEvent;

    @Before
    public void setUp() throws Exception {
        munitAssertion = mock(MunitAssertion.class);
        muleEvent = mock(MuleEvent.class);
        responseMuleEvent = mock(MuleEvent.class);
    }

    @Test
    public void testProcess() throws MuleException {
        when(munitAssertion.execute(muleEvent)).thenReturn(responseMuleEvent);
        assertionMp().process(muleEvent);

        verify(munitAssertion, times(1)).execute(muleEvent);
    }

    private RunAssertionMessageProcessor assertionMp() {
        RunAssertionMessageProcessor assertionMessageProcessor = new RunAssertionMessageProcessor();
        assertionMessageProcessor.setAssertion(munitAssertion);
        return assertionMessageProcessor;
    }

}
