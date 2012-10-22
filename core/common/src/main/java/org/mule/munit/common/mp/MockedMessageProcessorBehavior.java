package org.mule.munit.common.mp;

import org.mule.api.MuleMessage;

/**
 * <p>The representation of a Message Processor mocked behavior.</p>
 * <p>We use this in order to know that the Message processor must return</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockedMessageProcessorBehavior {
    /**
     * <p>The message processor call representation. When this call is executed then return returnMessage</p>
     */
    private MessageProcessorCall messageProcessorCall;

    /**
     * <p>The Mule message information that will be replaced in the flow Message</p>
     */
    private MuleMessage returnMuleMessage;

    public MockedMessageProcessorBehavior(MessageProcessorCall messageProcessorCall, MuleMessage returnMuleMessage) {
        this.messageProcessorCall = messageProcessorCall;
        this.returnMuleMessage = returnMuleMessage;
    }

    public MuleMessage getReturnMuleMessage() {
        return returnMuleMessage;
    }

    public MessageProcessorCall getMessageProcessorCall() {
        return messageProcessorCall;
    }
}
