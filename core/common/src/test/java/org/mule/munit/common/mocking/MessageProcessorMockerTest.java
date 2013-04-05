package org.mule.munit.common.mocking;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.registry.MuleRegistry;
import org.mule.modules.interceptor.processors.MessageProcessorBehavior;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MessageProcessorMockerTest {
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
        mocker().when("testMp")
                .ofNamespace("testNamespace")
                .withAttributes(new HashMap<String, Object>())
                .thenReturn(message);

        verify(manager).addBehavior(any(MessageProcessorBehavior.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failIfNoMessageProcessorNameNotSet(){
        mocker()
                .thenReturn(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateThatThenThrowChecksMessageProcessorExistence(){
         mocker().thenThrow(new Exception());
    }

    @Test
    public void validateThatBehaviorIsAddedWhenThenThrow(){
        mocker().when("mp").thenThrow(new Exception());

        verify(manager).addBehavior(any(MessageProcessorBehavior.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failIfNoMessageProcessorNameNotSetWheReturnSame(){
        mocker()
                .thenReturnSameEvent();
    }

    @Test
    public void validateThatBehaviorIsAddedWhenThenReturnSame(){
        mocker().when("mp").thenThrow(new Exception());

        verify(manager).addBehavior(any(MessageProcessorBehavior.class));
    }

    private MessageProcessorMocker mocker() {
        return new MessageProcessorMocker(muleContext);
    }
}
