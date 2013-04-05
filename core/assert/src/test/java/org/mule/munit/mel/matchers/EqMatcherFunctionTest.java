package org.mule.munit.mel.matchers;

import org.junit.Test;
import org.mule.modules.interceptor.matchers.Matcher;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class EqMatcherFunctionTest {


    public static final Object OBJECT = new Object();

    @Test
    public void callWithNull(){
        assertFalse(((Matcher) new EqMatcherFunction().call(null, null)).match(null));
    }

    @Test
    public void callWithEmpty(){
        assertFalse(((Matcher) new EqMatcherFunction().call(new Object[]{}, null)).match(null));
    }

    @Test
    public void callWithDifferent(){
        assertFalse(((Matcher) new EqMatcherFunction().call(new Object[]{new Object()}, null)).match(new Object()));
    }

    @Test
    public void callWithEquals(){
        assertTrue(((Matcher) new EqMatcherFunction().call(new Object[]{OBJECT}, null)).match(OBJECT));
    }
}
