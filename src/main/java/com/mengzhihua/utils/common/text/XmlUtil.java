package com.mengzhihua.utils.common.text;


import com.mengzhihua.utils.common.lang.AssertUtil;

/**
 * XML escape / unescape for embedding text in XML documents.
 */
public final class XmlUtil {

    private XmlUtil() {
    }

    public static String escape(String xml) {
        if (xml == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(xml.length());
        for (int i = 0; i < xml.length(); i++) {
            char c = xml.charAt(i);
            switch (c) {
                case '<' -> builder.append("&lt;");
                case '>' -> builder.append("&gt;");
                case '&' -> builder.append("&amp;");
                case '"' -> builder.append("&quot;");
                case '\'' -> builder.append("&apos;");
                default -> builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String unescape(String xml) {
        if (xml == null) {
            return null;
        }
        return xml.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&apos;", "'")
                .replace("&amp;", "&");
    }

    public static String wrap(String tag, String text) {
        AssertUtil.notBlank(tag, "tag must not be blank");
        return "<" + tag + ">" + escape(text == null ? "" : text) + "</" + tag + ">";
    }

    public static String pretty(String xml) {
        if (xml == null) {
            return null;
        }
        try {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setExpandEntityReferences(false);
            org.w3c.dom.Document document = factory.newDocumentBuilder()
                    .parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
            document.setXmlStandalone(true);
            javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            java.io.StringWriter writer = new java.io.StringWriter();
            transformer.transform(new javax.xml.transform.dom.DOMSource(document),
                    new javax.xml.transform.stream.StreamResult(writer));
            return writer.toString().trim();
        } catch (Exception ex) {
            throw new IllegalArgumentException("invalid xml", ex);
        }
    }

    public static String xpath(String xml, String expression) {
        AssertUtil.notBlank(xml, "xml must not be blank");
        AssertUtil.notBlank(expression, "xpath must not be blank");
        try {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setExpandEntityReferences(false);
            org.w3c.dom.Document document = factory.newDocumentBuilder()
                    .parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
            javax.xml.xpath.XPath xpath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
            return xpath.evaluate(expression, document);
        } catch (Exception ex) {
            throw new IllegalArgumentException("xpath failed", ex);
        }
    }
}
