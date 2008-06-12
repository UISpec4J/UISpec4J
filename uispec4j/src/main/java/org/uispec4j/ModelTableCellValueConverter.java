package org.uispec4j;

import java.awt.*;

/**
 * Converter that returns the object managed by the underlying JTable's model. This is to be used sparingly since
 * it can disclose the application's internals, and you probably want the tests to be as independent as possible
 * from the implementation.
 */
public class ModelTableCellValueConverter implements TableCellValueConverter {

  public static final TableCellValueConverter INSTANCE = new ModelTableCellValueConverter();

  public Object getValue(int row, int column, Component renderedComponent, Object modelObject) {
    return modelObject;
  }
}
