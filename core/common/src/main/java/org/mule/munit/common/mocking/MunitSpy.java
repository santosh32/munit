package org.mule.munit.common.mocking;


import org.mule.api.MuleContext;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.common.mp.MessageProcessorId;
import org.mule.munit.common.mp.SpyAssertion;

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

    public void running(List<MessageProcessor> beforeCall, List<MessageProcessor> afterCall){
        getManager().addSpyAssertion(new MessageProcessorId(messageProcessorName, messageProcessorNamespace),
                new SpyAssertion(beforeCall,afterCall));
    }


}
