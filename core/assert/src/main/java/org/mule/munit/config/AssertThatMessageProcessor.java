package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>
 *     Assert that message processor
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 * */
public class AssertThatMessageProcessor extends MunitMessageProcessor{

    /**
     * @see AssertModule#assertThat(String, Object, Object)
     */
    private String message;

    /**
     * @see AssertModule#assertThat(String, Object, Object)
     */
    private Object payloadIs;

    /**
     * @see MunitMessageProcessor#doProcess(org.mule.api.MuleMessage, org.mule.munit.AssertModule)
     */
    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.assertThat(message, evaluate(mulemessage,payloadIs), mulemessage.getPayload());
    }

    /**
     * @see org.mule.munit.config.MunitMessageProcessor#getProcessor()
     */
    @Override
    protected String getProcessor() {
        return "assertThat";
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setPayloadIs(Object value) {
        this.payloadIs = value;
    }
}
