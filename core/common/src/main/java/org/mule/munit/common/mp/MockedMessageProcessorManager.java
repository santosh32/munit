package org.mule.munit.common.mp;

import java.util.*;

/**
 * <p>The Class that manages the mocking process. Gets the behaviors, stores the message processor calls and stores
 * the spy process</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockedMessageProcessorManager {
    public static String ID = "_muleMockMpManager";

    /**
     * <p>These are the behaviors expected for different message processor mocks</p>
     */
    protected List<MockedMessageProcessorBehavior> behaviors = new ArrayList<MockedMessageProcessorBehavior>();

    /**
     * <p>These are the real calls of the message processors.</p>
     */
    protected List<MessageProcessorCall> calls = new ArrayList<MessageProcessorCall>();

    /**
     * <p>The spy process per message processor</p>
     */
    protected Map<MessageProcessorId, SpyAssertion> spyAssertions = new HashMap<MessageProcessorId, SpyAssertion>();


    /**
     * <p>Reset all the status</p>
     */
    public void reset(){
        behaviors.clear();
        calls.clear();
        spyAssertions.clear();
    }

    /**
     * <p>Retrieve all the execute calls for a message processor that satisfies the attribute matchers</p>
     * @param mpId The Message processor Id
     * @param attributesMatchers The attributes that the message processor must match
     * @return The List of message processor calls
     */
    public List<MessageProcessorCall> findCallsFor(MessageProcessorId mpId, Map<String,Object> attributesMatchers) {
        List<MessageProcessorCall> expected = new ArrayList<MessageProcessorCall>();
        MessageProcessorCall matchingCall = new MessageProcessorCall(mpId);
        matchingCall.setAttributes(attributesMatchers);
        for ( MessageProcessorCall call : calls){
            if (matchingCall.matchingWeight(call) >= 0){
                expected.add(call);
            }
        }
        return expected;
    }

    /**
     * <p>Gets the best matching Behavior. The best matching behavior is the one that mostly matches the attributes</p>
     * @param messageProcessorCall The comparing call
     * @return The best matching behavior
     */
    public MockedMessageProcessorBehavior getBetterMatchingBehavior(MessageProcessorCall messageProcessorCall) {
        Map.Entry<Integer, MockedMessageProcessorBehavior> bestMatchingBehavior = new AbstractMap.SimpleEntry<Integer, MockedMessageProcessorBehavior>(0, null);
        for ( MockedMessageProcessorBehavior behavior : behaviors ){
            int matchingWeight = behavior.getMessageProcessorCall().matchingWeight(messageProcessorCall);
            if ( matchingWeight >= 0 && matchingWeight>=bestMatchingBehavior.getKey()){
                bestMatchingBehavior.setValue(behavior);
            }
        }

        return bestMatchingBehavior.getValue();
    }


    public Map<MessageProcessorId, SpyAssertion> getSpyAssertions() {
        return spyAssertions;
    }

    public synchronized void addBehavior(MockedMessageProcessorBehavior behavior) {
        behaviors.add(behavior);
    }

    public synchronized void addCall(MessageProcessorCall call){
        calls.add(call);
    }

    public synchronized void addSpyAssertion(MessageProcessorId messageProcessor, SpyAssertion assertionMessageProcessor){
        spyAssertions.put(messageProcessor, assertionMessageProcessor);
    }
}
