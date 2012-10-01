package org.mule.munit.runner.mule;

import org.mule.api.MuleContext;
import org.mule.munit.runner.MuleContextManager;
import org.mule.munit.runner.MunitRunner;
import org.mule.munit.runner.mule.result.SuiteResult;
import org.mule.munit.runner.mule.result.notification.NotificationListener;
import org.mule.munit.runner.mule.result.output.DefaultOutputHandler;
import org.mule.munit.runner.mule.result.output.TestOutputHandler;


/**
 * <p>The Munit test runner</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitSuiteRunner  {

	private MuleContext muleContext;
	private MunitSuite suite;
    private TestOutputHandler handler = new DefaultOutputHandler();
    private MuleContextManager muleContextManager = new MuleContextManager();


	public MunitSuiteRunner(String resources) {

		try {
			muleContext = muleContextManager.startMule(resources);

            suite = new MunitSuiteBuilder(muleContext, handler).build(resources);

		} catch (Exception e) {
            muleContextManager.killMule(muleContext);
			throw new RuntimeException(e);
		}

	}

	public SuiteResult run() {
		return new MunitRunner<SuiteResult>(handler, muleContextManager, muleContext){

            @Override
            protected SuiteResult runSuite() throws Exception {
                return suite.run();
            }
        }.run();
	}

    public void setNotificationListener(NotificationListener notificationListener) {
		this.suite.setNotificationListener(notificationListener);
	}


	public int getNumberOfTests() {
		return suite.getNumberOfTests();
	}

}
