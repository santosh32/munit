package org.mule.munit.common.matchers;


public class DumbMatcher implements Matcher{
    boolean expectedValue;

    public DumbMatcher(boolean expectedValue) {
        this.expectedValue = expectedValue;
    }


    @Override
    public boolean match(Object o) {
        return expectedValue;
    }
}
