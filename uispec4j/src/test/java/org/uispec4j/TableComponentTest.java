package org.uispec4j;

import org.junit.Test;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class TableComponentTest extends UIComponentTestCase {
  private Table table;
  private JTable jTable;

  @Override
  public void setUp() throws Exception {
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

  @Override
  @Test
  public void testGetComponentTypeName() throws Exception {
    assertEquals("table", table.getDescriptionTypeName());
  }

  @Override
  @Test
  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<table name='myTable'/>", table.getDescription());
  }

  @Override
  @Test
  public void testFactory() throws Exception {
    checkFactory(new JTable(), Table.class);
  }

  protected UIComponent createComponent() {
    return table;
  }
}
