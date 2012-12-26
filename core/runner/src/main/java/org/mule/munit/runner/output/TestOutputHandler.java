package org.mule.munit.runner.output;


/**
 * <p>Defines how the stage notification of the test will be handled </p>
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public interface TestOutputHandler {

    /**
     * <p>Prints the MUnit flow information</p>
     * @param name The name of the Munit flow (before-test,after-test,before-suite,after-suite,test)
     * @param description The flow description
     */
    void printDescription(String name, String description);
    
    void printTestName(String suiteName);
}
