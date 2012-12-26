package org.mule.munit.runner.output;



/**
 * <p>Output printer interface</p>
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public interface OutputPrinter {
    /**
     * <p>Prints with an end of line the text passed as parameter</p>
     * @param text
     *     <p>The text we want to print</p>
     */
    void print(String text);
}
