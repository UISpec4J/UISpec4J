package org.uispec4j.interception.toolkit.empty;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

class DummyImage extends Image {

  public void flush() {
  }

  public Graphics getGraphics() {
    return Empty.NULL_GRAPHICS_2D;
  }

  public int getHeight(ImageObserver observer) {
    return Empty.DEFAULT_HEIGHT;
  }

  public int getWidth(ImageObserver observer) {
    return Empty.DEFAULT_HEIGHT;
  }

  public ImageProducer getSource() {
    return Empty.NULL_IMAGE_PRODUCER;
  }

  public Object getProperty(String name, ImageObserver observer) {
    return "";
  }
}