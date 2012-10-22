package org.mule.munit.common.matchers;

/**
 * <p>Matcher to check not null</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class NotNullMatcher implements Matcher{
    @Override
    public boolean match(Object o) {
        return o!=null;
    }
}
