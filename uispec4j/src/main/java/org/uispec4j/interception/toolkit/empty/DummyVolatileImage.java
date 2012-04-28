package org.uispec4j.interception.toolkit.empty;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.ImageCapabilities;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;

class DummyVolatileImage extends VolatileImage {
  public static final ImageCapabilities CAPABILITIES = new ImageCapabilities(false);
  public static final BufferedImage IMAGE = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

  public BufferedImage getSnapshot() {
    return IMAGE;
  }

  public int getWidth() {
    return Empty.DEFAULT_HEIGHT;
  }

  public int getHeight() {
    return Empty.DEFAULT_HEIGHT;
  }

  public Graphics2D createGraphics() {
    return Empty.NULL_GRAPHICS_2D;
  }

  public int validate(GraphicsConfiguration gc) {
    return 0;
  }

  public boolean contentsLost() {
    return false;
  }

  public ImageCapabilities getCapabilities() {
    return CAPABILITIES;
  }

  public int getWidth(ImageObserver observer) {
    return Empty.DEFAULT_HEIGHT;
  }

  public int getHeight(ImageObserver observer) {
    return Empty.DEFAULT_HEIGHT;
  }

  public Object getProperty(String name, ImageObserver observer) {
    return "";
  }
}