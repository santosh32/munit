package org.mule.munit.common.endpoint;

import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.api.endpoint.EndpointMessageProcessorChainFactory;
import org.mule.api.endpoint.EndpointURI;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.retry.RetryPolicyTemplate;
import org.mule.api.routing.filter.Filter;
import org.mule.api.security.EndpointSecurityFilter;
import org.mule.api.transaction.TransactionConfig;
import org.mule.api.transformer.Transformer;
import org.mule.api.transport.Connector;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.common.mocking.NonDefinedPayload;
import org.mule.processor.AbstractRedeliveryPolicy;

import java.util.List;
import java.util.Map;

/**
 * <p>Mocked outbound Endpoint</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockOutboundEndpoint implements OutboundEndpoint{

    private OutboundEndpoint realEndpoint;

    public MockOutboundEndpoint(OutboundEndpoint realEndpoint) {
        this.realEndpoint = realEndpoint;
    }

    @Override
    public List<String> getResponseProperties() {
        return null;
    }

    @Override
    public EndpointURI getEndpointURI() {
        return null;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getEncoding() {
        return null;
    }

    @Override
    public Connector getConnector() {
        return null;
    }

    @Override
    public List<Transformer> getTransformers() {
        return null;
    }

    @Override
    public List<Transformer> getResponseTransformers() {
        return null;
    }

    @Override
    public Map getProperties() {
        return null;
    }

    @Override
    public Object getProperty(Object key) {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public TransactionConfig getTransactionConfig() {
        return null;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public boolean isDeleteUnacceptedMessages() {
        return false;
    }

    @Override
    public EndpointSecurityFilter getSecurityFilter() {
        return null;
    }

    @Override
    public EndpointMessageProcessorChainFactory getMessageProcessorsFactory() {
        return null;
    }

    @Override
    public List<MessageProcessor> getMessageProcessors() {
        return null;
    }

    @Override
    public List<MessageProcessor> getResponseMessageProcessors() {
        return null;
    }

    @Override
    public MessageExchangePattern getExchangePattern() {
        return null;
    }

    @Override
    public int getResponseTimeout() {
        return 0;
    }

    @Override
    public String getInitialState() {
        return null;
    }

    @Override
    public MuleContext getMuleContext() {
        return null;
    }

    @Override
    public RetryPolicyTemplate getRetryPolicyTemplate() {
        return null;
    }

    @Override
    public String getEndpointBuilderName() {
        return null;
    }

    @Override
    public boolean isProtocolSupported(String protocol) {
        return false;
    }

    @Override
    public String getMimeType() {
        return null;
    }

    @Override
    public AbstractRedeliveryPolicy getRedeliveryPolicy() {
        return null;
    }

    @Override
    public boolean isDisableTransportTransformer() {
        return false;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {
        MockEndpointManager manager = (MockEndpointManager) getEndpointManager(event);

        String address = realAddressAsExpression();
        ExpressionManager expressionManager = event.getMuleContext().getExpressionManager();
        if ( expressionManager.isValidExpression(address) ){
            String realAddress = (String) expressionManager.evaluate(address, event);
            OutboundBehavior behavior = manager.getBehaviorFor(realAddress);

            if ( behavior == null ){
                return realEndpoint.process(event);
            }

            verifyAssertions(event, behavior.getAssertions());
            overrideMessage(event, behavior);
        }

        return event;
    }

    private void overrideMessage(MuleEvent event, OutboundBehavior behavior) {
        Object payload;
        if ( behavior.getPayload() == null || behavior.getPayload() instanceof NonDefinedPayload){
           payload = event.getMessage().getPayload();
        }
        else
        {
            payload = behavior.getPayload();

        }
        MuleMessage message = new DefaultMuleMessage(payload, event.getMuleContext());
        event.setMessage(message);

        if ( behavior.getInboundProperties() != null && !behavior.getInboundProperties().isEmpty()){
            message.addProperties(behavior.getInboundProperties(), PropertyScope.INBOUND);
        }

        if ( behavior.getOutboundProperties() != null && !behavior.getOutboundProperties().isEmpty() ){
            message.addProperties(behavior.getOutboundProperties(), PropertyScope.OUTBOUND);
        }

        if ( behavior.getSessionProperties() != null && !behavior.getSessionProperties().isEmpty()  ){
            message.addProperties(behavior.getSessionProperties(), PropertyScope.SESSION);
        }

        if ( behavior.getInvocationProperties() != null && !behavior.getInvocationProperties().isEmpty() ){
            message.addProperties(behavior.getInvocationProperties(), PropertyScope.INVOCATION);
        }
    }

    private void verifyAssertions(MuleEvent event, List<MessageProcessor> assertions) {
        if ( assertions == null ) return;

        for ( MessageProcessor processor : assertions ){
            try {
                processor.process(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String realAddressAsExpression() {
        return "#[string:" + realEndpoint.getAddress() + "]";
    }

    private Object getEndpointManager(MuleEvent event) {
        return event.getMuleContext().getRegistry().lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);
    }

    @Override
    public String getName() {
        return "Mocked Endpoint";
    }
}
