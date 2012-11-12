package org.mule.munit.config;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    @Before
    public void setUp(){
        when(muleContext.getRegistry()).thenReturn(registry);
    }

    @Test
    public void testSetters(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);

        testFlow.setExpected(EXPECTED);
        testFlow.setIgnore(true);

        assertTrue(testFlow.isIgnore());
        assertEquals(EXPECTED, testFlow.getExpected());
    }
    
    @Test
    public void testExceptionWhenNotDefined(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);
        
        assertFalse(testFlow.expectException(new Exception()));
    }

    @Test
    public void testExceptionWhenDefined(){
        MunitTestFlow testFlow = new MunitTestFlow("name", muleContext);
        testFlow.setExpected("java.lang.Exception");
        assertTrue(testFlow.expectException(new Exception()));
    }
}
