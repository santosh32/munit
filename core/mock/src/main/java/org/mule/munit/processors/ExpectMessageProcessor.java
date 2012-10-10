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
 * ExpectMessageProcessor invokes the {@link org.mule.munit.MockModule#expect(String, java.util.Map, Object, String)} method in {@link org.mule.munit.MockModule }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 *
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-09-24T08:53:54-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class ExpectMessageProcessor
    extends AbstractMessageProcessor<Object>
    implements Disposable, Initialisable, Startable, Stoppable, MessageProcessor
{

    protected Object when;
    protected String _whenType;
    protected Object parameters;
    protected Map<String, Object> _parametersType;
    protected Object mustReturn;
    protected Object _mustReturnType;
    protected Object mustReturnResponseFrom;
    protected String _mustReturnResponseFromType;

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
     * Sets mustReturn
     *
     * @param value Value to set
     */
    public void setMustReturn(Object value) {
        this.mustReturn = value;
    }

    /**
     * Sets when
     *
     * @param value Value to set
     */
    public void setWhen(Object value) {
        this.when = value;
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
     * Sets mustReturnResponseFrom
     *
     * @param value Value to set
     */
    public void setMustReturnResponseFrom(Object value) {
        this.mustReturnResponseFrom = value;
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
            final String _transformedWhen = ((String) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_whenType").getGenericType(), null, when));
            final Map<String, Object> _transformedParameters = ((Map<String, Object> ) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_parametersType").getGenericType(), null, parameters));
            final Object _transformedMustReturn = ((Object) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_mustReturnType").getGenericType(), null, mustReturn));
            final String _transformedMustReturnResponseFrom = ((String) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_mustReturnResponseFromType").getGenericType(), null, mustReturnResponseFrom));
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
                    ((MockModule) object).expect(_transformedWhen, _transformedParameters, _transformedMustReturn, _transformedMustReturnResponseFrom);
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
