package org.mule.munit.runner.mule.context;

import org.mule.munit.common.MunitCore;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.*;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;

/**
 * <p>
 *     We change the document loader in order to get the line numbers as element attribute.
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.4
 */
public class MunitDocumentLoader extends DefaultDocumentLoader {

    public Document loadDocument(InputSource inputSource, EntityResolver entityResolver,
                                 ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {

        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        LocationFilter locationFilter = new LocationFilter(xmlReader);
        SAXSource saxSource = new SAXSource(locationFilter, inputSource);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMResult domResult = new DOMResult();
        transformer.transform(saxSource, domResult);

        return (Document)domResult.getNode();
    }

    class LocationFilter extends XMLFilterImpl {

        public static final String NAMESPACE = "http://www.mule.org/munit";

        LocationFilter(XMLReader xmlReader) {
            super(xmlReader);
        }

        private Locator locator = null;

        @Override
        public void setDocumentLocator(Locator locator) {
            super.setDocumentLocator(locator);
            this.locator = locator;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            // Add extra attribute to elements to hold location
            String location = String.valueOf(locator.getLineNumber());
            Attributes2Impl attrs = new Attributes2Impl(attributes);

            attrs.addAttribute(NAMESPACE, MunitCore.LINE_NUMBER_ELEMENT_ATTRIBUTE, MunitCore.LINE_NUMBER_ELEMENT_ATTRIBUTE, "CDATA", location);
            super.startElement(uri, localName, qName, attrs);
        }
    }

}





