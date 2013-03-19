package org.mule.munit.common.mp;

import org.mule.api.processor.MessageProcessor;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 *     The Assertions the must be executed after and before a message processor call
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class SpyAssertion {
    /**
     * <p>
     *     The Message processors to be executed before the call
     * </p>
     */
    private List<MessageProcessor> beforeMessageProcessors = new ArrayList<MessageProcessor>();

    /**
     * <p>
     *     The Message processors to be executed after the call
     * </p>
     */
    private List<MessageProcessor> afterMessageProcessors = new ArrayList<MessageProcessor>();

    public SpyAssertion(List<MessageProcessor> beforeMessageProcessors, List<MessageProcessor> afterMessageProcessors) {
        this.beforeMessageProcessors = beforeMessageProcessors;
        this.afterMessageProcessors = afterMessageProcessors;
    }

    public List<MessageProcessor> getBeforeMessageProcessors() {
        return beforeMessageProcessors;
    }

    public List<MessageProcessor> getAfterMessageProcessors() {
        return afterMessageProcessors;
    }
}
