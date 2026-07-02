package com.alegs3.modemsim.validation;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class XmlSecurity {
    private static final long MAX_BYTES = 1_000_000;
    private static final int MAX_DEPTH = 64;
    private static final int MAX_TEXT_LENGTH = 50_000;

    private XmlSecurity() {
    }

    public static Document parse(Path xmlPath) {
        try {
            if (Files.size(xmlPath) > MAX_BYTES) {
                throw new ValidationException("XML file exceeds maximum size: " + xmlPath);
            }
            DocumentBuilderFactory factory = hardenedDocumentBuilderFactory();
            var builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new SilentErrorHandler());
            Document document = builder.parse(xmlPath.toFile());
            checkDepth(document, 0);
            return document;
        } catch (SAXException | IOException e) {
            throw new ValidationException("XML parse rejected: " + e.getMessage(), e);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationException("Unable to parse XML safely", e);
        }
    }

    public static void validate(Path xmlPath, Path schemaPath) {
        Document document = parse(xmlPath);
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            var schema = factory.newSchema(schemaPath.toFile());
            var validator = schema.newValidator();
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validator.validate(new DOMSource(document));
        } catch (Exception e) {
            throw new ValidationException("XML schema validation failed: " + e.getMessage(), e);
        }
    }

    private static DocumentBuilderFactory hardenedDocumentBuilderFactory() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        factory.setNamespaceAware(false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return factory;
    }

    private static void checkDepth(Node node, int depth) {
        if (depth > MAX_DEPTH) {
            throw new ValidationException("XML tree exceeds maximum depth");
        }
        String text = node.getNodeValue();
        if (text != null && text.length() > MAX_TEXT_LENGTH) {
            throw new ValidationException("XML text exceeds maximum length");
        }
        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            checkDepth(child, depth + 1);
        }
    }

    private static final class SilentErrorHandler implements ErrorHandler {
        @Override
        public void warning(SAXParseException exception) throws SAXException {
            throw exception;
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            throw exception;
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw exception;
        }
    }
}
