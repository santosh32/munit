
package org.mule.munit.config;

import org.mule.api.MuleMessage;
import org.mule.munit.AssertModule;
import org.mule.transport.NullPayload;


/**
 * <p>
 *     Sets the payload as null
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class SetNullPayloadMessageProcessor extends MunitMessageProcessor
{

    @Override
    protected void doProcess(MuleMessage mulemessage, AssertModule module) {
        mulemessage.setPayload(NullPayload.getInstance());
    }

    @Override
    protected String getProcessor() {
        return "SetNull";
    }
}
