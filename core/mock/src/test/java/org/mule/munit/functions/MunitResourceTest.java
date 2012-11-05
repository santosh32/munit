package org.mule.munit.functions;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class MunitResourceTest {

	@Test
	public void testMunitResourceAsStream() throws IOException {
		InputStream is = ((MunitResource) new GetResourceFunction().call(
				new Object[] { "testFile.txt" }, null)).asStream();
		assertEquals("Hello World!", IOUtils.toString(is, "UTF-8"));
	}

	@Test
	public void testMunitResourceAsString() {
		String path = "/testFile.txt";
		MunitResource file = new MunitResource(path);
		String result = file.asString();
		assertEquals("Hello World!", result);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testMunitResourceAsByteArray() throws IOException {
		String path = "/testFile.txt";
		MunitResource file = new MunitResource(path);
		byte[] result = file.asByteArray();
		assertEquals("Hello World!", IOUtils.toString(result));
	}

}
