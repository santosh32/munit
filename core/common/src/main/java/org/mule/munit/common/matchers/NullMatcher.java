package org.mule.munit.common.matchers;

/**
 * <p>Matcher to check null</p>
 *
 * Usage: <code>new NullMatcher().match(null)</code> will return true
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class NullMatcher implements Matcher{
    @Override
    public boolean match(Object o) {
        return o == null;
    }
}
