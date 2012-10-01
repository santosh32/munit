package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;


/**
 * <p>Assert that message processor</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertThatMessageProcessor extends MunitMessageProcessor{

    private String message;
    private Object payloadIs;

    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        module.assertThat(message, evaluate(mulemessage,payloadIs), mulemessage.getPayload());
    }

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
