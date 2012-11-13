package org.mule.munit.config;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.*;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.munit.AssertModule;
import org.mule.util.TemplateParser;

import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitMessageProcessorTest {

    public static final String EXP = "#[exp]";
    MuleEvent event;
    MuleMessage message;
    AssertModule module;
    ExpressionManager manager;
    MuleContext muleContext;
    MuleRegistry muleRegistry;

    @Before
    public void setUp(){
        event = mock(MuleEvent.class);
        message = mock(MuleMessage.class);
        module = mock(AssertModule.class);
        manager = mock(ExpressionManager.class);
        muleContext = mock(MuleContext.class);
        muleRegistry = mock(MuleRegistry.class);

        
        when(event.getMessage()).thenReturn(message);
        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        
    }


    @Test
    public void testInitializeWithNoModuleObject() throws InitialisationException, RegistrationException {
        MockMunitMessageProcessor mp = processorForInitialize(null);
        when(muleRegistry.lookupObject(AssertModule.class)).thenReturn(module);

        mp.initialise();

        assertEquals(manager, mp.expressionManager);
        assertNotNull(mp.patternInfo);
        assertEquals(module,mp.moduleObject);
    }

    @Test(expected = InitialisationException.class)
    public void testInitializeWithNoModuleObjectWhenObjectIsNotInRegistry() throws InitialisationException, RegistrationException {
        MockMunitMessageProcessor mp = processorForInitialize(null);
        when(muleRegistry.lookupObject(AssertModule.class)).thenReturn(null);

        mp.initialise();
    }

    @Test(expected = InitialisationException.class)
    public void testInitializeWithNoModuleObjectWhenObjectFails() throws InitialisationException, RegistrationException {
        MockMunitMessageProcessor mp = processorForInitialize(null);
        when(muleRegistry.lookupObject(AssertModule.class)).thenThrow(new RegistrationException(new Exception()));

        mp.initialise();
    }
    
    @Test
    public void testEvaluateWithNoString(){
        MockMunitMessageProcessor mp = new MockMunitMessageProcessor();
        Object o = new Object();
        Object result = mp.evaluate(message, o);

        assertEquals(o, result);
    }

    @Test
    public void getModuleWithString() throws InitialisationException, RegistrationException, MessagingException {
        MockMunitMessageProcessor mp = processorForInitialize("myModule");
        when(muleRegistry.lookupObject("myModule")).thenReturn(module);

        AssertModule moduleObtained = mp.getModule(event, "methodName");

        assertEquals(module,moduleObtained);
    }


    @Test(expected = MessagingException.class)
    public void getModuleWithStringFail() throws InitialisationException, RegistrationException, MessagingException {
        MockMunitMessageProcessor mp = processorForInitialize("myModule");
        when(muleRegistry.lookupObject("myModule")).thenReturn(null);

        mp.getModule(event, "methodName");
    }

    @Test
    public void processSuccessfully() throws MuleException {
        MockMunitMessageProcessor mp = new MockMunitMessageProcessor();
        mp.process(event);
    }

    @Test
    public void evaluateExpressionSuccessfully() throws MuleException {
        MockMunitMessageProcessor mp = new MockMunitMessageProcessor();
        mp.evaluate(message, EXP);

        verify(manager, times(1)).evaluate(EXP, message);
    }

    @Test
    public void evaluateNonExpressionSuccessfully() throws MuleException {
        MockMunitMessageProcessor mp = new MockMunitMessageProcessor();
        mp.evaluate(message, "any");

        verify(manager, times(1)).parse("any", message);
    }

    private MockMunitMessageProcessor processorForInitialize(Object module) throws RegistrationException {
        MockMunitMessageProcessor mp = new MockMunitMessageProcessor();
        mp.setMuleContext(muleContext);
        mp.moduleObject = module;
        when(muleContext.getExpressionManager()).thenReturn(manager);

        return mp;
    }


    private class MockMunitMessageProcessor extends MunitMessageProcessor{

        private MockMunitMessageProcessor() {
            this.setModuleObject(module);
            this.setRetryMax(1);
            this.retryCount = new AtomicInteger();
            this.patternInfo = TemplateParser.createMuleStyleParser().getStyle();
            this.expressionManager = manager;
        }

        @Override
        protected void doProcess(MuleMessage mulemessage, AssertModule module) {
            assertEquals(message, mulemessage);
            assertEquals(module, module);
        }

        @Override
        protected String getProcessor() {
            return "processor";
        }
    }
}
