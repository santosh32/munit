package org.mule.munit.common.mp;

import net.sf.cglib.proxy.MethodProxy;
import org.junit.Before;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.processor.MessageProcessor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitMessageProcessorInterceptorTest {

    public static final String NAMESPACE = "namespace";
    public static final String MP = "mp";
    public static final MessageProcessorId MESSAGE_PROCESSOR_ID = new MessageProcessorId(MP, NAMESPACE);
    public static final MessageProcessorCall MESSAGE_PROCESSOR_CALL = new MessageProcessorCall(MESSAGE_PROCESSOR_ID);
    public static final Exception EXCEPTION_TO_THROW = new Exception();
    public static final MockedMessageProcessorBehavior EXCEPTION_BEHAVIOR = new MockedMessageProcessorBehavior(MESSAGE_PROCESSOR_CALL, EXCEPTION_TO_THROW);
    public static final String PAYLOAD = "payload";
    public static final Object OBJECT = new Object();
    public static final String ATTR_VALUE = "hello";


    MockedMessageProcessorManager manager;
    MethodProxy proxy;
    private MuleEvent event;
    private MuleContext muleContext;
    private SpyMessageProcessor beforeAssertionMp;
    private SpyMessageProcessor afterAssertionMp;
    private ExpressionManager expressionManager;

    @Before
    public void setUp() throws Exception {
        manager = mock(MockedMessageProcessorManager.class);
        proxy = mock(MethodProxy.class);
        event = mock(MuleEvent.class);
        muleContext = mock(MuleContext.class);
        expressionManager = mock(ExpressionManager.class);
        beforeAssertionMp = new SpyMessageProcessor();
        afterAssertionMp = new SpyMessageProcessor();
    }

    private MockMunitMessageProcessorInterceptor interceptor() {
        MockMunitMessageProcessorInterceptor interceptor = new MockMunitMessageProcessorInterceptor(manager);
        interceptor.setId(MESSAGE_PROCESSOR_ID);
        return interceptor;
    }

    /**
     * <p>
     * Scenario:
     * No Spy before assertion.
     * No Spy after assertion.
     * Throw exception.
     * No attributes.
     * </p>
     */
    @Test
    public void interceptWithExceptionToThrow() throws Throwable {
        MunitMessageProcessorInterceptor interceptor = interceptor();
        interceptor.setAttributes(new HashMap<String, String>());

        when(manager.getSpyAssertions()).thenReturn(new HashMap<MessageProcessorId, SpyAssertion>());
        when(manager.getBetterMatchingBehavior(any(MessageProcessorCall.class))).thenReturn(EXCEPTION_BEHAVIOR);

        try{
            interceptor.process(new Object(), new Object[]{event}, proxy);
        }
        catch (Exception e){
            assertEquals(EXCEPTION_TO_THROW, e);
            verify(manager).addCall(any(MessageProcessorCall.class));
            return;
        }

        fail();
    }


    /**
     * <p>
     * Scenario:
     * No Spy before assertion.
     * No Spy after assertion.
     * Return Event.
     * No attributes.
     * </p>
     */
    @Test
    public void interceptWithValueToReturn() throws Throwable {
        MunitMessageProcessorInterceptor interceptor = interceptor();
        interceptor.setAttributes(new HashMap<String, String>());

        MuleMessage expectedMessage = muleMessage();

        when(manager.getSpyAssertions()).thenReturn(new HashMap<MessageProcessorId, SpyAssertion>());
        when(manager.getBetterMatchingBehavior(any(MessageProcessorCall.class))).thenReturn(returnValueBehavior());
        when(event.getMessage()).thenReturn(expectedMessage);

        MuleEvent processed = (MuleEvent) interceptor.process(new Object(), new Object[]{event}, proxy);

        verify(manager).addCall(any(MessageProcessorCall.class));
        assertEquals(expectedMessage, processed.getMessage());
    }


    /**
     * <p>
     * Scenario:
     * With Spy before assertion.
     * With Spy after assertion.
     * Return Event.
     * No attributes.
     * </p>
     */
    @Test
    public void interceptWithSpyBeforeAssertion() throws Throwable {
        MunitMessageProcessorInterceptor interceptor = interceptor();
        interceptor.setAttributes(new HashMap<String, String>());

        MuleMessage expectedMessage = muleMessage();

        when(manager.getSpyAssertions()).thenReturn(spyAssertions(createAssertions(beforeAssertionMp), createAssertions(afterAssertionMp)));
        when(manager.getBetterMatchingBehavior(any(MessageProcessorCall.class))).thenReturn(returnValueBehavior());
        when(event.getMessage()).thenReturn(expectedMessage);

        MuleEvent processed = (MuleEvent) interceptor.process(new Object(), new Object[]{event}, proxy);

        verify(manager).addCall(any(MessageProcessorCall.class));
        assertEquals(expectedMessage, processed.getMessage());
        assertTrue(beforeAssertionMp.called);
        assertTrue(afterAssertionMp.called);
    }

    /**
     * <p>
     * Scenario:
     * With Spy before assertion.
     * With Spy after assertion.
     * Return Event.
     * with attributes.
     * </p>
     */
    @Test
    public void interceptWithSpyBeforeAssertionWithAttributes() throws Throwable {
        MockMunitMessageProcessorInterceptor interceptor = interceptor();
        interceptor.setAttributes(getAttributes());
        interceptor.setContext(muleContext);

        MuleMessage expectedMessage = muleMessage();

        when(muleContext.getExpressionManager()).thenReturn(expressionManager);
        when(expressionManager.isExpression(ATTR_VALUE)).thenReturn(true);
        when(expressionManager.parse(ATTR_VALUE, event)).thenReturn("any");
        when(manager.getSpyAssertions()).thenReturn(spyAssertions(createAssertions(beforeAssertionMp), createAssertions(afterAssertionMp)));
        when(manager.getBetterMatchingBehavior(any(MessageProcessorCall.class))).thenReturn(returnValueBehavior());
        when(event.getMessage()).thenReturn(expectedMessage);

        MuleEvent processed = (MuleEvent) interceptor.process(new Object(), new Object[]{event}, proxy);

        verify(manager).addCall(any(MessageProcessorCall.class));
        assertEquals(expectedMessage, processed.getMessage());
        assertTrue(beforeAssertionMp.called);
        assertTrue(afterAssertionMp.called);
    }

    /**
     * <p>
     * Scenario:
     * Not Mocked message processor
     * </p>
     */
    @Test
    public void interceptWithNoMockedMessageProcessor() throws Throwable {
        MunitMessageProcessorInterceptor interceptor = interceptor();
        interceptor.setAttributes(new HashMap<String, String>());

        when(manager.getSpyAssertions()).thenReturn(new HashMap<MessageProcessorId, SpyAssertion>());
        when(manager.getBetterMatchingBehavior(any(MessageProcessorCall.class))).thenReturn(null);

        Object[] args = {event};
        when(proxy.invokeSuper(OBJECT, args)).thenReturn(event);

        Object processed = interceptor.process(OBJECT, args, proxy);

        verify(manager).addCall(any(MessageProcessorCall.class));
        assertEquals(event, processed);
    }

    @Test
    public void ifNoMessageProcessorClassThenCallProxyOnIntercept() throws Throwable {

        Method method = MunitMessageProcessorInterceptorTest.class.getMethod("ifNoMessageProcessorClassThenCallProxyOnIntercept");
        interceptor().intercept(OBJECT, method, null, proxy);

        verify(proxy).invokeSuper(OBJECT, null);
    }


    @Test
    public void interceptProcessCallsOnly() throws Throwable {

        Method method = MessageProcessor.class.getMethod("process", MuleEvent.class);
        MockMunitMessageProcessorInterceptor interceptor = interceptor();
        interceptor.setMockProcess(true);
        Object intercept = interceptor.intercept(OBJECT, method, null, proxy);

        verify(proxy, never()).invokeSuper(OBJECT, null);
        assertEquals(OBJECT, intercept);
    }

    private HashMap<MessageProcessorId, SpyAssertion> spyAssertions(List<MessageProcessor> before,List<MessageProcessor> after ) {
        HashMap<MessageProcessorId, SpyAssertion> map = new HashMap<MessageProcessorId, SpyAssertion>();
        map.put(MESSAGE_PROCESSOR_ID, new SpyAssertion(before, after));
        return map;
    }

    private ArrayList<MessageProcessor> createAssertions(MessageProcessor messageProcessor) {
        ArrayList<MessageProcessor> messageProcessors = new ArrayList<MessageProcessor>();
        messageProcessors.add(messageProcessor);
        return messageProcessors;
    }

    private MockedMessageProcessorBehavior returnValueBehavior() {
        return new MockedMessageProcessorBehavior(MESSAGE_PROCESSOR_CALL, muleMessage());
    }

    private MuleMessage muleMessage() {
        return new DefaultMuleMessage(PAYLOAD, muleContext);
    }

    private HashMap<String, String> getAttributes() {
        HashMap<String, String> attrs = new HashMap<String, String>();
        attrs.put("attr", ATTR_VALUE);
        return attrs;
    }


    private class SpyMessageProcessor implements MessageProcessor{

        boolean called;

        @Override
        public MuleEvent process(MuleEvent event) throws MuleException {
            called = true;
            return event;
        }
    }


}
