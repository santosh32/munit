package org.mule.munit.common;


import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.common.mocking.NonDefinedPayload;
import org.mule.transport.NullPayload;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.List;
import java.util.Map;

public class MunitUtils {

    public static void changeMessage(DefaultMuleMessage returnMuleMessage, DefaultMuleMessage message) {

        Object payload = returnMuleMessage.getPayload();
        if ( payload !=null && !payload.equals(NullPayload.getInstance()) && !(payload instanceof NonDefinedPayload)){
            message.setPayload(payload);
        }

        if ( returnMuleMessage.getInboundPropertyNames() != null && !returnMuleMessage.getInboundPropertyNames().isEmpty() ){
            for ( String property : returnMuleMessage.getInboundPropertyNames() ){
                message.setInboundProperty(property, returnMuleMessage.getInboundProperty(property));
            }
        }

        if ( returnMuleMessage.getSessionPropertyNames() != null && !returnMuleMessage.getSessionPropertyNames().isEmpty() ){
            for ( String property : returnMuleMessage.getSessionPropertyNames() ){
                message.setSessionProperty(property, returnMuleMessage.getSessionProperty(property));
            }
        }

        if ( returnMuleMessage.getInvocationPropertyNames() != null && !returnMuleMessage.getInvocationPropertyNames().isEmpty() ){
            for ( String property : returnMuleMessage.getInvocationPropertyNames() ){
                message.setInvocationProperty(property, returnMuleMessage.getInvocationProperty(property));
            }
        }

        if ( returnMuleMessage.getOutboundPropertyNames() != null && !returnMuleMessage.getOutboundPropertyNames().isEmpty() ){
            for ( String property : returnMuleMessage.getOutboundPropertyNames() ){
                message.setOutboundProperty(property, returnMuleMessage.getOutboundProperty(property));
            }
        }

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
