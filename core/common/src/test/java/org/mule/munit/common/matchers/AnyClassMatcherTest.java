package org.mule.munit.common.matchers;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnyClassMatcherTest {
    
    @Test
    public void testWithNoNull(){
        AnyClassMatcher anyClassMatcher = new AnyClassMatcher(String.class);

        assertTrue(anyClassMatcher.match("string"));
    }

    @Test
    public void testWithNull(){
        AnyClassMatcher anyClassMatcher = new AnyClassMatcher(String.class);

        assertFalse(anyClassMatcher.match(null));
    }
}
