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
public class MunitSpy extends MunitMockingTool {

    public MunitSpy(MuleContext muleContext) {
        super(muleContext);
    }

    /**
     * <p>Defines the name of the message processor to spy</p>
     *
     * @param name
     *      <p>The name of the message processor to spy</p>
     * @return
     *      <p>Itself</p>
     */
    public MunitSpy spyMessageProcessor(String name) {
        this.messageProcessorName = name;
        return this;
    }

    /**
     * <p>Defines the namespace of the message processor to spy</p>
     *
     * @param namespace
     *      <p>The namespace of the message processor to spy</p>
     * @return
     *      <p>Itself</p>
     */
    public MunitSpy ofNamespace(String namespace) {
        this.messageProcessorNamespace = namespace;
        return this;
    }

    /**
     * <p>The process to run before and after the message processor</p>
     * @param beforeCall
     *      <p>Processes to run before the message processor call</p>
     * @param afterCall
     *      <p>Processes to run after the message processor call</p>
     *
     */
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
