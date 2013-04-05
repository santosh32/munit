package org.mule.munit.mel.assertions;

import org.mule.modules.interceptor.matchers.Matcher;


/**
 * <p>
 *     Wrapper of a matcher to improve the MEL message assertion legibility.
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.4
 */
public class ElementMatcher {

    /**
     * <p>
     *     The element that has to be validated. Can be any object but it is going to be called with message
     *     properties, payload and attachments.
     * </p>
     */
    private Object element;

    /**
     * <p>
     *     Constructor of an use once and discard Object.
     * </p>
     */
    public ElementMatcher(Object element) {
        this.element = element;
    }

    /**
     * <p>
     *     Method just to call the matcher
     * </p>
     * @param matcher
     * <p>
     *     matcher that has to be called.
     * </p>
     * @return
     * <p>
     *     true/false depending on the matcher result.
     * </p>
     */
    public boolean is(Matcher matcher){
        return matcher.match(element);
    }
}
