package org.mule.munit;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.endpoint.*;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 10/9/12
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockEndpointFactory implements EndpointFactory {

    @Override
    public InboundEndpoint getInboundEndpoint(String uri) throws MuleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OutboundEndpoint getOutboundEndpoint(String uri) throws MuleException {
        return new MockOutboundEndpoint();
    }

    @Override
    public InboundEndpoint getInboundEndpoint(EndpointBuilder builder) throws MuleException {
        return null;
    }

    @Override
    public OutboundEndpoint getOutboundEndpoint(EndpointBuilder builder) throws MuleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public InboundEndpoint getInboundEndpoint(EndpointURI endpointUri) throws MuleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OutboundEndpoint getOutboundEndpoint(EndpointURI endpointUri) throws MuleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EndpointBuilder getEndpointBuilder(String uri) throws MuleException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMuleContext(MuleContext context) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
