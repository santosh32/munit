package org.mule.munit.endpoint;

import org.mule.api.processor.MessageProcessor;

import java.util.List;
import java.util.Map;

/**
 * <p>This class defines how the outbound endpoint must behave. It has the list of message processors that
 * asserts the incoming payload and the return message of the outbound.</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class OutboundBehavior {
    
    private Map<String, Object> outboundProperties;
    private Map<String, Object> inboundProperties;
    private Map<String, Object> invocationProperties;
    private Map<String, Object> sessionProperties;
    
    /**
     * <p>The outbound Mule Message</p>
     */
    private Object payload;

    /**
     * <p>The list of message processors for message assertion</p>
     */
    private List<MessageProcessor> assertions;

    public OutboundBehavior(Object payload, List<MessageProcessor> assertions) {
        this.payload = payload;
        this.assertions = assertions;
    }


    public Object getPayload() {
        return payload;
    }

    public List<MessageProcessor> getAssertions() {
        return assertions;
    }

    public Map<String, Object> getOutboundProperties() {
        return outboundProperties;
    }

    public void setOutboundProperties(Map<String, Object> outboundProperties) {
        this.outboundProperties = outboundProperties;
    }

    public Map<String, Object> getInboundProperties() {
        return inboundProperties;
    }

    public void setInboundProperties(Map<String, Object> inboundProperties) {
        this.inboundProperties = inboundProperties;
    }

    public Map<String, Object> getInvocationProperties() {
        return invocationProperties;
    }

    public void setInvocationProperties(Map<String, Object> invocationProperties) {
        this.invocationProperties = invocationProperties;
    }

    public Map<String, Object> getSessionProperties() {
        return sessionProperties;
    }

    public void setSessionProperties(Map<String, Object> sessionProperties) {
        this.sessionProperties = sessionProperties;
    }
}
