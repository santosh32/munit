package org.mule.munit.common.mp;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang.StringUtils;
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
    private String fileName;
    private String lineNumber;


    public Object process(Object obj,Object[] args, MethodProxy proxy) throws Throwable {
        MuleEvent event = (MuleEvent) args[0];

        MockedMessageProcessorManager manager = getMockedMessageProcessorManager();

        MessageProcessorCall messageProcessorCall = buildCall(event);
        runSpyBeforeAssertions(manager, event);

        registerCall(manager, messageProcessorCall);
        MockedMessageProcessorBehavior behavior = manager.getBetterMatchingBehavior(messageProcessorCall);
        if ( behavior != null ){
            if (behavior.getExceptionToThrow() != null) {
                runSpyAfterAssertions(manager, event);
                throw behavior.getExceptionToThrow();
            }

            MunitUtils.copyMessage((DefaultMuleMessage) behavior.getReturnMuleMessage(), (DefaultMuleMessage) event.getMessage());

            runSpyAfterAssertions(manager, event);
            return event;
        }

        runSpyAfterAssertions(manager, event);
        return proxy.invokeSuper(obj,args);
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

    private void registerCall(MockedMessageProcessorManager manager, MessageProcessorCall messageProcessorCall) {
        manager.addCall(messageProcessorCall);
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
            try{
                Object evaluate = evaluate(attrs.getValue(), event);
                processed.put(attrs.getKey(), evaluate);
            }catch (Throwable t){
                processed.put(attrs.getKey(), attrs.getValue());
            }
        }
        call.setAttributes(processed);
        call.setFlowConstruct(event.getFlowConstruct());
        call.setFileName(fileName);
        call.setLineNumber(lineNumber);
        return call;
    }


    private Object evaluate(String elementValue, MuleEvent event) {
        Object compareTo = elementValue;
        ExpressionManager expressionManager = getMuleContext().getExpressionManager();
        if ( expressionManager.isExpression(elementValue)){
            compareTo = expressionManager.parse(elementValue, event);
        }
        else if (!StringUtils.isEmpty(elementValue) ){
            Object o = getMuleContext().getRegistry().lookupObject(elementValue);
            if ( o != null ){
                compareTo = o;
            }
        }
        return compareTo;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }
}
