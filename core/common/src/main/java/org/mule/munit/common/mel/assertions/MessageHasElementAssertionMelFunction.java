package org.mule.munit.common.mel.assertions;

import org.mule.api.MuleMessage;
import org.mule.api.el.ExpressionLanguageContext;


public class MessageHasElementAssertionMelFunction extends AssertionMelFunction {
    private MessageHasElementAssertionCommand command;

    public MessageHasElementAssertionMelFunction(MessageHasElementAssertionCommand command) {
        this.command = command;
    }

    @Override
    public Object call(Object[] params, ExpressionLanguageContext context) {
        if ( params != null && params.length>0 && params[0] instanceof String ){
            MuleMessage muleMessage = getMuleMessageFrom(context);

            if ( muleMessage == null ){
                return false;
            }

            return command.messageHas((String) params[0], muleMessage);
        }
        return false;
    }
}
