package org.uispec4j;

import static org.uispec4j.Key.Modifier;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Utility for simulating mouse inputs.
 */
public class Mouse {

  private Mouse() {
  }

  /**
   * Clicks in the center of a UIComponent.
   */
  public static void click(UIComponent uiComponent) {
    doClick(uiComponent.getAwtComponent(), 1);
  }

  /**
   * Double clicks in the center of a UIComponent.
   */
  public static void doubleClick(UIComponent uiComponent) {
    doClick(uiComponent.getAwtComponent(), 2);
  }

  /**
   * Clicks in a given area of a UIComponent.
   */
  public static void doClickInRectangle(UIComponent uiComponent,
                                        Rectangle rect,
                                        boolean useRightClick,
                                        Key.Modifier keyModifier) {
    doClickInRectangle(uiComponent.getAwtComponent(), rect, useRightClick, keyModifier);
  }

  /**
   * Clicks in a given area of a Swing component.
   */
  public static void doClickInRectangle(Component component,
                                        Rectangle rect,
                                        boolean useRightClick,
                                        Key.Modifier keyModifier) {
    doClickInRectangle(component, rect, useRightClick, keyModifier, 1);
  }

  /**
   * Double clicks in a given area of a Swing component.
   */
  public static void doDoubleClickInRectangle(Component component, Rectangle rect) {
    doClickInRectangle(component, rect, false, Modifier.NONE, 2);
  }

  private static void doClickInRectangle(Component component, Rectangle rect, boolean useRightClick, Key.Modifier keyModifier, int nbClicks) {
    int modifiers = useRightClick ? MouseEvent.BUTTON3_MASK : MouseEvent.BUTTON1_MASK;
    modifiers |= keyModifier.getCode();
    final int x = rect.x + (rect.width / 2);
    final int y = rect.y + (rect.height / 2);
    component.dispatchEvent(new MouseEvent(component, MouseEvent.MOUSE_PRESSED, 1, modifiers, x, y, nbClicks, false));
    component.dispatchEvent(new MouseEvent(component, MouseEvent.MOUSE_RELEASED, 1, modifiers, x, y, nbClicks, useRightClick));
    component.dispatchEvent(new MouseEvent(component, MouseEvent.MOUSE_CLICKED, 1, modifiers, x, y, nbClicks, false));
  }

  private static void doClick(Component component, int clickCount) {
    doClickInRectangle(component, new Rectangle(), false, Modifier.NONE, clickCount);
  }
}
