package org.uispec4j.interception.toolkit.empty;

import java.awt.Dimension;
import java.awt.peer.SystemTrayPeer;

class DummySystemTrayPeer implements SystemTrayPeer {
  public Dimension getTrayIconSize() {
    return null;
  }
}