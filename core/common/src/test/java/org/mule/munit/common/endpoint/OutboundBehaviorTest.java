package org.mule.munit.common.endpoint;

import org.junit.Test;
import org.mule.api.processor.MessageProcessor;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

public class OutboundBehaviorTest {

    public static final ArrayList<MessageProcessor> ASSERTIONS = new ArrayList<MessageProcessor>();
    public static final HashMap<String,Object> INBOUND_PROPERTIES = new HashMap<String, Object>();
    public static final HashMap<String,Object> INVOCATION_PROPERTIES = new HashMap<String, Object>();
    public static final HashMap<String,Object> OUTBOUND_PROPERTIES = new HashMap<String, Object>();
    public static final HashMap<String,Object> SESSION_PROPERTIES = new HashMap<String, Object>();
    public static final String PAYLOAD = "anyPayload";

    @Test
    public void testGetters(){
        OutboundBehavior behavior = new OutboundBehavior(PAYLOAD, ASSERTIONS);

        behavior.setInboundProperties(INBOUND_PROPERTIES);
        behavior.setInvocationProperties(INVOCATION_PROPERTIES);
        behavior.setOutboundProperties(OUTBOUND_PROPERTIES);
        behavior.setSessionProperties(SESSION_PROPERTIES);

        assertEquals(INBOUND_PROPERTIES, behavior.getInboundProperties());
        assertEquals(INVOCATION_PROPERTIES, behavior.getInvocationProperties());
        assertEquals(OUTBOUND_PROPERTIES, behavior.getOutboundProperties());
        assertEquals(SESSION_PROPERTIES, behavior.getSessionProperties());
        assertEquals(ASSERTIONS, behavior.getAssertions());
        assertEquals(PAYLOAD, behavior.getPayload());
    }
}
