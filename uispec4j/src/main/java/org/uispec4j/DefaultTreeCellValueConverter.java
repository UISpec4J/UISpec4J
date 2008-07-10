package org.uispec4j;

import javax.swing.*;
import java.awt.*;

/**
 * Default implementation for the TreeCellValueConverter interface.
 * This converter returns the text displayed by JLabel components only - it will throw an
 * exception if another renderer component is not used.
 */
public class DefaultTreeCellValueConverter implements TreeCellValueConverter {
  public String getValue(Component renderedComponent, Object modelObject) {
    return getLabel(renderedComponent).getText();
  }

  public boolean isBold(Component renderedComponent, Object modelObject) {
    Font font = getLabel(renderedComponent).getFont();
    return (font != null) && font.isBold();
  }

  public Color getForeground(Component renderedComponent, Object modelObject) {
    return getLabel(renderedComponent).getForeground();
  }

  protected JLabel getLabel(Component renderedComponent) {
    if (!JLabel.class.isInstance(renderedComponent)) {
      throw new RuntimeException("Unexpected renderer for jTree: only JLabel is supported");
    }
    return (JLabel)renderedComponent;
  }
}
