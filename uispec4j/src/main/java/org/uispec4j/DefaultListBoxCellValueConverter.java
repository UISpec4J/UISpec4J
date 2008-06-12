package org.uispec4j;

import javax.swing.*;
import java.awt.*;

/**
 * Default implementation for the ListBoxCellValueConverter interface.
 * Returns the text displayed by the renderer, only in the case of JLabel renderers -
 * this method will return an empty String if the renderer is not a JLabel.
 */
public class DefaultListBoxCellValueConverter implements ListBoxCellValueConverter {
  public String getValue(int index, Component renderedComponent, Object modelObject) {
    if (renderedComponent instanceof JLabel) {
      return ((JLabel)renderedComponent).getText();
    }
    if (modelObject != null) {
      return modelObject.toString();
    }
    return "";
  }
}
