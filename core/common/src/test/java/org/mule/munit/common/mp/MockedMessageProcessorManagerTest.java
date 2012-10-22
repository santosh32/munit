package org.mule.munit.common.mp;

import org.junit.Test;
import org.mule.munit.common.matchers.EqMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MockedMessageProcessorManagerTest {

    public static final String TEST_NAMESPACE = "testNamespace";
    public static final String TEST_NAME = "testName";
    public static final MessageProcessorId MESSAGE_PROCESSOR_ID = new MessageProcessorId(TEST_NAME, TEST_NAMESPACE);

    @Test
    public void getCallsWithEmptyMatchers(){
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());

        List<MessageProcessorCall> callsFor = manager.findCallsFor(MESSAGE_PROCESSOR_ID, new HashMap<String, Object>());

        assertFalse(callsFor.isEmpty());
        assertEquals(MESSAGE_PROCESSOR_ID, callsFor.get(0).getMessageProcessorId());
    }

    @Test
     public void getCallsWithEmptyNonMatchers(){
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());

        HashMap<String, Object> attributesMatchers = new HashMap<String, Object>();
        attributesMatchers.put("attr", new EqMatcher("attrValue"));
        List<MessageProcessorCall> callsFor = manager.findCallsFor(MESSAGE_PROCESSOR_ID, attributesMatchers);

        assertFalse(callsFor.isEmpty());
        assertEquals(MESSAGE_PROCESSOR_ID, callsFor.get(0).getMessageProcessorId());
    }

    @Test
    public void getCallsWithEmptyInvalidMatchers(){
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());

        HashMap<String, Object> attributesMatchers = new HashMap<String, Object>();
        attributesMatchers.put("attr", new EqMatcher("attrValu"));
        List<MessageProcessorCall> callsFor = manager.findCallsFor(MESSAGE_PROCESSOR_ID, attributesMatchers);

        assertTrue(callsFor.isEmpty());
    }


    @Test
    public void getCallsWithEmptyInvalidId(){
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());

        List<MessageProcessorCall> callsFor = manager.findCallsFor(new MessageProcessorId("another", "another"), new HashMap<String, Object>());

        assertTrue(callsFor.isEmpty());
    }

    @Test
    public void validThatResetRemovesAll(){
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());
        manager.addBehavior(new MockedMessageProcessorBehavior(createCall(),null));
        manager.addSpyAssertion(MESSAGE_PROCESSOR_ID, new SpyAssertion(null,null));

        manager.reset();

        assertTrue(manager.behaviors.isEmpty());
        assertTrue(manager.spyAssertions.isEmpty());
        assertTrue(manager.calls.isEmpty());
    }

    @Test
    public void getTheBestMatchingBehavior(){
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        MessageProcessorCall bestMatchingCall = createCall();
        Map<String,Object> attributes = bestMatchingCall.getAttributes();
        attributes.put("attr2", "attrValue2");

        manager.addBehavior(new MockedMessageProcessorBehavior(createCall(), null));
        manager.addBehavior(new MockedMessageProcessorBehavior(bestMatchingCall, null));

        MockedMessageProcessorBehavior matched = manager.getBetterMatchingBehavior(bestMatchingCall);

        assertEquals(bestMatchingCall, matched.getMessageProcessorCall());

    }


    private MessageProcessorCall createCall() {
        MessageProcessorCall call = new MessageProcessorCall(MESSAGE_PROCESSOR_ID);
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("attr", "attrValue");
        call.setAttributes(attributes);
        return call;
    }
}
