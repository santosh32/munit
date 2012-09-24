package org.mule.munit.test;

import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationBuilder;
import org.mule.api.context.MuleContextBuilder;
import org.mule.api.context.MuleContextFactory;
import org.mule.config.DefaultMuleConfiguration;
import org.mule.config.builders.SimpleConfigurationBuilder;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.context.DefaultMuleContextBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.munit.config.*;
import org.mule.munit.test.result.SuiteResult;
import org.mule.munit.test.result.notification.NotificationListener;
import org.mule.tck.MuleTestUtils;
import org.mule.tck.TestingWorkListener;
import org.mule.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MunitTestRunner {
	public static final String CLASSNAME_ANNOTATIONS_CONFIG_BUILDER = "org.mule.org.mule.munit.config.AnnotationsConfigurationBuilder";

	private String resources;
	private MuleContext muleContext;
	private Suite suite;

	public MunitTestRunner(String resources) {
		this.resources = resources;

		try {
			muleContext = this.createMuleContext();
			muleContext.start();

			suite = new Suite(resources);

			List<MunitFlow> before = lookupFlows(MunitBeforeTest.class);
			List<MunitFlow> after = lookupFlows(MunitAfterTest.class);
			Collection<MunitTest> flowConstructs = lookupTests();
			for (MunitTest flowConstruct : flowConstructs) {
                 if ( !flowConstruct.isIgnore() ){
                     suite.add(new Test(before, flowConstruct, after));
                 }
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

    private Collection<MunitTest> lookupTests() {
        return new ArrayList<MunitTest>(muleContext.getRegistry()
                .lookupObjects(MunitTest.class));
    }

    protected MuleContext createMuleContext() throws Exception {
		// Should we set up the manager for every method?
		MuleContext context;

		MuleContextFactory muleContextFactory = new DefaultMuleContextFactory();
		List<ConfigurationBuilder> builders = new ArrayList<ConfigurationBuilder>();
		builders.add(new SimpleConfigurationBuilder(null));
		if (ClassUtils.isClassOnPath(CLASSNAME_ANNOTATIONS_CONFIG_BUILDER,
				getClass())) {
			builders.add((ConfigurationBuilder) ClassUtils.instanciateClass(
					CLASSNAME_ANNOTATIONS_CONFIG_BUILDER, ClassUtils.NO_ARGS,
					getClass()));
		}
		builders.add(getBuilder());
		// addBuilders(builders);
		MuleContextBuilder contextBuilder = new DefaultMuleContextBuilder();
		configureMuleContext(contextBuilder);
		context = muleContextFactory
				.createMuleContext(builders, contextBuilder);
		((DefaultMuleConfiguration) context.getConfiguration())
				.setShutdownTimeout(0);
		return context;
	}

	protected ConfigurationBuilder getBuilder() throws Exception {
		return new SpringXmlConfigurationBuilder(resources);
	}

	protected void configureMuleContext(MuleContextBuilder contextBuilder) {
		contextBuilder.setWorkListener(new TestingWorkListener());
	}

	private List<MunitFlow> lookupFlows(Class munitClass) {
		return new ArrayList<MunitFlow>(muleContext.getRegistry()
				.lookupObjects(munitClass));
	}

	public SuiteResult run() {
		try {
			process(lookupFlows(MunitBeforeSuite.class), muleEvent());

			SuiteResult result = suite.run();

			process(lookupFlows(MunitAfterSuite.class), muleEvent());

			muleContext.stop();
			muleContext.dispose();

			return result;

		} catch (Exception e) {
			throw new RuntimeException("Could not Run the suite", e);
		}

	}
	

	public void setNotificationListener(NotificationListener notificationListener) {
		this.suite.setNotificationListener(notificationListener);
	}

	private MuleEvent muleEvent() {
		try {
			return MuleTestUtils.getTestEvent(null,
					MessageExchangePattern.REQUEST_RESPONSE, muleContext);
		} catch (Exception e) {
			return null;
		}
	}

	private void process(Collection<MunitFlow> flowConstructs, MuleEvent event)
			throws MuleException {
		for (MunitFlow flowConstruct : flowConstructs) {
			System.out.printf("%n" + flowConstruct.getDescription() + "%n");
			(flowConstruct).process(event);
		}
	}

	public int getNumberOfTests() {
		return suite.getNumberOfTests();
	}

}
