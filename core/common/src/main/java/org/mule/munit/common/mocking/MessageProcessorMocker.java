package org.mule.munit.common.mocking;

import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.munit.common.mp.MessageProcessorCall;
import org.mule.munit.common.mp.MessageProcessorId;
import org.mule.munit.common.mp.MockedMessageProcessorBehavior;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

import java.util.Map;

/**
 * <p>This class is a Munit Tool to create Message processor mocks</p>
 *
 * <p>Usage:</p>
 *
 * <code>
 *     new MessageProcessorMocker(muleContext).when("mp").ofNamespace("namespace").theReturn(muleMessage);
 * </code>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MessageProcessorMocker extends MunitMockingTool {


    public MessageProcessorMocker(MuleContext muleContext) {
        super(muleContext);
    }

    /**
     * <p>Defines the name of the message processor to be mocked</p>
     *
     * @param name
     *      <p>The name of the message processor to be mocked</p>
     *
     * @return
     *      <p>The MessageProcessorMocker</p>
     */
    public MessageProcessorMocker when(String name) {
        this.messageProcessorName = name;
        return this;
    }

    /**
     * <p>Defines the namespace of the message processor to be mocked</p>
     *
     * @param namespace
     *      <p>The namespace of the message processor to be mocked</p>
     *
     * @return
     *      <p>The MessageProcessorMocker</p>
     */
    public MessageProcessorMocker ofNamespace(String namespace) {
        this.messageProcessorNamespace = namespace;
        return this;
    }

    /**
     * <p>Defines the attributes of the message processor to be mocked</p>
     *
     * @param attributes
     *      <p>The attributes of the message processor to be mocked</p>
     *
     * @return
     *      <p>The MessageProcessorMocker</p>
     */
    public MessageProcessorMocker withAttributes(Map<String, Object> attributes) {
        this.messageProcessorAttributes = attributes;
        return this;
    }

    /**
     * <p>Defines what @see #MuleMessage to return after the message processor call</p>
     *
     * @param message
     *      <p>The MuleMessage to return </p>
     */
    public void theReturn(MuleMessage message) {
        if ( messageProcessorName == null ){
            throw new IllegalArgumentException("You must specify at least the message processor name");
        }

        MockedMessageProcessorManager manager = getManager();
        MessageProcessorCall messageProcessorCall = new MessageProcessorCall(new MessageProcessorId(messageProcessorName, messageProcessorNamespace));
        messageProcessorCall.setAttributes(messageProcessorAttributes);
        manager.addBehavior(new MockedMessageProcessorBehavior(messageProcessorCall, message));
    }

    public void thenThrow(Throwable exception) {
        if ( messageProcessorName == null ){
            throw new IllegalArgumentException("You must specify at least the message processor name");
        }

        MockedMessageProcessorManager manager = getManager();
        MessageProcessorCall messageProcessorCall = new MessageProcessorCall(new MessageProcessorId(messageProcessorName, messageProcessorNamespace));
        messageProcessorCall.setAttributes(messageProcessorAttributes);
        manager.addBehavior(new MockedMessageProcessorBehavior(messageProcessorCall, exception));
    }
}
