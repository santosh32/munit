package org.mule.munit.runner.mule.result.output;


/**
 * <p>Prints the description in the system console</p>
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class DefaultOutputHandler implements TestOutputHandler{
    @Override
    public void print(String name, String description) {
        System.out.printf("%nExecuting "+name+"%n************%n"
                + description.replaceAll("\\.", "\\.%n") + "%n");
    }
}
