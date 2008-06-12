package org.uispec4j.xml;

import org.xml.sax.Attributes;

public interface Node {

  public Node getSubNode(String childName,
                         Attributes xmlAttrs);

  public void setValue(String value);

  public void complete();
}
