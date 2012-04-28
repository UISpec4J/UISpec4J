package org.uispec4j.interception.toolkit.empty;

import java.awt.image.ColorModel;

class DummyColorModel extends ColorModel {
  public DummyColorModel(int bits) {
    super(bits);
  }

  public DummyColorModel() {
    super(128);
  }

  public int getAlpha(int pixel) {
    return 0;
  }

  public int getBlue(int pixel) {
    return 0;
  }

  public int getGreen(int pixel) {
    return 0;
  }

  public int getRed(int pixel) {
    return 0;
  }
}