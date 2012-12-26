package org.mule.munit.runner.mule;

import org.mule.munit.runner.mule.result.SuiteResult;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.mule.result.notification.DummyNotificationListener;
import org.mule.munit.runner.mule.result.notification.NotificationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>MUnit Suite</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitSuite {

	private String name;
	private List<MunitTest> munitTests = new ArrayList<MunitTest>();
	private NotificationListener notificationListener = new DummyNotificationListener();
	
	public MunitSuite(String name) {
		this.name = name;
	}

	public void add(MunitTest test) {
		munitTests.add(test);
	}

    /**
     * <p>Runs all the tests of the suite </p>
     * @return The Result of the suite execution
     * @throws Exception If the suite failed for one reason.
     */
	public SuiteResult run() throws Exception {
		SuiteResult result = new SuiteResult(name);

		for (MunitTest test : munitTests) {
			notificationListener.notifyStartOf(test);
			TestResult testResult = test.run();
			result.add(testResult);
			notificationListener.notify(testResult);
		}

        notificationListener.notifyEnd(result);
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

    public String getName() {
        return name;
    }
}
