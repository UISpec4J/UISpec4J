package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.finder.FinderUtils;
import org.uispec4j.finder.StringMatcher;
import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.utils.ColorUtils;
import org.uispec4j.utils.ComponentColorChecker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Wrapper for JList components.</p>
 * This class provides means for checking the contents and selection of the list,
 * changing the selection, etc. For all these methods, when using String values,
 * the means of retrieving a String representation of the displayed values can be customized
 * using the {@link #setCellValueConverter(ListBoxCellValueConverter)} method and providing
 * a new {@link ListBoxCellValueConverter} implementation.
 * A {@link DefaultListBoxCellValueConverter} is set up by default.
 */
public class ListBox extends AbstractSwingUIComponent {
  public static final String TYPE_NAME = "listBox";
  public static final Class[] SWING_CLASSES = {JList.class};

  private JList jList;
  private ListBoxCellValueConverter cellValueConverter = new DefaultListBoxCellValueConverter();

  public ListBox(JList list) {
    this.jList = list;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public JList getAwtComponent() {
    return jList;
  }

  public void setCellValueConverter(ListBoxCellValueConverter cellValueConverter) {
    this.cellValueConverter = cellValueConverter;
  }

  public Assertion isEmpty() {
    return new Assertion() {
      public void check() {
        if (getSize() != 0) {
          AssertAdapter.fail("List should be empty but contains: " + ArrayUtils.toString(getContent()));
        }
      }
    };
  }

  public Assertion contentEquals(final String... displayedValues) {
    return new Assertion() {
      public void check() {
        ArrayUtils.assertEquals(displayedValues, getContent());
      }
    };
  }

  public Assertion contains(String item) {
    return contains(new String[]{item});
  }

  public Assertion contains(final String... items) {
    return new Assertion() {
      public void check() {
        List content = Arrays.asList(getContent());
        for (String item : items) {
          if (!content.contains(item)) {
            AssertAdapter.fail("Item '" + item + "' not found - actual content:" + content);
          }
        }
      }
    };
  }

  public void selectIndex(int index) {
    jList.setSelectedIndex(index);
  }

  public void selectIndices(int... indices) {
    jList.getSelectionModel().setValueIsAdjusting(true);
    jList.setSelectedIndices(indices);
    jList.getSelectionModel().setValueIsAdjusting(false);
  }

  public void select(String... values) {
    int[] indices = new int[values.length];
    for (int i = 0; i < values.length; i++) {
      indices[i] = getIndexForString(values[i]);
      if (indices[i] == -1) {
        AssertAdapter.fail("Item '" + values[i] + "' not found in " +
                           ArrayUtils.toString(getContent()));
      }
    }
    selectIndices(indices);
  }

  public void clearSelection() {
    jList.clearSelection();
  }

  public int getSize() {
    return jList.getModel().getSize();
  }

  public void doubleClick() {
    Mouse.doubleClick(this);
  }

  public void click(int row) {
    click(row, Key.Modifier.NONE);
  }

  public void click(int row, Key.Modifier modifier) {
    Rectangle rect = jList.getCellBounds(row, row);
    Mouse.doClickInRectangle(this, rect, false, modifier);
  }

  public void rightClick(int row) {
    Rectangle rect = jList.getCellBounds(row, row);
    Mouse.doClickInRectangle(this, rect, true, Key.Modifier.NONE);
  }

  public void doubleClick(int row) {
    Rectangle rect = jList.getCellBounds(row, row);
    Mouse.doDoubleClickInRectangle(jList, rect);
  }

  public Trigger triggerClick(final int row, final Key.Modifier modifier) {
    return new Trigger() {
      public void run() throws Exception {
        click(row, modifier);
      }
    };
  }

  public Trigger triggerRightClick(final int row) {
    return new Trigger() {
      public void run() throws Exception {
        rightClick(row);
      }
    };
  }

  public Trigger triggerDoubleClick(final int row) {
    return new Trigger() {
      public void run() throws Exception {
        doubleClick(row);
      }
    };
  }

  public Assertion selectionIsEmpty() {
    return new Assertion() {
      public void check() {
        if (jList.getSelectedIndices().length != 0) {
          String[] names = getSelectedItemNames();
          AssertAdapter.fail("Selection should be empty but is: " + ArrayUtils.toString(names));
        }
      }
    };
  }

  public Assertion selectionEquals(final String... items) {
    return new Assertion() {
      public void check() {
        ArrayUtils.assertEquals(items, getSelectedItemNames());
      }
    };
  }

  /**
   * Checks the foreground color of the table cells using either Color or String objects
   *
   * @see <a href="http://www.uispec4j.org/usingcolors.html">Using colors</a>
   */
  public Assertion foregroundEquals(final Object[] colors) {
    return new Assertion() {
      public void check() {
        checkColors(colors, ComponentColorChecker.FOREGROUND);
      }
    };
  }

  public Assertion foregroundNear(final int index, final Object expected) {
    return new Assertion() {
      public void check() {
        final Component component = getSwingRendererComponentAt(index);
        ColorUtils.assertSimilar("Error at (" + index + ")",
                                 expected, component.getForeground());
      }
    };
  }

  public Assertion backgroundNear(final int index, final Object expected) {
    return new Assertion() {
      public void check() {
        final Component component = getSwingRendererComponentAt(index);
        ColorUtils.assertSimilar("Error at (" + index + ")",
                                 expected, component.getBackground());
      }
    };
  }

  /**
   * Checks the background color of the List cells using either Color or String objects
   *
   * @see <a href="http://www.uispec4j.org/usingcolors.html">Using colors</a>
   */
  public Assertion backgroundEquals(final Object[] colors) {
    return new Assertion() {
      public void check() {
        checkColors(colors, ComponentColorChecker.BACKGROUND);
      }
    };
  }

  public Component getSwingRendererComponentAt(int index) {
    ListCellRenderer cellRenderer = jList.getCellRenderer();
    return cellRenderer.getListCellRendererComponent(jList,
                                                     jList.getModel().getElementAt(index),
                                                     index,
                                                     jList.isSelectedIndex(index),
                                                     false);
  }

  private void checkColors(Object[] colors, ComponentColorChecker colorChecker) {
    AssertAdapter.assertEquals(colors.length, jList.getModel().getSize());
    for (int index = 0; index < colors.length; index++) {
      colorChecker.check("Error at index " + index, colors[index], getSwingRendererComponentAt(index));
    }
  }

  private String[] getContent() {
    String[] names = new String[jList.getModel().getSize()];
    for (int i = 0, max = jList.getModel().getSize(); i < max; i++) {
      names[i] = getRenderedValue(i);
    }
    return names;
  }

  private String[] getSelectedItemNames() {
    int[] selectedIndices = jList.getSelectedIndices();
    String[] names = new String[selectedIndices.length];
    for (int i = 0; i < selectedIndices.length; i++) {
      names[i] = getRenderedValue(selectedIndices[i]);
    }
    return names;
  }

  private String getRenderedValue(int index) {
    return cellValueConverter.getValue(index, getComponent(index), jList.getModel().getElementAt(index));
  }

  private Component getComponent(int index) {
    ListCellRenderer renderer = jList.getCellRenderer();
    return renderer.getListCellRendererComponent(jList,
                                                 jList.getModel().getElementAt(index),
                                                 index, false, false);
  }

  private int getIndexForString(String searchedValue) {
    StringMatcher[] matchers = FinderUtils.getMatchers(searchedValue);
    for (StringMatcher matcher : matchers) {
      List<Integer> indexes = new ArrayList<Integer>();
      for (int listIndex = 0, max = jList.getModel().getSize(); listIndex < max; listIndex++) {
        String renderedValue = getRenderedValue(listIndex);
        if (matcher.matches(renderedValue)) {
          indexes.add(listIndex);
        }
      }
      if (indexes.size() == 1) {
        return indexes.get(0);
      }
      if (indexes.size() > 1) {
        String[] items = new String[indexes.size()];
        for (int j = 0; j < items.length; j++) {
          items[j] = getRenderedValue(j);
        }
        throw new ItemAmbiguityException(searchedValue, items);
      }
    }
    return -1;
  }
}
