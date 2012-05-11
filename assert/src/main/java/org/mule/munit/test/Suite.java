package org.mule.munit.test;

import java.util.ArrayList;
import java.util.List;

import org.mule.munit.test.result.MunitResult;
import org.mule.munit.test.result.SuiteResult;
import org.mule.munit.test.result.TestResult;
import org.mule.munit.test.result.notification.DummyNotificationListener;
import org.mule.munit.test.result.notification.NotificationListener;

public class Suite {

	private String name;
	private List<Test> munitTests = new ArrayList<Test>();
	private NotificationListener notificationListener = new DummyNotificationListener();
	
	public Suite(String name) {
		this.name = name;
	}

	public void add(Test test) {
		munitTests.add(test);
	}

	public SuiteResult run() throws Exception {
		SuiteResult result = new SuiteResult(name);

		for (Test test : munitTests) {
			notificationListener.notifyStartOf(test);
			TestResult testResult = test.run();
			result.add(testResult);
			notificationListener.notify(testResult);
		}

		return result;
	}

	public void setNotificationListener(NotificationListener notificationListener) {
		if (notificationListener == null )
			throw new IllegalArgumentException();
		
		this.notificationListener = notificationListener;
	}

	public int getNumberOfTests() {
		
		return munitTests.size();
	}

	
}
