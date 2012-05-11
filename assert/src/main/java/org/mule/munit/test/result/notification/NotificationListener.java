package org.mule.munit.test.result.notification;

import org.mule.munit.test.Test;
import org.mule.munit.test.result.TestResult;

public interface NotificationListener {

	void notifyStartOf(Test test);
	void notify(TestResult testResult);
}
