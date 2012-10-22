package org.mule.munit.common.mocking;


import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.common.mp.MessageProcessorId;
import org.mule.munit.common.mp.SpyAssertion;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class is a Munit Tool to create Message processor spiers</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitSpy extends MunitTool{

    public MunitSpy(MuleContext muleContext) {
        super(muleContext);
    }

    public MunitSpy spyMessageProcessor(String name) {
        this.messageProcessorName = name;
        return this;
    }

    public MunitSpy ofNamespace(String namespace) {
        this.messageProcessorNamespace = namespace;
        return this;
    }

    public void running(List<SpyProcess> beforeCall, List<SpyProcess> afterCall){
        getManager().addSpyAssertion(new MessageProcessorId(messageProcessorName, messageProcessorNamespace),
                createSpyAssertion(beforeCall, afterCall));
    }

    protected SpyAssertion createSpyAssertion(List<SpyProcess> beforeCall, List<SpyProcess> afterCall) {
        return new SpyAssertion(createMessageProcessors(beforeCall),createMessageProcessors(afterCall));
    }

    private ArrayList<MessageProcessor> createMessageProcessors(List<SpyProcess> beforeCall) {
        ArrayList<MessageProcessor> beforeMessageProcessors = new ArrayList<MessageProcessor>();
        beforeMessageProcessors.add(createMessageProcessorFromSpy(beforeCall));
        return beforeMessageProcessors;
    }

    private MessageProcessor createMessageProcessorFromSpy(final List<SpyProcess> beforeCall) {
        return new MessageProcessor() {
            @Override
            public MuleEvent process(MuleEvent event) throws MuleException {
                if (beforeCall != null )
                for ( SpyProcess process : beforeCall ){
                    process.spy(event);
                }
                return event;
            }
        };
    }


}
