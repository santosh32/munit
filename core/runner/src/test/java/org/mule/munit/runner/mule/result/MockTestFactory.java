package org.mule.munit.runner.mule.result;

import org.mule.munit.runner.mule.result.notification.Notification;


public class MockTestFactory {
    public static TestResult failingTest(String name) {
        TestResult testResult = new TestResult(name);
        testResult.setFailure(new Notification("fail", "Test fail"));
        return testResult;
    }

    public static TestResult succeedTest(String name) {
        TestResult testResult = new TestResult(name);
        return testResult;
    }

    public static TestResult errorTest(String name) {
        TestResult testResult = new TestResult(name);
        testResult.setError(new Notification("error", "Test error"));
        return testResult;
    }
}
