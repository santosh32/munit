package org.mule.munit.common.mocking;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.MuleRegistry;
import org.mule.munit.common.mp.MessageProcessorId;
import org.mule.munit.common.mp.MockedMessageProcessorManager;
import org.mule.munit.common.mp.SpyAssertion;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MunitSpierTest {

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
        new MunitSpier(muleContext).spyMessageProcessor("test")
                .ofNamespace("testNamespace")
                .running(new ArrayList<MessageProcessor>(), new ArrayList<MessageProcessor>());
        
        verify(manager).addSpyAssertion(any(MessageProcessorId.class),any(SpyAssertion.class));
    }
}
