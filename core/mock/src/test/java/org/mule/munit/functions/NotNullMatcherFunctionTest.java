package org.mule.munit.functions;

import org.junit.Test;
import org.mule.munit.common.matchers.Matcher;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Javier Casal
 * @version since 3.3.2
 */
public class NotNullMatcherFunctionTest {

    @Test
    public void callWithNull(){
        assertFalse(((Matcher) new NotNullMatcherFunction().call(null, null)).match(null));
    }

    @Test
    public void callWithNotNull(){
        assertTrue(((Matcher) new NotNullMatcherFunction().call(null, null)).match(new Object()));
    }


}
