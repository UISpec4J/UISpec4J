package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.xml.EventLogger;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ListBoxTest extends UIComponentTestCase {
  private static final String[] ALL_ITEMS = {"First Item", "Second Item", "Third Item"};

  private JList jList;
  private ListBox listBox;

  protected void setUp() throws Exception {
    super.setUp();
    init(new JList(new Object[]{"First Item", "Second Item", "Third Item"}));
  }

  private void init(JList list) {
    jList = list;
    jList.setName("myList");
    listBox = new ListBox(jList);
  }

  public void testGetComponentTypeName() throws Exception {
    assertEquals("listBox", listBox.getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<listBox name='myList'/>", listBox.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JList(), ListBox.class);
  }

  protected UIComponent createComponent() {
    return listBox;
  }

  public void testEmptyList() throws Exception {
    init(new JList());
    assertTrue(listBox.isEmpty());
  }

  public void testAssertEmptyFailure() throws Exception {
    try {
      assertTrue(listBox.isEmpty());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("List should be empty but contains: [First Item,Second Item,Third Item]",
                   e.getMessage());
    }
  }

  public void testContentEquals() throws Exception {
    assertTrue(listBox.contentEquals(ALL_ITEMS));
  }

  public void testContentEqualsWithNullItem() throws Exception {
    init(new JList(new Object[]{"First Item", null}));
    listBox.contentEquals("First Item", "");
  }

  public void testContentEqualsErrors() throws Exception {
    try {
      assertTrue(listBox.contentEquals("another", "list", "here"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
    try {
      assertTrue(listBox.contentEquals("another", "list", "here", "with", "more", "elements"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
    try {
      assertTrue(listBox.contentEquals("one element only"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }

  public void testContains() throws Exception {
    init(new JList(new Object[]{"one", "two", "three"}));
    assertTrue(listBox.contains("two"));
    assertTrue(listBox.contains("three", "one"));
  }

  public void testContainsErrors() throws Exception {
    init(new JList(new Object[]{"one", "two", "three"}));
    try {
      assertTrue(listBox.contains("unknown"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Item 'unknown' not found - actual content:[one, two, three]", e.getMessage());
    }

    try {
      assertTrue(listBox.contains(new String[]{"three", "unknown"}));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Item 'unknown' not found - actual content:[one, two, three]", e.getMessage());
    }
  }

  public void testSelectByIndex() throws Exception {
    assertEquals(-1, jList.getSelectedIndex());
    listBox.selectIndices(new int[]{2});
    assertEquals(2, jList.getSelectedIndex());
  }

  public void testSelectByName() throws Exception {
    checkSelectionByName("First Item", "Second Item", "Third Item");
  }

  public void testSelectionIsNotCaseSensitive() throws Exception {
    checkSelectionByName("first iteM", "second Item", "THIRD iTem");
  }

  public void testSelectionWithSubstring() throws Exception {
    checkSelectionByName("first", "second", "ird it");
  }

  public void testAmbiguityInSelection() throws Exception {
    try {
      listBox.select("item");
      throw new AssertionFailureNotDetectedError();
    }
    catch (ItemAmbiguityException e) {
      assertEquals("3 items are matching the same pattern 'item': [First Item,Second Item,Third Item]", e.getMessage());
    }
  }

  public void testSelectingAnUnknownValueThrowsAnException() throws Exception {
    try {
      listBox.select("unknown");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }

  public void testClearSelection() throws Exception {
    jList.setSelectedIndex(1);
    listBox.clearSelection();
    assertTrue(listBox.selectionIsEmpty());
  }

  public void testSelectByNameThrowsAnExceptionIfTheItemIsNotFound() throws Exception {
    try {
      listBox.select("unknown");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Item 'unknown' not found in [First Item,Second Item,Third Item]",
                   e.getMessage());
    }
  }

  public void testMultiSelectionWithIndices() throws Exception {
    listBox.selectIndices(0, 1);

    int[] selectedIndices = jList.getSelectedIndices();
    assertEquals(2, selectedIndices.length);
    assertEquals(0, selectedIndices[0]);
    assertEquals(1, selectedIndices[1]);
  }

  public void testMultiSelectionWithNames() throws Exception {
    listBox.select("first", "second");

    int[] selectedIndices = jList.getSelectedIndices();
    assertEquals(2, selectedIndices.length);
    assertEquals(0, selectedIndices[0]);
    assertEquals(1, selectedIndices[1]);
  }

  public void testMultiSelectionSetsTheAdjustingMode() throws Exception {
    final EventLogger logger = new EventLogger();
    jList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
          logger.log("valueChanged");
        }
      }
    });
    listBox.select("first", "second");
    logger.assertEquals("<log><valueChanged/></log>");
    listBox.selectIndices(0, 1);
    logger.assertEquals("<log><valueChanged/></log>");
  }

  public void testMultiSelectionWithNamesWhenSomeNamesDoNotMatch() throws Exception {
    try {
      listBox.select(new String[]{"first", "third", "fourth"});
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Item 'fourth' not found in [First Item,Second Item,Third Item]", e.getMessage());
    }
  }

  public void testGetSize() throws Exception {
    assertEquals(3, listBox.getSize());
  }

  public void testAssertSelectionEmpty() throws Exception {
    jList.setSelectedIndex(-1);
    assertTrue(listBox.selectionIsEmpty());
    jList.setSelectedIndex(1);
    try {
      assertTrue(listBox.selectionIsEmpty());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Selection should be empty but is: [Second Item]", e.getMessage());
    }
  }

  public void testAssertSelection() throws Exception {
    jList.setSelectedIndex(0);
    assertTrue(listBox.selectionEquals("First Item"));
    jList.setSelectedIndex(1);
    assertTrue(listBox.selectionEquals("Second Item"));
    jList.setSelectedIndices(new int[]{0, 2});
    assertTrue(listBox.selectionEquals("First Item", "Third Item"));
  }

  public void testAssertSelectionErrors() throws Exception {
    jList.setSelectedIndex(0);
    try {
      assertTrue(listBox.selectionEquals("Second Item"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Expected: [Second Item]\n" +
                   "Actual:   [First Item]", e.getMessage());
    }

    try {
      assertTrue(listBox.selectionEquals(new String[]{"First Item", "Third Item"}));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Expected: [First Item,Third Item]\n" +
                   "Actual:   [First Item]", e.getMessage());
    }
  }

  public void testPressingKeyForNavigatingInTheList() throws Exception {
    jList.setSelectedIndex(0);
    assertTrue(listBox.selectionEquals("First Item"));
    listBox.pressKey(Key.DOWN);
    assertTrue(listBox.selectionEquals("Second Item"));
    listBox.pressKey(Key.DOWN);
    assertTrue(listBox.selectionEquals("Third Item"));
    listBox.pressKey(Key.UP);
    assertTrue(listBox.selectionEquals("Second Item"));
  }

  public void testUsingShiftToSelectMultipleElements() throws Exception {
    jList.setSelectedIndex(0);
    listBox.pressKey(Key.shift(Key.DOWN));
    assertTrue(listBox.selectionEquals("First Item", "Second Item"));
    listBox.pressKey(Key.shift(Key.DOWN));
    assertTrue(listBox.selectionEquals(ALL_ITEMS));
  }

  public void testPressingDownAndUpKeysChangesTheSelection() throws Exception {
    DummyKeyListener keyListener = new DummyKeyListener();
    jList.addKeyListener(keyListener);
    jList.setSelectedIndex(0);
    assertTrue(listBox.selectionEquals("First Item"));
    listBox.pressKey(Key.DOWN);
    keyListener.checkEvents("keyPressed");
    assertTrue(listBox.selectionEquals("Second Item"));
    listBox.pressKey(Key.UP);
    keyListener.checkEvents("keyPressed");
    assertTrue(listBox.selectionEquals("First Item"));
  }

  public void testUsingARenderer() throws Exception {
    init(new JList(new Object[]{new Integer(3), new Integer(7), new Integer(11)}));
    jList.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list,
                                                    Object value,
                                                    int index,
                                                    boolean isSelected,
                                                    boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, "-" + value, index, isSelected, cellHasFocus);
      }
    });
    assertTrue(listBox.contentEquals(new String[]{"-3", "-7", "-11"}));
    listBox.selectIndex(0);
    assertTrue(listBox.selectionEquals("-3"));
    listBox.select("-7");
    assertTrue(listBox.selectionEquals("-7"));
  }

  public void testUsingACustomCellRenderer() throws Exception {
    init(new JList(new Object[]{new Integer(3), new Integer(7)}));
    jList.setCellRenderer(new ListCellRenderer() {
      public Component getListCellRendererComponent(JList list,
                                                    Object value,
                                                    int index,
                                                    boolean isSelected,
                                                    boolean cellHasFocus) {
        return new JButton("-" + value);
      }
    });
    listBox.setCellValueConverter(new ListBoxCellValueConverter() {
      public String getValue(int index, Component renderedComponent, Object modelObject) {
        return ((JButton)renderedComponent).getText();
      }
    });
    assertTrue(listBox.contentEquals(new String[]{"-3", "-7"}));
    listBox.selectIndex(0);
    assertTrue(listBox.selectionEquals("-3"));
    listBox.select("-7");
    assertTrue(listBox.selectionEquals("-7"));
  }

  public void testAssertContentEqualsAfterSettingACustomCellValueConverter() throws Exception {
    assertTrue(listBox.contentEquals(ALL_ITEMS));

    listBox.setCellValueConverter(new ListBoxCellValueConverter() {
      public String getValue(int index, Component renderedComponent, Object modelObject) {
        if (index == 0) {
          return "Item converted";
        }
        return modelObject.toString();
      }
    });
    assertTrue(listBox.contentEquals(new String[]{"Item converted", "Second Item", "Third Item"}));

    try {
      assertTrue(listBox.contentEquals(ALL_ITEMS));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      // Expected
    }

    try {
      assertTrue(listBox.isEmpty());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("List should be empty but contains: [Item converted,Second Item,Third Item]",
                   e.getMessage());
    }
  }

  public void testSelectionAfterSettingACustomCellValueConverter() throws Exception {
    assertTrue(listBox.contentEquals(ALL_ITEMS));

    listBox.setCellValueConverter(new ListBoxCellValueConverter() {
      public String getValue(int index, Component renderedComponent, Object modelObject) {
        if (index == 0) {
          return "Item converted";
        }
        return modelObject.toString();
      }
    });

    listBox.select("Item converted");

    assertTrue(listBox.selectionEquals("Item converted"));
    assertTrue(listBox.selectionEquals(new String[]{"Item converted"}));

    try {
      listBox.select("First Item");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Item 'First Item' not found in [Item converted,Second Item,Third Item]",
                   e.getMessage());
    }
  }

  public void testCellForegroundAndBackground() throws Exception {
    jList.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (index == 1) {
          component.setForeground(Color.red);
          component.setBackground(Color.pink);
        }
        return component;
      }
    });

    assertTrue(listBox.foregroundEquals(new String[]{"black", "red", "black"}));

    assertTrue(listBox.backgroundEquals(new String[]{"white", "pink", "white"}));

    try {
      assertTrue(listBox.backgroundEquals(new String[]{"white", "blue", "white"}));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Error at index 1 - expected:<BLUE> but was:<FFAFAF>", e.getMessage());
    }

    assertTrue(listBox.foregroundNear(0, "black"));
    assertTrue(listBox.foregroundNear(0, "010101"));

    assertTrue(listBox.backgroundNear(0, "white"));
    assertTrue(listBox.backgroundNear(0, "FEFEFE"));

    assertTrue(listBox.foregroundNear(1, "red"));
    assertTrue(listBox.foregroundNear(1, "DD1111"));

    assertTrue(listBox.backgroundNear(1, "pink"));
    assertTrue(listBox.backgroundNear(1, "FFAEAE"));
  }

  private void checkSelectionByName(String... names) {
    listBox.select(names[0]);
    assertTrue(listBox.selectionEquals("First Item"));
    assertEquals(0, jList.getSelectedIndex());
    listBox.select(names[1]);
    assertTrue(listBox.selectionEquals("Second Item"));
    assertEquals(1, jList.getSelectedIndex());
    listBox.select(names[2]);
    assertTrue(listBox.selectionEquals("Third Item"));
    assertEquals(2, jList.getSelectedIndex());
  }
}
