package org.mule.munit;

import org.mule.api.MuleEvent;

/**
 * <p>Interface that all the custom assertions must implement</p>
 *
 *@author Federico, Fernando
 * @version since 3.3.2
 */
public interface MunitAssertion {

    /**
     * <p>Method that asserts a mule event</p>
     *
     * @param muleEvent
     *      <p>Mule Event to be asserted</p></p>
     * @return
     *      <p>The original Mule Event</p>
     * @throws AssertionError
     *      <p>Case the assertion fails</p>
     */
    MuleEvent execute(MuleEvent muleEvent) throws AssertionError;
}
