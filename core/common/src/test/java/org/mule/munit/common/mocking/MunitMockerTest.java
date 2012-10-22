package org.mule.munit.common.mocking;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.registry.MuleRegistry;
import org.mule.munit.common.mp.MockedMessageProcessorBehavior;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MunitMockerTest {
    private MuleContext muleContext;
    private MuleRegistry muleRegistry;
    private MockedMessageProcessorManager manager;
    private MuleMessage message;

    @Before
    public void setUp(){
        muleContext = mock(MuleContext.class);
        muleRegistry = mock(MuleRegistry.class);
        manager = mock(MockedMessageProcessorManager.class);
        message = mock(MuleMessage.class);

        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(MockedMessageProcessorManager.ID)).thenReturn(manager);

    }
    
    @Test
    public void addBehavior(){
        MunitMocker munitMocker = new MunitMocker(muleContext);
        munitMocker.expectMessageProcessor("testMp")
                .ofNamespace("testNamespace")
                .withAttributes(new HashMap<String, Object>())
                .toReturn(message);

        verify(manager).addBehavior(any(MockedMessageProcessorBehavior.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failIfNoMessageProcessorNameNotSet(){
        MunitMocker munitMocker = new MunitMocker(muleContext);
        munitMocker
                .toReturn(message);
    }
}
