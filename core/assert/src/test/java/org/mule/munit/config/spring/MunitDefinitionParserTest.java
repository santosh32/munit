package org.mule.munit.config.spring;

import org.junit.Before;
import org.junit.Test;
import org.mule.munit.config.AssertOnEqualsMessageProcessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MunitDefinitionParserTest {

    public static final String A_MESSAGE = "A Message";
    public static final String EXPRESSION = "#[expression]";
    public static final String NON_EXPRESSION = "Non expression";
    Element element;

    @Before
    public void setUp(){
        element = mock(Element.class);
    }

    @Test
    public void test(){
        MunitDefinitionParser parser = new MunitDefinitionParser(AssertOnEqualsMessageProcessor.class, asList("message"), asList("expected", "value"));

        when(element.getAttribute("message")).thenReturn(A_MESSAGE);
        when(element.getAttribute("expected-ref")).thenReturn(NON_EXPRESSION);
        when(element.getAttribute("value-ref")).thenReturn(EXPRESSION);

        BeanDefinition beanDefinition = parser.parse(element, null);

        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        assertNotNull(beanDefinition);
        assertEquals(AssertOnEqualsMessageProcessor.class.getName(), beanDefinition.getBeanClassName());
        assertEquals(A_MESSAGE, propertyValues.getPropertyValue("message").getValue());
        assertEquals(EXPRESSION, propertyValues.getPropertyValue("value").getValue());
        assertEquals("#[registry:" + NON_EXPRESSION + "]", propertyValues.getPropertyValue("expected").getValue());

    }

    private static List<String> asList(String ... attrs){
        return Arrays.asList(attrs);
    }
}
