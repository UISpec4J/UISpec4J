package org.uispec4j.interception.toolkit;

import java.awt.*;

import org.uispec4j.interception.toolkit.empty.WindowPeeer;

public class UISpecWindowPeer extends WindowPeeer {
  private Toolkit toolkit;
  Window window;

  public UISpecWindowPeer(Toolkit toolkit, Window window) {
    this.toolkit = toolkit;
    this.window = window;
  }

  public void show() {
    UISpecDisplay.instance().showWindow(window);
    UISpecDisplay.instance().rethrowIfNeeded();
  }

  public Toolkit getToolkit() {
    return toolkit;
  }
}
