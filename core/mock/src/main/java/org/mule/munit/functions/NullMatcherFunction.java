package org.mule.munit.functions;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.common.matchers.NullMatcher;


public class NullMatcherFunction implements ExpressionLanguageFunction {
    @Override
    public Object call(Object[] params, ExpressionLanguageContext context) {
        return new NullMatcher();
    }
}
