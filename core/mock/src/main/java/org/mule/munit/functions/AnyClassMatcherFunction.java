package org.mule.munit.functions;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.mp.Matcher;

public class AnyClassMatcherFunction implements ExpressionLanguageFunction {
    @Override
    public Object call(final Object[] params, ExpressionLanguageContext context) {

        return new Matcher() {
            @Override
            public boolean match(Object o) {
                if ( params==null || params.length == 0 ){
                    return false;
                }
                if ( params[0] instanceof Class ){
                  return o.getClass().isAssignableFrom((Class) params[0]);
                }
                return false;
            }
        };
    }
}
