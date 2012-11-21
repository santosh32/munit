package org.mule.transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * Created with IntelliJ IDEA.
 * User: javier
 * Date: 11/21/12
 * Time: 4:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class someCustomTransformer extends AbstractMessageTransformer {

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
        String givenName = message.getInvocationProperty("givenName");
        String locationID = message.getInvocationProperty("locationID");

        if (locationID == null) {
            message.setInvocationProperty("res",givenName);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(givenName);
            sb.append("TESTING");
            sb.append(locationID);
            message.setInvocationProperty("res",sb.toString());
        }

        return message;
    }
}
