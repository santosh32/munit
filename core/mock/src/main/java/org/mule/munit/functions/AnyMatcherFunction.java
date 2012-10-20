package org.mule.munit.functions;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.mp.Matcher;


public class AnyMatcherFunction implements ExpressionLanguageFunction {
    private  Class expectedClass;

    public AnyMatcherFunction(Class expectedClass) {
        this.expectedClass = expectedClass;
    }

    @Override
    public Object call(Object[] params, ExpressionLanguageContext context) {
        return new Matcher() {
            @Override
            public boolean match(Object o) {
                if ( o == null ){
                    return false;
                }
                return expectedClass.isAssignableFrom(o.getClass());
            }
        };
    }
}
