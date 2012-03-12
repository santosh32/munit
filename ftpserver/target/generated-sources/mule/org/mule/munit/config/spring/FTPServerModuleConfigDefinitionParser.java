
package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.parsers.generic.AutoIdUtils;
import org.mule.munit.config.FTPServerModuleLifecycleAdapter;
import org.mule.util.TemplateParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class FTPServerModuleConfigDefinitionParser
    implements BeanDefinitionParser
{

    /**
     * Mule Pattern Info
     * 
     */
    private TemplateParser.PatternInfo patternInfo;

    public FTPServerModuleConfigDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        String name = element.getAttribute("name");
        if ((name == null)||StringUtils.isBlank(name)) {
            element.setAttribute("name", AutoIdUtils.getUniqueName(element, "mule-bean"));
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(FTPServerModuleLifecycleAdapter.class.getName());
        if (Initialisable.class.isAssignableFrom(FTPServerModuleLifecycleAdapter.class)) {
            builder.setInitMethodName(Initialisable.PHASE_NAME);
        }
        if (Disposable.class.isAssignableFrom(FTPServerModuleLifecycleAdapter.class)) {
            builder.setDestroyMethodName(Disposable.PHASE_NAME);
        }
        if ((element.getAttribute("port")!= null)&&(!StringUtils.isBlank(element.getAttribute("port")))) {
            builder.addPropertyValue("port", element.getAttribute("port"));
        }
        if ((element.getAttribute("secure")!= null)&&(!StringUtils.isBlank(element.getAttribute("secure")))) {
            builder.addPropertyValue("secure", element.getAttribute("secure"));
        }
        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        return definition;
    }

}
