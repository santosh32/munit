
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>Assert Null message processor</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertNullMessageProcessor extends MunitMessageProcessor
{

    private String message;

    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
       module.assertNull(message, mulemessage.getPayload());
    }

    @Override
    protected String getProcessor() {
        return "assertNull";
    }

    public void setMessage(String value) {
        this.message = value;
    }

}
