package org.mule.munit.common.matchers;

/**
 * <p>
 *     Matcher to check not null
 * </p>
 *
 * Usage: <code>new NotNullMatcher().match(null)</code> will return false
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class NotNullMatcher implements Matcher{

    /**
     *  @see Matcher#match(Object)
     */
    @Override
    public boolean match(Object o) {
        return o!=null;
    }
}
