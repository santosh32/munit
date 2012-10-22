
package org.mule.munit.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
 * OutboundEndpointMessageProcessor invokes the {@link org.mule.munit.MockModule#outboundEndpoint(java.lang.String, java.lang.Object, java.util.Map, java.util.Map, java.util.Map, java.util.Map, java.util.List)} method in {@link MockModule }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-22T05:47:49-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class OutboundEndpointMessageProcessor
    extends AbstractMessageProcessor<Object>
    implements Disposable, Initialisable, Startable, Stoppable, MessageProcessor
{

    protected Object address;
    protected String _addressType;
    protected Object returnPayload;
    protected Object _returnPayloadType;
    protected Object returnInvocationProperties;
    protected Map<String, Object> _returnInvocationPropertiesType;
    protected Object returnInboundProperties;
    protected Map<String, Object> _returnInboundPropertiesType;
    protected Object returnSessionProperties;
    protected Map<String, Object> _returnSessionPropertiesType;
    protected Object returnOutboundProperties;
    protected Map<String, Object> _returnOutboundPropertiesType;
    protected Object assertions;
    protected List<NestedProcessor> _assertionsType;

    /**
     * Obtains the expression manager from the Mule context and initialises the connector. If a target object  has not been set already it will search the Mule registry for a default one.
     * 
     * @throws InitialisationException
     */
    public void initialise()
        throws InitialisationException
    {
        if (assertions instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertions)) {
                if (messageProcessor instanceof Initialisable) {
                    ((Initialisable) messageProcessor).initialise();
                }
            }
        }
    }

    public void start()
        throws MuleException
    {
        if (assertions instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertions)) {
                if (messageProcessor instanceof Startable) {
                    ((Startable) messageProcessor).start();
                }
            }
        }
    }

    public void stop()
        throws MuleException
    {
        if (assertions instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertions)) {
                if (messageProcessor instanceof Stoppable) {
                    ((Stoppable) messageProcessor).stop();
                }
            }
        }
    }

    public void dispose() {
        if (assertions instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertions)) {
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
        if (assertions instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertions)) {
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
        if (assertions instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertions)) {
                if (messageProcessor instanceof FlowConstructAware) {
                    ((FlowConstructAware) messageProcessor).setFlowConstruct(flowConstruct);
                }
            }
        }
    }

    /**
     * Sets returnInboundProperties
     * 
     * @param value Value to set
     */
    public void setReturnInboundProperties(Object value) {
        this.returnInboundProperties = value;
    }

    /**
     * Sets assertions
     * 
     * @param value Value to set
     */
    public void setAssertions(Object value) {
        this.assertions = value;
    }

    /**
     * Sets returnInvocationProperties
     * 
     * @param value Value to set
     */
    public void setReturnInvocationProperties(Object value) {
        this.returnInvocationProperties = value;
    }

    /**
     * Sets returnOutboundProperties
     * 
     * @param value Value to set
     */
    public void setReturnOutboundProperties(Object value) {
        this.returnOutboundProperties = value;
    }

    /**
     * Sets address
     * 
     * @param value Value to set
     */
    public void setAddress(Object value) {
        this.address = value;
    }

    /**
     * Sets returnPayload
     * 
     * @param value Value to set
     */
    public void setReturnPayload(Object value) {
        this.returnPayload = value;
    }

    /**
     * Sets returnSessionProperties
     * 
     * @param value Value to set
     */
    public void setReturnSessionProperties(Object value) {
        this.returnSessionProperties = value;
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
            final String _transformedAddress = ((String) evaluateAndTransform(getMuleContext(), event, OutboundEndpointMessageProcessor.class.getDeclaredField("_addressType").getGenericType(), null, address));
            final Object _transformedReturnPayload = ((Object) evaluateAndTransform(getMuleContext(), event, OutboundEndpointMessageProcessor.class.getDeclaredField("_returnPayloadType").getGenericType(), null, returnPayload));
            final Map<String, Object> _transformedReturnInvocationProperties = ((Map<String, Object> ) evaluateAndTransform(getMuleContext(), event, OutboundEndpointMessageProcessor.class.getDeclaredField("_returnInvocationPropertiesType").getGenericType(), null, returnInvocationProperties));
            final Map<String, Object> _transformedReturnInboundProperties = ((Map<String, Object> ) evaluateAndTransform(getMuleContext(), event, OutboundEndpointMessageProcessor.class.getDeclaredField("_returnInboundPropertiesType").getGenericType(), null, returnInboundProperties));
            final Map<String, Object> _transformedReturnSessionProperties = ((Map<String, Object> ) evaluateAndTransform(getMuleContext(), event, OutboundEndpointMessageProcessor.class.getDeclaredField("_returnSessionPropertiesType").getGenericType(), null, returnSessionProperties));
            final Map<String, Object> _transformedReturnOutboundProperties = ((Map<String, Object> ) evaluateAndTransform(getMuleContext(), event, OutboundEndpointMessageProcessor.class.getDeclaredField("_returnOutboundPropertiesType").getGenericType(), null, returnOutboundProperties));
            final List<NestedProcessor> _transformedAssertions = new ArrayList<NestedProcessor>();
            if (assertions!= null) {
                for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) assertions)) {
                    _transformedAssertions.add(new NestedProcessorChain(event, getMuleContext(), messageProcessor));
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
                    ((MockModule) object).outboundEndpoint(_transformedAddress, _transformedReturnPayload, _transformedReturnInvocationProperties, _transformedReturnInboundProperties, _transformedReturnSessionProperties, _transformedReturnOutboundProperties, _transformedAssertions);
                    return null;
                }

            }
            , this, event);
            return event;
        } catch (MessagingException messagingException) {
            messagingException.setProcessedEvent(event);
            throw messagingException;
        } catch (Exception e) {
            throw new MessagingException(CoreMessages.failedToInvoke("outboundEndpoint"), event, e);
        }
    }

}
