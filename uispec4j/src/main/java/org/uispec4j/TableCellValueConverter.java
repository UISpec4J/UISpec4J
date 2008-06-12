package org.uispec4j;

import java.awt.*;

/**
 * Performs a conversion between internal and tested cell values in a {@link Table} component
 *
 * @see Table#setCellValueConverter
 * @see Table#setDefaultCellValueConverter
 */
public interface TableCellValueConverter {
  Object getValue(int row, int column, Component renderedComponent, Object modelObject);
}
