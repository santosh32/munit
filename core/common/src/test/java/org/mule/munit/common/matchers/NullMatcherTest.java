package org.mule.munit.common.matchers;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NullMatcherTest {
    @Test
    public void testNotNull(){
        assertFalse(new NullMatcher().match(new Object()));
    }

    @Test
    public void testNull(){
        assertTrue(new NullMatcher().match(null));
    }
}
