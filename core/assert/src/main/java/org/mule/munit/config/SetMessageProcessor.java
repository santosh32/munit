
package org.mule.munit.config;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.AssertModule;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 *     Sets the payload
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class SetMessageProcessor extends MunitMessageProcessor
{

    private Object payload;
    
    private Map<String, Object> invocationProperties;
    private Map<String, Object> inboundProperties;
    private Map<String, Object> sessionProperties;
    private Map<String, Object> outboundProperties;


    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        DefaultMuleMessage defaultMuleMessage = (DefaultMuleMessage) mulemessage;

        setProperties(defaultMuleMessage, inboundProperties, PropertyScope.INBOUND);
        setProperties(defaultMuleMessage, invocationProperties, PropertyScope.INVOCATION);
        setProperties(defaultMuleMessage, outboundProperties, PropertyScope.OUTBOUND);
        setProperties(defaultMuleMessage, sessionProperties, PropertyScope.SESSION);
        defaultMuleMessage.setPayload(evaluate(mulemessage,payload));
    }

    private Map<String,Object> setProperties(DefaultMuleMessage message, Map<String, Object> properties, PropertyScope scope) {
        Map<String, Object> evaluatedMap = new HashMap<String, Object>();
        if ( properties != null ){
            for (Map.Entry<String, Object> entry : properties.entrySet() ){
               message.setProperty(entry.getKey(), evaluate(message,entry.getValue()), scope);
            } 
        }

        return evaluatedMap;
    }



    @Override
    protected String getProcessor() {
        return "set";
    }

    public void setPayload(Object value) {
        this.payload = value;
    }

    public void setInvocationProperties(Map<String, Object> invocationProperties) {
        this.invocationProperties = invocationProperties;
    }

    public void setInboundProperties(Map<String, Object> inboundProperties) {
        this.inboundProperties = inboundProperties;
    }

    public void setSessionProperties(Map<String, Object> sessionProperties) {
        this.sessionProperties = sessionProperties;
    }

    public void setOutboundProperties(Map<String, Object> outboundProperties) {
        this.outboundProperties = outboundProperties;
    }
}
