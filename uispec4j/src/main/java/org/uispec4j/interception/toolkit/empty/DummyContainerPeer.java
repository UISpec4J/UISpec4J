package org.uispec4j.interception.toolkit.empty;

import java.awt.BufferCapabilities;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.peer.ContainerPeer;

import sun.awt.CausedFocusEvent;
import sun.java2d.pipe.Region;

class DummyContainerPeer extends DummyComponentPeer implements ContainerPeer {
  public void beginLayout() {
  }

  public void beginValidate() {
  }

  public void endLayout() {
  }

  public void endValidate() {
  }

  public boolean isPaintPending() {
    return false;
  }

  public void cancelPendingPaint(int x, int y, int w, int h) {
  }

  public void restack() {
  }

  public boolean isRestackSupported() {
    return false;
  }

  public Insets getInsets() {
    return Empty.NULL_INSETS;
  }

  public Insets insets() {
    return Empty.NULL_INSETS;
  }

  public void setBounds(int x, int y, int width, int height, int op) {
  }

  public boolean requestFocus(Component component, boolean b, boolean b1, long l, CausedFocusEvent.Cause cause) {
    return false;
  }

  public void reparent(ContainerPeer newContainer) {
  }

  public boolean isReparentSupported() {
    return false;
  }

  public void layout() {
  }

  public Rectangle getBounds() {
    return Empty.NULL_RECTANGLE;
  }

  public void show() {
  }

  public void flip(int x1, int y1, int x2, int y2, BufferCapabilities.FlipContents flipAction) {
  }

  public void applyShape(Region shape) {
  }
}