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


/**
 * ExpectFailMessageProcessor invokes the {@link org.mule.munit.MockModule#expectFail(String, String)} method in {@link org.mule.munit.MockModule }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 *
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-09-24T08:53:54-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class ExpectFailMessageProcessor
    extends AbstractMessageProcessor<Object>
    implements Disposable, Initialisable, Startable, Stoppable, MessageProcessor
{

    protected Object when;
    protected String _whenType;
    protected Object throwA;
    protected String _throwAType;

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
     * Sets throwA
     *
     * @param value Value to set
     */
    public void setThrowA(Object value) {
        this.throwA = value;
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
            final String _transformedWhen = ((String) evaluateAndTransform(getMuleContext(), event, ExpectFailMessageProcessor.class.getDeclaredField("_whenType").getGenericType(), null, when));
            final String _transformedThrowA = ((String) evaluateAndTransform(getMuleContext(), event, ExpectFailMessageProcessor.class.getDeclaredField("_throwAType").getGenericType(), null, throwA));
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
                    ((MockModule) object).expectFail(_transformedWhen, _transformedThrowA);
                    return null;
                }

            }
            , this, event);
            return event;
        } catch (MessagingException messagingException) {
            messagingException.setProcessedEvent(event);
            throw messagingException;
        } catch (Exception e) {
            throw new MessagingException(CoreMessages.failedToInvoke("expectFail"), event, e);
        }
    }

}
