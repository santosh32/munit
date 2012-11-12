package org.mule.munit.common.matchers;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class NotNullMatcherTest {
    @Test
    public void testNotNull(){
        assertTrue(new NotNullMatcher().match(new Object()));
    }

    @Test
    public void testNull(){
        assertFalse(new NotNullMatcher().match(null));
    }
}
