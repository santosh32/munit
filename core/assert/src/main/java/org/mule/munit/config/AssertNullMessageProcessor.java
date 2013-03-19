
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>
 *     Assert Null message processor
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class AssertNullMessageProcessor extends MunitMessageProcessor
{
    /**
     * @see AssertModule#assertNull(String, Object)
     */
    private String message;

    /**
     * @see MunitMessageProcessor#doProcess(org.mule.api.MuleMessage, org.mule.munit.AssertModule)
     */
    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
       module.assertNull(message, mulemessage.getPayload());
    }

    /**
     * @see org.mule.munit.config.MunitMessageProcessor#getProcessor()
     */
    @Override
    protected String getProcessor() {
        return "assertNull";
    }

    public void setMessage(String value) {
        this.message = value;
    }

}
