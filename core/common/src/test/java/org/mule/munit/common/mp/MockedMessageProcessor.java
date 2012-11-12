package org.mule.munit.common.mp;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.*;
import org.mule.api.processor.MessageProcessor;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockedMessageProcessor implements MessageProcessor, Startable,Initialisable, Disposable, MuleContextAware, FlowConstructAware, Stoppable {
    boolean calledDispose;
    boolean calledStart;
    boolean calledStop;
    boolean calledsetFlowConstruct;
    boolean calledInitialise;
    boolean calledSetMuleContext;

    @Override
    public void dispose() {
        calledDispose = true;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {
        return null;
    }

    @Override
    public void start() throws MuleException {
        this.calledStart = true;
    }

    @Override
    public void stop() throws MuleException {
        this.calledStop = true;
    }

    @Override
    public void setFlowConstruct(FlowConstruct flowConstruct) {
        this.calledsetFlowConstruct = true;
    }

    @Override
    public void initialise() throws InitialisationException {
        this.calledInitialise = true;
    }

    @Override
    public void setMuleContext(MuleContext context) {
        this.calledSetMuleContext = true;
    }
}
