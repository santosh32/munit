package org.mule.munit.test.result.notification;

public class Notification {

	private String fullMessage;
	private String shortMessage;

	public Notification(String shortMessage, String fullMessage) {
		this.shortMessage = shortMessage;
		this.fullMessage = fullMessage;
	}

	public String getFullMessage() {
		return fullMessage;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	
}
