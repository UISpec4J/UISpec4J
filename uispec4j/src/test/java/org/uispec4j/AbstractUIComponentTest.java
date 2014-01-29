package org.uispec4j;

import org.junit.Test;
import org.uispec4j.utils.ColorUtils;
import org.uispec4j.utils.UnitTestCase;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;
import java.awt.*;

public class AbstractUIComponentTest extends UnitTestCase {

  public void testGetComponentTypeName() throws Exception {
    XmlAssert.assertEquivalent("<awtLabel name='myName'/>",
                               new DummyAwtUIComponent("myName").getDescription());
  }

  private static final class DummyAwtUIComponent extends AbstractUIComponent {
    private final Component component = new Label();

    DummyAwtUIComponent(String name) {
      component.setName(name);
    }

    public Component getAwtComponent() {
      return component;
    }

    public String getDescriptionTypeName() {
      return "awtLabel";
    }
  }

  @Test
  public void testComponentUsesBlackAsDefaultForegroundColor() throws Exception {
    final JComponent jComponent = new JComponent() {
    };
    AbstractUIComponent uiComponent = new AbstractUIComponent() {
      public Component getAwtComponent() {
        return jComponent;
      }

      public String getDescriptionTypeName() {
        return "";
      }
    };

    assertTrue(uiComponent.foregroundEquals("black"));
  }

  @Test
  public void testForeground() throws Exception {
    DummyAwtUIComponent uiComponent = new DummyAwtUIComponent("name");
    uiComponent.component.setForeground(ColorUtils.getColor("2255F7"));

    assertTrue(uiComponent.foregroundEquals("2255F7"));
    assertFalse(uiComponent.foregroundEquals("2255F8"));

    assertTrue(uiComponent.foregroundNear("2255F7"));
    assertTrue(uiComponent.foregroundNear("2255F8"));
    assertFalse(uiComponent.foregroundNear("FF5500"));

    assertTrue(uiComponent.foregroundNear("blue"));
    assertFalse(uiComponent.foregroundNear("red"));
  }

  @Test
  public void testBackground() throws Exception {
    DummyAwtUIComponent uiComponent = new DummyAwtUIComponent("name");
    uiComponent.component.setBackground(ColorUtils.getColor("2255F7"));

    assertTrue(uiComponent.backgroundEquals("2255F7"));
    assertFalse(uiComponent.backgroundEquals("2255F8"));

    assertTrue(uiComponent.backgroundNear("2255F7"));
    assertTrue(uiComponent.backgroundNear("2255F8"));
    assertFalse(uiComponent.backgroundNear("FF5500"));

    assertTrue(uiComponent.backgroundNear("blue"));
    assertFalse(uiComponent.backgroundNear("red"));
  }

  @Test
  public void testGetContainer() throws Exception {
    JPanel rootPanel = new JPanel();
    rootPanel.setName("rootPanel");
    JPanel middlePanel = new JPanel();
    rootPanel.add(middlePanel);
    JLabel jLabel = new JLabel();
    middlePanel.add(jLabel);
    TextBox label = new TextBox(jLabel);

    assertSame(middlePanel, label.getContainer().getAwtComponent());

    assertSame(rootPanel, label.getContainer("rootPanel").getAwtComponent());
    assertNull(label.getContainer("unknown"));

    assertNull(new Panel(rootPanel).getContainer());
  }
}
