package org.mule.munit.common.mel.assertions;


import org.mule.api.MuleMessage;

public interface ElementMatcherBuilder {

    ElementMatcher build(String elementName, MuleMessage messageContext);
}
