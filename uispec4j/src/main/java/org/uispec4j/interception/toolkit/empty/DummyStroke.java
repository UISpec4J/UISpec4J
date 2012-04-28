package org.uispec4j.interception.toolkit.empty;

import java.awt.Shape;
import java.awt.Stroke;

public class DummyStroke implements Stroke {
  public Shape createStrokedShape(Shape p) {
    return Empty.NULL_RECTANGLE;
  }
}