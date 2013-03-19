package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.parsers.generic.AutoIdUtils;
import org.mule.munit.AssertModule;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     Assert Module Definition Parser
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class AssertModuleConfigDefinitionParser
        implements BeanDefinitionParser {

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        String name = element.getAttribute("name");
        if ((name == null) || StringUtils.isBlank(name)) {
            element.setAttribute("name", AutoIdUtils.getUniqueName(element, "mule-bean"));
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AssertModule.class.getName());
        if (Initialisable.class.isAssignableFrom(AssertModule.class)) {
            builder.setInitMethodName(Initialisable.PHASE_NAME);
        }
        if (Disposable.class.isAssignableFrom(AssertModule.class)) {
            builder.setDestroyMethodName(Disposable.PHASE_NAME);
        }

        if ( element.hasAttribute("mock-inbounds")){
            builder.addPropertyValue("mockInbounds", Boolean.valueOf(element.getAttribute("mock-inbounds")));
        }

        if ( element.hasAttribute("mock-connectors") ){
            builder.addPropertyValue("mockConnectors", Boolean.valueOf(element.getAttribute("mock-connectors")));
        }

        List<String> flowNames = new ArrayList<String>();
        Element exclusions = DomUtils.getChildElementByTagName(element, "exclude-inbound-mocking");
        if (exclusions != null) {
            List<Element> excludedFlows = DomUtils.getChildElementsByTagName(exclusions, "flow-name");
            if (excludedFlows != null) {
                for (Element excludedFlow : excludedFlows) {
                    Node valueNode = excludedFlow.getFirstChild();
                    if (valueNode != null && valueNode.getNodeValue() != null) {
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
