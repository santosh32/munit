package org.mule.munit.mel.assertions;

import org.mule.api.MuleMessage;
import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.el.context.MessageContext;

import java.lang.reflect.Field;


/**
 * <p>
 *    Abstract class that represents an MEL function for asserting
 * </p>
 *
 * <p>
 *     This class just contains a method to get the mule message when the function is called.
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.4
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
