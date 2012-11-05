package org.mule.munit.functions;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class GetResourceFunctionTest {
	/*
	 * If the params parameter has more than one object, then an
	 * IllegalArgumentException is expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void ifParamLengthIsGreatterThanOneThenFail() {
		Object[] params = new Object[] { "param1", "param2", "param3" };

		new GetResourceFunction().call(params, null);
	}

	/*
	 * If the params parameter has has no objects, then an
	 * IllegalArgumentException is expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void ifParamLengthIsSmallerThanOneThenFail() {
		new GetResourceFunction().call(new Object[] {}, null);
	}

	/*
	 * If the params parameter is null, then an IllegalArgumentException is
	 * expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void ifParamLengthIsNullThenFail() {
		new GetResourceFunction().call(null, null);
	}

	/*
	 * If the params parameter is not an instance of String, then an
	 * IllegalArgumentException is expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void ifParamLengthIsNotInstanceOfStringThenFail() {
		int intImput = 4;
		Object[] params = new Object[] { intImput };

		new GetResourceFunction().call(params, null);
	}

	/*
	 * A call to GetResourceFunction returns a MunitResource
	 */
	@Test
	public void aGetResourceFunctionCallIsInstanceOfMunitResource() {
		assertThat(new GetResourceFunction().call(
				new Object[] { "testFile.txt" }, null),
				is(instanceOf(MunitResource.class)));
	}

}
