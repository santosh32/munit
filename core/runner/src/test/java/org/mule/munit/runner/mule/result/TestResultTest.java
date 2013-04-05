package org.mule.munit.runner.mule.result;


import org.junit.Test;

import static org.junit.Assert.*;
import static org.mule.munit.runner.mule.result.MockTestFactory.*;

public class TestResultTest {

    /**
     * If the test has an error ten do not succeed
     */
    @Test
    public void ifHasErrorThenDoNotSucceed(){
        assertFalse(errorTest("test").hasSucceeded());
    }

    /**
     * If the test has a failure ten do not succeed
     */
    @Test
    public void ifHasFailureThenDoNotSucceed(){
       assertFalse(failingTest("test").hasSucceeded());
    }

    /**
     * If the test doesn't have a failure or error then succeed
     */
    @Test
    public void ifNoFailureOrErrorSucceed(){
        assertTrue(succeedTest("test").hasSucceeded());
    }


    /**
     * if has error then return 1 error
     */
    @Test
    public void ifErrorThenReturnOneError(){
        assertEquals(1, errorTest("test").getNumberOfErrors());
    }

    /**
     * if has failure then return 1 failure
     */
    @Test
    public void ifFailureThenReturnOneError(){
        assertEquals(1, failingTest("test").getNumberOfFailures());
    }

}
