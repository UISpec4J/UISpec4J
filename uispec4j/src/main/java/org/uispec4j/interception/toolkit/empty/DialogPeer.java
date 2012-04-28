package org.uispec4j.interception.toolkit.empty;

import java.awt.Window;

public abstract class DialogPeer extends WindowPeeer implements java.awt.peer.DialogPeer {
  public void setResizable(boolean resizeable) {
  }

  public void blockWindows(java.util.List<Window> windows) {
  }

  public void setTitle(String title) {
  }
}