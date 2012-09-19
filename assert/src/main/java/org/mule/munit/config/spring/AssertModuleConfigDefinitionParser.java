
package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.parsers.generic.AutoIdUtils;
import org.mule.munit.config.AssertModuleLifecycleAdapter;
import org.mule.util.TemplateParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class AssertModuleConfigDefinitionParser
    implements BeanDefinitionParser
{

    /**
     * Mule Pattern Info
     * 
     */
    private TemplateParser.PatternInfo patternInfo;

    public AssertModuleConfigDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        String name = element.getAttribute("name");
        if ((name == null)||StringUtils.isBlank(name)) {
            element.setAttribute("name", AutoIdUtils.getUniqueName(element, "mule-bean"));
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AssertModuleLifecycleAdapter.class.getName());
        if (Initialisable.class.isAssignableFrom(AssertModuleLifecycleAdapter.class)) {
            builder.setInitMethodName(Initialisable.PHASE_NAME);
        }
        if (Disposable.class.isAssignableFrom(AssertModuleLifecycleAdapter.class)) {
            builder.setDestroyMethodName(Disposable.PHASE_NAME);
        }
        
        String integration = element.getAttribute("mock-inbounds");
        if ( Boolean.valueOf(integration) ){
            builder.addPropertyValue("mockInbounds", Boolean.TRUE);
        }

        List<String> flowNames = new ArrayList<String>();
        Element exclusions = DomUtils.getChildElementByTagName(element, "exclude-inbound-mocking");
        if ( exclusions != null ){
            List<Element> excludedFlows = DomUtils.getChildElementsByTagName(exclusions, "flow-name");
            if ( excludedFlows != null ){
                for ( Element excludedFlow : excludedFlows ){
                    Node valueNode = excludedFlow.getFirstChild();
                    if ( valueNode != null && valueNode.getNodeValue() != null ){
                        flowNames.add(valueNode.getNodeValue());
                    }

                }

            }
        }

        builder.addPropertyValue("mockingExcludedFlows", flowNames);

        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        return definition;
    }

}
