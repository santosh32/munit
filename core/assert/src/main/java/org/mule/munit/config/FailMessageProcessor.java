
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>
 *     Assert Fail Message processor
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class FailMessageProcessor extends MunitMessageProcessor
{
    /**
     * @see AssertModule#fail(String)
     */
    private String message;

    /**
     * @see MunitMessageProcessor#doProcess(org.mule.api.MuleMessage, org.mule.munit.AssertModule)
     */
    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.fail(message);
    }

    /**
     * @see org.mule.munit.config.MunitMessageProcessor#getProcessor()
     */
    @Override
    protected String getProcessor() {
        return "Fail";
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
