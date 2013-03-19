package org.mule.munit;


import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextAware;
import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageExtension;
import org.mule.munit.common.endpoint.MunitSpringFactoryPostProcessor;
import org.mule.munit.mel.assertions.*;
import org.mule.munit.mel.matchers.*;
import org.mule.munit.mel.utils.FlowResultFunction;
import org.mule.munit.mel.utils.GetResourceFunction;
import org.mule.transport.NullPayload;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.mule.module.hamcrest.message.AttachmentMatcher.hasInboundAttachment;
import static org.mule.module.hamcrest.message.PropertyMatcher.*;
import static org.mule.module.hamcrest.message.PropertyMatcher.hasInvocationProperty;

/**
 * <p>Module to assert payload's results</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertModule extends MunitSpringFactoryPostProcessor implements ExpressionLanguageExtension
{


    private static String wrapMessage(String message)
    {
        return message == null ? "" : message;
    }

    /**
     * <p>Assert that the payload is equal to an expected value.</p>
     *
     * <p>The payloadIs-ref can be any Object/expression. </p>
     * <p>The assertion Fails if the payload is not equal to the payloadIs-ref</p>
     *
     * @param payloadIs Expected Value
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    public void assertThat(String message, Object payloadIs, Object payload)
    {
        assertEquals(wrapMessage(message), payloadIs, payload);
    }

    /**
     * <p>Assert for a true expression.</p>
     *
     * @param condition Boolean expression
     * @param message Description message to be shown in case of failure.
     */
    public void assertTrue(String message, Boolean condition)
    {
        junit.framework.Assert.assertTrue(wrapMessage(message), condition);
    }


    /**
     * <p>Check that two objects are equal.</p>
     *
     * @param expected Expected value.  If not provided the expected value is taken from the expected value Queue.
     * @param value Real value
     * @param message Description message to be shown in case of failure.
     */
    public void assertOnEquals(String message, Object expected, Object value)
    {
        assertEquals(wrapMessage(message), expected, value);
    }


    /**
     * Assert two objects are not equal
     *
     * @param expected expected value. If not provided the expected value is taken from the expected value Queue.
     * @param value real value
     * @param message description message
     */
    public void assertNotSame(String message, Object expected, Object value)
    {
        junit.framework.Assert.assertNotSame(wrapMessage(message), expected, value);
    }

    /**
     * <p>Check if an expression is false.</p>
     *
     * @param condition Boolean expression
     * @param message Description message to be shown in case of failure.
     */
    public void assertFalse(String message, Boolean condition)
    {
        junit.framework.Assert.assertFalse(wrapMessage(message), condition);
    }

    /**
     * <p>Assert for a Not Null payload. </p>
     *
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    public void assertNotNull(String message, Object payload)
    {
        junit.framework.Assert.assertFalse(wrapMessage(message), payload instanceof NullPayload);
    }

    /**
     * <p>Assert Null Payload. </p>
     *
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    public void assertNull(String message, Object payload)
    {
        junit.framework.Assert.assertTrue(wrapMessage(message), payload instanceof NullPayload);
    }


    /**
     * <p>Fail assertion.</p>
     *
     * @param message  Description message to be shown in case of failure.
     */
    public void fail(String message)
    {
        junit.framework.Assert.fail(wrapMessage(message));
    }

    @Override
    public void configureContext(ExpressionLanguageContext context) {
        context.declareFunction("messageHasPropertyInAnyScopeCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasPropertyInAnyScope(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasInboundPropertyCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasInboundProperty(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasOutboundPropertyCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasOutboundProperty(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasSessionPropertyCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasSessionProperty(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasInvocationPropertyCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasInvocationProperty(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasInboundAttachmentCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasInboundAttachment(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasOutboundAttachmentCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasInboundAttachment(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageInboundProperty",  new MessageMatchingAssertionMelFunction(new ElementMatcherFactory(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getInboundProperty(elementName));
            }
        }));

        context.declareFunction("messageOutboundProperty",  new MessageMatchingAssertionMelFunction(new ElementMatcherFactory(){
            @Override
            public ElementMatcher build(String elementName, MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getOutboundProperty(elementName));
            }
        }));

        context.declareFunction("messageInboundProperty",  new MessageMatchingAssertionMelFunction(new ElementMatcherFactory(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getInboundProperty(elementName));
            }
        }));

        context.declareFunction("messageInvocationProperty",  new MessageMatchingAssertionMelFunction(new ElementMatcherFactory(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getInvocationProperty(elementName));
            }
        }));

        context.declareFunction("messageInboundAttachment",  new MessageMatchingAssertionMelFunction(new ElementMatcherFactory(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getInboundAttachment(elementName));
            }
        }));

        context.declareFunction("messageOutboundAttachment",  new MessageMatchingAssertionMelFunction(new ElementMatcherFactory(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getOutboundAttachment(elementName));
            }
        }));

        context.declareFunction("eq", new EqMatcherFunction());
        context.declareFunction("anyBoolean", new AnyMatcherFunction(Boolean.class));
        context.declareFunction("anyByte", new AnyMatcherFunction(Byte.class));
        context.declareFunction("anyInt", new AnyMatcherFunction(Integer.class));
        context.declareFunction("anyDouble", new AnyMatcherFunction(Double.class));
        context.declareFunction("anyFloat", new AnyMatcherFunction(Float.class));
        context.declareFunction("anyShort", new AnyMatcherFunction(Short.class));
        context.declareFunction("anyObject", new AnyMatcherFunction(Object.class));
        context.declareFunction("anyString", new AnyMatcherFunction(String.class));
        context.declareFunction("anyList", new AnyMatcherFunction(List.class));
        context.declareFunction("anySet", new AnyMatcherFunction(Set.class));
        context.declareFunction("anyMap", new AnyMatcherFunction(Map.class));
        context.declareFunction("anyCollection", new AnyMatcherFunction(Collection.class));
        context.declareFunction("isNull", new NullMatcherFunction());
        context.declareFunction("isNotNull", new NotNullMatcherFunction());
        context.declareFunction("any", new AnyClassMatcherFunction());
        context.declareFunction("resultOfScript", new FlowResultFunction(muleContext));
        context.declareFunction("getResource", new GetResourceFunction());

    }



}
