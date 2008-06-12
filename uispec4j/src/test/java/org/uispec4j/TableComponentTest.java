package org.uispec4j;

import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;

public class TableComponentTest extends UIComponentTestCase {
  private Table table;
  private JTable jTable;

  protected void setUp() throws Exception {
    super.setUp();
    init(new JTable(new String[][]{}, new String[]{}));
  }

  private void init(JTable table) {
    jTable = table;
    jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    jTable.setName("myTable");
    jTable.setDefaultEditor(Integer.class, new DefaultCellEditor(new JComboBox(new Object[]{new Integer(3), new Integer(4), new Integer(5)})));
    this.table = (Table)UIComponentFactory.createUIComponent(jTable);
  }

  public void testGetComponentTypeName() throws Exception {
    assertEquals("table", table.getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<table name='myTable'/>", table.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JTable(), Table.class);
  }

  protected UIComponent createComponent() {
    return table;
  }
}
