package org.uispec4j.interception.toolkit.empty;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

public class DummyComposite implements Composite {

  public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
    return Empty.NULL_COMPOSITE_CONTEXT;
  }
}