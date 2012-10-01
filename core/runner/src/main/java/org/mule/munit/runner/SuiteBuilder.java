package org.mule.munit.runner;


import org.mule.api.MuleContext;
import org.mule.munit.config.MunitAfterTest;
import org.mule.munit.config.MunitBeforeTest;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * <p>Abstract class to create a Munit suite and its tests</p>
 *
 * <p>T is the Type of the Suite</p>
 * <p>E is the Type of the Tests</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public abstract class SuiteBuilder<T,E> {

    private MuleContext muleContext;

    /**
     * <p>The list of tests to be added in the suit</p>
     */
    protected List<E> tests = new ArrayList<E>();

    /**
     * <p>Create the suite based on the tests created</p>
     * @param name The Suite Name.
     * @return The Suite object
     */
    protected abstract T createSuite(String name);

    /**
     * <p>Create a test</p>
     * @param beforeTest Munit flows to be run before the test
     * @param test Munit Flow that represents the test
     * @param afterTest Munit flows to be run after the test
     * @return The Test Object
     */
    protected abstract E test(List<MunitFlow> beforeTest, MunitTestFlow test, List<MunitFlow> afterTest);

    /**
     * @param muleContext Used to create the tests and pre/post proccessors.
     */
    protected SuiteBuilder(MuleContext muleContext) {
        this.muleContext = muleContext;
    }

    /**
     * <p>Builds the Suite with a particular suite name, based on the mule context</p>
     * @param suiteName The desired suite name
     * @return The Suite Object
     */
    public T build(String suiteName){
        List<MunitFlow> before = lookupFlows(MunitBeforeTest.class);
        List<MunitFlow> after = lookupFlows(MunitAfterTest.class);
        Collection<MunitTestFlow> flowConstructs = lookupTests();
        for (MunitTestFlow flowConstruct : flowConstructs) {
            if ( !flowConstruct.isIgnore() ){
               tests.add(test(before, flowConstruct, after));
            }
        }
        
        return createSuite(suiteName);
    }

    private List<MunitFlow> lookupFlows(Class munitClass) {
        return new ArrayList<MunitFlow>(muleContext.getRegistry()
                .lookupObjects(munitClass));
    }

    private Collection<MunitTestFlow> lookupTests() {
        return new ArrayList<MunitTestFlow>(muleContext.getRegistry()
                .lookupObjects(MunitTestFlow.class));
    }
}
