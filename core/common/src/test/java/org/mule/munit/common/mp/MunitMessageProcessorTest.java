package org.mule.munit.common.mp;


import org.junit.Before;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.MuleRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class MunitMessageProcessorTest {

    public static final String TEST_NAMESPACE = "testNamespace";
    public static final String TEST_NAME = "testName";
    public static final MessageProcessorId MESSAGE_PROCESSOR_ID = new MessageProcessorId(TEST_NAME, TEST_NAMESPACE);
    MuleContext muleContext;
    MockedMessageProcessorManager manager;
    MuleRegistry muleRegistry;
    ExpressionManager expressionManager;
    MessageProcessor realMp;
    MuleEvent event;
    private MuleMessage muleMessage;

    @Before
    public void createMocks(){
        manager = mock(MockedMessageProcessorManager.class);
        muleContext = mock(MuleContext.class);
        muleRegistry = mock(MuleRegistry.class);
        expressionManager = mock(ExpressionManager.class);
        realMp = mock(MessageProcessor.class);
        event = mock(MuleEvent.class);
        muleMessage = mock(MuleMessage.class);

        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleContext.getExpressionManager()).thenReturn(expressionManager);

    }

    @Test
    public void ifNoBehaviorDefinedThenCallRealMp() throws MuleException {
       when(manager.getSpyAssertions()).thenReturn(new HashMap<MessageProcessorId, SpyAssertion>());

        MunitMessageProcessorForTest mp = createMp();

        mp.process(event);

        verify(realMp).process(event);
    }


    @Test
    public void ifBehaviorDefinedThenSetPayload() throws MuleException {
        MuleMessage expectedMessage = new DefaultMuleMessage("expectedPayload", muleContext);
        MuleMessage flowMessage = new DefaultMuleMessage("any", muleContext);

        when(manager.getSpyAssertions()).thenReturn(new HashMap<MessageProcessorId, SpyAssertion>());
        when(manager.getBetterMatchingBehavior(any(MessageProcessorCall.class))).thenReturn(
                new MockedMessageProcessorBehavior(new MessageProcessorCall(MESSAGE_PROCESSOR_ID), expectedMessage));
        when(event.getMessage()).thenReturn(flowMessage);

        MunitMessageProcessorForTest mp = createMp();

        mp.process(event);

        verify(realMp,times(0)).process(event);
        verify(manager).addCall(any(MessageProcessorCall.class));
        assertEquals("expectedPayload", flowMessage.getPayload());
    }

    @Test
    public void verifySpyAssertions() throws MuleException {
        MuleMessage expectedMessage = new DefaultMuleMessage("expectedPayload", muleContext);
        MuleMessage flowMessage = new DefaultMuleMessage("any", muleContext);

        HashMap<MessageProcessorId, SpyAssertion> spyAssertions = new HashMap<MessageProcessorId, SpyAssertion>();
        ArrayList<MessageProcessor> beforeMessageProcessors = new ArrayList<MessageProcessor>();
        CheckedMessageProcessor beforeMessageProcessor = new CheckedMessageProcessor();
        beforeMessageProcessors.add(beforeMessageProcessor);


        ArrayList<MessageProcessor> afterMessageProcessors = new ArrayList<MessageProcessor>();
        CheckedMessageProcessor afterMessageProcessor = new CheckedMessageProcessor();
        afterMessageProcessors.add(afterMessageProcessor);
        
        spyAssertions.put(MESSAGE_PROCESSOR_ID, new SpyAssertion(beforeMessageProcessors, afterMessageProcessors));
        
        when(manager.getSpyAssertions()).thenReturn(spyAssertions);
        when(manager.getBetterMatchingBehavior(any(MessageProcessorCall.class))).thenReturn(
                new MockedMessageProcessorBehavior(new MessageProcessorCall(MESSAGE_PROCESSOR_ID), expectedMessage));
        when(event.getMessage()).thenReturn(flowMessage);

        MunitMessageProcessorForTest mp = createMp();

        mp.process(event);

        verify(realMp,times(0)).process(event);
        verify(manager).addCall(any(MessageProcessorCall.class));
        assertEquals("expectedPayload", flowMessage.getPayload());
        assertTrue(beforeMessageProcessor.wasCalled);
        assertTrue(afterMessageProcessor.wasCalled);
    }

    private MunitMessageProcessorForTest createMp() {
        MunitMessageProcessorForTest mp = new MunitMessageProcessorForTest();
        mp.setId(MESSAGE_PROCESSOR_ID);
        mp.setAttributes(attributes());
        mp.setMuleContext(muleContext);
        mp.setRealMp(realMp);
        return mp;
    }

    private Map<String, String> attributes() {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("attr", "attrValue");
        return attributes;
    }

    @Test
    public void testDispose(){
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).dispose();
        assertTrue(realMp.calledDispose);
    }

    @Test
    public void testStop() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).stop();
        assertTrue(realMp.calledStop);
    }

    @Test
    public void testStart() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).start();
        assertTrue(realMp.calledStart);
    }

    @Test
    public void testInitialise() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).initialise();
        assertTrue(realMp.calledInitialise);
    }

    @Test
    public void testSetMuleContext() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).setMuleContext(null);
        assertTrue(realMp.calledSetMuleContext);
    }

    @Test
    public void testSetFlowConstruct() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).setFlowConstruct(null);
        assertTrue(realMp.calledsetFlowConstruct);
    }

    private MunitMessageProcessor munitMessageProcessor(MockedMessageProcessor realMp) {
        MunitMessageProcessor munitMessageProcessor = new MunitMessageProcessor();
        munitMessageProcessor.setRealMp(realMp);
        return munitMessageProcessor;
    }


    private class MunitMessageProcessorForTest extends MunitMessageProcessor{
        @Override
        protected MockedMessageProcessorManager getMockedMessageProcessorManager() {
            return manager;
        }
    }
    
    private class CheckedMessageProcessor implements MessageProcessor{
        boolean wasCalled;
        @Override
        public MuleEvent process(MuleEvent event) throws MuleException {
            wasCalled = true;
            return event;
        }
    }

}
