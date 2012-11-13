package org.mule.munit.common.mocking;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.endpoint.OutboundBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class is a Munit Tool to create Endpoint mocks</p>
 *
 * <p>This is though as a fluent pattern implementation</p>
 *
 * <p>Usage:</p>
 *
 * <code>
 *     new EndpointMocker(muleContext).expectEndpointWithAddress("http://localhost:8080").theReturn(muleMessage);
 * </code>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class EndpointMocker {

    /**
     * <p>The mule context</p>
     */
    private MuleContext muleContext;

    /**
     * <p>The endpoint address. The address is used to identify the endpoint</p>
     */
    private String address;

    /**
     * <p>The processes for Spying the entry of the inbound endpoint</p>
     */
    private List<SpyProcess> process;

    public EndpointMocker(MuleContext muleContext) {
        this.muleContext = muleContext;
    }

    /**
     * <p>Defines which endpoint to use based on the endpoint address</p>

     * @param address
     *      <p>The endpoint identification</p>

     * @return
     *      <p>The EndpointMocker object</p>
     */
    public EndpointMocker expectEndpointWithAddress(String address){
        this.address = address;
        return this;
    }

    /**
     * <p>Adds the spying processes to be consider when executing the endpoint</p>
     *
     * @param process
     *      <p>The spying processes</p>
     * @return
     *      <p>The EndpointMocker object</p>
     */
    public EndpointMocker withIncomingMessageSatisfying(List<SpyProcess> process){
        this.process = process;

        return this; 
    }

    /**
     * <p>Determines what value must the endpoint return</p>
     *
     * @param message
     *      <p>The @see #MuleMessage to return</p>
     */
    public void toReturn(MuleMessage message){
        OutboundBehavior behavior = new OutboundBehavior(message, createMessageProcessorFromSpy(process));

        MockEndpointManager factory = (MockEndpointManager) muleContext.getRegistry().lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);
        factory.addBehavior(address, behavior);
    }


    protected List<MessageProcessor> createMessageProcessorFromSpy(final List<SpyProcess> process) {
        List<MessageProcessor> messageProcessors = new ArrayList<MessageProcessor>();

        messageProcessors.add(new MessageProcessor() {
            @Override
            public MuleEvent process(MuleEvent event) throws MuleException {
                if (process != null ){
                    for ( SpyProcess spyProcess : process){
                        spyProcess.spy(event);
                    }
                }
                return event;
            }
        });
        
        return messageProcessors;
    }
}
