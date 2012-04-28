package org.uispec4j.interception.toolkit.empty;

import java.awt.CompositeContext;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class DummyCompositeContext implements CompositeContext {

  public void dispose() {
  }

  public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
  }
}