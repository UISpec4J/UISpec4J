package org.uispec4j.finder;

import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.*;

import javax.swing.*;
import java.awt.*;

public class PanelUIComponentFinderTest extends PanelComponentFinderTestCase {
  private JButton button;
  private static final ComponentMatcher CUSTOM_MATCHER = new ComponentMatcher() {
    public boolean matches(Component component) {
      String name = component.getName();
      return (name != null) && name.startsWith("custom");
    }
  };

  public void setUp() throws Exception {
    super.setUp();
    button = addComponent(JButton.class, "button1");
  }

  @Test
  public void testFindUIComponentByClass() throws Exception {
    assertNull(panel.findUIComponent(TextBox.class));

    Button uispecButton = panel.findUIComponent(Button.class);
    TestUtils.assertUIComponentRefersTo(this.button, uispecButton);

    addComponent(JButton.class, "button2");
    try {
      panel.findUIComponent(Button.class);
      fail();
    }
    catch (ComponentAmbiguityException e) {
      assertEquals(Messages.computeAmbiguityMessage(new String[]{"button1", "button2"}, Button.TYPE_NAME, null),
                   e.getMessage());
    }
  }

  @Test
  public void testFindUIComponentByName() throws Exception {
    assertNull(panel.findUIComponent(Button.class, "unknown"));
    assertNull(panel.findUIComponent(TextBox.class, "button"));
    TestUtils.assertUIComponentRefersTo(button, panel.findUIComponent(Button.class, "button1"));
    TestUtils.assertUIComponentRefersTo(button, panel.findUIComponent(Button.class, "button"));

    addComponent(JButton.class, "button2");
    try {
      panel.findUIComponent(Button.class, "button");
      fail();
    }
    catch (ComponentAmbiguityException e) {
      assertEquals(Messages.computeAmbiguityMessage(new String[]{"button1", "button2"}, Button.TYPE_NAME, "button"),
                   e.getMessage());
    }
  }

  @Test
  public void testFindUIComponentWithCustomComponentMatcher() throws Exception {
    assertNull(panel.findUIComponent(CUSTOM_MATCHER));

    button.setName("custom button");
    TestUtils.assertUIComponentRefersTo(button, panel.findUIComponent(CUSTOM_MATCHER));

    Component textField = addComponent(JTextField.class, "text");
    TestUtils.assertUIComponentRefersTo(button, panel.findUIComponent(CUSTOM_MATCHER));

    textField.setName("custom text");
    try {
      panel.findUIComponent(CUSTOM_MATCHER);
    }
    catch (ComponentAmbiguityException e) {
      assertEquals(Messages.computeAmbiguityMessage(new Component[]{button, textField},
                                                    null, null),
                   e.getMessage());
    }
  }

  @Test
  public void testGetUIComponentsByClass() throws Exception {
    JLabel label1 = addComponent(JLabel.class, "label1");
    JLabel label2 = addComponent(JLabel.class, "label2");
    JTextField jTextfield = addComponent(JTextField.class, "textField");

    TestUtils.assertUIComponentsReferTo(new Component[]{label2, jTextfield, label1},
                                        panel.getUIComponents(TextBox.class));

    TestUtils.assertUIComponentsReferTo(new Component[0],
                                        panel.getUIComponents(Table.class));

    TestUtils.assertSwingComponentsEquals(new Component[0],
                                          panel.getSwingComponents(JTable.class));
  }

  @Test
  public void testGetUIComponentsByName() throws Exception {
    JLabel label1 = addComponent(JLabel.class, "name1");
    JLabel label2 = addComponent(JLabel.class, "name2");
    JTextField jTextfield = addComponent(JTextField.class, "name2");
    addComponent(JLabel.class, "other label");
    addComponent(JTable.class, "table");

    TestUtils.assertUIComponentsReferTo(new Component[]{label1, label2, jTextfield},
                                        panel.getUIComponents(TextBox.class, "name"));
    TestUtils.assertSwingComponentsEquals(new JLabel[]{label1, label2},
                                          panel.getSwingComponents(JLabel.class, "name"));

    TestUtils.assertUIComponentsReferTo(new Component[0],
                                        panel.getUIComponents(Table.class, "name"));
    TestUtils.assertSwingComponentsEquals(new JTable[0],
                                          panel.getSwingComponents(JTable.class, "name"));
  }

  @Test
  public void testGetUIComponentsWithCustomComponentMatcher() throws Exception {
    TestUtils.assertUIComponentsReferTo(new Component[0],
                                        panel.getUIComponents(CUSTOM_MATCHER));

    button.setName("custom button");
    TestUtils.assertUIComponentsReferTo(new Component[]{button},
                                        panel.getUIComponents(CUSTOM_MATCHER));

    Component table = addComponent(JTable.class, "table");
    TestUtils.assertUIComponentsReferTo(new Component[]{button},
                                        panel.getUIComponents(CUSTOM_MATCHER));

    table.setName("custom table");
    TestUtils.assertUIComponentsReferTo(new Component[]{button, table},
                                        panel.getUIComponents(CUSTOM_MATCHER));
  }
}