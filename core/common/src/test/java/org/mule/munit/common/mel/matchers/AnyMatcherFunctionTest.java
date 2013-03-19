package org.mule.munit.common.mel.matchers;

import org.junit.Test;
import org.mule.munit.common.matchers.Matcher;

import java.util.*;

import static junit.framework.Assert.assertTrue;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AnyMatcherFunctionTest {
    
    @Test
    public void testAnyBoolean(){
        AnyMatcherFunction f = new AnyMatcherFunction(Boolean.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(Boolean.FALSE));
    }

    @Test
    public void testAnyString(){
        AnyMatcherFunction f = new AnyMatcherFunction(String.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match("string"));
    }

    @Test
    public void testAnyByte(){
        AnyMatcherFunction f = new AnyMatcherFunction(Byte.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(Byte.MIN_VALUE));
    }

    @Test
    public void testAnyInt(){
        AnyMatcherFunction f = new AnyMatcherFunction(Integer.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(9));
    }

    @Test
     public void testAnyDouble(){
        AnyMatcherFunction f = new AnyMatcherFunction(Double.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(new Double(0)));
    }

    @Test
    public void testAnyFloat(){
        AnyMatcherFunction f = new AnyMatcherFunction(Float.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(new Float(0)));
    }

    @Test
     public void testAnyShort(){
        AnyMatcherFunction f = new AnyMatcherFunction(Short.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(Short.MIN_VALUE));
    }

    @Test
    public void testAnyObject(){
        AnyMatcherFunction f = new AnyMatcherFunction(Object.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(""));
    }


    @Test
    public void testAnyCollection(){
        AnyMatcherFunction f = new AnyMatcherFunction(Collection.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(new ArrayList<String>()));
    }

    @Test
    public void testAnyList(){
        AnyMatcherFunction f = new AnyMatcherFunction(List.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(new ArrayList<String>()));
    }

    @Test
    public void testAnyMap(){
        AnyMatcherFunction f = new AnyMatcherFunction(Map.class);
        Matcher m = (Matcher) f.call(null, null);

        assertTrue(m.match(new HashMap<String,Object>()));
    }
}
