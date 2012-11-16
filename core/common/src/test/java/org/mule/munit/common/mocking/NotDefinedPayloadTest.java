package org.mule.munit.common.mocking;


import org.junit.Ignore;
import org.junit.Test;
import org.mule.transport.NullPayload;


import static org.junit.Assert.assertTrue;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
@Ignore
public class NotDefinedPayloadTest {

    @Test
    public void testItCreatesANonDefinedPayload(){
        assertTrue(NotDefinedPayload.getInstance() != null);
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
