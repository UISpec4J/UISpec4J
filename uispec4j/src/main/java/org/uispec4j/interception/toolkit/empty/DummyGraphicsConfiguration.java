package org.uispec4j.interception.toolkit.empty;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.VolatileImage;

import sun.awt.image.SunVolatileImage;

class DummyGraphicsConfiguration extends GraphicsConfiguration {
  public BufferedImage createCompatibleImage(int width, int height) {
    return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  public BufferedImage createCompatibleImage(int width, int height, int transparency) {
    return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  public VolatileImage createCompatibleVolatileImage(int width, int height) {
    return new SunVolatileImage(this, width, height, 0, getImageCapabilities());
  }

  public VolatileImage createCompatibleVolatileImage(int width, int height, int transparency) {
    return Empty.NULL_VOLATILE_IMAGE;
  }

  public Rectangle getBounds() {
    return Empty.NULL_RECTANGLE;
  }

  public ColorModel getColorModel() {
    return Empty.NULL_COLOR_MODEL;
  }

  public ColorModel getColorModel(int transparency) {
    return Empty.NULL_COLOR_MODEL;
  }

  public AffineTransform getDefaultTransform() {
    return Empty.NULL_AFFINE_TRANSFORM;
  }

  public GraphicsDevice getDevice() {
    return Empty.NULL_GRAPHICS_DEVICE;
  }

  public AffineTransform getNormalizingTransform() {
    return Empty.NULL_AFFINE_TRANSFORM;
  }
}