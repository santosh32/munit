package org.mule.munit.mel.assertions;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.el.ExpressionLanguageContext;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Federico, Fernando
 * @since 3.4
 */
public class MessageMatchingAssertionMelFunctionTest {

    private ElementMatcherFactory command =mock(ElementMatcherFactory.class);
    private ExpressionLanguageContext context = mock(ExpressionLanguageContext.class);
    private MuleMessage message = mock(MuleMessage.class);
    private ElementMatcher matcher = mock(ElementMatcher.class);

    @Test(expected = IllegalArgumentException.class)
    public void whenNullThrowException(){
        new MessageMatchingAssertionMelFunction(command).call(null, context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenEmptyThrowException(){
        new MessageMatchingAssertionMelFunction(command).call(new Object[]{}, context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenNotStringThrowException(){
        new MessageMatchingAssertionMelFunction(command).call(new Object[]{new Object()}, context);
    }

    @Test(expected = RuntimeException.class)
    public void whenNoMessageThenThrowException(){
        new MockFunction(command, null).call(new Object[]{"anyString"}, context);
    }

    @Test
    public void whenNMessageThenThrowException(){
        when(command.build("anyString",message)).thenReturn(matcher);
        assertEquals(matcher, new MockFunction(command, message).call(new Object[]{"anyString"}, context));
    }


    private class  MockFunction extends MessageMatchingAssertionMelFunction {
        MuleMessage message;
        public MockFunction(ElementMatcherFactory command, MuleMessage message) {
            super(command);
            this.message = message;
        }

        @Override
        protected MuleMessage getMuleMessageFrom(ExpressionLanguageContext context) {
            return message;
        }
    }

}
