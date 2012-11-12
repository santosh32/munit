package org.mule.munit.common;


import org.mule.api.MuleContext;
import org.mule.api.config.MuleProperties;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

/**
 * <p>Util class to manage the Mocking managers</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitCore {

    /**
     * <p>The Mule context for the Munit test</p>
     */
    private static MuleContext context;

    /**
     * <p>Sets the mule context for the Munit run</p>
     *
     * @param muleContext
     *      <p>The Mule Context</p>
     */
    public static synchronized void setMuleContext(MuleContext muleContext){
        context = muleContext;
    }

    /**
     * @return
     *      <p>Returns the stored mule context</p>
     */
    public static MuleContext getMuleContext(){
        return context;
    }

    /**
     * <p>Resets the status of Munit. Used after each test.</p>
     * @param muleContext The Mule context
     */
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
