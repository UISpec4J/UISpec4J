package org.uispec4j;

import org.uispec4j.xml.EventLogger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class MouseLogger extends EventLogger implements MouseListener {
  public MouseLogger(Component component) {
    component.addMouseListener(this);
  }

  public MouseLogger(UIComponent uiComponent) {
    this(uiComponent.getAwtComponent());
  }

  public void mouseClicked(MouseEvent e) {
    logEvent("mouseClicked", e);
  }

  public void mouseEntered(MouseEvent e) {
    logEvent("mouseEntered", e);
  }

  public void mouseExited(MouseEvent e) {
    logEvent("mouseExited", e);
  }

  public void mousePressed(MouseEvent e) {
    logEvent("mousePressed", e);
  }

  public void mouseReleased(MouseEvent e) {
    logEvent("mouseReleased", e);
  }

  private void logEvent(String eventName, MouseEvent e) {
    EventLogger.Log eventLogger = log(eventName);
    eventLogger.add("button", String.valueOf(e.getButton()));
    if (e.getClickCount() != 1) {
      eventLogger.add("clickCount", e.getClickCount());
    }
  }
}
