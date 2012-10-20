package org.mule.munit.functions;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.common.matchers.AnyClassMatcher;
import org.mule.munit.common.matchers.DumbMatcher;

public class AnyClassMatcherFunction implements ExpressionLanguageFunction {
    @Override
    public Object call(final Object[] params, ExpressionLanguageContext context) {
        if ( params==null || params.length == 0 ){
            return new DumbMatcher(false);
        }
        if ( params[0] instanceof Class ){
            return new AnyClassMatcher((Class) params[0]);
        }
        return new DumbMatcher(false);

    }
}
