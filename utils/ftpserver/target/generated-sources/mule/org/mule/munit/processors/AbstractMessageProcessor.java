
package org.mule.munit.processors;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.config.ConfigurationException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
import org.mule.api.registry.RegistrationException;
import org.mule.api.transformer.Transformer;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.TransformerTemplate;
import org.mule.transport.NullPayload;

@Generated(value = "Mule DevKit Version 3.3.1", date = "2013-01-02T10:50:16-03:00", comments = "Build 3.3.1.1298.3ae82a7")
public abstract class AbstractMessageProcessor<O >
    extends AbstractExpressionEvaluator
    implements FlowConstructAware, MuleContextAware
{

    /**
     * Module object
     * 
     */
    protected O moduleObject;
    /**
     * Mule Context
     * 
     */
    protected MuleContext muleContext;
    /**
     * Flow Construct
     * 
     */
    protected FlowConstruct flowConstruct;

    /**
     * Sets muleContext
     * 
     * @param value Value to set
     */
    public void setMuleContext(MuleContext value) {
        this.muleContext = value;
    }

    /**
     * Retrieves muleContext
     * 
     */
    public MuleContext getMuleContext() {
        return this.muleContext;
    }

    /**
     * Sets flowConstruct
     * 
     * @param value Value to set
     */
    public void setFlowConstruct(FlowConstruct value) {
        this.flowConstruct = value;
    }

    /**
     * Retrieves flowConstruct
     * 
     */
    public FlowConstruct getFlowConstruct() {
        return this.flowConstruct;
    }

    /**
     * Sets moduleObject
     * 
     * @param value Value to set
     */
    public void setModuleObject(O value) {
        this.moduleObject = value;
    }

    /**
     * Retrieves moduleObject
     * 
     */
    public O getModuleObject() {
        return this.moduleObject;
    }

    /**
     * Obtains the expression manager from the Mule context and initialises the connector. If a target object  has not been set already it will search the Mule registry for a default one.
     * 
     * @throws InstantiationException
     * @throws ConfigurationException
     * @throws IllegalAccessException
     * @throws RegistrationException
     */
    protected void findOrCreate(Class moduleClass, boolean shouldAutoCreate, MuleEvent muleEvent)
        throws IllegalAccessException, InstantiationException, ConfigurationException, RegistrationException
    {
        if (moduleObject == null) {
            moduleObject = ((O) muleContext.getRegistry().lookupObject(moduleClass));
            if (moduleObject == null) {
                if (shouldAutoCreate) {
                    moduleObject = ((O) moduleClass.newInstance());
                    muleContext.getRegistry().registerObject(moduleClass.getName(), moduleObject);
                } else {
                    throw new ConfigurationException(MessageFactory.createStaticMessage("Cannot find object"));
                }
            }
        }
        if (moduleObject instanceof String) {
            moduleObject = ((O) muleContext.getExpressionManager().evaluate(((String) moduleObject), muleEvent, true));
            if (moduleObject == null) {
                throw new ConfigurationException(MessageFactory.createStaticMessage("Cannot find object by config name"));
            }
        }
    }

    /**
     * Overwrites the event payload with the specified one
     * 
     */
    public void overwritePayload(MuleEvent event, Object resultPayload)
        throws Exception
    {
        TransformerTemplate.OverwitePayloadCallback overwritePayloadCallback = null;
        if (resultPayload == null) {
            overwritePayloadCallback = new TransformerTemplate.OverwitePayloadCallback(NullPayload.getInstance());
        } else {
            overwritePayloadCallback = new TransformerTemplate.OverwitePayloadCallback(resultPayload);
        }
        List<Transformer> transformerList;
        transformerList = new ArrayList<Transformer>();
        transformerList.add(new TransformerTemplate(overwritePayloadCallback));
        event.getMessage().applyTransformers(event, transformerList);
    }

}
