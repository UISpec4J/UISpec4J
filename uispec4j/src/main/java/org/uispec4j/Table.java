package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.dependency.InternalAssert;
import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.utils.ColorUtils;
import org.uispec4j.utils.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for JTable components.<p/>
 * The contents of the underlying table can be usually checked with String or Boolean values,
 * as in the following example:
 * <pre><code>
 * assertTrue(table.contentEquals(new String[]{
 *   {"Bart", "Simpson"},
 *   {"Marge", "Simpson"}
 * }));
 * </code></pre>
 * The conversion between the values (Strings) given in the test and the values
 * actually displayed by the table renderer is performed by a dedicated
 * {@link TableCellValueConverter}, which retrieves the graphical component that draws
 * the table cells and determines the displayed value accordingly.
 * A {@link DefaultTableCellValueConverter} is used by default by the Table component.
 */
public class Table extends AbstractUIComponent {
  public static final String TYPE_NAME = "table";
  public static final Class[] SWING_CLASSES = {JTable.class};

  private JTable jTable;
  private Header header = new Header();
  private TableCellValueConverter defaultCellValueConverter = new DefaultTableCellValueConverter();
  private Map cellValuesConvertersByColumn = new HashMap();

  public Table(JTable table) {
    this.jTable = table;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public JTable getAwtComponent() {
    return jTable;
  }

  public JTable getJTable() {
    return jTable;
  }

  /**
   * Returns a helper interface which gives access to the table header.
   */
  public Header getHeader() {
    return header;
  }

  /**
   * Sets a new converter for analyzing the table cells content.
   */
  public void setDefaultCellValueConverter(TableCellValueConverter cellValueConverter) {
    this.defaultCellValueConverter = cellValueConverter;
  }

  /**
   * Sets a new converter for analyzing the cells of a given column.
   */
  public void setCellValueConverter(int column, TableCellValueConverter tableCellValueConverter) {
    cellValuesConvertersByColumn.put(new Integer(column), tableCellValueConverter);
  }

  public void click(int row, int column) {
    click(row, column, Key.Modifier.NONE);
  }

  public void click(int row, int column, Key.Modifier modifier) {
    Rectangle rect = jTable.getCellRect(row, column, false);
    Mouse.doClickInRectangle(this, rect, false, modifier);
  }

  public void rightClick(int row, int column) {
    Rectangle rect = jTable.getCellRect(row, column, false);
    Mouse.doClickInRectangle(this, rect, true, Key.Modifier.NONE);
  }

  public void doubleClick(int row, int column) {
    Rectangle rect = jTable.getCellRect(row, column, false);
    Mouse.doDoubleClickInRectangle(jTable, rect);
  }

  public Trigger triggerClick(final int row, final int column, final Key.Modifier modifier) {
    return new Trigger() {
      public void run() throws Exception {
        click(row, column, modifier);
      }
    };
  }

  public Trigger triggerRightClick(final int row, final int column) {
    return new Trigger() {
      public void run() throws Exception {
        rightClick(row, column);
      }
    };
  }

  public Trigger triggerDoubleClick(final int row, final int column) {
    return new Trigger() {
      public void run() throws Exception {
        doubleClick(row, column);
      }
    };
  }

  public int getRowCount() {
    return jTable.getRowCount();
  }

  public Assertion rowCountEquals(final int count) {
    return new Assertion() {
      public void check() throws Exception {
        InternalAssert.assertEquals("Unexpected number of rows -", count, getRowCount());
      }
    };
  }

  public int getColumnCount() {
    return jTable.getColumnCount();
  }

  public Assertion columnCountEquals(final int count) {
    return new Assertion() {
      public void check() throws Exception {
        InternalAssert.assertEquals("Unexpected number of columns -", count, getColumnCount());
      }
    };
  }

  /**
   * Returns the object (String or Boolean) displayed in a given cell.<p/>
   * The returned object is that returned by the current TableCellValueConverter
   * used by the table.
   *
   * @see #setCellValueConverter(int,TableCellValueConverter)
   * @see #setDefaultCellValueConverter(TableCellValueConverter)
   */
  public Object getContentAt(int row, int column) {
    return getValueAt(row, column);
  }

  /**
   * Returns the displayed in a given cell using a specific converter.
   */
  public Object getContentAt(int row, int column, TableCellValueConverter converter) {
    return converter.getValue(row, column,
                              getSwingRendererComponentAt(row, column),
                              jTable.getValueAt(row, column));
  }

  /**
   * Returns a {@link Cell} object for interacting with the content of an individual table cell.<p/>
   * Sample usage:
   * <pre><code>
   * ComboBox comboBox = table.editCell(0, 0).getComboBox();
   * assertTrue(comboBox.contentEquals(choices));
   * comboBox.select("b");
   * </code></pre>
   */
  public Cell editCell(int row, int column) {
    InternalAssert.assertTrue("Cell (" + row + "," + column + ") is not editable",
                              jTable.isCellEditable(row, column));
    Component cellEditor = getSwingEditorComponentAt(row, column);
    JPanel cellPanel = new JPanel();
    cellPanel.add(cellEditor);
    return new Cell(cellPanel);
  }

  /**
   * Represents a table cell. This class extends Panel, so that all the component searching
   * methods available in Panel can be used to access the specific component displayed in the cell.
   */
  public class Cell extends Panel {
    public Cell(Container container) {
      super(container);
    }
  }

  /**
   * Inputs some text in a given cell.<p/>
   * This method only works when the underlying editor is a JTextField or a JComboBox -
   * it will throw an exception if this is not the case, or if the cell is not editable.
   * Please refer to {@link #editCell(int,int)} for a more flexible edition method.<p/>
   */
  public void editCell(int row, int column, String value, boolean validateChange) {
    if (!jTable.isCellEditable(row, column)) {
      throw new RuntimeException("Cell (" + row + ", " + column + ") is not editable");
    }
    Component cellEditor = getSwingEditorComponentAt(row, column);
    if (JTextField.class.isInstance(cellEditor)) {
      JTextField textField = ((JTextField)cellEditor);
      textField.setText(value);
      if (validateChange) {
        textField.postActionEvent();
      }
    }
    else {
      if (JComboBox.class.isInstance(cellEditor)) {
        JComboBox jCombo = (JComboBox)cellEditor;
        if (validateChange) {
          ComboBox comboBox = new ComboBox(jCombo);
          comboBox.select(value);
        }
      }
      else {
        throw new RuntimeException("Unexpected editor at (" + row + ", " + column + "): " + cellEditor.getClass().getName());
      }
    }
  }

  /**
   * Checks whether a header is displayed for this table.
   */
  public Assertion hasHeader() {
    return new Assertion() {
      public void check() {
        if (jTable.getTableHeader() == null) {
          InternalAssert.fail("The table contains an header");
        }
      }
    };
  }

  /**
   * Checks the values displayed in the table.<p/>
   * Sample usage:
   * <pre><code>
   * assertTrue(table.contentEquals(new Object[][]{
   *   {"a", Boolean.TRUE, "3"},
   *   {"c", Boolean.FALSE, "4"}
   * }));
   * </code></pre>
   * The conversion between the displayed values and the objects to
   * be given in the array can be customized with
   * {@link #setCellValueConverter(int,TableCellValueConverter)}
   */
  public Assertion contentEquals(final Object[][] expected) {
    return new Assertion() {
      public void check() {
        try {
          int expectedLength = expected.length;
          InternalAssert.assertEquals(lengthErrorMessage(expectedLength),
                                      expectedLength, getRowCount());
          for (int i = 0; i < expectedLength; i++) {
            checkRow(i, expected[i]);
          }
        }
        catch (Error e) {
          InternalAssert.assertEquals(ArrayUtils.toString(expected), getContent());
          throw e;
        }
      }
    };
  }

  /**
   * Checks the values displayed in the table for a given set of columns.
   *
   * @see #contentEquals(Object[][])
   */
  public Assertion contentEquals(final String[] columnNames, final Object[][] expected) {
    return new Assertion() {
      public void check() throws Exception {
        int rowCount = jTable.getRowCount();
        if (expected.length != rowCount) {
          throwError("Expected " + expected.length + " rows but found " + rowCount,
                     columnNames, expected);
        }

        for (int rowIndex = 0; rowIndex < expected.length; rowIndex++) {
          Object[] row = expected[rowIndex];
          if (columnNames.length != row.length) {
            InternalAssert.fail("Expected array should have " + columnNames.length + " elements for each row " +
                                "- invalid row " + rowIndex + ": " + ArrayUtils.toString(row));
          }

          for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            int actualIndex = findColumnIndex(columnNames[columnIndex]);
            if (!Utils.equals(expected[rowIndex][columnIndex], getValueAt(rowIndex, actualIndex))) {
              throwError("Error at (" + rowIndex + ", " + columnIndex + ")", columnNames, expected);
            }
          }
        }
      }
    };
  }

  private void throwError(String message, String[] columnNames, Object[][] expected) {
    String actualContent = getContent(columnNames);
    InternalAssert.assertEquals(message, ArrayUtils.toString(expected), actualContent);
    InternalAssert.fail("Actual: " + actualContent);// in case the string comparison didn't fail
  }

  /**
   * Checks the contents of a given cell.
   * The conversion between the displayed values and the objects
   * provided in this method can be customized with
   * {@link #setCellValueConverter(int,TableCellValueConverter)}
   *
   * @see #getContentAt(int,int)
   */
  public Assertion cellEquals(final int row, final int column, final Object expectedValue) {
    return cellEquals(row, column, expectedValue, getCellValueConverter(column));
  }

  /**
   * Checks the contents of a given cell using a specific data converter.
   * The conversion between the displayed values and the objects
   * provided in this method can be customized with
   * {@link #setCellValueConverter(int,TableCellValueConverter)}
   *
   * @see #getContentAt(int,int,TableCellValueConverter)
   */
  public Assertion cellEquals(final int row, final int column,
                              final Object expectedValue,
                              final TableCellValueConverter converter) {
    return new Assertion() {
      public void check() throws Exception {
        InternalAssert.assertEquals("Error at (" + row + "," + column + ") -",
                                    expectedValue, getContentAt(row, column, converter));
      }
    };
  }

  public Assertion rowEquals(final int rowIndex, final Object[] expectedRow) {
    return new Assertion() {
      public void check() {
        if (rowIndex < 0) {
          InternalAssert.fail("Row index should be positive");
        }
        if (rowIndex >= jTable.getRowCount()) {
          InternalAssert.fail("Table contains only " + jTable.getRowCount() + " rows, unable to access row " + rowIndex);
        }
        try {
          checkRow(rowIndex, expectedRow);
        }
        catch (Error e) {
          StringBuffer buffer = new StringBuffer();
          dumpRow(jTable, rowIndex, buffer, ",");
          InternalAssert.assertEquals(ArrayUtils.toString(expectedRow), buffer);
        }
      }
    };
  }

  public Assertion rowEquals(final int rowIndex, final String[] columnNames, final Object[] expected) {
    return new Assertion() {
      public void check() throws Exception {
        if (rowIndex < 0) {
          InternalAssert.fail("Row index should be positive");
        }
        if (rowIndex >= jTable.getRowCount()) {
          InternalAssert.fail("Table contains only " + jTable.getRowCount() + " rows, unable to access row " + rowIndex);
        }
        if (columnNames.length != expected.length) {
          InternalAssert.fail("Expected array should have " + columnNames.length + " elements for each row " +
                              "- invalid row " + rowIndex + ": " + ArrayUtils.toString(expected));
        }
        Object[] actual = new Object[expected.length];
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
          int actualIndex = findColumnIndex(columnNames[columnIndex]);
          actual[columnIndex] = getValueAt(rowIndex, actualIndex);
        }
        ArrayUtils.assertEquals("Unexpected content at row " + rowIndex, expected, actual);
      }
    };
  }

  public Assertion columnEquals(final int columnIndex, final Object[] expectedColumn) {
    return new Assertion() {
      public void check() {
        if (columnIndex < 0) {
          InternalAssert.fail("Column index should be positive");
        }
        if (columnIndex >= jTable.getColumnCount()) {
          InternalAssert.fail("Table contains only " + jTable.getColumnCount() + " columns, unable to access column " + columnIndex);
        }
        try {
          checkColumn(columnIndex, expectedColumn);
        }
        catch (Error e) {
          StringBuffer buffer = new StringBuffer();
          dumpColumn(jTable, columnIndex, buffer, ",");
          InternalAssert.assertEquals(ArrayUtils.toString(expectedColumn), buffer);
        }
      }
    };
  }

  public Assertion isEmpty() {
    return new Assertion() {
      public void check() {
        try {
          InternalAssert.assertEquals(0, jTable.getRowCount());
        }
        catch (Error e) {
          InternalAssert.fail("Expected: empty table but was:" + getContent());
        }
      }
    };
  }

  /**
   * Checks the foreground color of the table cells
   *
   * @see <a href="http://www.uispec4j.org/usingcolors.html">Using colors</a>
   */
  public Assertion foregroundEquals(final Object[][] colors) {
    return new Assertion() {
      public void check() {
        checkColors(colors, new ComponentColorAccessor() {
          public Color getColor(Component component) {
            return component.getForeground();
          }
        });
      }
    };
  }

  /**
   * Checks the background color of the table cells
   *
   * @see <a href="http://www.uispec4j.org/usingcolors.html">Using colors</a>
   */
  public Assertion backgroundEquals(final Object[][] colors) {
    return new Assertion() {
      public void check() {
        checkColors(colors, new ComponentColorAccessor() {
          public Color getColor(Component component) {
            return component.getBackground();
          }
        });
      }
    };
  }

  private interface ComponentPropertyAccessor {
    Object getProperty(Component component);
  }

  public Assertion borderEquals(final Border[][] borders) {
    return new Assertion() {
      public void check() {
        assertCellPropertyEquals(borders, new ComponentPropertyAccessor() {
          public Object getProperty(Component component) {
            if (!JComponent.class.isAssignableFrom(component.getClass())) {
              throw new RuntimeException("Component '" + component.getClass() + "' does not support borders");
            }
            return ((JComponent)component).getBorder();
          }
        });
      }
    };
  }

  private int findColumnIndex(String columnName) {
    for (int columnIndex = 0; columnIndex < jTable.getColumnCount(); columnIndex++) {
      if (jTable.getColumnName(columnIndex).equalsIgnoreCase(columnName)) {
        return columnIndex;
      }
    }
    InternalAssert.fail("Column '" + columnName + "' not found");
    return -1;
  }

  public Assertion isEditable(final boolean[][] expected) {
    return new Assertion() {
      public void check() {
        Boolean[][] actual = new Boolean[jTable.getRowCount()][jTable.getColumnCount()];
        for (int i = 0; i < actual.length; i++) {
          Boolean[] row = actual[i];
          for (int j = 0; j < row.length; j++) {
            actual[i][j] = Boolean.valueOf(jTable.isCellEditable(i, j));
          }
        }
        ArrayUtils.assertEquals(ArrayUtils.toBooleanObjects(expected), actual);
      }
    };
  }

  public Assertion columnIsEditable(final int columnIndex, final boolean isEditable) {
    return new Assertion() {
      public void check() {
        for (int i = 0; i < jTable.getRowCount(); i++) {
          if (jTable.isCellEditable(i, columnIndex) != isEditable) {
            if (isEditable) {
              InternalAssert.fail("Cell at row " + i + " is not editable");
            }
            else {
              InternalAssert.fail("Cell at row " + i + " is editable");
            }
          }
        }
      }
    };
  }

  public Assertion cellIsEditable(final int rowIndex, final int columnIndex) {
    return new Assertion() {
      public void check() {
        InternalAssert.assertTrue(jTable.isCellEditable(rowIndex, columnIndex));
      }
    };
  }

  public Assertion columnIsEditable(final String columnName, final boolean shouldBeEditable) {
    return columnIsEditable(findColumnIndex(columnName), shouldBeEditable);
  }

  private static interface ComponentColorAccessor {
    Color getColor(Component component);
  }

  public Assertion selectionIsEmpty() {
    return new Assertion() {
      public void check() {
        InternalAssert.assertTrue("Selection is not empty", jTable.getSelectionModel().isSelectionEmpty());
      }
    };
  }

  /**
   * Checks the selection on a cell-by-cell basis.
   */
  public Assertion selectionEquals(final boolean[][] expected) {
    return new Assertion() {
      public void check() {
        int rowCount = expected.length;
        int columnCount = expected[0].length;
        Boolean[][] actual = new Boolean[rowCount][columnCount];
        if (jTable.getCellSelectionEnabled()) {
          for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
              actual[row][column] = Boolean.valueOf(jTable.isCellSelected(row, column));
            }
          }
        }
        else {
          for (int row = 0; row < rowCount; row++) {
            boolean isRowSelected = jTable.isRowSelected(row);
            for (int column = 0; column < columnCount; column++) {
              actual[row][column] = Boolean.valueOf(isRowSelected);
            }
          }
        }
        ArrayUtils.orderedCompare(ArrayUtils.toBooleanObjects(expected), actual);
      }
    };
  }

  public Assertion rowIsSelected(final int rowIndex) {
    return new Assertion() {
      public void check() {
        InternalAssert.assertTrue(jTable.isRowSelected(rowIndex));
      }
    };
  }

  public Assertion cellIsSelected(final int rowIndex, final int columnIndex) {
    return new Assertion() {
      public void check() {
        if (!jTable.getCellSelectionEnabled()) {
          InternalAssert.fail("Cell-level selection is not supported on this table");
        }
        InternalAssert.assertTrue(jTable.isCellSelected(rowIndex, columnIndex));
      }
    };
  }

  public String toString() {
    return getContent();
  }

  private String getContent() {
    StringBuffer buffer = new StringBuffer();
    buffer.append('[');
    for (int row = 0, rowCount = jTable.getRowCount(); row < rowCount; row++) {
      if (row > 0) {
        buffer.append("\n ");
      }
      buffer.append('[');
      dumpRow(jTable, row, buffer, ",\t");
      buffer.append(']');
    }
    buffer.append(']');
    return buffer.toString();
  }

  private String getContent(String[] columnNames) {
    StringBuffer buffer = new StringBuffer();
    buffer.append('[');
    for (int row = 0, rowCount = jTable.getRowCount(); row < rowCount; row++) {
      if (row > 0) {
        buffer.append("\n ");
      }
      buffer.append('[');
      for (int col = 0, colCount = columnNames.length; col < colCount; col++) {
        buffer.append(getValueAt(row, findColumnIndex(columnNames[col])));
        if (col < (colCount - 1)) {
          buffer.append(",\t");
        }
      }
      buffer.append(']');
    }
    buffer.append(']');
    return buffer.toString();
  }

  public Component getSwingEditorComponentAt(int row, int column) {
    jTable.editCellAt(row, column);
    return jTable.getEditorComponent();
  }

  public Component getSwingRendererComponentAt(int row, int column) {
    return jTable
      .getCellRenderer(row, column)
      .getTableCellRendererComponent(jTable,
                                     jTable.getValueAt(row, column),
                                     jTable.isCellSelected(row, column),
                                     false,
                                     row, column);
  }

  public void resizeColumn(String columnName, int width) {
    findColumn(columnName).setPreferredWidth(width);
  }

  public Assertion columnSizeEquals(final String columnName, final int expectedWidth) {
    return new Assertion() {
      public void check() {
        InternalAssert.assertEquals(expectedWidth, findColumn(columnName).getPreferredWidth());
      }
    };
  }

  public Assertion columnSizeEquals(final int columnIndex, final int expectedWidth) {
    return new Assertion() {
      public void check() {
        InternalAssert.assertEquals(expectedWidth,
                                    jTable.getColumnModel().getColumn(columnIndex).getPreferredWidth());
      }
    };
  }

  public Assertion rowsAreSelected(final int[] rowIndexes) {
    return new Assertion() {
      public void check() {
        int[] actualSelection = jTable.getSelectedRows();
        Arrays.sort(actualSelection);
        int[] expectedSelection = rowIndexes.clone();
        Arrays.sort(expectedSelection);
        ArrayUtils.assertEquals(expectedSelection, actualSelection);
      }
    };
  }

  public void selectCell(int row, int column) {
    if (!jTable.getCellSelectionEnabled()) {
      InternalAssert.fail("Individual cell selection is not allowed on this table");
    }
    jTable.setRowSelectionInterval(row, row);
    jTable.setColumnSelectionInterval(column, column);
  }

  public void selectRow(int row) {
    jTable.setRowSelectionInterval(row, row);
    if (jTable.getCellSelectionEnabled()) {
      jTable.setColumnSelectionInterval(0, jTable.getColumnCount() - 1);
    }
  }

  public void selectRows(int[] rowIndexes) {
    jTable.getSelectionModel().setValueIsAdjusting(true);
    try {
      jTable.clearSelection();
      for (int i = 0; i < rowIndexes.length; i++) {
        int row = rowIndexes[i];
        jTable.addRowSelectionInterval(row, row);
      }
      if (jTable.getCellSelectionEnabled()) {
        jTable.setColumnSelectionInterval(0, jTable.getColumnCount() - 1);
      }
    }
    finally {
      jTable.getSelectionModel().setValueIsAdjusting(false);
    }
  }

  public void selectRows(int start, int end) {
    if (start > end) {
      throw new IllegalArgumentException("Invalid indexes: " + start + " > " + end);
    }
    jTable.setRowSelectionInterval(start, end);
  }

  public void selectBlock(int top, int left, int bottom, int right) {
    InternalAssert.assertTrue("Only row-level selection is allowed on this table",
                              jTable.getCellSelectionEnabled());
    if ((top > bottom) && (left > right)) {
      throw new IllegalArgumentException("Invalid block definition - expected top <= bottom and left <= right");
    }
    jTable.setRowSelectionInterval(top, bottom);
    jTable.setColumnSelectionInterval(left, right);
  }

  public void addRowToSelection(int row) {
    jTable.addRowSelectionInterval(row, row);
    if (jTable.getCellSelectionEnabled()) {
      jTable.setColumnSelectionInterval(0, jTable.getColumnCount() - 1);
    }
  }

  public void removeRowFromSelection(int row) {
    InternalAssert.assertTrue("Row " + row + " is not selected", jTable.isRowSelected(row));
    jTable.removeRowSelectionInterval(row, row);
  }

  public void clearSelection() {
    jTable.clearSelection();
  }

  /**
   * Asserts that the contents of the table starts with the specified rows.
   * This is useful for dealing with tables with hundreds of rows, as you can
   * just check that the first few are correct.
   *
   * @param expectedContent the rows at the start of the table
   * @return An assertion that will check the first rows of the table.
   */
  public Assertion startsWith(final Object[][] expectedContent) {
    return new Assertion() {
      public void check() throws Exception {
        int expectedLength = expectedContent.length;
        checkLengthGreaterThan(expectedLength);
        for (int i = 0; i < expectedLength; i++) {
          rowEquals(i, expectedContent[i]).check();
        }
      }
    };
  }

  /**
   * Asserts that the contents of the table ends with the specified rows.
   * This is useful for dealing with tables with hundreds of rows, as you can
   * just check that the last few are correct.
   *
   * @param expectedContent the rows at the end of the table
   * @return An assertion that will check the first rows of the table.
   */
  public Assertion endsWith(final Object[][] expectedContent) {
    return new Assertion() {
      public void check() throws Exception {
        int expectedLength = expectedContent.length;
        checkLengthGreaterThan(expectedLength);
        for (int i = 0; i < expectedLength; i++) {
          rowEquals(i + getRowCount() - expectedLength, expectedContent[i]).check();
        }
      }
    };
  }

  /**
   * Checks that the specified table contains a row.
   *
   * @param expectedRow The row the table should contain
   * @return An assertion that checks the contents of the table.
   */
  public Assertion containsRow(final Object[] expectedRow) {
    return new Assertion() {
      public void check() throws Exception {
        for (int i = 0; i < getRowCount(); i++) {
          if (rowEquals(i, expectedRow).isTrue()) {
            return;
          }
        }
        InternalAssert.fail("row " + ArrayUtils.toString(expectedRow) + " not found in table.");
      }
    };
  }

  /**
   * Helper interface for manipulating the table header, if any
   */
  public class Header {

    private Header() {
    }

    public String[] getColumnNames() {
      String[] columnNames = new String[jTable.getColumnCount()];
      for (int columnIndex = 0; columnIndex < jTable.getColumnCount(); columnIndex++) {
        columnNames[columnIndex] = jTable.getColumnName(columnIndex);
      }
      return columnNames;
    }

    public int findColumnIndex(String columnName) {
      return Table.this.findColumnIndex(columnName);
    }

    /**
     * Checks the column names.
     */
    public Assertion contentEquals(final String... expectedHeaders) {
      return new Assertion() {
        public void check() {
          checkHeader();
          try {
            InternalAssert.assertEquals(expectedHeaders.length, jTable.getColumnCount());
            for (int i = 0; i < expectedHeaders.length; i++) {
              InternalAssert.assertEquals(expectedHeaders[i], jTable.getColumnName(i));
            }
          }
          catch (Error e) {
            InternalAssert.assertEquals(ArrayUtils.toString(expectedHeaders), ArrayUtils.toString(getColumnNames()));
            throw e;
          }
        }
      };
    }

    /**
     * Returns a string description of the default background color.
     */
    public String getDefaultBackground() {
      checkHeader();
      return ColorUtils.getColorDescription(jTable.getTableHeader().getBackground());
    }

    /**
     * Checks the background color on each column of the table header.
     */
    public Assertion backgroundEquals(final Object[] expectedColors) {
      return new Assertion() {
        public void check() {
          checkHeader();
          TableModel model = jTable.getModel();
          TableCellRenderer headerRenderer = jTable.getTableHeader().getDefaultRenderer();
          for (int columnIndex = 0; columnIndex < model.getColumnCount(); columnIndex++) {
            Component component =
              headerRenderer.getTableCellRendererComponent(jTable,
                                                           model.getColumnName(columnIndex),
                                                           false,
                                                           false,
                                                           -1,
                                                           columnIndex);
            ColorUtils.assertEquals("Unexpected color at column " + columnIndex,
                                    expectedColors[columnIndex], component.getBackground());
          }
        }
      };
    }

    public void click(int columnIndex) {
      checkHeader();
      JTableHeader tableHeader = jTable.getTableHeader();
      Mouse.doClickInRectangle(tableHeader, tableHeader.getHeaderRect(columnIndex), false, Key.Modifier.NONE);
    }

    public void click(String columnName) {
      checkHeader();
      JTableHeader tableHeader = jTable.getTableHeader();
      int columnIndex = findColumnIndex(columnName);
      Mouse.doClickInRectangle(tableHeader, tableHeader.getHeaderRect(columnIndex), false, Key.Modifier.NONE);
    }

    public Trigger triggerClick(final String columnName) {
      return new Trigger() {
        public void run() throws Exception {
          click(columnName);
        }
      };
    }

    public Trigger triggerClick(final int columnIndex) {
      return new Trigger() {
        public void run() throws Exception {
          click(columnIndex);
        }
      };
    }

    public void rightClick(int columnIndex) {
      checkHeader();
      JTableHeader tableHeader = jTable.getTableHeader();
      Mouse.doClickInRectangle(tableHeader, tableHeader.getHeaderRect(columnIndex), true, Key.Modifier.NONE);
    }

    public void rightClick(String columnName) {
      checkHeader();
      JTableHeader tableHeader = jTable.getTableHeader();
      int columnIndex = findColumnIndex(columnName);
      Mouse.doClickInRectangle(tableHeader, tableHeader.getHeaderRect(columnIndex), true, Key.Modifier.NONE);
    }

    public Trigger triggerRightClick(final int columnIndex) {
      return new Trigger() {
        public void run() throws Exception {
          rightClick(columnIndex);
        }
      };
    }

    public Trigger triggerRightClick(final String columnName) {
      return new Trigger() {
        public void run() throws Exception {
          rightClick(columnName);
        }
      };
    }

    private void checkHeader() {
      InternalAssert.assertNotNull("The table contains no header", jTable.getTableHeader());
    }
  }

  private void checkColors(Object[][] colors, ComponentColorAccessor accessor) {
    InternalAssert.assertEquals(colors.length, jTable.getRowCount());
    for (int row = 0; row < colors.length; row++) {
      for (int col = 0; col < colors[row].length; col++) {
        TableCellRenderer cellRenderer = jTable.getCellRenderer(row, col);
        Component component =
          cellRenderer.getTableCellRendererComponent(jTable,
                                                     jTable.getModel().getValueAt(row, col),
                                                     jTable.isCellSelected(row, col),
                                                     false, row, col);
        ColorUtils.assertEquals("Error at (" + row + ", " + col + ") -",
                                colors[row][col], accessor.getColor(component));
      }
    }
  }

  private TableColumn findColumn(String columnName) {
    int columnIndex = findColumnIndex(columnName);
    if (columnIndex == -1) {
      throw new RuntimeException("Column '" + columnName + "' not found");
    }
    return jTable.getColumnModel().getColumn(columnIndex);
  }

  private void dumpRow(JTable table, int row, StringBuffer buffer, String separator) {
    for (int col = 0, colCount = table.getColumnCount(); col < colCount; col++) {
      buffer.append(getValueAt(row, col));
      if (col < (colCount - 1)) {
        buffer.append(separator);
      }
    }
  }

  private void dumpColumn(JTable table, int col, StringBuffer buffer, String separator) {
    for (int row = 0, rowCount = table.getRowCount(); row < rowCount; row++) {
      buffer.append(getValueAt(row, col));
      if (row < (rowCount - 1)) {
        buffer.append(separator);
      }
    }
  }

  private void checkRow(int rowIndex, Object[] expectedRow) {
    InternalAssert.assertEquals(expectedRow.length, jTable.getColumnCount());
    for (int columnIndex = 0; columnIndex < expectedRow.length; columnIndex++) {
      checkValueAt(rowIndex, columnIndex, expectedRow[columnIndex]);
    }
  }

  private void checkColumn(int columnIndex, Object[] expectedColumn) {
    InternalAssert.assertEquals(expectedColumn.length, jTable.getRowCount());
    for (int rowIndex = 0; rowIndex < expectedColumn.length; rowIndex++) {
      checkValueAt(rowIndex, columnIndex, expectedColumn[rowIndex]);
    }
  }

  private void checkValueAt(int rowIndex, int columnIndex, Object expectedValue) {
    InternalAssert.assertEquals("Element at (" + rowIndex + ", " + columnIndex + ") does not match",
                                expectedValue,
                                getValueAt(rowIndex, columnIndex));
  }

  private Object getValueAt(int rowIndex, int columnIndex) {
    return getCellValueConverter(columnIndex).getValue(rowIndex, columnIndex,
                                                       getSwingRendererComponentAt(rowIndex, columnIndex),
                                                       jTable.getValueAt(rowIndex, columnIndex));
  }

  private TableCellValueConverter getCellValueConverter(int columnIndex) {
    TableCellValueConverter specificConverter = (TableCellValueConverter)cellValuesConvertersByColumn.get(new Integer(columnIndex));
    if (specificConverter != null) {
      return specificConverter;
    }
    return defaultCellValueConverter;
  }

  private void assertCellPropertyEquals(Object[][] properties, ComponentPropertyAccessor accessor) {
    InternalAssert.assertEquals(properties.length, jTable.getRowCount());
    for (int row = 0; row < properties.length; row++) {
      for (int col = 0; col < properties[row].length; col++) {
        TableCellRenderer cellRenderer = jTable.getCellRenderer(row, col);
        Component component =
          cellRenderer.getTableCellRendererComponent(jTable,
                                                     jTable.getModel().getValueAt(row, col),
                                                     jTable.isCellSelected(row, col), false, row, col);
        InternalAssert.assertEquals("Error at (" + row + ", " + col + ") -",
                                    properties[row][col], accessor.getProperty(component));
      }
    }
  }

  private void checkLengthGreaterThan(int expectedLength) {
    InternalAssert.assertTrue(lengthErrorMessage(expectedLength), getRowCount() > expectedLength);
  }

  private String lengthErrorMessage(int expectedLength) {
    return "Table contains only " + getRowCount() + " rows whereas " + expectedLength + " rows are expected.";
  }
}
