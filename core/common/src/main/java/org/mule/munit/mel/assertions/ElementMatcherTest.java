package org.mule.munit.mel.assertions;

import org.junit.Test;
import org.mule.munit.common.matchers.Matcher;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Federico, Fernando
 * @since 3.4
 */
public class ElementMatcherTest {

    public static final Object ELEMENT = new Object();
    private Matcher matcher = mock(Matcher.class);

    @Test
    public void elementMatcherMustCallTheOriginalMatcher(){
        ElementMatcher elementMatcher = new ElementMatcher(ELEMENT);
        when(matcher.match(ELEMENT)).thenReturn(true);

        assertTrue(elementMatcher.is(matcher));

        verify(matcher).match(ELEMENT);

    }

}
