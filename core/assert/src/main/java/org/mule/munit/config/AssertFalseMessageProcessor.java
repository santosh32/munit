
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>
 *     Assert False message processor
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class AssertFalseMessageProcessor extends MunitMessageProcessor
{
    /**
     * @see AssertModule#assertFalse(String, Boolean)
     */
    private String message;

    /**
     * @see AssertModule#assertFalse(String, Boolean)
     */
    private Object condition;

    /**
     * @see MunitMessageProcessor#doProcess(org.mule.api.MuleMessage, org.mule.munit.AssertModule)
     */
    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.assertFalse(message, (Boolean) evaluate(mulemessage,condition));
    }

    /**
     * @see org.mule.munit.config.MunitMessageProcessor#getProcessor()
     */
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
