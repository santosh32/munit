package org.mule.munit.common.mp;

import org.mule.api.processor.MessageProcessor;

import java.util.ArrayList;
import java.util.List;


public class SpyAssertion {
    private List<MessageProcessor> beforeMessageProcessors = new ArrayList<MessageProcessor>();
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
