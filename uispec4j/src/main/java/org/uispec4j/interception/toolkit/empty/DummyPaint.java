package org.uispec4j.interception.toolkit.empty;

import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public class DummyPaint implements Paint {

  public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
    return Empty.NULL_PAINT_CONTEXT;
  }

  public int getTransparency() {
    return 0;
  }
}