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
import org.mule.munit.common.matchers.Matcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.fail;


public class MunitMessageProcessor implements MessageProcessor, Startable,Initialisable, Disposable, MuleContextAware, FlowConstructAware, Stoppable {
    private MessageProcessor realMp;
    private String namespace;
    private String name;
    private Map<String,String> attributes;
    private MuleContext muleContext;

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {
        MockedMessageProcessorManager manager = getMockMpProcessor();

        runSpyBeforeAssertions(manager, event);

        List<MockedMessageProcessorBehavior> behaviors = manager.getBehaviorsFor(namespace, name);
        for ( MockedMessageProcessorBehavior behavior  : behaviors ){
            int matchedCount = 0;
            for ( Map.Entry<String,String> entry: attributes.entrySet() ){
               Map<String, Object> parameters = behavior.getParameters();
               if ( parameters.containsKey(entry.getKey()) ){
                  matchedCount += isDesired(parameters.get(entry.getKey()), entry.getValue(),event) ? 1 : 0;
               }
           }
            if ( matchedCount == behavior.getParameters().size()){
                manager.addCall(buildCall(event));
                event.getMessage().setPayload(behavior.getReturnValue());

                runSpyAfterAssertions(manager, event);
                return event;
            }
        }

        manager.addCall(buildCall(event));
        runSpyAfterAssertions(manager, event);
        return realMp.process(event);
    }

    private void runSpyAfterAssertions(MockedMessageProcessorManager manager, MuleEvent event) {
        Map<String, SpyAssertion> assertions = manager.getSpyAssertions();
        if ( assertions.isEmpty() ){
            return;
        }

        SpyAssertion spyAssertion = assertions.get(getFullName());
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

    private String getFullName() {
        return namespace + ":" + name;
    }

    private void runSpyBeforeAssertions(MockedMessageProcessorManager manager, MuleEvent event) {
        Map<String, SpyAssertion> assertions = manager.getSpyAssertions();
        if ( assertions.isEmpty() ){
            return;
        }
        SpyAssertion spyAssertion = assertions.get(getFullName());
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
        MessageProcessorCall call = new MessageProcessorCall(name, namespace);
        Map<String, Object> processed = new HashMap<String, Object>();
        for (Map.Entry<String,String> attrs : attributes.entrySet() ){
            Object evaluate = evaluate(attrs.getValue(), event);
            processed.put(attrs.getKey(), evaluate);
        }
        call.setAttributes(processed);
        return call;
    }

    private boolean isDesired(Object matcher, String elementValue, MuleEvent event) {
        Object compareTo = evaluate(elementValue, event);

        if ( matcher instanceof Matcher) {
            return ((Matcher) matcher).match(compareTo);
        }

        return matcher.equals(compareTo);
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

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
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


    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
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

    private MockedMessageProcessorManager getMockMpProcessor() {
        return ((MockedMessageProcessorManager) muleContext.getRegistry().lookupObject(MockedMessageProcessorManager.ID));
    }

}
