
package org.mule.munit.processors;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.NestedProcessor;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
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
import org.mule.munit.process.NestedProcessorChain;


/**
 * SpyMessageProcessor invokes the {@link org.mule.munit.MockModule#spy(java.lang.String, java.util.List, java.util.List)} method in {@link MockModule }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-22T05:47:49-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class SpyMessageProcessor
    extends AbstractMessageProcessor<Object>
    implements Disposable, Initialisable, Startable, Stoppable, MessageProcessor
{

    protected Object messageProcessor;
    protected String _messageProcessorType;
    protected Object assertionsBeforeCall;
    protected List<NestedProcessor> _assertionsBeforeCallType;
    protected Object assertionsAfterCall;
    protected List<NestedProcessor> _assertionsAfterCallType;

    /**
     * Obtains the expression manager from the Mule context and initialises the connector. If a target object  has not been set already it will search the Mule registry for a default one.
     * 
     * @throws InitialisationException
     */
    public void initialise()
        throws InitialisationException
    {
        if (assertionsBeforeCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsBeforeCall)) {
                if (messageProcessor instanceof Initialisable) {
                    ((Initialisable) messageProcessor).initialise();
                }
            }
        }
        if (assertionsAfterCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsAfterCall)) {
                if (messageProcessor instanceof Initialisable) {
                    ((Initialisable) messageProcessor).initialise();
                }
            }
        }
    }

    public void start()
        throws MuleException
    {
        if (assertionsBeforeCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsBeforeCall)) {
                if (messageProcessor instanceof Startable) {
                    ((Startable) messageProcessor).start();
                }
            }
        }
        if (assertionsAfterCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsAfterCall)) {
                if (messageProcessor instanceof Startable) {
                    ((Startable) messageProcessor).start();
                }
            }
        }
    }

    public void stop()
        throws MuleException
    {
        if (assertionsBeforeCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsBeforeCall)) {
                if (messageProcessor instanceof Stoppable) {
                    ((Stoppable) messageProcessor).stop();
                }
            }
        }
        if (assertionsAfterCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsAfterCall)) {
                if (messageProcessor instanceof Stoppable) {
                    ((Stoppable) messageProcessor).stop();
                }
            }
        }
    }

    public void dispose() {
        if (assertionsBeforeCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsBeforeCall)) {
                if (messageProcessor instanceof Disposable) {
                    ((Disposable) messageProcessor).dispose();
                }
            }
        }
        if (assertionsAfterCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsAfterCall)) {
                if (messageProcessor instanceof Disposable) {
                    ((Disposable) messageProcessor).dispose();
                }
            }
        }
    }

    /**
     * Set the Mule context
     * 
     * @param context Mule context to set
     */
    public void setMuleContext(MuleContext context) {
        super.setMuleContext(context);
        if (assertionsBeforeCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsBeforeCall)) {
                if (messageProcessor instanceof MuleContextAware) {
                    ((MuleContextAware) messageProcessor).setMuleContext(context);
                }
            }
        }
        if (assertionsAfterCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsAfterCall)) {
                if (messageProcessor instanceof MuleContextAware) {
                    ((MuleContextAware) messageProcessor).setMuleContext(context);
                }
            }
        }
    }

    /**
     * Sets flow construct
     * 
     * @param flowConstruct Flow construct to set
     */
    public void setFlowConstruct(FlowConstruct flowConstruct) {
        super.setFlowConstruct(flowConstruct);
        if (assertionsBeforeCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsBeforeCall)) {
                if (messageProcessor instanceof FlowConstructAware) {
                    ((FlowConstructAware) messageProcessor).setFlowConstruct(flowConstruct);
                }
            }
        }
        if (assertionsAfterCall instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsAfterCall)) {
                if (messageProcessor instanceof FlowConstructAware) {
                    ((FlowConstructAware) messageProcessor).setFlowConstruct(flowConstruct);
                }
            }
        }
    }

    /**
     * Sets assertionsBeforeCall
     * 
     * @param value Value to set
     */
    public void setAssertionsBeforeCall(Object value) {
        this.assertionsBeforeCall = value;
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
     * Sets assertionsAfterCall
     * 
     * @param value Value to set
     */
    public void setAssertionsAfterCall(Object value) {
        this.assertionsAfterCall = value;
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
            final String _transformedMessageProcessor = ((String) evaluateAndTransform(getMuleContext(), event, SpyMessageProcessor.class.getDeclaredField("_messageProcessorType").getGenericType(), null, messageProcessor));
            final List<NestedProcessor> _transformedAssertionsBeforeCall = new ArrayList<NestedProcessor>();
            if (assertionsBeforeCall!= null) {
                for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsBeforeCall)) {
                    _transformedAssertionsBeforeCall.add(new NestedProcessorChain(event, getMuleContext(), messageProcessor));
                }
            }
            final List<NestedProcessor> _transformedAssertionsAfterCall = new ArrayList<NestedProcessor>();
            if (assertionsAfterCall!= null) {
                for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertionsAfterCall)) {
                    _transformedAssertionsAfterCall.add(new NestedProcessorChain(event, getMuleContext(), messageProcessor));
                }
            }
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
                    ((MockModule) object).spy(_transformedMessageProcessor, _transformedAssertionsBeforeCall, _transformedAssertionsAfterCall);
                    return null;
                }

            }
            , this, event);
            return event;
        } catch (MessagingException messagingException) {
            messagingException.setProcessedEvent(event);
            throw messagingException;
        } catch (Exception e) {
            throw new MessagingException(CoreMessages.failedToInvoke("spy"), event, e);
        }
    }

}
