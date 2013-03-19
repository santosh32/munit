package org.mule.munit.mel.utils;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

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
	
	@Test(expected = IllegalArgumentException.class)
	public void testCaseWhenFileDoesNotExist(){
		new MunitResource("anything").asStream();
	}

	@Test
	public void testMunitResourceAsByteArray() throws IOException {
		String path = "/testFile.txt";
		MunitResource file = new MunitResource(path);
		byte[] result = file.asByteArray();
		assertEquals("Hello World!", new String(result));
	}

}
