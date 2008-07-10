package org.uispec4j;

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
    private final Component comp = new Label();

    DummyAwtUIComponent(String name) {
      comp.setName(name);
    }

    public Component getAwtComponent() {
      return comp;
    }

    public String getDescriptionTypeName() {
      return "awtLabel";
    }
  }

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
}
