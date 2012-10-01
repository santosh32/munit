
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>Assert Not null message processor</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertNotNullMessageProcessor extends MunitMessageProcessor
{

    private String message;

    public void setMessage(String value) {
        this.message = value;
    }

    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.assertNotNull(message, mulemessage.getPayload());
    }

    @Override
    protected String getProcessor() {
        return "assertNotNull";
    }

}
