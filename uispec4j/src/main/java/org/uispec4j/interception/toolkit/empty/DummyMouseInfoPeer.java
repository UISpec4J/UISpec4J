package org.uispec4j.interception.toolkit.empty;

import java.awt.Point;
import java.awt.Window;
import java.awt.peer.MouseInfoPeer;

class DummyMouseInfoPeer implements MouseInfoPeer {
  public int fillPointWithCoords(Point point) {
    return 0;
  }

  public boolean isWindowUnderMouse(Window window) {
    return false;
  }
}