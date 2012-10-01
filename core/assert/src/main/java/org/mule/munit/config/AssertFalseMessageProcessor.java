
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>Assert False message processor</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertFalseMessageProcessor extends MunitMessageProcessor
{

    private String message;
    private Object condition;

    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.assertFalse(message, (Boolean) evaluate(mulemessage,condition));
    }

    @Override
    protected String getProcessor() {
        return "assertFalse";
    }


    public void setMessage(String value) {
        this.message = value;
    }

    public void setCondition(Object value) {
        this.condition = value;
    }
}
