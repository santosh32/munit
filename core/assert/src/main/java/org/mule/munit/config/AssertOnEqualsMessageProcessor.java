
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>
 *     Assert on Equals message processor
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class AssertOnEqualsMessageProcessor extends MunitMessageProcessor
{
    /**
     * @see AssertModule#assertOnEquals(String, Object, Object)
     */
    private String message;

    /**
     * @see AssertModule#assertOnEquals(String, Object, Object)
     */
    private Object expected;

    /**
     * @see AssertModule#assertOnEquals(String, Object, Object)
     */
    private Object value;

    /**
     * @see MunitMessageProcessor#doProcess(org.mule.api.MuleMessage, org.mule.munit.AssertModule)
     */
    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.assertOnEquals(message, evaluate(mulemessage, expected), evaluate(mulemessage, value));
    }

    /**
     * @see org.mule.munit.config.MunitMessageProcessor#getProcessor()
     */
    @Override
    protected String getProcessor() {
        return "assertOnEquals";
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public void setExpected(Object value) {
        this.expected = value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
