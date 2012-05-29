package org.mule.munit;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.NestedProcessor;
import org.mule.api.processor.MessageProcessor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 3/12/12
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockedMessageProcessor implements MessageProcessor{
    private MPMockModule mockModule;

    public MockedMessageProcessor() {
    }

    public MockedMessageProcessor(MPMockModule mpMockModule) {
        this.mockModule = mpMockModule;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {
        List<NestedProcessor> assertions = mockModule.getAssertion();

        if ( assertions != null )
        {
            for ( NestedProcessor assertion : assertions )
            {
                try {
                    assertion.process(event.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        if ( mockModule.getResponse() != null )
        {
            event.getMessage().setPayload(mockModule.getResponse());
        }
        return event;
    }
}
