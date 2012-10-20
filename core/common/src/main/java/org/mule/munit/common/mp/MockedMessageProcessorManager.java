package org.mule.munit.common.mp;

import org.apache.commons.lang.StringUtils;
import org.mule.munit.common.matchers.Matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockedMessageProcessorManager {
    public static String ID = "_muleMockMpManager";

    private List<MockedMessageProcessorBehavior> behaviors = new ArrayList<MockedMessageProcessorBehavior>();
    private List<MessageProcessorCall> calls = new ArrayList<MessageProcessorCall>();
    private Map<String, SpyAssertion> spyAssertions = new HashMap<String, SpyAssertion>();
    
    public List<MockedMessageProcessorBehavior> getBehaviorsFor(String namespace, String name){
        List<MockedMessageProcessorBehavior> expected = new ArrayList<MockedMessageProcessorBehavior>();
        for ( MockedMessageProcessorBehavior behavior : behaviors){

            if (isExpected(namespace, name, behavior.getName(), behavior.getNamespace())){
                expected.add(behavior);
            }
        }
        return expected;
    }

    private boolean isExpected(String namespace, String name, String behaviorName, String behaviorNamespace) {
        return behaviorName.equals(name) && ( !StringUtils.isEmpty(namespace) && namespace.equals(behaviorNamespace) );
    }

    public void reset(){
        behaviors.clear();
        calls.clear();
    }

    public synchronized void addBehavior(MockedMessageProcessorBehavior behavior) {
        behaviors.add(behavior);
    }

    public synchronized void addCall(MessageProcessorCall call){
        calls.add(call);
    }

    public synchronized void addSpyAssertion(String messageProcessor, SpyAssertion assertionMessageProcessor){
        spyAssertions.put(messageProcessor, assertionMessageProcessor);
    }
    
    public List<MessageProcessorCall> findCallsFor(String name, String namespace, Map<String,Object> attributesMatchers) {
        List<MessageProcessorCall> expected = new ArrayList<MessageProcessorCall>();
        for ( MessageProcessorCall call : calls){

            if (isExpected(namespace, name, call.getName(), call.getNamespace())){
                if ( attributesMatchers == null ){
                    expected.add(call);
                }
                else{
                    int totalMatching = 0;
                    for (Map.Entry<String,Object> matchers : attributesMatchers.entrySet() ){
                       Map<String, Object> attributes = call.getAttributes();
                       if ( attributes.containsKey(matchers.getKey()) ){
                           totalMatching += matches( matchers.getValue(), attributes.get(matchers.getKey())) ? 1 :0 ;
                       }

                   }
                    if ( totalMatching == attributesMatchers.size()){
                        expected.add(call);
                    }
                }
            }
        }
        return expected;
    }

    private boolean matches(Object matcher, Object value) {
        if ( matcher instanceof Matcher ){
            return ((Matcher) matcher).match(value);
        }
        else{
            return  matcher.equals(value);
        }
    }

    public Map<String, SpyAssertion> getSpyAssertions() {
        return spyAssertions;
    }
}
