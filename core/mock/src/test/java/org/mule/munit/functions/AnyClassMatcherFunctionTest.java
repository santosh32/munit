package org.mule.munit.functions;

import org.junit.Test;
import org.mule.munit.common.matchers.Matcher;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class AnyClassMatcherFunctionTest {
    
    @Test
    public void testWithBoolean(){
        AnyClassMatcherFunction function = new AnyClassMatcherFunction();

        Matcher matcher = (Matcher) function.call(new Class[]{Boolean.class}, null);
        assertTrue(matcher.match(Boolean.FALSE));
    }

    @Test
    public void testWithstring(){
        AnyClassMatcherFunction function = new AnyClassMatcherFunction();

        Matcher matcher = (Matcher) function.call(new Class[]{Boolean.class}, null);
        assertFalse(matcher.match(""));
    }
}
