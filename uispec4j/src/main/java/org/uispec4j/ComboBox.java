package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
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
public class ComboBox extends AbstractSwingUIComponent {

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
    for (StringMatcher stringMatcher : stringMatchers) {
      List<Integer> indexes = new ArrayList<Integer>();
      for (int modelIndex = 0, max = model.getSize(); modelIndex < max; modelIndex++) {
        if (stringMatcher.matches(getRenderedValue(modelIndex))) {
          indexes.add(modelIndex);
        }
      }
      if (indexes.size() == 1) {
        jComboBox.setSelectedIndex(indexes.get(0));
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

    List content = Arrays.asList(getContent());
    AssertAdapter.fail(value + " not found in ComboBox - actual content: " + content);
  }

  /**
   * <p>Changes the displayed text, in case of an editable combo box. </p>
   * <p>This method will throw an exception if the component is not editable.</p>
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

  /**
   * Checks that the combo box displays the given value and that it shows no elements when expanded.
   */
  public Assertion isEmpty(final String displayedValue) {
    return new Assertion() {
      public void check() {
        if (jComboBox.getItemCount() != 0) {
          AssertAdapter.fail("Unexpected content: " + ArrayUtils.toString(getContent()));
        }
        AssertAdapter.assertEquals(displayedValue, getRenderedValue(-1));
      }
    };
  }

  public Assertion selectionEquals(final String selection) {
    return new Assertion() {
      public void check() {
        if (jComboBox.getSelectedItem() == null) {
          if (selection != null) {
            AssertAdapter.assertEquals(selection, getRenderedValue(-1));
          }
        }
        else {
          AssertAdapter.assertEquals(selection, getRenderedValue(jComboBox.getSelectedItem(), -1));
        }
      }
    };
  }

  public Assertion isEditable() {
    return new Assertion() {
      public void check() {
        if (!jComboBox.isEditable()) {
          AssertAdapter.fail("The combo box is not editable");
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
