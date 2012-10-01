package org.mule.munit.runner.mule.result.notification;

/**
 * <p>The representation of the test notification</p>
 * @author Federico, Fernando
 * @version since 3.3.2
 */
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
