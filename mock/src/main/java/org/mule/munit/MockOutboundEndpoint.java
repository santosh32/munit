package org.mule.munit;

import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.NestedProcessor;
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
import org.mule.processor.AbstractRedeliveryPolicy;

import java.util.List;
import java.util.Map;

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
        MockEndpointFactory factory = (MockEndpointFactory) event.getMuleContext().getRegistry().lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);
        String address = "#[string:" + realEndpoint.getAddress() + "]";
        Object payload = null;
        ExpressionManager expressionManager = event.getMuleContext().getExpressionManager();
        if ( expressionManager.isExpression(address) ){
            String evaluate = (String) expressionManager.evaluate(address, event);
            OutboundBehaviour behaviour = factory.getExpectations().get(evaluate);

            if ( behaviour == null ){
                return realEndpoint.process(event);
            }

            payload = behaviour.getReturnValue();
            List<NestedProcessor> assertions = behaviour.getAssertions();
            for ( NestedProcessor processor : assertions ){
                try {
                    processor.process(event.getMessage().getPayload());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            event.getMessage().setPayload(payload);
        }



        return event;
    }

    @Override
    public String getName() {
        return null;
    }
}
