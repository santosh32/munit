
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>Assert not Same message processor</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertNotSameMessageProcessor extends MunitMessageProcessor
{

    private String message;
    private Object expected;
    private Object value;

    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.assertNotSame(message, evaluate(mulemessage, expected), evaluate(mulemessage, value));
    }

    @Override
    protected String getProcessor() {
        return "assertNotSame";
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
