package org.mule.munit.common.matchers;

/**
 * <p>Equality matcher</p>
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
