package org.uispec4j.interception.toolkit.empty;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;

class DummyGraphicsDevice extends GraphicsDevice {
  public static final GraphicsConfiguration[] CONFIGURATION = new GraphicsConfiguration[0];

  public int getType() {
    return 0;
  }

  public GraphicsConfiguration getDefaultConfiguration() {
    return Empty.NULL_GRAPHICS_CONFIGURATION;
  }

  public GraphicsConfiguration[] getConfigurations() {
    return CONFIGURATION;
  }

  public String getIDstring() {
    return "id";
  }
}