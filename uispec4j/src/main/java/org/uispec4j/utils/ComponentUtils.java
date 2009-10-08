package org.uispec4j.utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Miscellaneous utilities designed for internal use.
 */
public class ComponentUtils {
  public static boolean hasDisplayedName(Class componentClass) {
    return ((AbstractButton.class.isAssignableFrom(componentClass))
            || (JLabel.class.isAssignableFrom(componentClass))
            || (JTextComponent.class.isAssignableFrom(componentClass))
    );
  }

  public static String getDisplayedName(Component component) {
    if (AbstractButton.class.isInstance(component)) {
      return ((AbstractButton)component).getText();
    }
    else if (JLabel.class.isInstance(component)) {
      return ((JLabel)component).getText();
    }
    else if (JTextComponent.class.isInstance(component)) {
      return ((JTextComponent)component).getText();
    }
    return null;
  }

  public static void close(org.uispec4j.Window window) {
    window.getAwtComponent().setVisible(false);
  }

  public static String extractName(Component component) {
    String displayedName = getDisplayedName(component);
    if (!isEmpty(displayedName)) {
      return displayedName;
    }
    JLabel label = findLabelFor(component);
    if (label != null) {
      return label.getText();
    }
    return component.getName();
  }

  private static boolean isEmpty(String name) {
    return name == null || name.length() == 0;
  }

  public static JLabel findLabelFor(Component awtComp) {
    JLabel label = (JLabel)((JComponent)awtComp).getClientProperty("labeledBy");
    return label;
  }
}
