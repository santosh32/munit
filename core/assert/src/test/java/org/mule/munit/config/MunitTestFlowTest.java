package org.mule.munit.config;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.registry.MuleRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitTestFlowTest {

    public static final String EXPECTED = "expected";
    MuleContext muleContext = mock(MuleContext.class);
    MuleRegistry registry = mock(MuleRegistry.class);
    private MuleEvent muleEvent = mock(MuleEvent.class);
    private ExpressionManager expressionManager = mock(ExpressionManager.class);


    @Before
    public void setUp(){
        when(muleContext.getRegistry()).thenReturn(registry);
        when(muleContext.getExpressionManager()).thenReturn(expressionManager);

    }

    @Test
    public void testSetters(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);

        testFlow.setExpectExceptionThatSatisfies(EXPECTED);
        testFlow.setIgnore(true);

        assertTrue(testFlow.isIgnore());
        assertEquals(EXPECTED, testFlow.getExpectExceptionThatSatisfies());
    }
    
    @Test
    public void testExceptionWhenMatchesExpression(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);
        testFlow.setExpectExceptionThatSatisfies(EXPECTED);

        when(expressionManager.isExpression(EXPECTED)).thenReturn(true);
        when(expressionManager.evaluate(EXPECTED, muleEvent)).thenReturn(true);

        assertTrue(testFlow.expectException(new Exception(), muleEvent));
    }

    @Test(expected =  junit.framework.AssertionFailedError.class)
    public void testExceptionWhenDoesntMatchExpression(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);
        testFlow.setExpectExceptionThatSatisfies(EXPECTED);

        when(expressionManager.isExpression(EXPECTED)).thenReturn(true);
        when(expressionManager.evaluate(EXPECTED, muleEvent)).thenReturn(false);

        testFlow.expectException(new Exception(), muleEvent);
    }

    @Test
    public void testExceptionWheIsNotExpressionButMatchesName(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);
        testFlow.setExpectExceptionThatSatisfies(Exception.class.getCanonicalName());

        when(expressionManager.isExpression(EXPECTED)).thenReturn(false);

        assertTrue(testFlow.expectException(new Exception(), muleEvent));
    }

    @Test(expected =  junit.framework.AssertionFailedError.class)
    public void testExceptionWheIsNotExpressionAndDoesntMatchName(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);
        testFlow.setExpectExceptionThatSatisfies("any");

        when(expressionManager.isExpression(EXPECTED)).thenReturn(false);

        testFlow.expectException(new Exception(), muleEvent);
    }

    @Test(expected =  junit.framework.AssertionFailedError.class)
    public void testMessagingWithNoCauseException(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);
        testFlow.setExpectExceptionThatSatisfies("any");

        when(expressionManager.isExpression(EXPECTED)).thenReturn(false);

        testFlow.expectException(new MessagingException(muleEvent, null), muleEvent);
    }

    @Test(expected =  junit.framework.AssertionFailedError.class)
    public void testMessagingWithCauseExceptionThatDoesntMatchTheName(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);
        testFlow.setExpectExceptionThatSatisfies("any");

        when(expressionManager.isExpression(EXPECTED)).thenReturn(false);

        testFlow.expectException(new MessagingException(muleEvent, new Exception()), muleEvent);
    }

    @Test
    public void testMessagingWithCauseExceptionThattMatchesTheName(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);
        testFlow.setExpectExceptionThatSatisfies(Exception.class.getCanonicalName());

        when(expressionManager.isExpression(EXPECTED)).thenReturn(false);

        testFlow.expectException(new MessagingException(muleEvent, new Exception()), muleEvent);
    }


}
