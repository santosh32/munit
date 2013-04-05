package org.mule.munit.common;


import org.mule.api.MuleContext;
import org.mule.api.config.MuleProperties;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.modules.interceptor.processors.MessageProcessorCall;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.mp.MockedMessageProcessorManager;
import org.mule.munit.common.mp.MunitMessageProcessorCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     Util class to manage Munit tests.
 *
 *     This class should have minimal functionality as any Util class.
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitCore {

    public static final String LINE_NUMBER_ELEMENT_ATTRIBUTE = "__MUNIT_LINE_NUMBER";

    /**
     * <p>
     *     The Mule context for the Munit test.
     * </p>
     *
     * <p>
     *     This static value makes Munit test not able to run in parallel, which might be a good feature in the future.
     *     but it make no sense when transports are declared (i.e. flows with ports declared)
     * </p>
     *
     * <p>
     *     Check out that most of the methods of this class receives the {@link MuleContext} as parameter. This is because
     *     we want to get rid off this static reference.
     * </p>
     */
    private static MuleContext context;

    /**
     * <p>
     *     Sets the mule context for the Munit run
     * </p>
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
     * <p>
     *     Resets the status of Munit. Used after each test.
     * </p>
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

    /**
     * <p>
     *     Adds the {@link MockedMessageProcessorManager} to the {@link MuleRegistry}
     * </p>
     * @param muleContext
     * <p>
     *     The mule context where the manager must be registered.
     * </p>
     */
    public static void registerManager(MuleContext muleContext){
        try {
            MuleRegistry registry = muleContext.getRegistry();
            if ( registry.lookupObject(MockedMessageProcessorManager.ID) == null) {
                registry.registerObject(MockedMessageProcessorManager.ID, new MockedMessageProcessorManager());
            }
        } catch (RegistrationException e) {
           // Very uncommon scenario.
           throw new RuntimeException(e);
        }
    }


    /**
     * <p>
     *     Builds the mule Stack Trace based on the Munit registered calls.
     * </p>
     *
     * <p>
     *     The Mule stack trace contains the executed {@link org.mule.api.processor.MessageProcessor} in the test in the
     *     same format as JAVA.
     * </p>
     *
     * @since 3.4
     * @param muleContext
     * <p>
     *     The mule context
     * </p>
     * @return
     * <p>
     *     A list of JAVA stack trace elements.
     * </p>
     */
    public static List<StackTraceElement> buildMuleStackTrace(MuleContext muleContext) {
        MockedMessageProcessorManager manager = (MockedMessageProcessorManager) muleContext.getRegistry().lookupObject(MockedMessageProcessorManager.ID);
        List<MunitMessageProcessorCall> calls = manager.getCalls();

        List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>();

        StringBuffer stackTrace = new StringBuffer();
        for (MunitMessageProcessorCall call : calls ){
            stackTraceElements.add(0, new StackTraceElement(getFlowConstructName(call), getFullName(call), call.getFileName(), lineNumber(call)));
            stackTrace.insert(0, call.getMessageProcessorId().getFullName());
        }
        return stackTraceElements;
    }

    private static Integer lineNumber(MunitMessageProcessorCall call) {
        String lineNumber = call.getLineNumber();
        if ( lineNumber == null ){
            return 0;
        }
        return Integer.valueOf(lineNumber);
    }

    private static String getFullName(MessageProcessorCall call) {
        String fullName = call.getMessageProcessorId().getFullName();
        Map<String,Object> attributes = call.getAttributes();
        attributes.toString();
        attributes.remove("name");
        attributes.remove(LINE_NUMBER_ELEMENT_ATTRIBUTE);


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
