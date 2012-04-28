package org.uispec4j.interception.toolkit.empty;

import java.awt.Image;
import java.awt.MenuBar;
import java.awt.Rectangle;

public abstract class FramePeer extends WindowPeeer implements java.awt.peer.FramePeer {
  public int getState() {
    return 0;
  }

  public void setState(int state) {
  }

  public void setResizable(boolean resizeable) {
  }

  public void setIconImage(Image im) {
  }

  public void setMenuBar(MenuBar mb) {
  }

  public void setMaximizedBounds(Rectangle bounds) {
  }

  public void setBoundsPrivate(int x, int y, int width, int height) {
  }

  public Rectangle getBoundsPrivate() {
    return null;
  }

  public void setTitle(String title) {
  }
}