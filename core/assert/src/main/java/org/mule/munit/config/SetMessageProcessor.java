
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>Sets the payload</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class SetMessageProcessor extends MunitMessageProcessor
{

    private Object payload;


    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        mulemessage.setPayload(evaluate(mulemessage, payload));
    }

    @Override
    protected String getProcessor() {
        return "set";
    }

    public void setPayload(Object value) {
        this.payload = value;
    }

}
