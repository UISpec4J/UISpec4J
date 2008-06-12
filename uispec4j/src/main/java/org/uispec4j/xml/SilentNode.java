package org.uispec4j.xml;

import org.xml.sax.Attributes;

public class SilentNode implements Node {
  public static final SilentNode INSTANCE = new SilentNode();

  private SilentNode() {
  }

  public Node getSubNode(String childName, Attributes xmlAttrs) throws RuntimeException {
    return this;
  }

  public void setValue(String value) {
  }

  public void complete() {
  }
}
