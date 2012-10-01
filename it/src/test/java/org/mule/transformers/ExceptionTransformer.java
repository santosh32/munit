package org.mule.transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractMessageTransformer;

public class ExceptionTransformer extends AbstractMessageTransformer{
    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
        throw new TransformerException(MessageFactory.createStaticMessage("Error"));
    }
}
