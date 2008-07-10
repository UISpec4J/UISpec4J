package org.uispec4j;

import javax.swing.*;
import java.awt.*;

/**
 * Default implementation for the TableCellValueConverter interface.
 * This converter returns the displayed value for JLabel and JComboBox components, and a Boolean
 * in the case of a JCheckBox. If another renderer type is used, it will returned the stringified object
 * used by the internal model of the table (using toString()), or an empty string if this model
 * object is null.
 */
public class DefaultTableCellValueConverter implements TableCellValueConverter {

  public Object getValue(int row, int column, Component renderedComponent, Object modelObject) {
    if (renderedComponent instanceof JLabel) {
      return ((JLabel)renderedComponent).getText();
    }
    else if (renderedComponent instanceof JComboBox) {
      return ((JComboBox)renderedComponent).getSelectedItem().toString();
    }
    else if (renderedComponent instanceof JCheckBox) {
      return ((JCheckBox)renderedComponent).isSelected();
    }
    if (modelObject != null) {
      return modelObject.toString();
    }
    return "";
  }
}
