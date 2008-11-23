package org.uispec4j.utils;

import org.uispec4j.interception.toolkit.Empty;

import javax.swing.*;
import java.awt.*;

/** Use this to prevent Swing from painting your components during the tests.
 * This can speed up the execution of your tests. */
public class DummyRepaintManager extends RepaintManager {

  public static boolean isInstalled() {
    return RepaintManager.currentManager(null) instanceof DummyRepaintManager;
  }

  public static void init() {
    RepaintManager.setCurrentManager(new DummyRepaintManager());
  }

  public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
  }

  public synchronized void addInvalidComponent(JComponent invalidComponent) {
  }

  public Rectangle getDirtyRegion(JComponent aComponent) {
    return Empty.NULL_RECTANGLE;
  }

  public Dimension getDoubleBufferMaximumSize() {
    return Empty.NULL_DIMENSION;
  }

  public Image getOffscreenBuffer(Component c, int proposedWidth, int proposedHeight) {
    return Empty.NULL_IMAGE;
  }

  public Image getVolatileOffscreenBuffer(Component c, int proposedWidth, int proposedHeight) {
    return Empty.NULL_VOLATILE_IMAGE;
  }

  public boolean isCompletelyDirty(JComponent aComponent) {
    return false;
  }

  public boolean isDoubleBufferingEnabled() {
    return false;
  }

  public void markCompletelyClean(JComponent aComponent) {
  }

  public void markCompletelyDirty(JComponent aComponent) {
  }

  public void paintDirtyRegions() {
  }

  public synchronized void removeInvalidComponent(JComponent component) {
  }

  public void setDoubleBufferingEnabled(boolean aFlag) {
  }

  public void setDoubleBufferMaximumSize(Dimension d) {
  }

  public void validateInvalidComponents() {
  }
}
