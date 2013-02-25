package org.mule.munit.runner.mule.context;

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
 * Created with IntelliJ IDEA.
 * User: fernandofederico
 * Date: 2/22/13
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class MunitDocumentLoader extends DefaultDocumentLoader {

    public Document loadDocument(InputSource inputSource, EntityResolver entityResolver,
                                 ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {

        DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);

        DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);

        Document document = builder.newDocument();
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

            attrs.addAttribute("http://myNamespace", "location", "location", "CDATA", location);
            super.startElement(uri, localName, qName, attrs);
        }
    }

}





