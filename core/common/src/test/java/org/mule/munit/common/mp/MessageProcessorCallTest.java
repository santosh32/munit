package org.mule.munit.common.mp;


import org.junit.Test;
import org.mule.munit.common.matchers.EqMatcher;

import java.util.HashMap;

import static junit.framework.Assert.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MessageProcessorCallTest {

    public static final HashMap<String,Object> ATTRIBUTES = new HashMap<String, Object>();
    public static final String TEST_NAME = "testName";
    public static final String TEST_NAMESPACE = "testNamespace";
    public static final MessageProcessorId MESSAGE_PROCESSOR_ID = new MessageProcessorId(TEST_NAME, TEST_NAMESPACE);

    @Test
    public void gettersReturnOriginalValue(){
        MessageProcessorCall call = new MessageProcessorCall(MESSAGE_PROCESSOR_ID);
        call.setAttributes(ATTRIBUTES);

        assertEquals(MESSAGE_PROCESSOR_ID, call.getMessageProcessorId());
        assertEquals(ATTRIBUTES, call.getAttributes());
    }

    @Test
    public void attributesMustNeverBeNull(){
        MessageProcessorCall call = new MessageProcessorCall(MESSAGE_PROCESSOR_ID);

        assertNotNull(call.getAttributes());
    }

    @Test
    public void returnTrueCallOf(){
        MessageProcessorCall call = new MessageProcessorCall(MESSAGE_PROCESSOR_ID);

        assertTrue(call.isCallOf(MESSAGE_PROCESSOR_ID));
    }

    
    @Test
    public void ifSameNameButDifferentAttributeReturnNegative(){
        MessageProcessorCall call = createMatcherCall();

        MessageProcessorCall call2 = createRealCall("attr", "attrValue2");

        assertEquals(-1, call.matchingWeight(call2));
    }

    @Test
    public void ifSameNameAndSameAttributeReturnWeight(){
        MessageProcessorCall call = createMatcherCall();
        MessageProcessorCall call2 = createRealCall("attr", "attrValue");

        assertEquals(1, call.matchingWeight(call2));
    }

    @Test
    public void ifSameNameAndnoMatchingAttributeReturnZero(){
        MessageProcessorCall call = new MessageProcessorCall(MESSAGE_PROCESSOR_ID);
        MessageProcessorCall call2 = createRealCall("attr", "attrValue");

        assertEquals(0, call.matchingWeight(call2));
    }

    @Test
    public void ifDifferentNameThenReturnNegative(){
        MessageProcessorCall call = new MessageProcessorCall(new MessageProcessorId("another", "another"));
        MessageProcessorCall call2 = createRealCall("attr", "attrValue");

        assertEquals(-1, call.matchingWeight(call2));
    }

    private MessageProcessorCall createRealCall(String attr, String attrValue) {
        MessageProcessorCall call = new MessageProcessorCall(MESSAGE_PROCESSOR_ID);
        HashMap<String, Object> attributes = new HashMap<String, Object>();

        attributes.put(attr, attrValue);
        call.setAttributes(attributes);
        return call;
    }

    private MessageProcessorCall createMatcherCall() {
        MessageProcessorCall call = new MessageProcessorCall(MESSAGE_PROCESSOR_ID);
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("attr", new EqMatcher("attrValue"));
        call.setAttributes(attributes);
        return call;
    }


}
