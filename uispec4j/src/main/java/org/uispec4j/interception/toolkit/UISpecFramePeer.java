package org.uispec4j.interception.toolkit;

import java.awt.*;

public class UISpecFramePeer extends Empty.FramePeer {
  private Toolkit toolkit;
  private Frame frame;

  public UISpecFramePeer(Toolkit toolkit, Frame frame) {
    this.toolkit = toolkit;
    this.frame = frame;
  }

  public void show() {
    UISpecDisplay.instance().showFrame(frame);
    UISpecDisplay.instance().rethrowIfNeeded();
  }

  public Toolkit getToolkit() {
    return toolkit;
  }
}
