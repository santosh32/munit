package org.mule.munit.functions;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.common.matchers.AnyClassMatcher;


public class AnyMatcherFunction implements ExpressionLanguageFunction {
    private Class expectedClass;

    public AnyMatcherFunction(Class expectedClass) {
        this.expectedClass = expectedClass;
    }

    @Override
    public Object call(Object[] params, ExpressionLanguageContext context) {
        return new AnyClassMatcher(expectedClass);
    }
}
