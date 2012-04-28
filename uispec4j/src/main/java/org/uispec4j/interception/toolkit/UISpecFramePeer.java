package org.uispec4j.interception.toolkit;

import java.awt.*;

import org.uispec4j.interception.toolkit.empty.FramePeer;

public class UISpecFramePeer extends FramePeer {
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
