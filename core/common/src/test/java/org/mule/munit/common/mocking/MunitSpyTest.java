package org.mule.munit.common.mocking;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.MuleRegistry;
import org.mule.munit.common.mp.MessageProcessorId;
import org.mule.munit.common.mp.MockedMessageProcessorManager;
import org.mule.munit.common.mp.SpyAssertion;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MunitSpyTest {

    private MuleContext muleContext;
    private MuleRegistry muleRegistry;
    private MockedMessageProcessorManager manager;

    @Before
    public void setUp(){
        muleContext = mock(MuleContext.class);
        muleRegistry = mock(MuleRegistry.class);
        manager = mock(MockedMessageProcessorManager.class);

        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(MockedMessageProcessorManager.ID)).thenReturn(manager);

    }
    
    @Test
    public void testAddSpy(){
        new MunitSpy(muleContext).spyMessageProcessor("test")
                .ofNamespace("testNamespace")
                .running(new ArrayList<SpyProcess>(), new ArrayList<SpyProcess>());
        
        verify(manager).addSpyAssertion(any(MessageProcessorId.class), any(SpyAssertion.class));
    }

    @Test
    public void testRunSpyProcess() throws MuleException {
        ArrayList<SpyProcess> calls = new ArrayList<SpyProcess>();
        Spy spy = new Spy();
        calls.add(spy);
        SpyAssertion spyAssertion = new MunitSpy(muleContext).spyMessageProcessor("test")
                .ofNamespace("testNamespace")
                .createSpyAssertion(calls, calls);

        for (MessageProcessor mp : spyAssertion.getAfterMessageProcessors()) {
            mp.process(null);
        }

        for (MessageProcessor mp : spyAssertion.getBeforeMessageProcessors()) {
            mp.process(null);
        }

        assertEquals(2, spy.timesCalled);

    }
    
    private class Spy implements SpyProcess{
        int timesCalled =0;

        @Override
        public void spy(MuleEvent event) {
            timesCalled ++;
        }
    }
}
