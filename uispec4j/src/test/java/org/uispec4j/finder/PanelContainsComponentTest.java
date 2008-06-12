package org.uispec4j.finder;

import org.uispec4j.Button;
import org.uispec4j.TextBox;

import javax.swing.*;
import java.awt.*;

public class PanelContainsComponentTest extends PanelComponentFinderTestCase {
  private JButton button;

  private static final ComponentMatcher CUSTOM_MATCHER = new ComponentMatcher() {
    public boolean matches(Component component) {
      String name = component.getName();
      return (name != null) && name.startsWith("custom");
    }
  };

  protected void setUp() throws Exception {
    super.setUp();
    button = (JButton) addComponent(JButton.class, "button1");
  }

  public void testContainsComponentByClass() throws Exception {
    assertFalse(panel.containsUIComponent(TextBox.class));
    assertFalse(panel.containsSwingComponent(JTextField.class));

    assertTrue(panel.containsUIComponent(Button.class));
    assertTrue(panel.containsSwingComponent(JButton.class));

    assertFalse(panel.containsUIComponent(JButton.class));
    assertFalse(panel.containsSwingComponent(Button.class));

    addComponent(JButton.class, "button2");
    assertTrue(panel.containsUIComponent(Button.class));
    assertTrue(panel.containsSwingComponent(JButton.class));
  }

  public void testContainsComponentByName() throws Exception {
    assertFalse(panel.containsUIComponent(Button.class, "unknown"));
    assertFalse(panel.containsSwingComponent(JTextField.class, "button"));

    assertTrue(panel.containsUIComponent(Button.class, "button1"));
    assertTrue(panel.containsUIComponent(Button.class, "button"));
    assertTrue(panel.containsSwingComponent(JButton.class, "button1"));
    assertTrue(panel.containsSwingComponent(JButton.class, "button"));

    addComponent(JButton.class, "button2");
    assertTrue(panel.containsUIComponent(Button.class, "button"));
    assertTrue(panel.containsSwingComponent(JButton.class, "button"));
  }

  public void testContainsComponentWithCustomComponentMatcher() throws Exception {
    assertFalse(panel.containsComponent(CUSTOM_MATCHER));

    button.setName("custom button");
    assertTrue(panel.containsComponent(CUSTOM_MATCHER));

    Component textField = addComponent(JTextField.class, "text");
    assertTrue(panel.containsComponent(CUSTOM_MATCHER));

    textField.setName("custom text");
    assertTrue(panel.containsComponent(CUSTOM_MATCHER));
  }
}