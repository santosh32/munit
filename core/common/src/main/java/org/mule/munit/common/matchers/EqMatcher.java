package org.mule.munit.common.matchers;

public class EqMatcher implements Matcher{
    private Object expected;

    public EqMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public boolean match(Object o) {
        if ( o == null ){
            return false;
        }
        return o.equals(expected);
    }
}
