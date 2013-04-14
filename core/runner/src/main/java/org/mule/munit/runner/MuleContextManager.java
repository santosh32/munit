package org.mule.munit.runner;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationBuilder;
import org.mule.api.context.MuleContextBuilder;
import org.mule.config.DefaultMuleConfiguration;
import org.mule.config.builders.SimpleConfigurationBuilder;
import org.mule.context.DefaultMuleContextBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.munit.common.MunitCore;
import org.mule.munit.runner.mule.context.MockingConfiguration;
import org.mule.munit.runner.mule.context.MunitSpringXmlConfigurationBuilder;
import org.mule.munit.runner.output.DefaultOutputHandler;
import org.mule.tck.TestingWorkListener;
import org.mule.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * <p>Starts and stops mule</p>
 *
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MuleContextManager {

    public static final String CLASSNAME_ANNOTATIONS_CONFIG_BUILDER = "org.mule.org.mule.munit.config.AnnotationsConfigurationBuilder";

    private MockingConfiguration configuration;
    private Properties startupProperties;

    public MuleContextManager(MockingConfiguration configuration, Properties startupProperties) {
        this.configuration = configuration;
        this.startupProperties = startupProperties;
    }

    public MuleContext startMule(String resources) throws Exception {
        MuleContext context = createMule(resources);
        context.start();

        MunitCore.setMuleContext(context);

        return context;
    }

    public void killMule(MuleContext muleContext) {
        try {
            if ( muleContext != null && !muleContext.isStopped() )
            {
                muleContext.stop();
            }
        } catch (MuleException e1) {

        }
        if (muleContext != null && !muleContext.isDisposed() )
        {
            muleContext.dispose();
        }
    }

    private MuleContext createMule(String resources) throws Exception {
        defineLogOutput(resources);

        MuleContext context;
        org.mule.api.context.MuleContextFactory muleContextFactory = new DefaultMuleContextFactory();

        List<ConfigurationBuilder> builders = new ArrayList<ConfigurationBuilder>();
        builders.add(new SimpleConfigurationBuilder(startupProperties));
        if (ClassUtils.isClassOnPath(CLASSNAME_ANNOTATIONS_CONFIG_BUILDER,
                getClass())) {
            builders.add((ConfigurationBuilder) ClassUtils.instanciateClass(
                    CLASSNAME_ANNOTATIONS_CONFIG_BUILDER, ClassUtils.NO_ARGS,
                    getClass()));
        }
        builders.add(getBuilder(resources));
        MuleContextBuilder contextBuilder = new DefaultMuleContextBuilder();
        configureMuleContext(contextBuilder);
        context = muleContextFactory
                .createMuleContext(builders, contextBuilder);
        ((DefaultMuleConfiguration) context.getConfiguration())
                .setShutdownTimeout(0);

        return context;
    }

    private void defineLogOutput(String resources) throws IOException {
        String path = System.getProperty(DefaultOutputHandler.OUTPUT_FOLDER_PROPERTY);
        if ( path != null){
            String name =resources.replace(".xml", "");
            Logger logger = Logger.getRootLogger();
            logger.removeAllAppenders();
            logger.addAppender(new FileAppender(new SimpleLayout(), String.format(path, name)));
            logger.setLevel(Level.INFO);
        }
    }


    protected ConfigurationBuilder getBuilder(String resources) throws Exception {
        return new MunitSpringXmlConfigurationBuilder(resources, configuration);
    }

    protected void configureMuleContext(MuleContextBuilder contextBuilder) {
        contextBuilder.setWorkListener(new TestingWorkListener());
    }

}
