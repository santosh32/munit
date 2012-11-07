package org.mule.munit.functions;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;


public class GetResourceFunction implements ExpressionLanguageFunction{

	@Override
    public Object call(final Object[] params, ExpressionLanguageContext context) {
    	if (params == null || params.length != 1 || !(params[0] instanceof String)){
            throw new IllegalArgumentException("You must provide one parameter that " +
            									"indicates where the resource file is placed");
        }
    	return new MunitResource("/" + (String) params[0]);
    }
	
}
