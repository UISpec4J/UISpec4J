package org.uispec4j.interception.toolkit;

import java.awt.*;

public class UISpecWindowPeer extends Empty.WindowPeeer {
  Window window;

  public UISpecWindowPeer(Window window) {
    this.window = window;
  }

  public void show() {
    UISpecDisplay.instance().showWindow(window);
    UISpecDisplay.instance().rethrowIfNeeded();
  }

  public Toolkit getToolkit() {
    return UISpecToolkit.instance();
  }
}
