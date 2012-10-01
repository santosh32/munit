
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>Assert Fail Message processor</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class FailMessageProcessor extends MunitMessageProcessor
{

    private String message;

    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.fail(message);
    }

    @Override
    protected String getProcessor() {
        return "Fail";
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
