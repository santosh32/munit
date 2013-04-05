package org.mule.munit.common.mp;

import org.mule.modules.interceptor.processors.MessageProcessorCall;
import org.mule.modules.interceptor.processors.MessageProcessorId;
import org.mule.modules.interceptor.processors.MessageProcessorManager;

import java.util.*;

/**
 * <p>
 *     The Class that manages the mocking process. Gets the behaviors, stores the message processor calls and stores
 * the spy process
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockedMessageProcessorManager  extends MessageProcessorManager{
    public static String ID = "_muleMockMpManager";

      /**
     * <p>
     *     These are the real calls of the message processors.
     * </p>
     */
    protected List<MunitMessageProcessorCall> calls = new LinkedList<MunitMessageProcessorCall>();

    /**
     * <p>
     *     The spy process per message processor
     * </p>
     */
    protected Map<MessageProcessorId, SpyAssertion> spyAssertions = new HashMap<MessageProcessorId, SpyAssertion>();


    /**
     * <p>
     *     Reset all the status
     * </p>
     */
    public void reset(){
        behaviors.clear();
        calls.clear();
        spyAssertions.clear();
    }

    /**
     * <p>
     *     Retrieve all the execute calls for a message processor that satisfies the attribute matchers
     * </p>
     *
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

    public Map<MessageProcessorId, SpyAssertion> getSpyAssertions() {
        return spyAssertions;
    }

    public synchronized void addCall(MunitMessageProcessorCall call){
        calls.add(call);
    }

    public synchronized void addSpyAssertion(MessageProcessorId messageProcessor, SpyAssertion assertionMessageProcessor){
        spyAssertions.put(messageProcessor, assertionMessageProcessor);
    }

    public List<MunitMessageProcessorCall> getCalls() {
        return new LinkedList<MunitMessageProcessorCall>(calls);
    }
}
