package org.mule.munit.common.mocking;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.modules.interceptor.processors.MessageProcessorBehavior;
import org.mule.modules.interceptor.processors.MessageProcessorCall;
import org.mule.modules.interceptor.processors.MessageProcessorId;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

import java.util.Map;

/**
 * <p>
 *     This class is a Munit Tool to create Message processor mocks
 * </p>
 *
 * <p>Usage:</p>
 *
 * <code>
 *     new MessageProcessorMocker(muleContext).when("mp").ofNamespace("namespace").thenReturn(muleMessage);
 * </code>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class MessageProcessorMocker extends MunitMockingTool {


    public MessageProcessorMocker(MuleContext muleContext) {
        super(muleContext);
    }

    /**
     * <p>
     *     Defines the name of the message processor to be mocked
     * </p>
     *
     * @param name
     *      <p>
     *          The name of the message processor to be mocked
     *      </p>
     *
     * @return
     *      <p>
     *          The MessageProcessorMocker
     *      </p>
     */
    public MessageProcessorMocker when(String name) {
        this.messageProcessorName = name;
        return this;
    }

    /**
     * <p>
     *     Defines the namespace of the message processor to be mocked
     * </p>
     *
     * @param namespace
     *      <p>
     *          The namespace of the message processor to be mocked
     *      </p>
     *
     * @return
     *      <p>
     *          The MessageProcessorMocker
     *      </p>
     */
    public MessageProcessorMocker ofNamespace(String namespace) {
        this.messageProcessorNamespace = namespace;
        return this;
    }

    /**
     * <p>
     *     Defines the attributes of the message processor to be mocked
     * </p>
     *
     * @param attributes
     *      <p>
     *          The attributes of the message processor to be mocked
     *      </p>
     *
     * @return
     *      <p>
     *          The MessageProcessorMocker
     *      </p>
     */
    public MessageProcessorMocker withAttributes(Map<String, Object> attributes) {
        this.messageProcessorAttributes = attributes;
        return this;
    }

    /**
     * <p>
     *     Defines what {@link MuleMessage} to return after the message processor call
     * </p>
     *
     * @param message
     *      <p>
     *          The MuleMessage to return
     *      </p>
     */
    public void thenReturn(MuleMessage message) {
        validateMessageProcessorName();

        MockedMessageProcessorManager manager = getManager();
        MessageProcessorCall messageProcessorCall = new MessageProcessorCall(new MessageProcessorId(messageProcessorName, messageProcessorNamespace));
        messageProcessorCall.setAttributes(messageProcessorAttributes);
        manager.addBehavior(new MessageProcessorBehavior(messageProcessorCall, message));
    }

    /**
     * <p>
     *     Defines that the message processor must throw an exception when called.
     * </p>
     * @param exception
     * <p>
     *     The exception to be throw
     * </p>
     */
    public void thenThrow(Throwable exception) {
        validateMessageProcessorName();

        MockedMessageProcessorManager manager = getManager();
        MessageProcessorCall messageProcessorCall = new MessageProcessorCall(new MessageProcessorId(messageProcessorName, messageProcessorNamespace));
        messageProcessorCall.setAttributes(messageProcessorAttributes);
        manager.addBehavior(new MessageProcessorBehavior(messageProcessorCall, exception));
    }


    /**
     * <p>
     *    Determines that the mocked message processor must return the same event as before its call.
     * </p>
     */
    public void thenReturnSameEvent() {
        validateMessageProcessorName();

        MockedMessageProcessorManager manager = getManager();
        MessageProcessorCall messageProcessorCall = new MessageProcessorCall(new MessageProcessorId(messageProcessorName, messageProcessorNamespace));
        messageProcessorCall.setAttributes(messageProcessorAttributes);
        manager.addBehavior(new MessageProcessorBehavior(messageProcessorCall, new DefaultMuleMessage(NotDefinedPayload.getInstance(), muleContext)));

    }

    private void validateMessageProcessorName() {
        if ( messageProcessorName == null ){
            throw new IllegalArgumentException("You must specify at least the message processor name");
        }
    }
}
