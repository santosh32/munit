package org.mule.munit.config;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.expression.ExpressionManager;
import org.mule.munit.AssertModule;
import org.mule.util.TemplateParser;

import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MunitMessageProcessorTest {

    public static final String EXP = "#[exp]";
    MuleEvent event;
    MuleMessage message;
    AssertModule module;
    ExpressionManager manager;
    
    @Before
    public void setUp(){
        event = mock(MuleEvent.class);
        message = mock(MuleMessage.class);
        module = mock(AssertModule.class);
        manager = mock(ExpressionManager.class);
        
        when(event.getMessage()).thenReturn(message);
        
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
