package org.uispec4j;

import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.Functor;

import javax.swing.*;
import java.awt.*;

public class ComboBoxTest extends UIComponentTestCase {
  private ComboBox comboBox;
  private JComboBox jComboBox;

  protected void setUp() throws Exception {
    super.setUp();
    init(new JComboBox(new String[]{"one", "two", "three"}));
  }

  private void init(JComboBox box) {
    jComboBox = box;
    jComboBox.setName("marcel");
    comboBox = new ComboBox(jComboBox);
  }

  public void testGetComponentTypeName() throws Exception {
    assertEquals("comboBox", comboBox.getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    assertEquals("<comboBox name=\"marcel\"/>", comboBox.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JComboBox(), ComboBox.class);
  }

  protected UIComponent createComponent() {
    return comboBox;
  }

  public void testCheckContent() throws Exception {
    assertTrue(comboBox.contentEquals("one", "two", "three"));
  }

  public void testCheckContentWithErrors() throws Exception {
    try {
      assertTrue(comboBox.contentEquals("one", "two", "unknown", "three"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
    }
  }

  public void testContains() throws Exception {
    assertTrue(comboBox.contains("two"));
    assertTrue(comboBox.contains("two", "one"));
  }

  public void test() throws Exception {
    try {
      assertTrue(comboBox.contains("unknown"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Item 'unknown' not found - actual content:[one, two, three]", e.getMessage());
    }

    try {
      assertTrue(comboBox.contains("three", "unknown"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Item 'unknown' not found - actual content:[one, two, three]", e.getMessage());
    }
  }

  public void testCheckContentWithSpecificJLabelRenderer() throws Exception {
    jComboBox.setRenderer(new DummyRenderer());
    assertTrue(comboBox.contentEquals("(one)", "(two)", "(three)"));
  }

  public void testCheckContentWithNoJLabelUsesTheModelValue() throws Exception {
    jComboBox.setRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return new JComboBox();
      }
    });
    assertTrue(comboBox.contentEquals("one", "two", "three"));
  }

  public void testUsingACustomCellRenderer() throws Exception {
    jComboBox.setRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (index == -1) {
          return new JLabel(value.toString());
        }
        else {
          return new JTextField("level " + value);
        }
      }
    });
    comboBox.setCellValueConverter(new ListBoxCellValueConverter() {
      public String getValue(int index, Component renderedComponent, Object modelObject) {
        if (index == -1) {
          return ((JLabel)renderedComponent).getText();
        }
        else {
          return ((JTextField)renderedComponent).getText();
        }
      }
    });

    assertTrue(comboBox.contentEquals("level one", "level two", "level three"));
    comboBox.select("level two");
    assertTrue(comboBox.selectionEquals("two"));
    try {
      comboBox.select("unknown");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
    }
    try {
      assertTrue(comboBox.isEmpty("<no item>"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Unexpected content: [level one,level two,level three]", e.getMessage());
    }
  }

  public void testAssertEmptyChecksTheDisplayedValue() throws Exception {
    jComboBox.setRenderer(new DummyRenderer());
    jComboBox.removeAllItems();
    assertTrue(comboBox.isEmpty("<no item>"));
  }

  public void testAssertEmptyFailures() throws Exception {
    try {
      assertTrue(comboBox.isEmpty("<no item>"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Unexpected content: [one,two,three]", e.getMessage());
    }

    jComboBox.setRenderer(new DummyRenderer());
    jComboBox.removeAllItems();
    try {
      assertTrue(comboBox.isEmpty("error"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("expected:<[error]> but was:<[<no item>]>", e.getMessage());
    }
  }

  public void testCheckSelectionUsesNullWhenNothingIsSelected() throws Exception {
    jComboBox.setSelectedIndex(-1);
    assertTrue(comboBox.selectionEquals(null));
  }

  public void testCheckSelectionUsesDisplayedNullValueWhenNothingIsSelected() throws Exception {
    jComboBox.setRenderer(new ListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return new JLabel("null text");
      }
    });
    jComboBox.setSelectedIndex(-1);
    assertTrue(comboBox.selectionEquals("null text"));
  }


  public void testClickSelectsTheFirstItem() throws Exception {
    jComboBox.setSelectedIndex(1);
    comboBox.click();
    assertEquals(0, jComboBox.getSelectedIndex());
  }

  public void testClickDoesNothingIfTheComboIsEmpty() throws Exception {
    init(new JComboBox());
    comboBox.click();
    assertEquals(-1, jComboBox.getSelectedIndex());
  }

  public void testBasicSelection() throws Exception {
    comboBox.select("two");
    assertEquals(1, jComboBox.getSelectedIndex());
    assertTrue(comboBox.selectionEquals("two"));
    comboBox.select("one");
    assertEquals(0, jComboBox.getSelectedIndex());
    assertTrue(comboBox.selectionEquals("one"));
  }

  public void testBasicSelectionWithCustomModel() throws Exception {
    jComboBox.setModel(new VerySimpleComboBoxModel("one", "two", "three"));
    comboBox.select("two");
    assertEquals(1, jComboBox.getSelectedIndex());
    assertTrue(comboBox.selectionEquals("two"));
    comboBox.select("one");
    assertEquals(0, jComboBox.getSelectedIndex());
    assertTrue(comboBox.selectionEquals("one"));
  }

  public void testSelectionIsNotCaseSensitive() throws Exception {
    comboBox.select("TwO");
    assertEquals(1, jComboBox.getSelectedIndex());
    assertTrue(comboBox.selectionEquals("two"));
    comboBox.select("oNe");
    assertEquals(0, jComboBox.getSelectedIndex());
    assertTrue(comboBox.selectionEquals("one"));
  }

  public void testSelectionWithSubstring() throws Exception {
    comboBox.select("tw");
    assertEquals(1, jComboBox.getSelectedIndex());
    assertTrue(comboBox.selectionEquals("two"));
    comboBox.select("ne");
    assertEquals(0, jComboBox.getSelectedIndex());
    assertTrue(comboBox.selectionEquals("one"));
  }

  public void testAmbiguityInSelection() throws Exception {
    try {
      comboBox.select("o");
      throw new AssertionFailureNotDetectedError();
    }
    catch (ItemAmbiguityException e) {
      assertEquals("2 items are matching the same pattern 'o': [one,two]", e.getMessage());
    }
  }

  public void testSelectingAnUnknownValueThrowsAnException() throws Exception {
    try {
      comboBox.select("unknown");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
    }
  }

  public void testCheckSelectedError() throws Exception {
    try {
      assertTrue(comboBox.selectionEquals("unknown"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
    }
  }

  public void testAssertEditable() throws Exception {
    jComboBox.setEditable(false);
    assertFalse(comboBox.isEditable());
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        assertTrue(comboBox.isEditable());
      }
    });
    jComboBox.setEditable(true);
    assertTrue(comboBox.isEditable());
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        assertFalse(comboBox.isEditable());
      }
    });
  }

  public void testSetTextIsAvailableOnlyWhenComponentIsEditable() {
    comboBox.select("two");
    assertTrue(comboBox.selectionEquals("two"));
    try {
      comboBox.setText("notInList");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("The combo box is not editable", e.getMessage());
    }
    assertTrue(comboBox.selectionEquals("two"));

    jComboBox.setEditable(true);
    comboBox.setText("notInList");
    assertEquals("notInList", jComboBox.getSelectedItem());
    assertTrue(comboBox.selectionEquals("notInList"));
    assertTrue(comboBox.contentEquals("one", "two", "three"));
    comboBox.select("one");
    assertTrue(comboBox.selectionEquals("one"));
  }

  public void testCheckSelectionUsesTheProperRendererIndex() throws Exception {
    jComboBox.setRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (index < 0) {
          super.setText("selected");
        }
        return this;
      }
    });
    assertTrue(comboBox.contentEquals("one", "two", "three"));
    comboBox.select("one");
    assertTrue(comboBox.selectionEquals("selected"));
    comboBox.select("two");
    assertTrue(comboBox.selectionEquals("selected"));
  }

  public void testExceptionThrownByTheModel() throws Exception {
    jComboBox.setModel(new DefaultComboBoxModel() {
      public int getSize() {
        return 1;
      }

      public Object getElementAt(int index) {
        throw new NullPointerException("boum");
      }
    });
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        assertTrue(comboBox.contentEquals(new String[1]));
      }
    }, "boum");
  }

  private static class DummyRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      String renderedValue = (index == -1) ? "<no item>" : "(" + (String)value + ")";
      return super.getListCellRendererComponent(list, renderedValue, index, isSelected, cellHasFocus);
    }
  }

  private static class VerySimpleComboBoxModel extends AbstractListModel implements ComboBoxModel{
    private final String[] content;
    private Object selectedObject = null;

    public VerySimpleComboBoxModel(String... content) {
      this.content = content;
    }

    public int getSize() {
      return content.length;
    }

    public Object getElementAt(int index) {
      return content[index];
    }

    public Object getSelectedItem() {
      return selectedObject;
    }

    public void setSelectedItem(Object anItem) {
      selectedObject = anItem;
    }
  }
}
