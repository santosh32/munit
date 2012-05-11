package org.mule.munit.config;

import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.lifecycle.*;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.RegistrationException;
import org.mule.config.i18n.CoreMessages;
import org.mule.config.i18n.MessageFactory;
import org.mule.util.TemplateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class ValidateCallsMessageProcessor     implements FlowConstructAware, MuleContextAware, Disposable, Initialisable, Startable, Stoppable, MessageProcessor
{

    private static Logger logger = LoggerFactory.getLogger(ValidateCallsMessageProcessor.class);
    /**
     * Module object
     *
     */
    private Object moduleObject;
    /**
     * Mule Context
     *
     */
    private MuleContext muleContext;
    /**
     * Mule Expression Manager
     *
     */
    private ExpressionManager expressionManager;
    /**
     * Mule Pattern Info
     *
     */
    private TemplateParser.PatternInfo patternInfo;
    /**
     * Flow construct
     *
     */
    private FlowConstruct flowConstruct;
    /**
     * Variable used to track how many retries we have attempted on this message processor
     *
     */
    private AtomicInteger retryCount;
    /**
     * Maximum number of retries that can be attempted.
     *
     */
    private int retryMax;

    /**
     * Obtains the expression manager from the Mule context and initialises the connector. If a target object  has not been set already it will search the Mule registry for a default one.
     *
     * @throws org.mule.api.lifecycle.InitialisationException
     */
    public void initialise()
            throws InitialisationException
    {
        retryCount = new AtomicInteger();
        expressionManager = muleContext.getExpressionManager();
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
        if (moduleObject == null) {
            try {
                moduleObject = muleContext.getRegistry().lookupObject(AssertModuleLifecycleAdapter.class);
                if (moduleObject == null) {
                    throw new InitialisationException(MessageFactory.createStaticMessage("Cannot find object"), this);
                }
            } catch (RegistrationException e) {
                throw new InitialisationException(CoreMessages.initialisationFailure("org.mule.munit.config.AssertModuleLifecycleAdapter"), e, this);
            }
        }
        if (moduleObject instanceof String) {
            moduleObject = muleContext.getRegistry().lookupObject(((String) moduleObject));
            if (moduleObject == null) {
                throw new InitialisationException(MessageFactory.createStaticMessage("Cannot find object by org.mule.munit.config name"), this);
            }
        }
    }

    public void start()
            throws MuleException
    {
    }

    public void stop()
            throws MuleException
    {
    }

    public void dispose() {
    }

    /**
     * Set the Mule context
     *
     * @param context Mule context to set
     */
    public void setMuleContext(MuleContext context) {
        this.muleContext = context;
    }

    /**
     * Sets flow construct
     *
     * @param flowConstruct Flow construct to set
     */
    public void setFlowConstruct(FlowConstruct flowConstruct) {
        this.flowConstruct = flowConstruct;
    }

    /**
     * Sets the instance of the object under which the processor will execute
     *
     * @param moduleObject Instace of the module
     */
    public void setModuleObject(Object moduleObject) {
        this.moduleObject = moduleObject;
    }

    /**
     * Sets retryMax
     *
     * @param value Value to set
     */
    public void setRetryMax(int value) {
        this.retryMax = value;
    }



    /**
     * Invokes the MessageProcessor.
     *
     * @param event MuleEvent to be processed
     * @throws MuleException
     */
    public MuleEvent process(MuleEvent event)
            throws MuleException
    {
        AssertModuleLifecycleAdapter castedModuleObject = null;
        if (moduleObject instanceof String) {
            castedModuleObject = ((AssertModuleLifecycleAdapter) muleContext.getRegistry().lookupObject(((String) moduleObject)));
            if (castedModuleObject == null) {
                throw new MessagingException(CoreMessages.failedToCreate("validateCalls"), event, new RuntimeException("Cannot find the configuration specified by the org.mule.munit.config-ref attribute."));
            }
        } else {
            castedModuleObject = ((AssertModuleLifecycleAdapter) moduleObject);
        }
        try {
            retryCount.getAndIncrement();

            castedModuleObject.validateCalls();
            retryCount.set(0);
            return event;
        } catch (Exception e) {
            throw new MessagingException(CoreMessages.failedToInvoke("validateCalls"), event, e);
        }
    }
}
