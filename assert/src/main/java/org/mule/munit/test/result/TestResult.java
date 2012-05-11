package org.mule.munit.test.result;

import org.mule.munit.test.result.notification.Notification;

public class TestResult implements MunitResult {

	private String name;
	private Notification failure;
	private Notification error;
	
	
	public TestResult(String name) {
		this.name = name;
	}

	public String getTestName() {
		return name;
	}

	public boolean hasSucceded() {
	 return ( error != null && failure != null );
	}

	public int getNumberOfFailures() {
		return failure != null ? 1 : 0;
	}

	public int getNumberOfErrors() {
		return error != null ? 1 : 0;
	}

	public Notification getFailure() {
		return failure;
	}

	public void setFailure(Notification failure) {
		this.failure = failure;
	}

	public Notification getError() {
		return error;
	}

	public void setError(Notification error) {
		this.error = error;
	}
	
	

}
