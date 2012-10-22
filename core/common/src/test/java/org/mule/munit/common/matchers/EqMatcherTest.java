package org.mule.munit.common.matchers;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqMatcherTest {
    
    @Test
    public void testEqualityWithoutNull(){
        EqMatcher matcher = new EqMatcher("value");
        
        assertTrue(matcher.match("value"));
    }
    
    @Test
    public void testWithNull(){
        EqMatcher matcher = new EqMatcher("value");

        assertFalse(matcher.match(null));
    }
}
