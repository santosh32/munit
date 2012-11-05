package org.mule.munit.common.endpoint;

import org.junit.Before;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
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
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertFalse;
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
    private MuleMessage muleMessage;

    @Before
    public void setUp(){
        realEndpoint = mock(OutboundEndpoint.class);
        event = mock(MuleEvent.class);
        context = mock(MuleContext.class);
        registry = mock(MuleRegistry.class);
        expressionManager = mock(ExpressionManager.class);
        endpointManager = mock(MockEndpointManager.class);
        messageProcessor = mock(MessageProcessor.class);
        muleMessage = mock(DefaultMuleMessage.class);
        
        when(event.getMuleContext()).thenReturn(context);
        when(event.getMessage()).thenReturn(muleMessage);
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


    private HashMap<String, Object> properties() {
        HashMap<String, Object> prop = new HashMap<String, Object>();
        prop.put("any", "any");
        return prop;
    }

    @Test
    public void testVerifyAssertionIsCalled() throws MuleException {
        when(endpointManager.getBehaviorFor(ADDRESS)).thenReturn(new OutboundBehavior(muleMessage, buildMessageAssertions()));

        new MockOutboundEndpoint(realEndpoint).process(event);

        verify(messageProcessor, times(1)).process(event);
    }


    @Test
    public void testVerifyNotAssert() throws MuleException {
        when(endpointManager.getBehaviorFor(ADDRESS)).thenReturn(new OutboundBehavior(muleMessage, null));

        new MockOutboundEndpoint(realEndpoint).process(event);

        verify(messageProcessor, never()).process(event);
    }

    @Test
    public void testNotDefinedMethods(){
        MockOutboundEndpoint endpoint = new MockOutboundEndpoint(null);

        assertNull(endpoint.getResponseProperties());
        assertNull(endpoint.getEndpointURI());
        assertNull(endpoint.getAddress());
        assertNull(endpoint.getEncoding());
        assertNull(endpoint.getConnector());
        assertNull(endpoint.getTransformers());
        assertNull(endpoint.getResponseTransformers());
        assertNull(endpoint.getProperties());
        assertNull(endpoint.getProperty(null));
        assertNull(endpoint.getProtocol());
        assertNull(endpoint.getFilter());
        assertNull(endpoint.getTransactionConfig());
        assertNull(endpoint.getSecurityFilter());
        assertNull(endpoint.getMessageProcessorsFactory());
        assertNull(endpoint.getMessageProcessors());
        assertNull(endpoint.getResponseMessageProcessors());
        assertNull(endpoint.getExchangePattern());
        assertNull(endpoint.getInitialState());
        assertNull(endpoint.getMuleContext());
        assertNull(endpoint.getRetryPolicyTemplate());
        assertNull(endpoint.getRetryPolicyTemplate());
        assertNull(endpoint.getMimeType());
        assertNull(endpoint.getRedeliveryPolicy());
        assertFalse(endpoint.isReadOnly());
        assertFalse(endpoint.isDisableTransportTransformer());
        assertFalse(endpoint.isProtocolSupported(""));
        assertEquals(0, endpoint.getResponseTimeout());
    }

    private ArrayList<MessageProcessor> buildMessageAssertions() {
        ArrayList<MessageProcessor> assertions = new ArrayList<MessageProcessor>();
        assertions.add(messageProcessor);
        return assertions;
    }


}
