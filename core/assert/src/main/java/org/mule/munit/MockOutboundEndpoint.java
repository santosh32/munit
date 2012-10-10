package org.mule.munit;

import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.endpoint.EndpointMessageProcessorChainFactory;
import org.mule.api.endpoint.EndpointURI;
import org.mule.api.endpoint.OutboundEndpoint;
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

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 10/9/12
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockOutboundEndpoint implements OutboundEndpoint{
    @Override
    public List<String> getResponseProperties() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EndpointURI getEndpointURI() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAddress() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getEncoding() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Connector getConnector() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Transformer> getTransformers() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Transformer> getResponseTransformers() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map getProperties() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getProperty(Object key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProtocol() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isReadOnly() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TransactionConfig getTransactionConfig() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Filter getFilter() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isDeleteUnacceptedMessages() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EndpointSecurityFilter getSecurityFilter() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EndpointMessageProcessorChainFactory getMessageProcessorsFactory() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<MessageProcessor> getMessageProcessors() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<MessageProcessor> getResponseMessageProcessors() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MessageExchangePattern getExchangePattern() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getResponseTimeout() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getInitialState() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MuleContext getMuleContext() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RetryPolicyTemplate getRetryPolicyTemplate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getEndpointBuilderName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isProtocolSupported(String protocol) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getMimeType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AbstractRedeliveryPolicy getRedeliveryPolicy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isDisableTransportTransformer() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {
        return event;
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
