package org.mule.munit.common.mp;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.common.MunitCore;
import org.mule.munit.common.MunitUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>It intercept the @see MessageProcessor#process() calls</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitMessageProcessorInterceptor implements MethodInterceptor{

    private MessageProcessorId id;
    private Map<String,String> attributes;


    public Object process(Object obj,Object[] args, MethodProxy proxy) throws Throwable {
        MuleEvent event = (MuleEvent) args[0];

        MockedMessageProcessorManager manager = getMockedMessageProcessorManager();

        runSpyBeforeAssertions(manager, event);
        MessageProcessorCall messageProcessorCall = buildCall(event);
        MockedMessageProcessorBehavior behavior = manager.getBetterMatchingBehavior(messageProcessorCall);
        if ( behavior != null ){
            MunitUtils.copyMessage((DefaultMuleMessage) behavior.getReturnMuleMessage(), (DefaultMuleMessage) event.getMessage());

            registerCall(event, manager, messageProcessorCall);
            return event;
        }

        registerCall(event, manager, messageProcessorCall);
        return proxy.invokeSuper(obj,args);
    }

    private void registerCall(MuleEvent event, MockedMessageProcessorManager manager, MessageProcessorCall messageProcessorCall) {
        manager.addCall(messageProcessorCall);
        runSpyAfterAssertions(manager, event);
    }

    private void runSpyAfterAssertions(MockedMessageProcessorManager manager, MuleEvent event) {
        SpyAssertion spyAssertion = getAssertionFrom(manager);
        if (spyAssertion == null) return;

        MunitUtils.verifyAssertions(event, spyAssertion.getAfterMessageProcessors());
    }

    private void runSpyBeforeAssertions(MockedMessageProcessorManager manager, MuleEvent event) {
        SpyAssertion spyAssertion = getAssertionFrom(manager);
        if (spyAssertion == null) return;

        MunitUtils.verifyAssertions(event, spyAssertion.getBeforeMessageProcessors());
    }

    private SpyAssertion getAssertionFrom(MockedMessageProcessorManager manager) {
        Map<MessageProcessorId, SpyAssertion> assertions = manager.getSpyAssertions();
        if ( assertions.isEmpty() ){
            return null;
        }
        SpyAssertion spyAssertion = assertions.get(id);
        if ( spyAssertion == null ){
            return null;
        }
        return spyAssertion;
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
        ExpressionManager expressionManager = getMuleContext().getExpressionManager();
        if ( expressionManager.isExpression(elementValue)){
            compareTo = expressionManager.parse(elementValue, event);
        }
        else {
            Object o = getMuleContext().getRegistry().lookupObject(elementValue);
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
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        if ( MessageProcessor.class.isAssignableFrom(declaringClass) && method.getName().equals("process") )
        {

            return process(obj,args,proxy);
        }

          return proxy.invokeSuper(obj, args);
    }

    protected MockedMessageProcessorManager getMockedMessageProcessorManager() {
        return ((MockedMessageProcessorManager) getMuleContext().getRegistry().lookupObject(MockedMessageProcessorManager.ID));
    }


    public void setId(MessageProcessorId id) {
        this.id = id;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public MuleContext getMuleContext() {
        return MunitCore.getMuleContext();
    }
}
