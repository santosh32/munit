package org.mule.notifiers;

import org.mule.munit.runner.mule.MunitTest;
import org.mule.munit.runner.mule.result.SuiteResult;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.mule.result.notification.NotificationListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationListenerDecorator implements NotificationListener {

    private List<NotificationListener> notificationListeners = new ArrayList<NotificationListener>();


    @Override
    public void notifyStartOf(MunitTest test) {
        for ( NotificationListener notificationListener : notificationListeners){
            notificationListener.notifyStartOf(test);
        }
    }

    @Override
    public void notify(TestResult testResult) {
        for ( NotificationListener notificationListener : notificationListeners){
            notificationListener.notify(testResult);
        }
    }

    @Override
    public void notifyEnd(SuiteResult result) {
        for ( NotificationListener notificationListener : notificationListeners){
            notificationListener.notifyEnd(result);
        }
    }

    public void addNotificationListener(NotificationListener listener){
        notificationListeners.add(listener);
    }
}
