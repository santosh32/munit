package org.mule.munit.common.mel.assertions;

import org.mule.munit.common.matchers.Matcher;


public class ElementMatcher {

    private Object element;

    public ElementMatcher(Object element) {
        this.element = element;
    }

    public boolean is(Matcher matcher){
        return matcher.match(element);
    }
}
