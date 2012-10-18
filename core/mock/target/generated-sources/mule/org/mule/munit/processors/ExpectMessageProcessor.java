
package org.mule.munit.processors;

import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.api.process.ProcessAdapter;
import org.mule.api.process.ProcessCallback;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.i18n.CoreMessages;
import org.mule.munit.MockModule;
import org.mule.munit.adapters.MockModuleProcessAdapter;


/**
 * ExpectMessageProcessor invokes the {@link org.mule.munit.MockModule#expect(java.lang.String, java.util.Map, java.lang.Object, java.lang.String)} method in {@link MockModule }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-18T01:17:40-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class ExpectMessageProcessor
    extends AbstractMessageProcessor<Object>
    implements Disposable, Initialisable, Startable, Stoppable, MessageProcessor
{

    protected Object thatMessageProcessor;
    protected String _thatMessageProcessorType;
    protected Object parameters;
    protected Map<String, Object> _parametersType;
    protected Object toReturn;
    protected Object _toReturnType;
    protected Object toReturnResponseFrom;
    protected String _toReturnResponseFromType;

    /**
     * Obtains the expression manager from the Mule context and initialises the connector. If a target object  has not been set already it will search the Mule registry for a default one.
     * 
     * @throws InitialisationException
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
     * Sets thatMessageProcessor
     * 
     * @param value Value to set
     */
    public void setThatMessageProcessor(Object value) {
        this.thatMessageProcessor = value;
    }

    /**
     * Sets toReturn
     * 
     * @param value Value to set
     */
    public void setToReturn(Object value) {
        this.toReturn = value;
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
     * Sets toReturnResponseFrom
     * 
     * @param value Value to set
     */
    public void setToReturnResponseFrom(Object value) {
        this.toReturnResponseFrom = value;
    }

    /**
     * Invokes the MessageProcessor.
     * 
     * @param event MuleEvent to be processed
     * @throws MuleException
     */
    public MuleEvent process(final MuleEvent event)
        throws MuleException
    {
        try {
            findOrCreate(MockModuleProcessAdapter.class, true, event);
            final String _transformedThatMessageProcessor = ((String) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_thatMessageProcessorType").getGenericType(), null, thatMessageProcessor));
            final Map<String, Object> _transformedParameters = ((Map<String, Object> ) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_parametersType").getGenericType(), null, parameters));
            final Object _transformedToReturn = ((Object) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_toReturnType").getGenericType(), null, toReturn));
            final String _transformedToReturnResponseFrom = ((String) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_toReturnResponseFromType").getGenericType(), null, toReturnResponseFrom));
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
                    ((MockModule) object).expect(_transformedThatMessageProcessor, _transformedParameters, _transformedToReturn, _transformedToReturnResponseFrom);
                    return null;
                }

            }
            , this, event);
            return event;
        } catch (MessagingException messagingException) {
            messagingException.setProcessedEvent(event);
            throw messagingException;
        } catch (Exception e) {
            throw new MessagingException(CoreMessages.failedToInvoke("expect"), event, e);
        }
    }

}
