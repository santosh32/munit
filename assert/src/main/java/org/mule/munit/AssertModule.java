package org.mule.munit;


import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.Payload;
import org.mule.transport.NullPayload;

import static junit.framework.Assert.assertEquals;

/**
 * <p>Module for asserting in Munit tests.</p>
 *
 * @author Federico, Fernando
 */
@Module(name="munit", schemaVersion="1.0")
public class AssertModule
{
    private static String wrapMessage(String message)
    {
        return message == null ? "" : message;
    }


    /**
     * <p>Assert that the payload is equal to an expected value.</p>
     *
     * <p>The payloadIs-ref can be any Object/expression. </p>
     * <p>The assertion Fails if the payload is not equal to the payloadIs-ref</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertThat}
     *
     * @param payloadIs Expected Value
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertThat(@Optional String message, Object payloadIs, @Payload Object payload)
    {
        assertEquals(wrapMessage(message), payloadIs, payload);
    }

    /**
     * <p>Assert for a true expression.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertTrue}
     *
     * @param condition Boolean expression
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertTrue(@Optional String message, Boolean condition)
    {
        junit.framework.Assert.assertTrue(wrapMessage(message), condition);
    }


    /**
     * <p>Check that two objects are equal.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertOnEquals}
     *
     * @param expected Expected value
     * @param value Real value
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertOnEquals(@Optional String message, Object expected, Object value)
    {
        assertEquals(wrapMessage(message), expected, value);
    }


    /**
     * Assert two objects are not equal
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertNotSame}
     *
     * @param expected expected value
     * @param value real value
     * @param message description message
     */
    @Processor
    public void assertNotSame(@Optional String message, Object expected, Object value)
    {
        junit.framework.Assert.assertNotSame(wrapMessage(message), expected, value);
    }

    /**
     * <p>Check if an expression is false.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertFalse}
     *
     * @param condition Boolean expression
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertFalse(@Optional String message, Boolean condition)
    {
        junit.framework.Assert.assertFalse(wrapMessage(message), condition);
    }

    /**
     * <p>Assert for a Not Null payload. </p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertNotNull}
     *
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertNotNull(@Optional String message, @Payload Object payload)
    {
        junit.framework.Assert.assertFalse(wrapMessage(message), payload instanceof NullPayload);
    }

    /**
     * <p>Assert Null Payload. </p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertNull}
     *
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertNull(@Optional String message, @Payload Object payload)
    {
        junit.framework.Assert.assertTrue(wrapMessage(message), payload instanceof NullPayload);
    }

    /**
     * <p>Defines the payload for testing.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:set}
     *
     * @param payload payload
     * @return The testing payload
     */
    @Processor
    public Object set(Object payload)
    {
        return payload;
    }

    /**
     * <p>Defines a Null payload for testing.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:setNullPayload}
     * @return Null payload
     */
    @Processor
    public NullPayload setNullPayload()
    {
        return NullPayload.getInstance();
    }


    /**
     * <p>Fail assertion.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:fail}
     *
     * @param message  Description message to be shown in case of failure.
     */
    @Processor
    public void fail(@Optional String message)
    {
        junit.framework.Assert.fail(wrapMessage(message));
    }

}
