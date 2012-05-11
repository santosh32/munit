package org.mule.munit.test.result;

public interface MunitResult {
	
	String getTestName();
	
	boolean hasSucceded();
	
	int getNumberOfFailures();
	
	int getNumberOfErrors();

}
