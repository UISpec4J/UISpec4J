package org.uispec4j;

import java.awt.*;

/**
 * Performs a conversion between internal and tested cell values in a {@link ListBox} component
 * This interface is meant to be used when the JList component used by the application
 * uses renderers other than JLabel components.
 *
 * @see ListBox#setCellValueConverter
 */
public interface ListBoxCellValueConverter {
  String getValue(int index, Component renderedComponent, Object modelObject);
}
