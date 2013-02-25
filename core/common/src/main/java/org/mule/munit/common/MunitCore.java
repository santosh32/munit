package org.mule.munit.common;


import org.mule.api.MuleContext;
import org.mule.api.config.MuleProperties;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.mp.MessageProcessorCall;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    public static List<StackTraceElement> getStackTraceElements(MuleContext muleContext) {
        MockedMessageProcessorManager manager = (MockedMessageProcessorManager) muleContext.getRegistry().lookupObject(MockedMessageProcessorManager.ID);
        List<MessageProcessorCall> calls = manager.getCalls();

        List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>();

        StringBuffer stackTrace = new StringBuffer();
        for (MessageProcessorCall call : calls ){
            stackTraceElements.add(0, new StackTraceElement(getFlowConstructName(call), getFullName(call), call.getFileName(), Integer.valueOf(call.getLineNumber())));
            stackTrace.insert(0, call.getMessageProcessorId().getFullName());
        }
        return stackTraceElements;
    }

    private static String getFullName(MessageProcessorCall call) {
        String fullName = call.getMessageProcessorId().getFullName();
        Map<String,Object> attributes = call.getAttributes();
        attributes.toString();
        attributes.remove("name");
        attributes.remove("location");


        return fullName + attributes.toString();
    }



    private static String getFlowConstructName(MessageProcessorCall call) {
        FlowConstruct flowConstruct = call.getFlowConstruct();
        if (flowConstruct == null ){
            return "";
        }
        return flowConstruct.getName();
    }
}
