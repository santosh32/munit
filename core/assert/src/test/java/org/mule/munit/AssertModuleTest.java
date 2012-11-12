package org.mule.munit;


import junit.framework.AssertionFailedError;
import org.junit.Test;
import org.mule.transport.NullPayload;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertModuleTest {

    /**
     * Two equal payloads should not fail
     */
    @Test
    public void assertPayloadOkWithEmptyMessage(){
        module().assertThat(null, "a", "a");
    }

    /**
     * Two different payloads must fail
     */
    @Test(expected = AssertionFailedError.class)
    public void assertPayloadFailWithEmptyMessage(){
        module().assertThat(null, "a", "b");
    }

    /**
     * Assert True accepts true values
     */
    @Test
    public void assertTrueOkWithTrueValues(){
        module().assertTrue(null, true);
    }

    /**
     * Assert True rejects false values
     */
    @Test(expected = AssertionFailedError.class)
    public void assertTrueRejectsFalseValues(){
        module().assertTrue(null, false);
    }

    /**
     * if two expressions are equal then return ok
     */
    @Test
    public void assertOnEqualsOk(){
        module().assertOnEquals(null, "a", "a");
    }

    /**
     * if two expressions are not equal then fail
     */
    @Test(expected = AssertionFailedError.class)
    public void assertOnEqualsFail(){
        module().assertOnEquals(null, "a", "b");
    }

    /**
     * if are equal Fail
     */
    @Test(expected = AssertionFailedError.class)
    public void assertNotSameFail(){
        module().assertNotSame(null, "a", "a");
    }

    /**
     * if are different ok
     */
    @Test
    public void assertNotSameOk(){
        module().assertNotSame(null, "a", "b");
    }


    /**
     * if true then fail
     */
    @Test(expected = AssertionFailedError.class)
    public void assertTrueFail(){
        module().assertFalse(null, true);
    }

    /**
     * if is false ok
     */
    @Test
    public void assertFalseOk(){
        module().assertFalse(null, false);
    }

    /**
     * if Null Payload ThenFail
     */
    @Test(expected = AssertionFailedError.class)
    public void assertNotNullFail(){
        module().assertNotNull(null, NullPayload.getInstance());
    }

    /**
     * if not Null then ok
     */
    @Test
    public void assertNotNullOk(){
        module().assertNotNull(null, new Object());
    }

    /**
     * if not Null Payload ThenFail
     */
    @Test(expected = AssertionFailedError.class)
    public void assertNullFail(){
        module().assertNull(null, new Object());
    }

    /**
     * if  Null then ok
     */
    @Test
    public void assertNullOk(){
        module().assertNull(null, NullPayload.getInstance());
    }

    /**
     * No matter what just fail
     * @return
     */
    @Test(expected = AssertionFailedError.class)
    public void assertFail(){
        module().fail(null);
    }

    private static AssertModule module() {
        return new AssertModule();
    }
}
