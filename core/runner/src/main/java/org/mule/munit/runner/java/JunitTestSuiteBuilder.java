package org.mule.munit.runner.java;


import junit.framework.TestSuite;
import org.mule.api.MuleContext;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.SuiteBuilder;

import java.util.List;

/**
 * <p>Creates a Munit Suite and its tests to be run with Junit framework</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class JunitTestSuiteBuilder extends SuiteBuilder<TestSuite, MunitTest> {


    protected JunitTestSuiteBuilder(MuleContext muleContext) {
        super(muleContext);
    }

    @Override
    protected TestSuite createSuite(String name) {
        TestSuite testSuite = new TestSuite(name);
        for ( MunitTest test : tests ){
            testSuite.addTest(test);
        }
        return testSuite;
    }

    @Override
    protected MunitTest test(List<MunitFlow> beforeTest, MunitTestFlow test, List<MunitFlow> afterTest) {
        return new MunitTest(beforeTest,test, afterTest);
    }
}
