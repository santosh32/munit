package org.mule.munit.processors;

import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.lifecycle.*;
import org.mule.api.process.ProcessAdapter;
import org.mule.api.process.ProcessCallback;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.i18n.CoreMessages;
import org.mule.munit.MockModule;
import org.mule.munit.adapters.MockModuleProcessAdapter;

import javax.annotation.Generated;
import java.util.List;
import java.util.Map;


/**
 * VerifyCallMessageProcessor invokes the {@link org.mule.munit.MockModule#verifyCall(String, java.util.Map, Integer, Integer, Integer)} method in {@link org.mule.munit.MockModule }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 *
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-09-24T08:53:54-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class VerifyCallMessageProcessor
    extends AbstractMessageProcessor<Object>
    implements Disposable, Initialisable, Startable, Stoppable, MessageProcessor
{

    protected Object messageProcessor;
    protected String _messageProcessorType;
    protected Object parameters;
    protected Map<String, Object> _parametersType;
    protected Object times;
    protected Integer _timesType;
    protected Object atLeast;
    protected Integer _atLeastType;
    protected Object atMost;
    protected Integer _atMostType;

    /**
     * Obtains the expression manager from the Mule context and initialises the connector. If a target object  has not been set already it will search the Mule registry for a default one.
     *
     * @throws org.mule.api.lifecycle.InitialisationException
     */
    public void initialise()
        throws InitialisationException
    {
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
        super.setMuleContext(context);
    }

    /**
     * Sets flow construct
     *
     * @param flowConstruct Flow construct to set
     */
    public void setFlowConstruct(FlowConstruct flowConstruct) {
        super.setFlowConstruct(flowConstruct);
    }

    /**
     * Sets atMost
     *
     * @param value Value to set
     */
    public void setAtMost(Object value) {
        this.atMost = value;
    }

    /**
     * Sets times
     *
     * @param value Value to set
     */
    public void setTimes(Object value) {
        this.times = value;
    }

    /**
     * Sets parameters
     *
     * @param value Value to set
     */
    public void setParameters(Object value) {
        this.parameters = value;
    }

    /**
     * Sets messageProcessor
     *
     * @param value Value to set
     */
    public void setMessageProcessor(Object value) {
        this.messageProcessor = value;
    }

    /**
     * Sets atLeast
     *
     * @param value Value to set
     */
    public void setAtLeast(Object value) {
        this.atLeast = value;
    }

    /**
     * Invokes the MessageProcessor.
     *
     * @param event MuleEvent to be processed
     * @throws org.mule.api.MuleException
     */
    public MuleEvent process(final MuleEvent event)
        throws MuleException
    {
        try {
            findOrCreate(MockModuleProcessAdapter.class, false, event);
            final String _transformedMessageProcessor = ((String) evaluateAndTransform(getMuleContext(), event, VerifyCallMessageProcessor.class.getDeclaredField("_messageProcessorType").getGenericType(), null, messageProcessor));
            final Map<String, Object> _transformedParameters = ((Map<String, Object> ) evaluateAndTransform(getMuleContext(), event, VerifyCallMessageProcessor.class.getDeclaredField("_parametersType").getGenericType(), null, parameters));
            final Integer _transformedTimes = ((Integer) evaluateAndTransform(getMuleContext(), event, VerifyCallMessageProcessor.class.getDeclaredField("_timesType").getGenericType(), null, times));
            final Integer _transformedAtLeast = ((Integer) evaluateAndTransform(getMuleContext(), event, VerifyCallMessageProcessor.class.getDeclaredField("_atLeastType").getGenericType(), null, atLeast));
            final Integer _transformedAtMost = ((Integer) evaluateAndTransform(getMuleContext(), event, VerifyCallMessageProcessor.class.getDeclaredField("_atMostType").getGenericType(), null, atMost));
            ProcessTemplate<Object, Object> processTemplate = ((ProcessAdapter<Object> ) getModuleObject()).getProcessTemplate();
            processTemplate.execute(new ProcessCallback<Object,Object>() {


                public List<Class> getManagedExceptions() {
                    return null;
                }

                public boolean isProtected() {
                    return false;
                }

                public Object process(Object object)
                    throws Exception
                {
                    ((MockModule) object).verifyCall(_transformedMessageProcessor, _transformedParameters, _transformedTimes, _transformedAtLeast, _transformedAtMost);
                    return null;
                }

            }
            , this, event);
            return event;
        } catch (MessagingException messagingException) {
            messagingException.setProcessedEvent(event);
            throw messagingException;
        } catch (Exception e) {
            throw new MessagingException(CoreMessages.failedToInvoke("verifyCall"), event, e);
        }
    }

}
