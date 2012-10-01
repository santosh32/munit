package org.mule.munit.runner.mule.result.notification;

import org.mule.munit.runner.mule.MunitTest;
import org.mule.munit.runner.mule.result.TestResult;

/**
 * <p>The default notification listener for a Suite. Does nothing.</p>
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class DummyNotificationListener implements NotificationListener{
    @Override
    public void notifyStartOf(MunitTest test) {

    }

    @Override
    public void notify(TestResult testResult) {

    }
}
