
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>
 *     Assert Not null message processor
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class AssertNotNullMessageProcessor extends MunitMessageProcessor
{
    /**
     * @see AssertModule#assertNotNull(String, Object)
     */
    private String message;

    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * @see MunitMessageProcessor#doProcess(org.mule.api.MuleMessage, org.mule.munit.AssertModule)
     */
    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.assertNotNull(message, mulemessage.getPayload());
    }

    /**
     * @see org.mule.munit.config.MunitMessageProcessor#getProcessor()
     */
    @Override
    protected String getProcessor() {
        return "assertNotNull";
    }

}
