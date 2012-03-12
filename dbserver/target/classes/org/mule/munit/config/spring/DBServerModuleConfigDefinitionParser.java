
package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.parsers.generic.AutoIdUtils;
import org.mule.munit.config.DBServerModuleLifecycleAdapter;
import org.mule.util.TemplateParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class DBServerModuleConfigDefinitionParser
    implements BeanDefinitionParser
{

    /**
     * Mule Pattern Info
     * 
     */
    private TemplateParser.PatternInfo patternInfo;

    public DBServerModuleConfigDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        String name = element.getAttribute("name");
        if ((name == null)||StringUtils.isBlank(name)) {
            element.setAttribute("name", AutoIdUtils.getUniqueName(element, "mule-bean"));
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DBServerModuleLifecycleAdapter.class.getName());
        if (Initialisable.class.isAssignableFrom(DBServerModuleLifecycleAdapter.class)) {
            builder.setInitMethodName(Initialisable.PHASE_NAME);
        }
        if (Disposable.class.isAssignableFrom(DBServerModuleLifecycleAdapter.class)) {
            builder.setDestroyMethodName(Disposable.PHASE_NAME);
        }
        if ((element.getAttribute("jdbcUrl")!= null)&&(!StringUtils.isBlank(element.getAttribute("jdbcUrl")))) {
            builder.addPropertyValue("jdbcUrl", element.getAttribute("jdbcUrl"));
        }
        if ((element.getAttribute("creationalScript")!= null)&&(!StringUtils.isBlank(element.getAttribute("creationalScript")))) {
            builder.addPropertyValue("creationalScript", element.getAttribute("creationalScript"));
        }
        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        return definition;
    }

}
