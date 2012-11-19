package org.mule.munit.common.mocking;


import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class NotDefinedPayloadTest {

    @Test
    public void testItCreatesANonDefinedPayload(){
        assertTrue(NotDefinedPayload.getInstance() != null);
    }


    @Test
    public void testIfNonDefinedPayloadThenNonDefined(){
        assertTrue(NotDefinedPayload.isNotDefined(NotDefinedPayload.getInstance()));
    }
}
