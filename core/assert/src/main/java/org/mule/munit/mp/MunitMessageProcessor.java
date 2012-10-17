package org.mule.munit.mp;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.*;
import org.mule.api.processor.MessageProcessor;

import java.util.List;


public class MunitMessageProcessor implements MessageProcessor, Startable,Initialisable, Disposable, MuleContextAware, FlowConstructAware, Stoppable {
    private MessageProcessor realMp;
    private String namespace;
    private String name;
    private String namespaceUri;
    private MuleContext muleContext;

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {
        List<MpBehavior> behaviorsFor = getMockMpProcessor().getBehaviorsFor(namespace, name);
        if ( behaviorsFor.isEmpty()){
            return realMp.process(event);
        }
        event.getMessage().setPayload(behaviorsFor.get(0).getReturnValue());

        return event;
    }


    public void setRealMp(MessageProcessor realMp) {
        this.realMp = realMp;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public void dispose() {
        if ( realMp instanceof Disposable ){
            ((Disposable) realMp).dispose();
        }
    }

    @Override
    public void setFlowConstruct(FlowConstruct flowConstruct) {
        if ( realMp instanceof FlowConstructAware ){
            ((FlowConstructAware) realMp).setFlowConstruct(flowConstruct);
        }
    }

    @Override
    public void initialise() throws InitialisationException {
        if ( realMp instanceof Initialisable){
            ((Initialisable) realMp).initialise();
        }
    }

    @Override
    public void setMuleContext(MuleContext context) {
        if ( realMp instanceof MuleContextAware ){
            ((MuleContextAware) realMp).setMuleContext(context);
        }
        this.muleContext = context;
    }

    @Override
    public void stop() throws MuleException {
        if ( realMp instanceof  Stoppable ){
            ((Stoppable) realMp).stop();
        }
    }

    @Override
    public void start() throws MuleException {
        if ( realMp instanceof Startable ){
            ((Startable) realMp).start();
        }

    }

    private MockMpManager getMockMpProcessor() {
        return ((MockMpManager) muleContext.getRegistry().lookupObject(MockMpManager.ID));
    }

}
