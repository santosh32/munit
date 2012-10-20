package org.mule.munit.common.matchers;


public class AnyClassMatcher implements Matcher {

    private Class expectedClass;

    public AnyClassMatcher(Class expectedClass) {
        this.expectedClass = expectedClass;
    }

    @Override
    public boolean match(Object o) {
        if (o == null) {
            return false;
        }
        return this.expectedClass.isAssignableFrom(o.getClass());
    }
}
