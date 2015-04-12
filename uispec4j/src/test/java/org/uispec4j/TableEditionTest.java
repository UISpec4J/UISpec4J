package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.utils.AssertionFailureNotDetectedError;

import javax.swing.*;

public class TableEditionTest extends TableTestCase {
  public void testEditCellForString() throws Exception {
    table.editCell(0, 0, "value", false);
    assertTrue(table.contentEquals(new Object[][]{
      {"a", Boolean.TRUE, "3"},
      {"c", Boolean.FALSE, "4"}
    }));
    table.editCell(0, 0, "value", true);
    assertTrue(table.contentEquals(new Object[][]{
      {"value", Boolean.TRUE, "3"},
      {"c", Boolean.FALSE, "4"}
    }));
  }

  public void testEditCellForComboBox() throws Exception {
    table.editCell(0, 2, "5", false);
    assertTrue(table.contentEquals(new Object[][]{
      {"a", Boolean.TRUE, "3"},
      {"c", Boolean.FALSE, "4"}
    }));
    table.editCell(0, 2, "5", true);
    assertTrue(table.contentEquals(new Object[][]{
      {"a", Boolean.TRUE, "5"},
      {"c", Boolean.FALSE, "4"}
    }));
  }

  public void testEditCellErrors() throws Exception {
    try {
      table.editCell(1, 0, "notEditable", true);
      fail();
    }
    catch (RuntimeException e) {
      assertEquals("Cell (1, 0) is not editable", e.getMessage());
    }

    try {
      table.editCell(1, 1, "cellIsNotAJTextFieldNorAComboBox", true);
      fail();
    }
    catch (Exception e) {
      assertEquals("Unexpected editor at (1, 1): javax.swing.JCheckBox", e.getMessage());
    }
  }

  public void testAssertEditable() throws Exception {
    jTable.setModel(new MyModel() {
      public boolean isCellEditable(int row, int col) {
        return (row + col) % 2 == 0;
      }
    });
    assertTrue(table.isEditable(new boolean[][]{
      {true, false, true},
      {false, true, false}
    }));

    try {
      assertTrue(table.isEditable(new boolean[][]{
        {false, false, false},
        {false, false, false}
      }));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Error at row 0:\n" +
                   "Expected: [false,false,false]\n" +
                   "Actual:   [true,false,true]",
                   e.getMessage());
    }
  }

  public void testAssertCellEditable() throws Exception {
    jTable.setModel(new MyModel() {
      public boolean isCellEditable(int row, int col) {
        return (col == 0) || ((col == 1) && (row == 1));
      }
    });

    checkAssertCellEditable(new boolean[][]{
      {true, false, false},
      {true, true, false}
    });
  }

  private void checkAssertCellEditable(boolean[][] values) {
    for (int row = 0; row < values.length; row++) {
      boolean[] rowValues = values[row];
      for (int column = 0; column < rowValues.length; column++) {
        boolean value = rowValues[column];
        Assertion result = table.cellIsEditable(row, column);
        assertTrue((value) ? result : not(result));
      }
    }
  }

  public void testAssertColumnEditableWithColumnIndex() throws Exception {
    jTable.setModel(new MyModel() {
      public boolean isCellEditable(int row, int col) {
        return (col == 0) || ((col == 1) && (row == 1));
      }
    });

    assertTrue(table.columnIsEditable(0, true));
    assertTrue(table.columnIsEditable(2, false));

    try {
      assertTrue(table.columnIsEditable(1, true));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Cell at row 0 is not editable", e.getMessage());
    }
  }

  public void testAssertColumnEditableWithColumnName() throws Exception {
    jTable.setModel(new MyModel() {
      public boolean isCellEditable(int row, int col) {
        return (col == 0) || ((col == 1) && (row == 1));
      }
    });

    assertTrue(table.columnIsEditable("0", true));
    assertTrue(table.columnIsEditable("2", false));

    try {
      assertTrue(table.columnIsEditable("1", true));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Cell at row 0 is not editable", e.getMessage());
    }

    try {
      assertTrue(table.columnIsEditable("unknown", true));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Column 'unknown' not found - actual names: [0, 1, 2]", e.getMessage());
    }
  }

  public void testEditingACellWithAComboBox() throws Exception {
    String[] choices = new String[]{"a", "b", "c"};
    jTable.setDefaultEditor(String.class, new DefaultCellEditor(new JComboBox(choices)));
    assertTrue(table.contentEquals(new Object[][]{
      {"a", Boolean.TRUE, "3"},
      {"c", Boolean.FALSE, "4"}
    }));
    ComboBox comboBox = table.editCell(0, 0).getComboBox();
    assertTrue(comboBox.contentEquals(choices));
    comboBox.select("b");
    assertTrue(table.contentEquals(new Object[][]{
      {"b", Boolean.TRUE, "3"},
      {"c", Boolean.FALSE, "4"}
    }));
  }

  public void testEditingACellWithATextField() throws Exception {
    jTable.setDefaultEditor(String.class, new DefaultCellEditor(new JTextField()));
    assertTrue(table.contentEquals(new Object[][]{
      {"a", Boolean.TRUE, "3"},
      {"c", Boolean.FALSE, "4"}
    }));
    table.editCell(0, 0).getTextBox().setText("new");
    assertTrue(table.contentEquals(new Object[][]{
      {"new", Boolean.TRUE, "3"},
      {"c", Boolean.FALSE, "4"}
    }));
  }

  public void testEditCellChecksThatTheCellIsEditable() throws Exception {
    jTable.setModel(new MyModel() {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    });
    try {
      table.editCell(0, 0);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Cell (0,0) is not editable", e.getMessage());
    }
  }
}
