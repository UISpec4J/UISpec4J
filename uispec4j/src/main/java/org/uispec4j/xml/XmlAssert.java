package org.uispec4j.xml;

import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.xml.sax.Attributes;

import java.io.Reader;
import java.io.StringReader;
import java.util.*;

public class XmlAssert {

  private XmlAssert() {
    // Static class
  }

  public static void assertEquivalent(String xmlA, String xmlB) {
    ComparableNode aNode = createXmlEquivalentComparableNode(new StringReader(xmlA));
    ComparableNode bNode = createXmlEquivalentComparableNode(new StringReader(xmlB));
    if (!aNode.equals(bNode)) {
      AssertAdapter.assertEquals(prepareXmlString(xmlA), prepareXmlString(xmlB));
    }
  }

  private static String prepareXmlString(String input) {
    return input.replaceAll("[ ]+<", "<").replaceAll("\n", "").replaceAll("><", ">\n<").replace('\'', '"');
  }

  public static void assertEquals(String xmlA, String xmlB) {
    ComparableNode aNode = createXmlEqualComparableNode(new StringReader(xmlA));
    ComparableNode bNode = createXmlEqualComparableNode(new StringReader(xmlB));
    if (!aNode.equals(bNode)) {
      AssertAdapter.assertEquals(prepareXmlString(xmlA), prepareXmlString(xmlB));
    }
  }

  private static ComparableNode createXmlEqualComparableNode(Reader reader) {
    EqualComparator comparableNode = new EqualComparator();
    new SaxParser().parse(comparableNode, reader);
    return comparableNode;
  }

  private static ComparableNode createXmlEquivalentComparableNode(Reader reader) {
    EquivalentComparator comparableNode = new EquivalentComparator();
    new SaxParser().parse(comparableNode, reader);
    return comparableNode;
  }

  static abstract class ComparableNode implements Node {
    String tag = "";
    Map attributes;
    Map childrenOccurences = new HashMap();
    List children = new ArrayList();
    String text = "";

    protected abstract boolean comparator(Object o);

    public ComparableNode(String tag, Attributes attributes) {
      this.tag = tag;
      this.attributes = map(attributes);
    }

    public String toString() {
      if ("root".equals(tag)) {
        return childrenString();
      }
      else if ((!childrenOccurences.isEmpty()) || (text.length() > 0)) {
        return "<" + tag + attributeString() + ">" + childrenString() + text + "</" + tag + ">";
      }
      else {
        return "<" + tag + attributeString() + "/>";
      }
    }

    public void setValue(String value) {
      text = value.trim();
    }

    public void complete() {
    }

    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof ComparableNode)) {
        return false;
      }
      final ComparableNode comparableXml = (ComparableNode)o;

      if (attributes != null ? !attributes.equals(comparableXml.attributes) : comparableXml.attributes != null) {
        return false;
      }
      if (tag != null ? !tag.equals(comparableXml.tag) : comparableXml.tag != null) {
        return false;
      }
      if (text != null ? !text.equals(comparableXml.text) : comparableXml.text != null) {
        return false;
      }

      return comparator(o);
    }

    public int hashCode() {
      int result = tag.hashCode();
      result = 29 * result + attributes.hashCode();
      return result;
    }

    private Map map(Attributes xmlAttrs) {
      Map map = new HashMap();
      if (xmlAttrs == null) {
        return map;
      }
      for (int i = 0; i < xmlAttrs.getLength(); i++) {
        String localName = xmlAttrs.getLocalName(i);
        map.put(localName, xmlAttrs.getValue(i));
      }
      return map;
    }

    void addChild(Object child) {
      int val = 1;
      if (childrenOccurences.containsKey(child)) {
        Object occurence = childrenOccurences.get(child);
        if (occurence != null) {
          val = Integer.valueOf((String)occurence).intValue() + 1;
        }
      }
      childrenOccurences.put(child, Integer.toString(val));
      children.add(child);
    }

    private String childrenString() {
      StringBuffer sb = new StringBuffer();
      for (Iterator iterator = childrenOccurences.keySet().iterator(); iterator.hasNext();) {
        ComparableNode node = (ComparableNode)iterator.next();
        sb.append(node);
        sb.append("\n");
      }
      return sb.toString();
    }

    private String attributeString() {
      StringBuffer sb = new StringBuffer();
      for (Iterator iterator = attributes.entrySet().iterator(); iterator.hasNext();) {
        Map.Entry e = (Map.Entry)iterator.next();
        sb.append(" " + e.getKey() + "=\"" + e.getValue() + "\"");
      }
      return sb.toString();
    }
  }

  static class EquivalentComparator extends ComparableNode {
    public EquivalentComparator(String tag, Attributes attributes) {
      super(tag, attributes);
    }

    public EquivalentComparator() {
      super("root", null);
    }

    public Node getSubNode(String childName, Attributes xmlAttrs) throws RuntimeException {
      ComparableNode o = new EquivalentComparator(childName, xmlAttrs);
      addChild(o);
      return o;
    }

    protected boolean comparator(Object o) {
      final ComparableNode comparableXml = (ComparableNode)o;
      if (childrenOccurences != null ? !childrenOccurences.equals(comparableXml.childrenOccurences) : comparableXml.childrenOccurences != null) {
        return false;
      }
      return true;
    }
  }

  static class EqualComparator extends ComparableNode {
    public EqualComparator(String tag, Attributes attributes) {
      super(tag, attributes);
    }

    public EqualComparator() {
      super("root", null);
    }

    public Node getSubNode(String childName, Attributes xmlAttrs) throws RuntimeException {
      ComparableNode o = new EqualComparator(childName, xmlAttrs);
      addChild(o);
      return o;
    }

    protected boolean comparator(Object o) {
      final ComparableNode comparableXml = (ComparableNode)o;
      if (children != null ? !children.equals(comparableXml.children) : comparableXml.children != null) {
        return false;
      }
      return true;
    }
  }
}
