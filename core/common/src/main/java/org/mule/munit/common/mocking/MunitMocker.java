package org.mule.munit.common.mocking;

import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.munit.common.mp.MockedMessageProcessorBehavior;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

import java.util.Map;

public class MunitMocker extends MunitTool{


    public MunitMocker(MuleContext muleContext) {
        super(muleContext);
    }

    public MunitMocker expectMessageProcessor(String name) {
        this.messageProcessorName = name;
        return this;
    }

    public MunitMocker ofNamespace(String namespace) {
        this.messageProcessorNamespace = namespace;
        return this;
    }

    public MunitMocker WithAttributes(Map<String, Object> attributes) {
        this.messageProcessorAttributes = attributes;
        return this;
    }

    public void toReturn(MuleMessage message) {
        if ( messageProcessorName == null ){
            throw new IllegalArgumentException("You must specify at least the message processor name");
        }

        MockedMessageProcessorManager manager = getManager();
        manager.addBehavior(new MockedMessageProcessorBehavior(messageProcessorName,
                messageProcessorNamespace, messageProcessorAttributes, message.getPayload()));
    }
}
