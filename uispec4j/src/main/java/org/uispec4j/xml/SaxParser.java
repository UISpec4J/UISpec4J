package org.uispec4j.xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.Reader;
import java.util.Stack;

class SaxParser extends DefaultHandler {

  private Stack nodesStack = new Stack();
  private SAXParser parser;
  private StringBuffer charBuffer;

  public SaxParser() {
    try {
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      saxParserFactory.setNamespaceAware(true);
      parser = saxParserFactory.newSAXParser();
    }
    catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
    catch (SAXException e) {
      throw new RuntimeException(e);
    }
    charBuffer = new StringBuffer();
  }

  public void parse(Node rootNode, Reader reader) throws RuntimeException {
    nodesStack.clear();
    nodesStack.push(rootNode);
    try {
      parser.parse(new InputSource(reader), this);
      rootNode.complete();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void startElement(String uri, String local, String qName, Attributes atts) {
    charBuffer.setLength(0);
    Node currentNode = (Node)nodesStack.peek();
    Node child;
    try {
      child = currentNode.getSubNode(local, atts);
    }
    catch (RuntimeException e) {
      child = SilentNode.INSTANCE;
    }
    if (child == null) {
      throw new NullPointerException();
    }
    else {
      nodesStack.push(child);
    }
  }

  public void characters(char[] chars, int start, int length) throws SAXException {
    charBuffer.append(chars, start, length);
  }

  public void endElement(String uri, String localName, String qName) throws SAXException {
    Node previousNode = ((Node)nodesStack.peek());
    previousNode.setValue(charBuffer.toString());
    charBuffer.setLength(0);
    Node node = (Node)nodesStack.pop();
    node.complete();
  }
}