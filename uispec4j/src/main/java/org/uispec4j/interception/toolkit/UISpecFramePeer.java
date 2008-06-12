package org.uispec4j.interception.toolkit;

import java.awt.*;

public class UISpecFramePeer extends Empty.FramePeer {
  private Frame frame;

  public UISpecFramePeer(Frame frame) {
    this.frame = frame;
  }

  public void show() {
    UISpecDisplay.instance().showFrame(frame);
    UISpecDisplay.instance().rethrowIfNeeded();
  }

  public Toolkit getToolkit() {
    return UISpecToolkit.instance();
  }
}
