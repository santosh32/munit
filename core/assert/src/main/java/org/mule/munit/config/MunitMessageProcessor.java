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
import org.mule.munit.AssertModule;
import org.mule.util.TemplateParser;

import java.util.concurrent.atomic.AtomicInteger;

import static org.mule.munit.common.MunitCore.buildMuleStackTrace;

/**
 * <p>
 *     Generic Munit Message Processor.
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
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

    /**
     * <p>
     *     Executes the message processor code. In case of an assertion error it throws a new exception with the
     *     mule stack trace (@since 3.4)
     * </p>
     * @param event
     * <p>
     *     The mule event to be processed.
     * </p>
     * @return
     * <p>
     *     The result mule event
     * </p>
     * @throws MuleException
     * <p>
     *     In case of error. If the assertion fails, it throws an {@link AssertionError}
     * </p>
     */
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
            AssertionError exception = new AssertionError(getMessage(error));
            exception.setStackTrace(buildMuleStackTrace(muleContext).toArray(new StackTraceElement[]{}));

            throw  exception;
        }
        catch (Exception e) {
            throw new MessagingException(CoreMessages.failedToInvoke(getProcessor()), event, e);
        }
    }


    /**
     * <p>
     *     The method that do the actual process
     * </p>
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

    private String getMessage(AssertionError error) {
        String message = error.getMessage();
        if ( StringUtils.isEmpty(message )) {
            return this.getProcessor();
        }
        return message;
    }
}
