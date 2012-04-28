package org.uispec4j.interception.toolkit.empty;

import java.awt.PaintContext;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

public class DummyPaintContext implements PaintContext {

  public void dispose() {
  }

  public ColorModel getColorModel() {
    return Empty.NULL_COLOR_MODEL;
  }

  public Raster getRaster(int x, int y, int w, int h) {
    return Raster.createBandedRaster(0, 0, 0, 0, Empty.NULL_POINT);
  }
}