package org.mule.munit.common.matchers;

/**
 * <p>
 *     Dumb matcher that returns the specified value
 * </p>
 *
 * Usage: <code>new DumbMatcher(true).match(any)</code> will return always true, no matter <code>any</code> value
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class DumbMatcher implements Matcher{
    boolean expectedValue;

    public DumbMatcher(boolean expectedValue) {
        this.expectedValue = expectedValue;
    }

    /**
     *  @see Matcher#match(Object)
     */
    @Override
    public boolean match(Object o) {
        return expectedValue;
    }
}
