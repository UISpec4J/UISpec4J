package org.uispec4j;

import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.utils.UnitTestCase;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

public abstract class TableTestCase extends UnitTestCase {
  Table table;
  JTable jTable;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    init(new JTable(new TableSelectionTest.MyModel()));
  }

  private void init(JTable table) {
    jTable = table;
    jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    jTable.setName("myTable");
    jTable.setDefaultEditor(Integer.class, new DefaultCellEditor(new JComboBox(new Object[]{3, 4, 5})));
    this.table = (Table)UIComponentFactory.createUIComponent(jTable);
  }

  protected class MyModel extends AbstractTableModel {
    String[] columnNames = new String[]{"0", "1", "2"};
    Object[][] rowData = new Object[][]{{"a", Boolean.TRUE, 3},
                                        {"c", Boolean.FALSE, 4}};
    Class[] columnClasses = new Class[]{String.class, Boolean.class, Integer.class};

    public String getColumnName(int column) {
      return columnNames[column];
    }

    public int getRowCount() {
      return rowData.length;
    }

    public int getColumnCount() {
      return columnNames.length;
    }

    public Object getValueAt(int row, int col) {
      return rowData[row][col];
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      rowData[rowIndex][columnIndex] = aValue;
      fireTableCellUpdated(rowIndex, columnIndex);
    }

    public Class getColumnClass(int columnIndex) {
      return columnClasses[columnIndex];
    }

    public boolean isCellEditable(int row, int column) {
      return (row != 1) || (column != 0);
    }
  }

  void installJPanelRenderer() {
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return new JPanel();
      }
    };
    jTable.setDefaultRenderer(Boolean.class, renderer);
  }
}
