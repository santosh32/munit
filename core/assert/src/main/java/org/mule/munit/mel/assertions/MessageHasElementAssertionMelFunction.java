package org.mule.munit.mel.assertions;

import org.mule.api.MuleMessage;
import org.mule.api.el.ExpressionLanguageContext;


/**
 * <p>
 *     MEL function to evaluate the existence of an Element in a Message.
 *
 *     All the #[messageHas*PropertyCalled('something') is implemented with this Object
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.4
 */
public class MessageHasElementAssertionMelFunction extends AssertionMelFunction {
    /**
     * <p>
     *     The command that does the assertion
     * </p>
     */
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
