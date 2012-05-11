package org.mule.munit.test.result.notification;

import java.io.PrintStream;

import org.mule.munit.test.Test;
import org.mule.munit.test.result.TestResult;

public class StreamNotificationListener implements NotificationListener{

	private PrintStream out;
	private boolean debugMode;
	
	public StreamNotificationListener(PrintStream out) {
		this.out = out;
	}

	public void notifyStartOf(Test test) {
		out.println("RUNNIG " + test.getName());
	}

	public void notify(TestResult testResult) {
		Notification notification = null;
		if ( testResult.getNumberOfErrors() > 0 )
		{
			out.println("ERROR - The test " + testResult.getTestName() + " finished with an Error.");
			out.flush();
			notification = testResult.getError();
		}
		else if ( testResult.getFailure() != null )
		{
			out.println("FAILURE - The test " + testResult.getTestName() + " finished with a Failure.");
			out.flush();
			notification = testResult.getFailure();
		}
		
		if ( notification != null )
		{
			out.println(notification.getShortMessage());
			out.flush();
			if (debugMode)
			{
				out.println(notification.getFullMessage());
			}
		}
		else
		{
			out.println("SUCCESS - Test " + testResult.getTestName() + " finished Successfully.");
			out.flush();
		}
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	

}
