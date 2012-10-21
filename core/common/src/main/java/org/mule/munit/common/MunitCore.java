package org.mule.munit.common;


import org.mule.api.MuleContext;
import org.mule.api.config.MuleProperties;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

public class MunitCore {
    
    public static void reset(MuleContext muleContext){
        MockEndpointManager endpointFactory = (MockEndpointManager) muleContext.getRegistry().lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);
        endpointFactory.resetBehaviors();

        MockedMessageProcessorManager mpManager = (MockedMessageProcessorManager) muleContext.getRegistry().lookupObject(MockedMessageProcessorManager.ID);

        if ( mpManager != null ){
            mpManager.reset();
        }
    }
    
    public static void registerManager(MuleContext muleContext){
        try {
            MuleRegistry registry = muleContext.getRegistry();
            if ( registry.lookupObject(MockedMessageProcessorManager.ID) == null) {
                registry.registerObject(MockedMessageProcessorManager.ID, new MockedMessageProcessorManager());
            }
        } catch (RegistrationException e) {

        }
    }
}
