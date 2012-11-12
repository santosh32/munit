package org.mule.munit.common.mocking;


import org.junit.Test;
import org.mule.transport.NullPayload;

import static org.junit.Assert.assertTrue;

public class NotDefinedPayloadTest {

    @Test
    public void testItCreatesANonDefinedPayload(){
        assertTrue(NotDefinedPayload.getInstance() instanceof NotDefinedPayload);
    }


    @Test
    public void testIfNullPayloadThenNonDefined(){
        assertTrue(NotDefinedPayload.isNotDefined(NullPayload.getInstance()));
    }

    @Test
    public void testIfNonDefinedPayloadThenNonDefined(){
        assertTrue(NotDefinedPayload.isNotDefined(NotDefinedPayload.getInstance()));
    }
}
