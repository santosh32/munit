package org.mule.munit.functions;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.common.matchers.NotNullMatcher;

public class NotNullMatcherFunction implements ExpressionLanguageFunction {
    @Override
    public Object call(Object[] params, ExpressionLanguageContext context) {
        return new NotNullMatcher();
    }
}
