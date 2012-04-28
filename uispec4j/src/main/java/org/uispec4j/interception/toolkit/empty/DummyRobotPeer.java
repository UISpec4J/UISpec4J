package org.uispec4j.interception.toolkit.empty;

import java.awt.Rectangle;
import java.awt.peer.RobotPeer;

class DummyRobotPeer implements RobotPeer {

  public void keyPress(int keycode) {
  }

  public void keyRelease(int keycode) {
  }

  public void mousePress(int buttons) {
  }

  public void mouseRelease(int buttons) {
  }

  public void mouseWheel(int wheelAmt) {
  }

  public int getRGBPixel(int x, int y) {
    return 0;
  }

  public void mouseMove(int x, int y) {
  }

  public int[] getRGBPixels(Rectangle bounds) {
    return new int[0];
  }

  public void dispose() {
  }
}