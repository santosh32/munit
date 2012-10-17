package org.mule.munit.runner.mule.context;

import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.util.SpringXMLUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class MunitBeanDefinitionParserDelegate extends MuleHierarchicalBeanDefinitionParserDelegate {
    public MunitBeanDefinitionParserDelegate(XmlReaderContext readerContext, DefaultBeanDefinitionDocumentReader spring) {
        super(readerContext, spring);
    }

    public BeanDefinition parseCustomElement(Element element, BeanDefinition parent)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("parsing: " + SpringXMLUtils.elementToString(element));
        }
        if (SpringXMLUtils.isBeansNamespace(element))
        {
            return handleSpringElements(element, parent);
        }
        else
        {
            String namespaceUri = element.getNamespaceURI();
            NamespaceHandlerResolver namespaceHandlerResolver = getReaderContext().getNamespaceHandlerResolver();
            NamespaceHandler handler = namespaceHandlerResolver.resolve(namespaceUri);
            if (handler == null)
            {
                getReaderContext().error("Unable to locate NamespaceHandler for namespace [" + namespaceUri + "]", element);
                return null;
            }

            boolean noRecurse = false;
            boolean forceRecurse = false;
            BeanDefinition finalChild;

            do {
                ParserContext parserContext = new ParserContext(getReaderContext(), this, parent);
                finalChild = new MunitHandlerWrapper(handler).parse(element, parserContext);
                registerBean(element, finalChild);
                if ( finalChild != null && finalChild.getPropertyValues().getPropertyValue("realMp") != null ){
                    registerBean(element, (BeanDefinition) finalChild.getPropertyValues().getPropertyValue("realMp").getValue());
                }
                noRecurse = noRecurse || testFlag(finalChild, MULE_NO_RECURSE);
                forceRecurse = forceRecurse || testFlag(finalChild, MULE_FORCE_RECURSE);
            } while (null != finalChild && testFlag(finalChild, MULE_REPEAT_PARSE));



            boolean isRecurse;
            if (noRecurse)
            {
                // no recursion takes precedence, as recursion is set by default
                isRecurse = false;
            }
            else
            {
                if (forceRecurse)
                {
                    isRecurse = true;
                }
                else
                {
                    // default behaviour if no control specified
                    isRecurse = SpringXMLUtils.isMuleNamespace(element);
                }
            }

            if (isRecurse)
            {
                NodeList list = element.getChildNodes();
                for (int i = 0; i < list.getLength(); i++)
                {
                    if (list.item(i) instanceof Element)
                    {
                        parseCustomElement((Element) list.item(i), finalChild);
                    }
                }
            }

            // If a parser requests post-processing we call again after children called

            if (testFlag(finalChild, MULE_POST_CHILDREN))
            {
                ParserContext parserContext = new ParserContext(getReaderContext(), this, parent);
                finalChild = handler.parse(element, parserContext);
            }

            return finalChild;
        }
    }

}
