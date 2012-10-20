package org.mule.munit.functions;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.common.matchers.DumbMatcher;
import org.mule.munit.common.matchers.EqMatcher;


public class EqMatcherFunction implements ExpressionLanguageFunction{
    @Override
    public Object call(final Object[] params, ExpressionLanguageContext context) {
        if ( params==null || params.length == 0 ){
            return new DumbMatcher(false);
        }
        return new EqMatcher(params[0]);
    }
}
