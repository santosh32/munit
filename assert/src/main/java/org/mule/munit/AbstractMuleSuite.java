
package org.mule.munit;

import junit.framework.TestSuite;
import org.junit.runner.RunWith;

@RunWith(MuleSuiteRunner.class)
public abstract class AbstractMuleSuite extends TestSuite
{
    public abstract String getConfigResources();

}
