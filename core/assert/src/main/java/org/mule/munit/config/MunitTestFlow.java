package org.mule.munit.config;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.config.MuleProperties;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.munit.endpoint.MockEndpointManager;
import org.mule.munit.mp.MockedMessageProcessorManager;

import static junit.framework.Assert.assertEquals;


/**
 * <p>The Test Flow</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitTestFlow extends MunitFlow{
    private boolean ignore;
    private String expected;

    public MunitTestFlow(String name, MuleContext muleContext) {
        super(name, muleContext);

        registerMpManager(muleContext);
    }



    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isIgnore() {
        return ignore;
    }
    
    public boolean expectException(Throwable t){
        if ( !StringUtils.isEmpty(expected)){
            assertEquals(expected, t.getClass().getName());
            return true;
        }
        return false;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {
        MuleEvent process = super.process(event);

        MockEndpointManager factory = (MockEndpointManager) muleContext.getRegistry().lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);
        MockedMessageProcessorManager mpManager = (MockedMessageProcessorManager) muleContext.getRegistry().lookupObject(MockedMessageProcessorManager.ID);
        factory.resetBehaviors();
        mpManager.reset();

        return process;
    }


    private void registerMpManager(MuleContext muleContext) {
        try {
            MuleRegistry registry = muleContext.getRegistry();
            if ( registry.lookupObject(MockedMessageProcessorManager.ID) == null) {
                registry.registerObject(MockedMessageProcessorManager.ID, new MockedMessageProcessorManager());
            }
        } catch (RegistrationException e) {

        }
    }


}
