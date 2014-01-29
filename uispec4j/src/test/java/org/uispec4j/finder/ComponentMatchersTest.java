package org.uispec4j.finder;

import org.junit.Test;
import org.uispec4j.ComponentAmbiguityException;
import org.uispec4j.ItemNotFoundException;
import org.uispec4j.TestUtils;

import javax.swing.*;
import java.awt.*;

public class ComponentMatchersTest extends PanelComponentFinderTestCase {
  private JButton button1;
  private JButton button2;
  private JTextField textField;
  private Component otherButton;

  public void setUp() throws Exception {
    super.setUp();
    button1 = addComponent(JButton.class, "displayed1");
    button1.setName("inner1");
    button2 = addComponent(JButton.class, "displayed2");
    button2.setName("inner2");
    textField = addComponent(JTextField.class, "displayed1");
    textField.setName("inner1");
    otherButton = addComponent(JButton.class, "other");
    otherButton.setName("else");
  }

  @Test
  public void testClassComponentMatcher() throws Exception {
    TestUtils.assertSwingComponentsEquals(new JTextField[]{textField},
                                          panel.getSwingComponents(ComponentMatchers.fromClass(JTextField.class)));
  }

  @Test
  public void testDisplayedNameIdentity() throws Exception {
    TestUtils.assertUIComponentRefersTo(button2,
                                        panel.getButton(ComponentMatchers.displayedNameIdentity("displayed2")));

    try {
      panel.getButton(ComponentMatchers.displayedNameIdentity("displayed"));
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals("No component found", e.getMessage());
    }

    try {
      panel.getButton(ComponentMatchers.displayedNameIdentity("inner2"));
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals("No component found", e.getMessage());
    }

    TestUtils.assertSwingComponentsEquals(new Component[]{button1, textField},
                                          panel.getSwingComponents(ComponentMatchers.displayedNameIdentity("displayed1")));
  }

  @Test
  public void testDisplayedNameSubstring() throws Exception {
    TestUtils.assertUIComponentRefersTo(button2,
                                        panel.getButton(ComponentMatchers.displayedNameSubstring("displayed2")));

    try {
      panel.getButton(ComponentMatchers.displayedNameSubstring("displayed"));
      fail();
    }
    catch (ComponentAmbiguityException e) {
      assertEquals(Messages.computeAmbiguityMessage(new Component[]{button1, button2}, null, null), e.getMessage());
    }

    assertNull(panel.findSwingComponent(ComponentMatchers.displayedNameSubstring("inner")));

    TestUtils.assertSwingComponentsEquals(new Component[]{button1, button2, textField},
                                          panel.getSwingComponents(ComponentMatchers.displayedNameSubstring("displayed")));
  }

  @Test
  public void testDisplayedNameRegexp() throws Exception {
    TestUtils.assertUIComponentRefersTo(button2,
                                        panel.getButton(ComponentMatchers.displayedNameRegexp("displaye.?2")));

    try {
      panel.getButton(ComponentMatchers.displayedNameRegexp("displayed.?"));
      fail();
    }
    catch (ComponentAmbiguityException e) {
      assertEquals(Messages.computeAmbiguityMessage(new Component[]{button1, button2}, null, null), e.getMessage());
    }

    try {
      panel.getButton(ComponentMatchers.displayedNameRegexp("inn.*"));
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals("No component found", e.getMessage());
    }

    TestUtils.assertSwingComponentsEquals(new Component[]{button1, button2, textField},
                                          panel.getSwingComponents(ComponentMatchers.displayedNameRegexp("dis.*")));
  }

  @Test
  public void testInnerNameIdentity() throws Exception {
    TestUtils.assertUIComponentRefersTo(button2,
                                        panel.getButton(ComponentMatchers.innerNameIdentity("inner2")));

  }

  @Test
  public void testInnerNameSubstring() throws Exception {
    TestUtils.assertSwingComponentsEquals(new Component[]{button1, button2, textField},
                                          panel.getSwingComponents(ComponentMatchers.innerNameSubstring("inner")));
  }

  @Test
  public void testInnerNameRegexp() throws Exception {
    TestUtils.assertUIComponentRefersTo(button2,
                                        panel.getButton(ComponentMatchers.innerNameRegexp("inne.?2")));

    try {
      panel.getButton(ComponentMatchers.innerNameRegexp("disp.*"));
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals("No component found", e.getMessage());
    }

    TestUtils.assertSwingComponentsEquals(new Component[]{button1, button2, textField},
                                          panel.getSwingComponents(ComponentMatchers.innerNameRegexp("inn.*")));
  }

  @Test
  public void testComponentLabelFor() throws Exception {
    JLabel labelForButton2 = addComponent(JLabel.class, "this is the second button");
    labelForButton2.setLabelFor(button2);

    TestUtils.assertUIComponentRefersTo(button2,
                                        panel.getButton(ComponentMatchers.componentLabelFor("second button")));
  }

  @Test
  public void testSearchWithTooltip() throws Exception {
    button1.setToolTipText("button 1");

    TestUtils.assertUIComponentRefersTo(button1,
                                        panel.getButton(ComponentMatchers.toolTipEquals("button 1")));

    TestUtils.assertSwingComponentsEquals(new Component[]{button2, textField, otherButton},
                                          panel.getSwingComponents(ComponentMatchers.toolTipEquals(null)));
  }
}
