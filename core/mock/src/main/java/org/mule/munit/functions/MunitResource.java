package org.mule.munit.functions;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class MunitResource {
	
	private String path;
	
	public MunitResource(String path) {
		this.path = path;
	}

	public InputStream asStream() {
		InputStream streamImput = null;	
		try {
			streamImput = getClass().getResourceAsStream(path);
		} catch (NullPointerException npe) {
			System.out.println("The resource in " + path + " was not found");
			npe.printStackTrace();
		}
		return streamImput;
	}
	
	public String asString() {
		String stringImput = null;
		try {
			stringImput = IOUtils.toString(getClass().getResourceAsStream(path));
		} catch (NullPointerException npe) {
			System.out.println("The resource in " + path + " was not found");
			npe.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("The resource " + path + " is not valid");
			ioe.printStackTrace();
		}
		return stringImput;
	}
	
	public byte[] asByteArray() {
		byte[] byteArrayImput = null;	
		try {
			byteArrayImput = IOUtils.toByteArray(getClass().getResourceAsStream(path));
		} catch (NullPointerException npe) {
			System.out.println("The resource in " + path + "was not found");
			npe.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("The resource " + path + "is not valid");
			ioe.printStackTrace();
		}
		return byteArrayImput;
	}

}
