package org.mule.munit.common.mp;


import org.mule.munit.common.matchers.Matcher;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>The representation of a Message Processor call</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MessageProcessorCall {

    /**
     * <p>The Id of the message processor</p>
     */
    private MessageProcessorId messageProcessorId;

    /**
     * <p>The xml attributes of the message processor with its object (the attribute resolution)</p>
     */
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public MessageProcessorCall(MessageProcessorId messageProcessorId) {
        this.messageProcessorId = messageProcessorId;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public MessageProcessorId getMessageProcessorId() {
        return messageProcessorId;
    }

    /**
     * <p>Defines if this call is of a particular message processor</p>
     *
     * @param id The id of the message processor
     * @return true/false
     */
    public boolean isCallOf(MessageProcessorId id) {
        return this.messageProcessorId.equals(id);
    }


    /**
     * <p>If the current call matches exactly with the call sent by parameter. Then it returns the number of attributes</p>
     * <p>of the current call, else returns -1</p>
     * @param call The Message processor call that we need to compare to.
     * @return The total number of matching attributes or -1
     */
    public int matchingWeight(MessageProcessorCall call) {
        if (messageProcessorId.equals(call.getMessageProcessorId())) {

            Map<String, Object> callAttributes = call.getAttributes();
            for (Map.Entry<String, Object> myAttribute : attributes.entrySet()) {
                if (!callHasAttribute(callAttributes, myAttribute)) {
                    return -1;
                }
            }

            return attributes.size();
        }
        return -1;
    }

    private boolean callHasAttribute(Map<String, Object> callAttributes, Map.Entry<String, Object> myAttribute) {
        String myAttributeKey = myAttribute.getKey();
        return callAttributes.containsKey(myAttributeKey) &&
                matchAttributeValue(callAttributes.get(myAttributeKey), myAttribute.getValue());
    }


    private boolean matchAttributeValue(Object value, Object matcher) {

        if (matcher instanceof Matcher) {
            return ((Matcher) matcher).match(value);
        }

        return matcher.equals(value);

    }
}


