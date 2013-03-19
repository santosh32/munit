package org.mule.munit.common.mel.assertions;

import org.mule.api.MuleMessage;
import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.el.context.MessageContext;

import java.lang.reflect.Field;


/**
 * <p>
 *
 * </p>
 */
public abstract class AssertionMelFunction implements ExpressionLanguageFunction {

    protected MuleMessage getMuleMessageFrom(ExpressionLanguageContext context) {
        // TODO: Change this once mule inserts the mule message in the context.
        try{
            MessageContext messageContext = context.getVariable("message");

            Field reqField = MessageContext.class.getDeclaredField("message");
            reqField.setAccessible(true);
            return (MuleMessage) reqField.get(messageContext);
        }
        catch (Exception e){
            return null;
        }
    }
}
