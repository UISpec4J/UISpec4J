package org.uispec4j.interception.toolkit;

import java.awt.*;

public class UISpecWindowPeer extends Empty.WindowPeeer {
  private final Window window;

  public UISpecWindowPeer(Window window) {
    this.window = window;
  }

  public void show() {
    if (Window.Type.POPUP.equals(window.getType())) {
      return;
    }
    UISpecDisplay.instance().showWindow(window);
    UISpecDisplay.instance().rethrowIfNeeded();
  }

  public Toolkit getToolkit() {
    return UISpecToolkit.instance();
  }
}
