package org.mule.munit.runner.mule.result;

/**
 * <p>The Representation of a Test Result.</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public interface MunitResult {
	
	String getTestName();

	boolean hasSucceeded();

	int getNumberOfFailures();
	
	int getNumberOfErrors();

    int getNumberOfTests();

    float getTime();

    int getNumberOfSkipped();
}
