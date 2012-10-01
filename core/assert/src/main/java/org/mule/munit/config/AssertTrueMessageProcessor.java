
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>Assert true message processor</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertTrueMessageProcessor extends MunitMessageProcessor

{
    private String message;
    private Object condition;

    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.assertTrue(message, (Boolean) evaluate(mulemessage, condition));
    }

    @Override
    protected String getProcessor() {
        return "assertTrue";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCondition(Object value) {
        this.condition = value;
    }

}
