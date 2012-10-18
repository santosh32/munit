package org.mule.munit;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.NestedProcessor;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.transport.PropertyScope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 10/10/12
 * Time: 8:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class NestedMessageProcessor implements MessageProcessor {

    public NestedMessageProcessor(NestedProcessor nestedProcessor) {
        this.nestedProcessor = nestedProcessor;
    }

    private NestedProcessor nestedProcessor;
    
    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {

        MuleMessage message = event.getMessage();
        Map<String, Object> properties =new HashMap<String, Object>();

        Set<String> propertyNames = message.getPropertyNames(PropertyScope.INVOCATION);
        for ( String prop : propertyNames) {
            properties.put(prop, message.getProperty(prop, PropertyScope.INVOCATION));
        }


        try {
            nestedProcessor.process(message.getPayload(), properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return event;
    }
}
