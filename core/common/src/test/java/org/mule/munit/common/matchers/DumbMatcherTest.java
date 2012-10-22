package org.mule.munit.common.matchers;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DumbMatcherTest {

    @Test
    public void testTrue(){
        DumbMatcher dumbMatcher = new DumbMatcher(true);
        assertTrue(dumbMatcher.match(null));
    }

    @Test
    public void testFalse(){
        DumbMatcher dumbMatcher = new DumbMatcher(false);
        assertFalse(dumbMatcher.match(null));
    }
}
