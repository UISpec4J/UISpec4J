package org.uispec4j.interception.toolkit.empty;

import java.awt.Desktop;
import java.awt.peer.DesktopPeer;
import java.io.File;
import java.io.IOException;
import java.net.URI;

class DummyDesktopPeer implements DesktopPeer {

  public boolean isSupported(Desktop.Action action) {
    return false;
  }

  public void open(File file) throws IOException {
  }

  public void edit(File file) throws IOException {
  }

  public void print(File file) throws IOException {
  }

  public void mail(URI uri) throws IOException {
  }

  public void browse(URI uri) throws IOException {
  }
}