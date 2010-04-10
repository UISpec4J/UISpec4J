package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.finder.StringMatcher;
import org.uispec4j.utils.ColorUtils;
import org.uispec4j.xml.XmlWriter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Wrapper for JTabbedPane components.
 */
public class TabGroup extends AbstractUIComponent {
  public static final String TYPE_NAME = "tabGroup";
  public static final Class[] SWING_CLASSES = {JTabbedPane.class};

  private JTabbedPane jTabbedPane;

  public TabGroup(JTabbedPane jTab) {
    this.jTabbedPane = jTab;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public JTabbedPane getAwtComponent() {
    return jTabbedPane;
  }

  public Panel getSelectedTab() {
    Component selectedComponent = jTabbedPane.getSelectedComponent();
    if (!JPanel.class.isInstance(selectedComponent)) {
      AssertAdapter.fail("tabGroup.getSelectedTab() only supports JPanel components inside a JTabbedPane");
    }
    return new Panel((JPanel)selectedComponent);
  }

  public void selectTab(String tabLabel) {
    final int index = getTabIndex(tabLabel);
    AssertAdapter.assertTrue(tabNotFound(tabLabel), index >= 0);
    jTabbedPane.setSelectedIndex(index);
    UISpecAssert.assertTrue(new Assertion() {
      public void check() {
        AssertAdapter.assertTrue(jTabbedPane.getSelectedIndex() == index);
      }
    });
  }

  public Assertion tabColorEquals(final String[] colors) {
    return new Assertion() {
      public void check() {
        int tabCount = jTabbedPane.getTabCount();
        AssertAdapter.assertEquals("You specified " + colors.length + " colors but there are " +
                                    tabCount + " tabs -",
                                    colors.length, tabCount);
        for (int i = 0; i < colors.length; i++) {
          String color = colors[i];
          if (!ColorUtils.equals(color, jTabbedPane.getForegroundAt(i))) {
            AssertAdapter.fail("Unexpected color for tab '" + jTabbedPane.getTitleAt(i) +
                                "' (index " + i + ") - expected " + ColorUtils.getColorDescription(color) +
                                " but was " + ColorUtils.getColorDescription(jTabbedPane.getForegroundAt(i)));
          }
        }
      }
    };
  }

  public Assertion selectedTabEquals(final String tabLabel) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(tabLabel, jTabbedPane.getTitleAt(jTabbedPane.getSelectedIndex()));
      }
    };
  }

  public Assertion tabNamesEquals(final String[] tabLabels) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(tabLabels.length, jTabbedPane.getTabCount());
        for (int i = 0; i < tabLabels.length; i++) {
          AssertAdapter.assertEquals(tabLabels[i], jTabbedPane.getTitleAt(i));
        }
      }
    };
  }

  protected void getSubDescription(Container container, XmlWriter.Tag tag) {
    if (container == jTabbedPane) {
      Component selectedComponent = jTabbedPane.getSelectedComponent();
      if (selectedComponent != null) {
        getDescription(selectedComponent, tag, false);
        return;
      }
    }
    else {
      super.getSubDescription(container, tag);
    }
  }

  private int getTabIndex(String tabLabel) {
    int index = jTabbedPane.indexOfTab(tabLabel);
    if (index >= 0) {
      return index;
    }
    for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
      String title = jTabbedPane.getTitleAt(i);
      if (StringMatcher.substring(tabLabel).matches(title)) {
        return i;
      }
    }
    return -1;
  }

  private String tabNotFound(String name) {
    return "There is no tab labelled '" + name + "' - existing tab names: " + getTabNames();
  }

  private java.util.List<String> getTabNames() {
    java.util.List<String> result = new ArrayList<String>();
    for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
      result.add(jTabbedPane.getTitleAt(i));
    }
    return result;
  }
}
