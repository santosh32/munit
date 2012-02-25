package org.mule.munit;


import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Payload;
import org.mule.transport.NullPayload;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Module for asserting in Munit tests.
 *
 * @author Federico, Fernando
 */
@Module(name="assert", schemaVersion="1.0")
public class AssertModule
{
    /**
     * Assert that the payload is equal to an expected value.
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertThat}
     *
     * @param payloadIs Expected Value
     * @param payload payload
     */
    @Processor
    public void assertThat(Object payloadIs, @Payload Object payload)
    {
        assertEquals(payloadIs, payload);
    }

    /**
     * Assert for a true expression.
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertTrue}
     *
     * @param condition expected value
     */
    @Processor
    public void assertTrue(Boolean condition)
    {
        junit.framework.Assert.assertTrue("Expected True but was False", condition);
    }


    /**
     * Check that two objects are equal.
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertOnEquals}
     *
     * @param expected expected value
     * @param value real value
     */
    @Processor
    public void assertOnEquals(Object expected, Object value)
    {
        assertEquals(expected, value);
    }

    /**
     * Check if an expression is false
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertFalse}
     *
     * @param condition expected value
     */

    @Processor
    public void assertFalse(Boolean condition)
    {
        junit.framework.Assert.assertFalse("Expected False but was True", condition);
    }

    /**
     * Assert Not Null
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertNotNull}
     *
     * @param payload payload
     */
    @Processor
    public void assertNotNull(@Payload Object payload)
    {
        junit.framework.Assert.assertFalse(payload instanceof NullPayload);
    }

    /**
     * Assert Null
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertNull}
     *
     * @param payload payload
     */
    @Processor
    public void assertNull(@Payload Object payload)
    {
        junit.framework.Assert.assertTrue(payload instanceof NullPayload);
    }
}
