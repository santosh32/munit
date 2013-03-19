package org.mule.munit.common.mel.matchers;

import org.junit.Test;
import org.mule.munit.common.matchers.Matcher;
import org.mule.munit.common.mel.matchers.NullMatcherFunction;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Javier Casal
 * @version since 3.3.2
 */
public class NullMatcherFunctionTest {

    @Test
    public void callWithNull(){
        assertTrue(((Matcher) new NullMatcherFunction().call(null, null)).match(null));
    }

    @Test
    public void callWithNotNull(){
        assertFalse(((Matcher) new NullMatcherFunction().call(null, null)).match(new Object()));
    }
}
