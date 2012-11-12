package org.mule.munit.common.mocking;

/**
 * <p>This class is a marker to know that the payload must not be overridden</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class NonDefinedPayload {

    public static NonDefinedPayload getInstance(){
       return new NonDefinedPayload();
    }
}
