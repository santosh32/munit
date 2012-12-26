package org.mule.munit.runner.mule;


import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.munit.runner.output.DefaultOutputHandler;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class MunitSuiteBuilderTest {

    MuleContext muleContext;

    @Before
    public void setUp(){
        muleContext = mock(MuleContext.class);

    }

    /**
     * Do not accept null handlers
     */
    @Test(expected = IllegalArgumentException.class)
    public void handlerMustNotBeNull(){
        new MunitSuiteBuilder(muleContext, null);
    }

    /**
     * Never return null on test
     */
    @Test
    public void createTestMustNotBeNull(){
        assertNotNull(new MunitSuiteBuilder(muleContext, new DefaultOutputHandler()).test(null, null, null));
    }


    /**
     * Never return null when creating a suite.
     */
    @Test
    public void createSuiteMustNotBeNull(){
        assertNotNull(new MunitSuiteBuilder(muleContext, new DefaultOutputHandler()).createSuite("test"));
    }

}
