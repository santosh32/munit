package org.mule.munit;

import junit.framework.*;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.*;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.config.ConfigurationBuilder;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.context.MuleContextBuilder;
import org.mule.api.context.MuleContextFactory;
import org.mule.api.registry.MuleRegistry;
import org.mule.config.DefaultMuleConfiguration;
import org.mule.config.builders.SimpleConfigurationBuilder;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.construct.Flow;
import org.mule.context.DefaultMuleContextBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.tck.MuleTestUtils;
import org.mule.tck.TestingWorkListener;
import org.mule.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MuleSuiteRunner extends Runner implements Filterable, Sortable{
    public static final String CLASSNAME_ANNOTATIONS_CONFIG_BUILDER = "org.mule.config.AnnotationsConfigurationBuilder";

    TestSuite testSuite;
    MuleContext muleContext;
    String resources;

    public MuleSuiteRunner(Class testClass) {
        try {
            Method getConfigResources = testClass.getMethod("getConfigResources");
            resources = (String) getConfigResources.invoke(testClass.newInstance());
            
            muleContext = this.createMuleContext();
            muleContext.start();

            testSuite = new TestSuite();

            Flow before = lookupFlowConstruct("before.each.test");
            Flow after = lookupFlowConstruct("after.each.test");
            MuleRegistry registry = muleContext.getRegistry();
            Collection<FlowConstruct> flowConstructs = registry.lookupFlowConstructs();
            for ( FlowConstruct flowConstruct : flowConstructs )
            {
                if ( flowConstruct.getName().startsWith("test"))
                {
                    final Flow flow = (Flow) flowConstruct;
                    testSuite.addTest(new MuleTest(before,flow, after));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    
    @Override
    public Description getDescription() {
        return makeDescription(testSuite);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TestListener createAdaptingListener(final RunNotifier notifier) {
        return new OldTestClassAdaptingListener(notifier);
    }
    @Override
    public void run(RunNotifier notifier) {
        TestResult result= new TestResult();
        result.addListener(createAdaptingListener(notifier));
        Flow beforeSuite = lookupFlowConstruct("before.suite");
        Flow afterSuite = lookupFlowConstruct("after.suite");

        try
        {
            if ( beforeSuite != null )
                beforeSuite.process(muleEvent());

            testSuite.run(result);

            if ( afterSuite != null )
                afterSuite.process(muleEvent());
        }
        catch(Exception e)
        {
            throw new RuntimeException("Could not Run the suite", e);
        }

    }

    protected Flow lookupFlowConstruct(String name)
    {
        return (Flow) muleContext.getRegistry().lookupFlowConstruct(name);
    }

    private static Description makeDescription(Test test) {
            
        if (test instanceof TestSuite) {
            TestSuite ts= (TestSuite) test;
            String name= ts.getName() == null ? createSuiteDescription(ts) : ts.getName();
            Description description= Description.createSuiteDescription(name);
            int n= ts.testCount();
            for (int i= 0; i < n; i++) {
                Description made= makeDescription(ts.testAt(i));
                description.addChild(made);
            }
            return description;
        }

        MuleTest mt = (MuleTest) test;
        return Description.createTestDescription(mt.getClass(), mt.getName());
    }

    private static String createSuiteDescription(TestSuite ts) {
        int count= ts.countTestCases();
        String example = count == 0 ? "" : String.format(" [example: %s]", ts.testAt(0));
        return String.format("TestSuite with %s tests%s", count, example);
    }

    protected MuleContext createMuleContext() throws Exception
    {
        // Should we set up the manager for every method?
        MuleContext context;

            MuleContextFactory muleContextFactory = new DefaultMuleContextFactory();
            List<ConfigurationBuilder> builders = new ArrayList<ConfigurationBuilder>();
            builders.add(new SimpleConfigurationBuilder(null));
            if (ClassUtils.isClassOnPath(CLASSNAME_ANNOTATIONS_CONFIG_BUILDER, getClass()))
            {
                builders.add((ConfigurationBuilder) ClassUtils.instanciateClass(CLASSNAME_ANNOTATIONS_CONFIG_BUILDER,
                        ClassUtils.NO_ARGS, getClass()));
            }
            builders.add(getBuilder());
           // addBuilders(builders);
            MuleContextBuilder contextBuilder = new DefaultMuleContextBuilder();
            configureMuleContext(contextBuilder);
            context = muleContextFactory.createMuleContext(builders, contextBuilder);
             ((DefaultMuleConfiguration) context.getConfiguration()).setShutdownTimeout(0);
        return context;
    }


    public void filter(Filter filter) throws NoTestsRemainException {
            TestSuite filtered= new TestSuite(testSuite.getName());
            int n= testSuite.testCount();
            for (int i= 0; i < n; i++) {
                Test test= testSuite.testAt(i);
                if (filter.shouldRun(makeDescription(test)))
                    filtered.addTest(test);
            }
           testSuite = filtered;
    }

    public void sort(Sorter sorter) {
        if (testSuite instanceof Sortable) {
            Sortable adapter= (Sortable) testSuite;
            adapter.sort(sorter);
        }
    }
    protected ConfigurationBuilder getBuilder() throws Exception
    {
        return new SpringXmlConfigurationBuilder(resources);
    }

    protected void configureMuleContext(MuleContextBuilder contextBuilder)
    {
        contextBuilder.setWorkListener(new TestingWorkListener());
    }

    private final class OldTestClassAdaptingListener implements
    TestListener {
        private final RunNotifier fNotifier;

        private OldTestClassAdaptingListener(RunNotifier notifier) {
            fNotifier= notifier;
        }

    public void endTest(Test test) {
        fNotifier.fireTestFinished(asDescription(test));
    }

    public void startTest(Test test) {
        fNotifier.fireTestStarted(asDescription(test));
    }

    // Implement junit.framework.TestListener
    public void addError(Test test, Throwable t) {
        Failure failure= new Failure(asDescription(test), t);
        fNotifier.fireTestFailure(failure);
    }

    private Description asDescription(Test test) {
        if (test instanceof Describable) {
            Describable facade= (Describable) test;
            return facade.getDescription();
        }
        return Description.createTestDescription(getEffectiveClass(test), getName(test));
    }

    private Class<? extends Test> getEffectiveClass(Test test) {
        return test.getClass();
    }

    private String getName(Test test) {
        if (test instanceof TestCase)
            return ((TestCase) test).getName();
        else
            return test.toString();
    }

    public void addFailure(Test test, AssertionFailedError t) {
        addError(test, t);
    }
        

}
    public class MuleTest extends TestCase
    {
        private Flow before;
        Flow flow;
        private Flow after;


        public MuleTest(Flow before, Flow flow, Flow after) {
            this.before = before;
            this.flow = flow;
            this.after = after;
        }

        public String getName()
        {
            return flow.getName();
        }

        @Override
        public int countTestCases() {
            return 1;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void runTest() throws Throwable {
            MuleEvent event = muleEvent();
            if (before != null)
                before.process(event);
            flow.process(event);

            if (after != null )
                after.process(event);
        }

    }

    private MuleEvent muleEvent()  {
        try {
            return MuleTestUtils.getTestEvent(null, MessageExchangePattern.REQUEST_RESPONSE, muleContext);
        } catch (Exception e) {
            return null;
        }
    }
}
