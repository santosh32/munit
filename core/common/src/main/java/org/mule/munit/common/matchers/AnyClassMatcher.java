package org.mule.munit.common.matchers;


/**
 * <p>Matcher to match a specified class</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AnyClassMatcher implements Matcher {

    /**
     * <p>The wanted class</p>
     */
    private Class expectedClass;

    public AnyClassMatcher(Class expectedClass) {
        this.expectedClass = expectedClass;
    }

    @Override
    public boolean match(Object o) {
        if (o == null) {
            return false;
        }
        return this.expectedClass.isAssignableFrom(o.getClass());
    }
}
