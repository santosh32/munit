package org.mule.munit.common;


import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;

import java.util.List;
import java.util.Set;

import static org.mule.munit.common.mocking.NotDefinedPayload.isNotDefined;

/**
 * <p>Common class for common stuffs in Munit</p>
 *
 * <p>Do not use this class for everything</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitUtils {

    /**
     * <p>Copy one message to another</p>
     *
     * @param original
     *       <p>The origin message</p>
     *
     * @param copyToMessage
     *       <p>The result message</p>
     */
    public static void copyMessage(DefaultMuleMessage original, DefaultMuleMessage copyToMessage) {

        Object payload = original.getPayload();
        if (payload != null && !isNotDefined(payload)){
            copyToMessage.setPayload(payload);
        }

        if (containsProperties(original.getInboundPropertyNames())){
            for ( String property : original.getInboundPropertyNames()){
                copyToMessage.setInboundProperty(property, original.getInboundProperty(property));
            }
        }

        if (containsProperties(original.getSessionPropertyNames())){
            for ( String property : original.getSessionPropertyNames() ){
                copyToMessage.setSessionProperty(property, original.getSessionProperty(property));
            }
        }

        if (containsProperties(original.getInvocationPropertyNames())){
            for ( String property : original.getInvocationPropertyNames() ){
                copyToMessage.setInvocationProperty(property, original.getInvocationProperty(property));
            }
        }

        if (containsProperties(original.getOutboundPropertyNames())){
            for ( String property : original.getOutboundPropertyNames()) {
                copyToMessage.setOutboundProperty(property, original.getOutboundProperty(property));
            }
        }

    }

    private static boolean containsProperties(Set<String> inboundPropertyNames) {
        return inboundPropertyNames != null && !inboundPropertyNames.isEmpty();
    }

    public static void verifyAssertions(MuleEvent event, List<MessageProcessor> assertions) {
        if ( assertions == null ) return;

        for ( MessageProcessor processor : assertions ){
            try {
                processor.process(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }





}
