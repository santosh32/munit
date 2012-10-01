package org.mule.munit.runner.mule.result.notification;

import org.mule.munit.runner.mule.MunitTest;
import org.mule.munit.runner.mule.result.TestResult;

/**
 * <p>Clases implementing this interface handle the notification of a test result. </p>
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public interface NotificationListener {

	void notifyStartOf(MunitTest test);
	void notify(TestResult testResult);
}
