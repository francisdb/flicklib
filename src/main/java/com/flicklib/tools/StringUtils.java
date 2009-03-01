package com.flicklib.tools;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.StartTag;

public final class StringUtils {

    private StringUtils() {}

    /**
     * remove brackets from the start and end of the string.
     * 
     * @param text
     * @return
     */
    public static String unbracket(String text) {
        text = text.trim();
        if (text.startsWith("(")) {
            text = text.substring(1);
        }
        if (text.endsWith(")")) {
            text = text.substring(0, text.length() - 1);
        }
        return text.trim();
    }
    
    
    public static boolean isElementAttributeValue(Element element, String name, String value) {
        return value.equals(element.getAttributeValue(name));
    }

    public static boolean isElementAttributeValueContains(Element element, String name, String value) {
        String attrValue = element.getAttributeValue(name);
        if (attrValue!=null) {
            return attrValue.contains(value);
        }
        return false;
    }

    public static boolean isElementAttributeValue(StartTag element, String name, String value) {
        return value.equals(element.getAttributeValue(name));
    }

    public static boolean isElementAttributeValueContains(StartTag element, String name, String value) {
        String attrValue = element.getAttributeValue(name);
        if (attrValue!=null) {
            return attrValue.contains(value);
        }
        return false;
    }
    

}
