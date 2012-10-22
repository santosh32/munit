
package org.mule.munit.processors;

import java.util.List;
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
import org.mule.munit.Attribute;
import org.mule.munit.MockModule;
import org.mule.munit.MunitMuleMessage;
import org.mule.munit.adapters.MockModuleProcessAdapter;


/**
 * ExpectMessageProcessor invokes the {@link org.mule.munit.MockModule#expect(java.lang.String, org.mule.munit.MunitMuleMessage, java.util.List)} method in {@link MockModule }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-22T05:47:49-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class ExpectMessageProcessor
    extends AbstractMessageProcessor<Object>
    implements Disposable, Initialisable, Startable, Stoppable, MessageProcessor
{

    protected Object messageProcessor;
    protected String _messageProcessorType;
    protected Object toReturn;
    protected MunitMuleMessage _toReturnType;
    protected Object attributes;
    protected List<Attribute> _attributesType;

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
     * Sets toReturn
     * 
     * @param value Value to set
     */
    public void setToReturn(Object value) {
        this.toReturn = value;
    }

    /**
     * Sets attributes
     * 
     * @param value Value to set
     */
    public void setAttributes(Object value) {
        this.attributes = value;
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
            final String _transformedMessageProcessor = ((String) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_messageProcessorType").getGenericType(), null, messageProcessor));
            final MunitMuleMessage _transformedToReturn = ((MunitMuleMessage) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_toReturnType").getGenericType(), null, toReturn));
            final List<Attribute> _transformedAttributes = ((List<Attribute> ) evaluateAndTransform(getMuleContext(), event, ExpectMessageProcessor.class.getDeclaredField("_attributesType").getGenericType(), null, attributes));
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
                    ((MockModule) object).expect(_transformedMessageProcessor, _transformedToReturn, _transformedAttributes);
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
