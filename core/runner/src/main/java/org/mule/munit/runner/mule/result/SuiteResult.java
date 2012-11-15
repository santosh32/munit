package org.mule.munit.runner.mule.result;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Is the representation of the Suite result.</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class SuiteResult implements MunitResult {

	private List<MunitResult> results = new ArrayList<MunitResult>();
	private String name;

	public SuiteResult(String name) {
		this.name = name;
	}


    /**
     * <p>Notifies if the Suite failed or not</p>
     * @return false if one of the tests in the suite didn't succeed.
     */
	public boolean hasSucceeded() {
		for (MunitResult result : results) {
			if (!result.hasSucceeded())
				return false;
		}
		return true;
	}

    /**
     * @return The number of test with failures in the suite
     */
	public int getNumberOfFailures() {
		int failures = 0;
		for ( MunitResult result : results )
		{
			failures += result.getNumberOfFailures();
		}
		
		return failures;
				
	}

    /**
     * @return The number of test with errors in the suite
     */
	public int getNumberOfErrors() {
		int errors = 0;
		for ( MunitResult result : results )
		{
			errors += result.getNumberOfErrors();
		}
		
		return errors;
	}

    @Override
    public int getNumberOfTests() {
        return this.results.size();
    }

    @Override
    public float getTime() {
        float total = 0;
        for ( MunitResult result : results ){
            total+=result.getTime();
        }

        return total;
    }

    @Override
    public int getNumberOfSkipped() {
        int skipped = 0;
        for ( MunitResult result : results )
        {
            skipped += result.getNumberOfSkipped();
        }

        return skipped;
    }

    /**
     * @return The suite name.
     */
	public String getTestName() {
		return name;
	}

    /**
     * @return All the failing tests of the Suite
     */
	public List<MunitResult> getFailingTests()
	{
		List<MunitResult> failingTests = new ArrayList<MunitResult>();
		for ( MunitResult result : results)
		{
			if ( result.getNumberOfFailures() > 0 )
			{
				failingTests.add(result);
			}
		}
		return failingTests;
	}

    /**
     * @return All the test with errors of the Suite
     */
	public List<MunitResult> getErrorTests()
	{
		List<MunitResult> errorTests = new ArrayList<MunitResult>();
		for ( MunitResult result : results)
		{
			if ( result.getNumberOfErrors() > 0 )
			{
				errorTests.add(result);
			}
		}
		return errorTests;
	}
	
	public void add(MunitResult result)
	{
		results.add(result);
	}

}
