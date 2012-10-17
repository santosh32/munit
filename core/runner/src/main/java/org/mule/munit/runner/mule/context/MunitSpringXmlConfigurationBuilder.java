package org.mule.munit.runner.mule.context;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.config.ConfigResource;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.springframework.context.ApplicationContext;

public class MunitSpringXmlConfigurationBuilder extends SpringXmlConfigurationBuilder{


    public MunitSpringXmlConfigurationBuilder(String configResources) throws ConfigurationException {
        super(configResources);
    }

    @Override
    protected ApplicationContext createApplicationContext(MuleContext muleContext, ConfigResource[] configResources) throws Exception {
        return new MunitApplicationContext( muleContext, configResources);
    }
}
