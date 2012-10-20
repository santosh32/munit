package org.mule.munit;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.NestedProcessor;
import org.mule.api.processor.MessageProcessor;

public class NestedMessageProcessor implements MessageProcessor {

    public NestedMessageProcessor(NestedProcessor nestedProcessor) {
        this.nestedProcessor = nestedProcessor;
    }

    private NestedProcessor nestedProcessor;
    
    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {

        try {
            nestedProcessor.process();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return event;
    }
}
