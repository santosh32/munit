package org.mule.munit.functions;

import java.io.File;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;

/**
 * <p>
 * This is an extension that picks an indicated file, an returns its content in
 * the payload of the message from where the method was called. The type of the
 * returned value can be String, InputStream or byte array, depending of the
 * operation aplicated to the MunitResource returned
 * </p>
 * 
 * @author Javier Casal
 * @version since 3.3.2
 */
public class GetResourceFunction implements ExpressionLanguageFunction {

	@Override
	public Object call(final Object[] params, ExpressionLanguageContext context) {
		if (params == null || params.length != 1
				|| !(params[0] instanceof String)) {
			throw new IllegalArgumentException(
					"You must provide one parameter that "
							+ "indicates where the resource file is placed");
		}
		return new MunitResource(File.separator + (String) params[0]);
	}

}
