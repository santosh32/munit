package org.mule.munit.test.result;

import java.util.ArrayList;
import java.util.List;

public class SuiteResult implements MunitResult {

	private List<MunitResult> results = new ArrayList<MunitResult>();
	private String name;

	public SuiteResult(String name) {
		this.name = name;
	}

	
	public boolean hasSucceded() {
		for (MunitResult result : results) {
			if (!result.hasSucceded())
				return false;
		}
		return true;
	}

	public int getNumberOfFailures() {
		int failures = 0;
		for ( MunitResult result : results )
		{
			failures += result.getNumberOfFailures();
		}
		
		return failures;
				
	}

	public int getNumberOfErrors() {
		int errors = 0;
		for ( MunitResult result : results )
		{
			errors += result.getNumberOfErrors();
		}
		
		return errors;
	}
	
	public String getTestName() {
		return name;
	}

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
