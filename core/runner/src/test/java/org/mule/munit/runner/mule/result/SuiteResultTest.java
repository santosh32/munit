package org.mule.munit.runner.mule.result;


import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mule.munit.runner.mule.result.MockTestFactory.*;

public class SuiteResultTest {

    /**
     * If none of the test succeed then the suite failed.
     */
    @Test
    public void ifNoTestSucceedThenFail(){
        SuiteResult testSuite = new SuiteResult("testSuite");
        testSuite.add(failingTest("test1"));
        testSuite.add(failingTest("test2"));

        assertFalse(testSuite.hasSucceeded());
    }

    /**
     * If one test failed then the suite failed.
     */
    @Test
    public void ifOneTestFailedThenFail(){
        SuiteResult testSuite = new SuiteResult("testSuite");
        testSuite.add(failingTest("test1"));
        testSuite.add(succeedTest("test2"));

        assertFalse(testSuite.hasSucceeded());
    }

    /**
     * If All tests succeeded then Succeed
     */
    @Test
    public void ifAllSucceededThenSucceed(){
        SuiteResult testSuite = new SuiteResult("testSuite");
        testSuite.add(succeedTest("test1"));
        testSuite.add(succeedTest("test2"));

        assertTrue(testSuite.hasSucceeded());
    }

    /**
     * If no tests the succeed.
     */
    @Test
    public void ifNoTestThenSucceed(){
        assertTrue(new SuiteResult("testSuite").hasSucceeded());
    }

    /**
     * It must return the number of tests that have failures.
     */
    @Test
    public void retrieveTheNumberOfFailingTests(){
        SuiteResult testSuite = new SuiteResult("testSuite");
        testSuite.add(failingTest("test0"));
        testSuite.add(failingTest("test1"));
        testSuite.add(succeedTest("test2"));
        testSuite.add(errorTest("test3"));

        assertFalse(testSuite.hasSucceeded());
        assertEquals(2, testSuite.getNumberOfFailures());
    }


    /**
     * It must return 0 if no failing tests
     */
    @Test
    public void returnZeroIfNoFailingTestFound(){
        SuiteResult testSuite = new SuiteResult("testSuite");
        testSuite.add(succeedTest("test2"));
        testSuite.add(errorTest("test3"));

        assertFalse(testSuite.hasSucceeded());
        assertEquals(0, testSuite.getNumberOfFailures());
    }


    /**
     * It must return the number of error that have failures.
     */
    @Test
    public void retrieveTheNumberOfErrorTests(){
        SuiteResult testSuite = new SuiteResult("testSuite");
        testSuite.add(failingTest("test1"));
        testSuite.add(succeedTest("test2"));
        testSuite.add(errorTest("test3"));
        testSuite.add(errorTest("test4"));

        assertFalse(testSuite.hasSucceeded());
        assertEquals(2, testSuite.getNumberOfErrors());
    }

    /**
     * If no error then return 0.
     */
    @Test
    public void returnZeroIfNoErrorFound(){
        SuiteResult testSuite = new SuiteResult("testSuite");
        testSuite.add(failingTest("test1"));
        testSuite.add(succeedTest("test2"));

        assertFalse(testSuite.hasSucceeded());
        assertEquals(0, testSuite.getNumberOfErrors());
    }


    /**
     * Return all the tests that have error
     */
    @Test
    public void returnTheCorrectNumberOfErrorTests(){
        SuiteResult testSuite = new SuiteResult("testSuite");
        testSuite.add(failingTest("test1"));
        testSuite.add(succeedTest("test2"));
        testSuite.add(errorTest("test3"));
        testSuite.add(errorTest("test4"));

        List<MunitResult> errorTests = testSuite.getErrorTests();
        assertEquals(2, errorTests.size());
        assertEquals("test3", errorTests.get(0).getTestName());
        assertEquals("test4", errorTests.get(1).getTestName());
    }

    /**
     * Return all the tests that have failures
     */
    @Test
    public void returnTheCorrectNumberOfFailingTests(){
        SuiteResult testSuite = new SuiteResult("testSuite");
        testSuite.add(failingTest("test1"));
        testSuite.add(succeedTest("test2"));
        testSuite.add(errorTest("test3"));
        testSuite.add(errorTest("test4"));

        List<MunitResult> failingTests = testSuite.getFailingTests();
        assertEquals(1, failingTests.size());
        assertEquals("test1", failingTests.get(0).getTestName());
    }



}
