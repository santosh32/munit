package org.mule.munit.config;

import org.apache.commons.lang.StringUtils;
import org.mule.api.*;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.RegistrationException;
import org.mule.config.i18n.CoreMessages;
import org.mule.config.i18n.MessageFactory;
import org.mule.construct.Flow;
import org.mule.munit.AssertModule;
import org.mule.munit.common.mp.MessageProcessorCall;
import org.mule.munit.common.mp.MockedMessageProcessorManager;
import org.mule.util.TemplateParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mule.munit.common.MunitCore.getStackTraceElements;

/**
 * <p>Generic Munit Message Processor</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public abstract class MunitMessageProcessor implements Initialisable, MessageProcessor, MuleContextAware, FlowConstructAware{

    protected FlowConstruct flowConstruct;
    protected Object moduleObject;
    protected MuleContext muleContext;
    protected ExpressionManager expressionManager;
    protected TemplateParser.PatternInfo patternInfo;
    protected AtomicInteger retryCount;
    protected int retryMax;

    public void initialise()
            throws InitialisationException
    {
        retryCount = new AtomicInteger();
        expressionManager = muleContext.getExpressionManager();
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
        if (moduleObject == null) {
            try {
                moduleObject = muleContext.getRegistry().lookupObject(AssertModule.class);
                if (moduleObject == null) {
                    throw new InitialisationException(MessageFactory.createStaticMessage("Cannot find object"), this);
                }
            } catch (RegistrationException e) {
                throw new InitialisationException(CoreMessages.initialisationFailure("org.mule.munit.AssertModule"), e, this);
            }
        }

    }

    protected AssertModule getModule(MuleEvent event, String methodName) throws MessagingException {
        AssertModule castedModuleObject;
        if (moduleObject instanceof String) {
            castedModuleObject = ((AssertModule) muleContext.getRegistry().lookupObject(((String) moduleObject)));
            if (castedModuleObject == null) {
                throw new MessagingException(CoreMessages.failedToCreate(methodName), event, new RuntimeException("Cannot find the configuration specified by the org.mule.munit.config-ref attribute."));
            }
        } else {
            castedModuleObject = ((AssertModule) moduleObject);
        }
        return castedModuleObject;
    }


    protected Object evaluate(MuleMessage muleMessage, Object source) {
        if (source instanceof String) {
            String stringSource = ((String) source);
            if (stringSource.startsWith(patternInfo.getPrefix())&&stringSource.endsWith(patternInfo.getSuffix())) {
                return expressionManager.evaluate(stringSource, muleMessage);
            } else {
                    return expressionManager.parse(stringSource, muleMessage);
            }
        }
        return source;
    }

    public MuleEvent process(MuleEvent event)
            throws MuleException
    {
        MuleMessage mulemessage = event.getMessage();
        AssertModule module = getModule(event, getProcessor());
        try {
            retryCount.getAndIncrement();
            doProcess(mulemessage, module);
            retryCount.set(0);
            return event;
        }catch (AssertionError error){
            List<StackTraceElement> stackTraceElements = getStackTraceElements(muleContext);

            AssertionError exception = new AssertionError(getMessage(error));
                     exception.setStackTrace(stackTraceElements.toArray(new StackTraceElement[]{}));


            throw  exception;
        }
        catch (Exception e) {
            throw new MessagingException(CoreMessages.failedToInvoke(getProcessor()), event, e);
        }
    }



    private String getMessage(AssertionError error) {
        String message = error.getMessage();
        if ( StringUtils.isEmpty(message )) {
            return this.getProcessor();
        }
        return message;
    }

    /**
     * <p>The method that do the actual process</p>
     *
     * @param mulemessage
     *      <p>The mule Message</p>
     * @param module
     *      <p>The instance of the assert module</p>
     */
    protected abstract void doProcess(MuleMessage mulemessage, AssertModule module);

    /**
     * @return
     *      <p>The name of the processor</p>
     */
    protected abstract String getProcessor();

    public void setMuleContext(MuleContext context) {
        this.muleContext = context;
    }

    public void setModuleObject(Object moduleObject) {
        this.moduleObject = moduleObject;
    }

    public void setRetryMax(int value) {
        this.retryMax = value;
    }


    public void setFlowConstruct(FlowConstruct flowConstruct) {
        this.flowConstruct = flowConstruct;
    }
}
