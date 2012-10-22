package org.mule.munit.common.endpoint;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.endpoint.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Mule Endpoint factory that generates mocked endpoints.</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockEndpointManager implements EndpointFactory {

    protected EndpointFactory defaultFactory;
    protected Map<String,OutboundBehavior> behaviors = new HashMap<String, OutboundBehavior>();

    @Override
    public InboundEndpoint getInboundEndpoint(String uri) throws MuleException {
        return defaultFactory.getInboundEndpoint(uri);
    }

    @Override
    public OutboundEndpoint getOutboundEndpoint(String uri) throws MuleException {
        return new MockOutboundEndpoint(defaultFactory.getOutboundEndpoint(uri));
    }

    @Override
    public InboundEndpoint getInboundEndpoint(EndpointBuilder builder) throws MuleException {
        return defaultFactory.getInboundEndpoint(builder);
    }

    @Override
    public OutboundEndpoint getOutboundEndpoint(EndpointBuilder builder) throws MuleException {
        return new MockOutboundEndpoint(defaultFactory.getOutboundEndpoint(builder));
    }

    @Override
    public InboundEndpoint getInboundEndpoint(EndpointURI endpointUri) throws MuleException {
        return defaultFactory.getInboundEndpoint(endpointUri);
    }

    @Override
    public OutboundEndpoint getOutboundEndpoint(EndpointURI endpointUri) throws MuleException {
        return new MockOutboundEndpoint(defaultFactory.getOutboundEndpoint(endpointUri));
    }

    @Override
    public EndpointBuilder getEndpointBuilder(String uri) throws MuleException {
        return defaultFactory.getEndpointBuilder(uri);
    }

    @Override
    public void setMuleContext(MuleContext context) {
    }

    public void setDefaultFactory(EndpointFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
    }
    
    public void addBehavior(String address, OutboundBehavior payload){
        behaviors.put(address, payload);
    }

    public OutboundBehavior getBehaviorFor(String address){
        return this.behaviors.get(address);
    }

    public void resetBehaviors() {
        this.behaviors.clear();
    }
}
