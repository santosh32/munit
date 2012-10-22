package org.mule.munit.common.mocking;

import org.mule.api.MuleContext;
import org.mule.munit.common.mp.MessageProcessorCall;
import org.mule.munit.common.mp.MessageProcessorId;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.fail;

/**
 * <p>This is the general Munit Tool</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitVerifier extends MunitTool{

    public MunitVerifier(MuleContext muleContext) {
        super(muleContext);
    }

    public MunitVerifier verifyCallOfMessageProcessor(String name) {
        this.messageProcessorName = name;
        return this;
    }


    public MunitVerifier ofNamespace(String namespace) {
        this.messageProcessorNamespace = namespace;
        return this;
    }

    public void times(Integer times) {
        List<MessageProcessorCall> executedCalls = getExecutedCalls();
        
        if (executedCalls.size() != times) {
            fail("On " + getFullName() + ".Expected " + times +
                    " but got " + executedCalls.size() + " calls");
        }
    }

    private List<MessageProcessorCall> getExecutedCalls() {
        return getManager().findCallsFor(new MessageProcessorId(messageProcessorName,
                messageProcessorNamespace), messageProcessorAttributes);
    }

    public MunitVerifier withAttributes(Map<String, Object> attributes) {
        this.messageProcessorAttributes = attributes;
        return this;
    }

    public void atLeast(Integer atLeast) {
        checkValidQuery();

        List<MessageProcessorCall> executedCalls = getExecutedCalls();

        if (executedCalls.size() < atLeast) {
            fail("On " + getFullName() + ".Expected at least " + atLeast + " but got " + executedCalls.size() + " calls");
        }
    }

    public void atMost(Integer atMost) {
        checkValidQuery();
        List<MessageProcessorCall> executedCalls = getExecutedCalls();

        if (executedCalls.size() > atMost) {
            fail("On " + getFullName() + ".Expected at least " + atMost + " but got " + executedCalls.size() + " calls");
        }
    }

    public void atLeastOnce() {
        checkValidQuery();
        List<MessageProcessorCall> executedCalls = getExecutedCalls();
        if (executedCalls.isEmpty()) {
            fail("On " + getFullName()  + ".It was never called");
        }

    }

}
