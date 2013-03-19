package org.mule.munit.common.mocking;


import org.mule.api.MuleContext;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     This is the general Munit Tool
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class MunitMockingTool {

    protected MuleContext muleContext;

    protected String messageProcessorName ;
    protected String messageProcessorNamespace = "mule";
    protected Map<String, Object> messageProcessorAttributes = new HashMap<String, Object>();

    public MunitMockingTool(MuleContext muleContext) {
        this.muleContext = muleContext;
    }

    protected void checkValidQuery() {
        if ( messageProcessorName == null ){
            throw new IllegalArgumentException("You must provide at least the Message processor name");
        }
    }

    protected MockedMessageProcessorManager getManager() {
        return (MockedMessageProcessorManager) muleContext.getRegistry().lookupObject(MockedMessageProcessorManager.ID);
    }

    protected String getFullName() {
        return messageProcessorNamespace + ":" + messageProcessorName;
    }

}
