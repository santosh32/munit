package org.mule.munit.common.mocking;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.MuleRegistry;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.endpoint.OutboundBehavior;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class EndpointMockerTest {

    public static final String ADDRESS = "address";
    private MuleContext muleContext;
    private MuleMessage muleMessage;
    private MuleRegistry muleRegistry;
    private MockEndpointManager endpointManager;
    private MuleEvent mockEvent;

    @Before
    public void setUp(){
        muleContext = mock(MuleContext.class);
        muleMessage = mock(MuleMessage.class);
        muleRegistry = mock(MuleRegistry.class);
        endpointManager = mock(MockEndpointManager.class);
        mockEvent = mock(MuleEvent.class);
        
        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(endpointManager);
    }

    @Test
    public void testAddressIsSetCorrectly(){

        SpyProcessImpl spyProcess = (SpyProcessImpl) spyProcess();
        EndpointMocker endpointMocker = new MockEndpointMocker(muleContext);
        endpointMocker.expectEndpointWithAddress(ADDRESS)
                .withIncomingMessageSatisfying(createSpyProcess(spyProcess))
        .toReturn(muleMessage);


        verify(endpointManager).addBehavior(eq(ADDRESS), any(OutboundBehavior.class));
        assertTrue(spyProcess.called);

    }

    private ArrayList<SpyProcess> createSpyProcess(SpyProcess spy) {
        ArrayList<SpyProcess> spyProcesses = new ArrayList<SpyProcess>();
        spyProcesses.add(spy);
        return spyProcesses;
    }

    private SpyProcess spyProcess() {
        return new SpyProcessImpl();
    }

    private class MockEndpointMocker extends EndpointMocker{



        public MockEndpointMocker(MuleContext muleContext) {
            super(muleContext);
        }

        @Override
        protected List<MessageProcessor> createMessageProcessorFromSpy(List<SpyProcess> process) {
            List<MessageProcessor> messageProcessorFromSpy = super.createMessageProcessorFromSpy(process);
            for ( MessageProcessor mp : messageProcessorFromSpy ){
                try {
                    mp.process(mockEvent);
                } catch (MuleException e) {

                }
            }
            return messageProcessorFromSpy;
        }
    }

    private class SpyProcessImpl implements SpyProcess{
        boolean called;
        @Override
        public void spy(MuleEvent event) throws MuleException {
            called = true;
        }
    }
}
