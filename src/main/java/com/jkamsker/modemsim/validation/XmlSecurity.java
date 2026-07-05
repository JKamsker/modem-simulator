package com.jkamsker.modemsim.validation;

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
import java.util.regex.Pattern;

public final class XmlSecurity {
    private static final long MAX_BYTES = 1_000_000;
    private static final int MAX_DEPTH = 64;
    private static final int MAX_TEXT_LENGTH = 50_000;
    private static final Pattern SCHEMA_LOCATION_ATTR = Pattern.compile(
            "(?is)schemaLocation\\s*=\\s*(['\"])([^'\"]*)\\1");

    private XmlSecurity() {
    }

    public static Document parse(Path xmlPath) {
        try {
            if (Files.size(xmlPath) > MAX_BYTES) {
                throw new ValidationException("XML file exceeds maximum size: " + xmlPath);
            }
            rejectExternalSchemaReferenceText(xmlPath);
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
        validate(parse(xmlPath), schemaPath);
    }

    public static Document parseValidated(Path xmlPath, Path schemaPath) {
        Document document = parse(xmlPath);
        validate(document, schemaPath);
        return document;
    }

    private static void validate(Document document, Path schemaPath) {
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
        rejectXInclude(node);
        rejectExternalSchemaReference(node);
        String text = node.getNodeValue();
        if (text != null && text.length() > MAX_TEXT_LENGTH) {
            throw new ValidationException("XML text exceeds maximum length");
        }
        checkAttributeLengths(node);
        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            checkDepth(child, depth + 1);
        }
    }

    private static void checkAttributeLengths(Node node) {
        if (!node.hasAttributes()) {
            return;
        }
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            if (node.getAttributes().item(i).getNodeValue().length() > MAX_TEXT_LENGTH) {
                throw new ValidationException("XML attribute exceeds maximum length");
            }
        }
    }

    private static void rejectXInclude(Node node) {
        String name = node.getNodeName();
        if ("http://www.w3.org/2001/XInclude".equals(node.getNamespaceURI())
                || "xi:include".equals(name) || name.endsWith(":include")) {
            throw new ValidationException("XML XInclude is not allowed");
        }
    }

    private static void rejectExternalSchemaReference(Node node) {
        if (!node.hasAttributes()) {
            return;
        }
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            Node attribute = node.getAttributes().item(i);
            if (attribute.getNodeName().endsWith("schemaLocation") && !attribute.getNodeValue().isBlank()) {
                throw new ValidationException("XML external schema references are not allowed");
            }
        }
    }

    private static void rejectExternalSchemaReferenceText(Path xmlPath) throws IOException {
        String content = Files.readString(xmlPath);
        var matcher = SCHEMA_LOCATION_ATTR.matcher(content);
        while (matcher.find()) {
            if (!matcher.group(2).isBlank()) {
                throw new ValidationException("XML external schema references are not allowed");
            }
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
