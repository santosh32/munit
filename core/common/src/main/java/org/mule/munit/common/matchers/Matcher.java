package org.mule.munit.common.matchers;

/**
 * <p>Matcher definition</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public interface Matcher {

    /**
     * <p>Comparing method</p>
     * @param o
     *      <p>The object that is needed to compare </p>
     * @return
     *      <p>true/false based on the matching result</p>
     */
    boolean match(Object o);
}
