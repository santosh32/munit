package org.mule.munit.mel.assertions;


import org.mule.api.MuleMessage;
import org.mule.api.el.ExpressionLanguageContext;


public class MessageMatchingAssertionMelFunction extends AssertionMelFunction{
    private ElementMatcherFactory command;

    public MessageMatchingAssertionMelFunction(ElementMatcherFactory command) {
        this.command = command;
    }

    @Override
    public Object call(Object[] params, ExpressionLanguageContext context) {
        if ( params != null && params.length>0 && params[0] instanceof String ){
            MuleMessage messageFrom = getMuleMessageFrom(context);

            if ( messageFrom == null ){
                throw new RuntimeException("Could not get message;");
            }

            return command.build((String) params[0], messageFrom);
        }

        throw new IllegalArgumentException("Invalid parameter");
    }
}
