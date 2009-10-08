package org.uispec4j;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

class DummyKeyListener implements KeyListener {
  private final java.util.List<String> events = new ArrayList<String>();

  public void keyPressed(KeyEvent event) {
    events.add("keyPressed");
  }

  public void keyReleased(KeyEvent event) {
    events.add("keyReleased");
  }

  public void keyTyped(KeyEvent event) {
    events.add("keyTyped");
  }

  public void checkEvents(String... expectedEvents) {
    TestUtils.assertEquals(expectedEvents, events);
    events.clear();
  }
}
