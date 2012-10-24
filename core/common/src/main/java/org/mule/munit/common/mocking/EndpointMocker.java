package org.mule.munit.common.mocking;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.endpoint.OutboundBehavior;

import java.util.*;

/**
 * <p>This class is a Munit Tool to create Endpoint mocks</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class EndpointMocker {
    
    private MuleContext muleContext;
    private String address;
    private List<SpyProcess> process;

    public EndpointMocker(MuleContext muleContext) {
        this.muleContext = muleContext;
    }
    
    public EndpointMocker expectEndpointWithAddress(String address){
        this.address = address;
        return this;
    }
    
    public EndpointMocker withIncomingMessageSatisfying(List<SpyProcess> process){
        this.process = process;

        return this; 
    }
    
    public void toReturn(MuleMessage message){
        OutboundBehavior behavior = new OutboundBehavior(message.getPayload(), createMessageProcessorFromSpy(process));

        behavior.setInboundProperties(getMapOf(message, PropertyScope.INBOUND));
        behavior.setInvocationProperties(getMapOf(message, PropertyScope.INVOCATION));
        behavior.setOutboundProperties(getMapOf(message, PropertyScope.OUTBOUND));
        behavior.setSessionProperties(getMapOf(message, PropertyScope.SESSION));

        MockEndpointManager factory = (MockEndpointManager) muleContext.getRegistry().lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);
        factory.addBehavior(address, behavior);
    }

    private Map<String, Object> getMapOf(MuleMessage message, PropertyScope inbound) {
        Map<String,Object> properties = new HashMap<String, Object>();
        Set<String> propertyNames = message.getPropertyNames(inbound);
        if ( propertyNames != null ){
            for ( String property : propertyNames ){
                properties.put(property, message.getProperty(property, inbound));
            }
        }
        
        return properties;
    }

    private List<MessageProcessor> createMessageProcessorFromSpy(final List<SpyProcess> process) {
        List<MessageProcessor> messageProcessors = new ArrayList<MessageProcessor>();

        messageProcessors.add(new MessageProcessor() {
            @Override
            public MuleEvent process(MuleEvent event) throws MuleException {
                if (process != null ){
                    for ( SpyProcess spyProcess : process){
                        spyProcess.spy(event);
                    }
                }
                return event;
            }
        });
        
        return messageProcessors;
    }
}
