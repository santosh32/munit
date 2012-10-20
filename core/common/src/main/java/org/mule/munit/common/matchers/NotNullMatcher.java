package org.mule.munit.common.matchers;

public class NotNullMatcher implements Matcher{
    @Override
    public boolean match(Object o) {
        return o!=null;
    }
}
