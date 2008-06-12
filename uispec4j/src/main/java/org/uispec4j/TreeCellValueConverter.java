package org.uispec4j;

import java.awt.*;

/**
 * Performs a conversion between internal and tested cell values in a {@link Tree} component
 *
 * @see Tree#setCellValueConverter
 */
public interface TreeCellValueConverter {

  /**
   * Returns the textual representation of the given tree object
   *
   * @param renderedComponent the Component returned by the JTree's renderer
   * @param modelObject       the Object returned by the JTree's internal model
   */
  String getValue(Component renderedComponent, Object modelObject);

  /**
   * Indicates whether the given tree object is painted in bold
   */
  boolean isBold(Component renderedComponent, Object modelObject);

  /**
   * Returns the foreground color used in the representation of the object
   */
  Color getForeground(Component renderedComponent, Object modelObject);
}
