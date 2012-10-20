package org.mule.munit.common.matchers;

public class NullMatcher implements Matcher{
    @Override
    public boolean match(Object o) {
        return o == null;
    }
}
