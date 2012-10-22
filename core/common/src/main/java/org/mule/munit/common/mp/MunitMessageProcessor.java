package org.mule.munit.common.mp;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.lifecycle.*;
import org.mule.api.processor.MessageProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.fail;


/**
 * <p>This is the Message processor Wrapper.</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitMessageProcessor implements MessageProcessor, Startable,Initialisable, Disposable, MuleContextAware, FlowConstructAware, Stoppable {
    private MessageProcessor realMp;
    private MessageProcessorId id;
    private Map<String,String> attributes;
    private MuleContext muleContext;

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {
        MockedMessageProcessorManager manager = getMockedMessageProcessorManager();

        runSpyBeforeAssertions(manager, event);
        MessageProcessorCall messageProcessorCall = buildCall(event);
        MockedMessageProcessorBehavior behavior = manager.getBetterMatchingBehavior(messageProcessorCall);
        if ( behavior != null ){
            event.getMessage().setPayload(behavior.getReturnMuleMessage().getPayload());

            registerCall(event, manager, messageProcessorCall);
            return event;
        }

        registerCall(event, manager, messageProcessorCall);
        return realMp.process(event);
    }

    private void registerCall(MuleEvent event, MockedMessageProcessorManager manager, MessageProcessorCall messageProcessorCall) {
        manager.addCall(messageProcessorCall);
        runSpyAfterAssertions(manager, event);
    }

    private void runSpyAfterAssertions(MockedMessageProcessorManager manager, MuleEvent event) {
        Map<MessageProcessorId, SpyAssertion> assertions = manager.getSpyAssertions();
        if ( assertions.isEmpty() ){
            return;
        }

        SpyAssertion spyAssertion = assertions.get(id);
        if ( spyAssertion == null ){
            return;
        }
        List<MessageProcessor> beforeCall = spyAssertion.getAfterMessageProcessors();
        for (MessageProcessor nestedProcessor : beforeCall) {
            try {
                nestedProcessor.process(event);
            } catch (Exception e) {
                fail("The after call assertions failed");
            }
        }
    }

    private void runSpyBeforeAssertions(MockedMessageProcessorManager manager, MuleEvent event) {
        Map<MessageProcessorId, SpyAssertion> assertions = manager.getSpyAssertions();
        if ( assertions.isEmpty() ){
            return;
        }
        SpyAssertion spyAssertion = assertions.get(id);
        if ( spyAssertion == null ){
            return;
        }
        List<MessageProcessor> beforeCall = spyAssertion.getBeforeMessageProcessors();
        for (MessageProcessor nestedProcessor : beforeCall) {
            try {
                nestedProcessor.process(event);
            } catch (Exception e) {
                fail("The before call assertions failed");
            }
        }
    }

    private MessageProcessorCall buildCall(MuleEvent event) {
        MessageProcessorCall call = new MessageProcessorCall(id);
        Map<String, Object> processed = new HashMap<String, Object>();
        for (Map.Entry<String,String> attrs : attributes.entrySet() ){
            Object evaluate = evaluate(attrs.getValue(), event);
            processed.put(attrs.getKey(), evaluate);
        }
        call.setAttributes(processed);
        return call;
    }


    private Object evaluate(String elementValue, MuleEvent event) {
        Object compareTo;
        ExpressionManager expressionManager = muleContext.getExpressionManager();
        if ( expressionManager.isExpression(elementValue)){
            compareTo = expressionManager.evaluate(elementValue, event);
        }
        else {
            Object o = muleContext.getRegistry().lookupObject(elementValue);
            if ( o != null ){
                compareTo = o;
            }
            else{
                compareTo = elementValue;
            }
        }
        return compareTo;
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

    protected MockedMessageProcessorManager getMockedMessageProcessorManager() {
        return ((MockedMessageProcessorManager) muleContext.getRegistry().lookupObject(MockedMessageProcessorManager.ID));
    }

    public void setRealMp(MessageProcessor realMp) {
        this.realMp = realMp;
    }

    public void setId(MessageProcessorId id) {
        this.id = id;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}
