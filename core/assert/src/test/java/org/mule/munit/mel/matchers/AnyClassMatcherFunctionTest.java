package org.mule.munit.mel.matchers;

import org.junit.Test;
import org.mule.modules.interceptor.matchers.Matcher;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
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

    @Test
    public void testWithNull(){
        AnyClassMatcherFunction function = new AnyClassMatcherFunction();

        Matcher matcher = (Matcher) function.call(null, null);
        assertFalse(matcher.match(null));
    }

    @Test
    public void testWitEmpty(){
        AnyClassMatcherFunction function = new AnyClassMatcherFunction();

        Matcher matcher = (Matcher) function.call(new Class[]{}, null);
        assertFalse(matcher.match(null));
    }

    @Test
    public void testWitNonClass(){
        AnyClassMatcherFunction function = new AnyClassMatcherFunction();

        Matcher matcher = (Matcher) function.call(new Object[]{new Object()}, null);
        assertFalse(matcher.match(null));
    }
}
