package org.mule.munit.common.matchers;

/**
 * <p>Equality matcher. It compares using the equal method of Object</p>
 *
 * Usage: <code>new EqMatcher("desired").match("desired");</code> will return true.
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class EqMatcher implements Matcher{
    /**
     * <p>The expected object</p>
     */
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
