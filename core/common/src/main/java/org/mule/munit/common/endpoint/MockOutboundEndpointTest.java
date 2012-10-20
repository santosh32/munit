package org.mule.munit.common.endpoint;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.MuleRegistry;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class MockOutboundEndpointTest {

    public static final String ADDRESS = "http://localhost:8080/test";
    public static final String ADDRESS_EXPRESSION = "#[string:http://localhost:8080/test]";
    private OutboundEndpoint realEndpoint;
    private MuleEvent event;
    private MuleContext context;
    private MuleRegistry registry;
    private ExpressionManager expressionManager;
    private MockEndpointManager endpointManager;
    private MessageProcessor messageProcessor;

    @Before
    public void setUp(){
        realEndpoint = mock(OutboundEndpoint.class);
        event = mock(MuleEvent.class);
        context = mock(MuleContext.class);
        registry = mock(MuleRegistry.class);
        expressionManager = mock(ExpressionManager.class);
        endpointManager = mock(MockEndpointManager.class);
        messageProcessor = mock(MessageProcessor.class);
        
        when(event.getMuleContext()).thenReturn(context);
        when(context.getRegistry()).thenReturn(registry);
        when(registry.lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(endpointManager);
        when(context.getExpressionManager()).thenReturn(expressionManager);
        when(realEndpoint.getAddress()).thenReturn(ADDRESS);
        when(expressionManager.isValidExpression(ADDRESS_EXPRESSION)).thenReturn(true);
        when(expressionManager.evaluate(ADDRESS_EXPRESSION, event)).thenReturn(ADDRESS);
    }

    /**
     * <p>If the endpoint manager does not have any mocked address then execute real endpoint</p>
     */
    @Test
    public void testNotMockedEndpoint() throws MuleException {
        when(endpointManager.getBehaviorFor(ADDRESS)).thenReturn(null);

        new MockOutboundEndpoint(realEndpoint).process(event);

        verify(realEndpoint, times(1)).process(event);
    }
    
    @Test
    public void testVerifyAssertionIsCalled() throws MuleException {
        when(endpointManager.getBehaviorFor(ADDRESS)).thenReturn(new OutboundBehavior("payload", buildMessageAssertions()));

        new MockOutboundEndpoint(realEndpoint).process(event);

        verify(messageProcessor, times(1)).process(event);
        verify(event,times(1)).setMessage(any(MuleMessage.class));
    }


    @Test
    public void testVerifyNotAssert() throws MuleException {
        when(endpointManager.getBehaviorFor(ADDRESS)).thenReturn(new OutboundBehavior("payload", null));

        new MockOutboundEndpoint(realEndpoint).process(event);

        verify(messageProcessor, never()).process(event);
        verify(event,times(1)).setMessage(any(MuleMessage.class));
    }

    private ArrayList<MessageProcessor> buildMessageAssertions() {
        ArrayList<MessageProcessor> assertions = new ArrayList<MessageProcessor>();
        assertions.add(messageProcessor);
        return assertions;
    }


}
