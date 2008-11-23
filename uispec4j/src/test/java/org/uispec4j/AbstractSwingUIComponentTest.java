package org.uispec4j;

import junit.framework.TestCase;
import org.uispec4j.assertion.UISpecAssert;

import javax.swing.*;

public class AbstractSwingUIComponentTest extends TestCase {

  public void testTooltips() throws Exception {
    DummySwingUIComponent component = new DummySwingUIComponent("label");
    component.getAwtComponent().setToolTipText("Hello world!");

    UISpecAssert.assertThat(component.tooltipEquals("Hello world!"));
    UISpecAssert.assertFalse(component.tooltipEquals("hello world!"));
    UISpecAssert.assertFalse(component.tooltipEquals("Hello world"));

    UISpecAssert.assertThat(component.tooltipContains("world"));
    UISpecAssert.assertFalse(component.tooltipContains("something else"));
  }

  private static final class DummySwingUIComponent extends AbstractSwingUIComponent {
    private final JComponent component = new JLabel();

    DummySwingUIComponent(String name) {
      component.setName(name);
    }

    public JComponent getAwtComponent() {
      return component;
    }

    public String getDescriptionTypeName() {
      return "awtLabel";
    }
  }

}
