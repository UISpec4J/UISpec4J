package org.uispec4j.interception.toolkit.empty;

import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;

public class DummyImageProducer implements ImageProducer {
  public void addConsumer(ImageConsumer ic) {
  }

  public boolean isConsumer(ImageConsumer ic) {
    return false;
  }

  public void removeConsumer(ImageConsumer ic) {
  }

  public void requestTopDownLeftRightResend(ImageConsumer ic) {
  }

  public void startProduction(ImageConsumer ic) {
  }
}