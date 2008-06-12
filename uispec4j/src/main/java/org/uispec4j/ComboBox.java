package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.assertion.dependency.InternalAssert;
import org.uispec4j.finder.FinderUtils;
import org.uispec4j.finder.StringMatcher;
import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.xml.XmlWriter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Wrapper for JComboBox components.
 * This class provides means for checking the contents and selection of the comboBox,
 * changing the selection, etc. For all these methods, when using String values,
 * the means of retrieving a String representation of the displayed values can be customized
 * using the {@link #setCellValueConverter(ListBoxCellValueConverter)} method and providing
 * a new {@link ListBoxCellValueConverter} implementation.
 * A {@link DefaultListBoxCellValueConverter} is set up by default.
 */
public class ComboBox extends AbstractUIComponent {

  public static final String TYPE_NAME = "comboBox";
  public static final Class[] SWING_CLASSES = {JComboBox.class};
  private static final JList REFERENCE_JLIST = new JList();

  private JComboBox jComboBox;
  private ListBoxCellValueConverter cellValueConverter = new DefaultListBoxCellValueConverter();

  public ComboBox(JComboBox combo) {
    this.jComboBox = combo;
  }

  public JComboBox getAwtComponent() {
    return jComboBox;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public void setCellValueConverter(ListBoxCellValueConverter cellValueConverter) {
    this.cellValueConverter = cellValueConverter;
  }

  protected void getSubDescription(Container container, XmlWriter.Tag tag) {
    // ignore the combo inner button
  }

  /**
   * Selects the first item in the list, if not empty
   */
  public void click() {
    if (jComboBox.getModel().getSize() > 0) {
      jComboBox.setSelectedIndex(0);
    }
  }

  public void select(String value) {
    ListModel model = jComboBox.getModel();
    StringMatcher[] stringMatchers = FinderUtils.getMatchers(value);
    for (int i = 0; i < stringMatchers.length; i++) {
      StringMatcher stringMatcher = stringMatchers[i];
      List indexes = new ArrayList();
      for (int j = 0, max = model.getSize(); j < max; j++) {
        if (stringMatcher.matches(getRenderedValue(j))) {
          indexes.add(new Integer(j));
        }
      }
      if (indexes.size() == 1) {
        jComboBox.setSelectedIndex(((Integer)indexes.get(0)).intValue());
        return;
      }
      if (indexes.size() > 1) {
        String[] items = new String[indexes.size()];
        for (int j = 0; j < items.length; j++) {
          items[j] = getRenderedValue(j);
        }
        throw new ItemAmbiguityException(value, items);
      }
    }
    InternalAssert.fail(value + " not found in ComboBox");
  }

  /**
   * Changes the displayed text, in case of an editable combo box. <p/>
   * This method will throw an exception if the component is not editable.<p/>
   */
  public void setText(String text) {
    UISpecAssert.assertTrue(isEditable());
    jComboBox.setSelectedItem(text);
  }

  public Assertion contentEquals(final String... expected) {
    return new Assertion() {
      public void check() {
        ArrayUtils.assertEquals(expected, getContent());
      }
    };
  }

  public Assertion contains(final String item) {
    return contains(new String[]{item});
  }

  public Assertion contains(final String... items) {
    return new Assertion() {
      public void check() throws Exception {
        List content = Arrays.asList(getContent());
        for (int i = 0; i < items.length; i++) {
          String item = items[i];
          if (!content.contains(item)) {
            InternalAssert.fail("Item '" + item + "' not found - actual content:" + content);
          }
        }
      }
    };
  }

  /**
   * Checks that the combo box displays the given value and that it shows no elements when expanded.
   */
  public Assertion isEmpty(final String displayedValue) {
    return new Assertion() {
      public void check() {
        if (jComboBox.getItemCount() != 0) {
          InternalAssert.fail("Unexpected content: " + ArrayUtils.toString(getContent()));
        }
        InternalAssert.assertEquals(displayedValue, getRenderedValue(-1));
      }
    };
  }

  public Assertion selectionEquals(final String selection) {
    return new Assertion() {
      public void check() {
        if (jComboBox.getSelectedItem() == null) {
          InternalAssert.assertNull("The combo box has no selected item", selection);
        }
        else {
          InternalAssert.assertEquals(selection, getRenderedValue(jComboBox.getSelectedItem(), -1));
        }
      }
    };
  }

  public Assertion isEditable() {
    return new Assertion() {
      public void check() {
        if (!jComboBox.isEditable()) {
          InternalAssert.fail("The combo box is not editable");
        }
      }
    };
  }

  private String[] getContent() {
    ComboBoxModel model = jComboBox.getModel();
    String[] comboContents = new String[model.getSize()];
    for (int i = 0; i < comboContents.length; i++) {
      comboContents[i] = getRenderedValue(i);
    }
    return comboContents;
  }

  private String getRenderedValue(int index) {
    return getRenderedValue(jComboBox.getModel().getElementAt(index), index);
  }

  private String getRenderedValue(Object value, int index) {
    Component component =
      jComboBox.getRenderer().getListCellRendererComponent(REFERENCE_JLIST,
                                                           value, index, false, false);
    Object modelObject = (index == -1) ? null : jComboBox.getModel().getElementAt(index);
    return cellValueConverter.getValue(index, component, modelObject);
  }
}
