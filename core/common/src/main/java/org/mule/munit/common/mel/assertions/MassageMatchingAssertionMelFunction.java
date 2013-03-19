package org.mule.munit.common.mel.assertions;


import org.mule.api.MuleMessage;
import org.mule.api.el.ExpressionLanguageContext;

public class MassageMatchingAssertionMelFunction extends AssertionMelFunction{
    private ElementMatcherBuilder command;

    public MassageMatchingAssertionMelFunction(ElementMatcherBuilder command) {
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
