package com.jkamsker.modemsim.validation;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public final class Dom {
    private Dom() {
    }

    public static List<Element> children(Element parent, String name) {
        List<Element> result = new ArrayList<>();
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element element && (name == null || element.getTagName().equals(name))) {
                result.add(element);
            }
        }
        return result;
    }

    public static Element child(Element parent, String name) {
        return children(parent, name).stream().findFirst().orElse(null);
    }

    public static String attr(Element element, String name, String fallback) {
        return element != null && element.hasAttribute(name) ? element.getAttribute(name) : fallback;
    }

    public static boolean boolAttr(Element element, String name, boolean fallback) {
        return Boolean.parseBoolean(attr(element, name, Boolean.toString(fallback)));
    }

    public static int intAttr(Element element, String name, int fallback) {
        String value = attr(element, name, null);
        return value == null || value.isBlank() ? fallback : Integer.parseInt(value);
    }
}
