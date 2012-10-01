package org.mule.munit.runner;


import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.munit.config.MunitAfterTest;
import org.mule.munit.config.MunitBeforeTest;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SuiteBuilderTest {
    
    
    private MuleContext muleContext;
    private MuleRegistry registry;
    private List<MunitAfterTest> afterTestFlows = new ArrayList<MunitAfterTest>();
    private List<MunitBeforeTest> beforeTestFlows = new ArrayList<MunitBeforeTest>();
    private MunitTest munitTest;
    
    @Before
    public void setUp(){
        muleContext = mock(MuleContext.class);
        registry = mock(MuleRegistry.class);
        munitTest = mock(MunitTest.class);
        
        when(muleContext.getRegistry()).thenReturn(registry);
    }

    /**
     * Checks that all the non Ignored Tests are in the suite
     */
    @Test
    public void beforeAndAfterFlowsMustBeRetrieved() throws RegistrationException {
        MockSuiteBuilder builder = new MockSuiteBuilder(muleContext, true);
        runTest(builder);
    }


    /**
     * Checks that ignored tests are not in the suite
     */
    @Test
    public void ignoredFlowMustNotBeInTheSuite() throws RegistrationException {
        MockSuiteBuilder builder = new MockSuiteBuilder(muleContext, false);

        when(munitTest.isIgnore()).thenReturn(true);
        runTest(builder);

    }

    private void runTest(MockSuiteBuilder builder) {
        when(registry.lookupObjects(MunitBeforeTest.class)).thenReturn(beforeTestFlows);
        when(registry.lookupObjects(MunitAfterTest.class)).thenReturn(afterTestFlows);
        when(registry.lookupObjects(MunitTest.class)).thenReturn(Arrays.asList(new MunitTest[]{munitTest}));

        builder.build("test");

        verify(registry, times(1)).lookupObjects(MunitBeforeTest.class);
        verify(registry, times(1)).lookupObjects(MunitAfterTest.class);
        verify(registry, times(1)).lookupObjects(MunitTest.class);
    }

    private class MockSuiteBuilder extends SuiteBuilder<MockSuite, MockTest>{
        private boolean mustNotHaveTests;

        protected MockSuiteBuilder(MuleContext muleContext, boolean mustHaveTests) {
            super(muleContext);
            this.mustNotHaveTests = mustHaveTests;
        }

        @Override
        protected MockSuite createSuite(String name) {
            if ( mustNotHaveTests ){
                assertFalse(this.tests.isEmpty());
                return new MockSuite();
            }
            assertTrue(this.tests.isEmpty());
            return new MockSuite();
        }

        @Override
        protected MockTest test(List<MunitFlow> beforeTest, MunitFlow test, List<MunitFlow> afterTest) {
            assertEquals(afterTestFlows, afterTest);
            assertEquals(beforeTestFlows,beforeTest);
            assertEquals(munitTest, test);
            return new MockTest();
        }

    }

    private class MockSuite {
    }

    private class MockTest {
    }
}