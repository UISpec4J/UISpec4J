package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.utils.ColorUtils;
import org.uispec4j.utils.KeyUtils;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlWriter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.StringWriter;

/**
 * Base class for UIComponent implementations.
 */
public abstract class AbstractUIComponent implements UIComponent {

  public final String getDescription() {
    StringWriter writer = new StringWriter();
    XmlWriter.Tag tag = XmlWriter.startTag(writer, getDescriptionTypeName());
    Component component = getAwtComponent();
    addAttributes(component, tag);
    if (component instanceof Container) {
      getSubDescription((Container)component, tag);
    }
    tag.end();
    return writer.toString();
  }

  protected void getSubDescription(Container container, XmlWriter.Tag tag) {
    Component[] components = container.getComponents();
    for (Component component : components) {
      getDescription(component, tag, true);
    }
  }

  protected void getDescription(Component component, XmlWriter.Tag tag, boolean showVisibleOnly) {
    if (!JComponent.class.isInstance(component)) {
      return;
    }
    JComponent jComponent = (JComponent)component;
    if (showVisibleOnly && !jComponent.isVisible()) {
      return;
    }
    if (jComponent instanceof JScrollPane) {
      getSubDescription(((JScrollPane)jComponent).getViewport(), tag);
      return;
    }
    AbstractUIComponent guiComponent =
      (AbstractUIComponent)UIComponentFactory.createUIComponent(jComponent);
    if ((guiComponent == null) || isPanelWithNoName(guiComponent)) {
      getSubDescription(jComponent, tag);
      return;
    }
    XmlWriter.Tag childTag = tag.start(guiComponent.getDescriptionTypeName());
    guiComponent.addAttributes(jComponent, childTag);
    guiComponent.getSubDescription(jComponent, childTag);
    childTag.end();
  }

  protected void addAttributes(Component component, XmlWriter.Tag tag) {
    if ((component.getName() != null) && (component.getName().length() != 0)) {
      tag.addAttribute("name", computeComponentName(component));
    }
    String label = getLabel();
    if ((label != null) && (label.length() > 0)) {
      tag.addAttribute("label", label);
    }
    if (component instanceof JTextComponent) {
      String text = ((JTextComponent)component).getText();
      if (text != null && !"".equals(text)) {
        tag.addAttribute("text", cutTextIfLong(text));
      }
    }
    if (component instanceof JLabel) {
      String text = ((JLabel)component).getText();
      if (text != null && !"".equals(text)) {
        tag.addAttribute("text", cutTextIfLong(text));
      }
    }
  }

  private String cutTextIfLong(String text) {
    if (text.length() > 30) {
      return text.substring(0, 13) + "..." + text.substring(text.length() - 13);
    }
    return text;
  }

  public String getName() {
    return getAwtComponent().getName();
  }

  public String getLabel() {
    return null;
  }

  public Assertion isVisible() {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue(getAwtComponent().isVisible());
      }
    };
  }

  public Assertion isEnabled() {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue(getAwtComponent().isEnabled());
      }
    };
  }

  /**
   * <p>Checks the foreground color of the component. </p>
   * The color can be given in either hexadecimal ("FF45C0") or human-readable ("red") format.
   *
   * @see <a href="http://www.uispec4j.org/colors">Using colors</a>
   */
  public Assertion foregroundEquals(final String expectedColor) {
    return new Assertion() {
      public void check() {
        Color foreground = getAwtComponent().getForeground();
        if (foreground == null) {
          foreground = Color.BLACK;
        }
        ColorUtils.assertEquals(expectedColor, foreground);
      }
    };
  }

  /**
   * <p>Checks that the foreground color of the component is close to the given value. </p>
   * The color can be given in either hexadecimal ("FF45C0") or human-readable ("red") format.
   *
   * @see <a href="http://www.uispec4j.org/colors">Using colors</a>
   */
  public Assertion foregroundNear(final String expectedColor) {
    return new Assertion() {
      public void check() {
        Color foreground = getAwtComponent().getForeground();
        if (foreground == null) {
          foreground = Color.BLACK;
        }
        ColorUtils.assertSimilar(expectedColor, foreground);
      }
    };
  }

  /**
   * Checks the background color of the component
   * The color can be given in either hexadecimal ("FF45C0") or human-readable ("red") format.
   *
   * @see <a href="http://www.uispec4j.org/colors">Using colors</a>
   */
  public Assertion backgroundEquals(final String expectedColor) {
    return new Assertion() {
      public void check() {
        ColorUtils.assertEquals(expectedColor, getAwtComponent().getBackground());
      }
    };
  }

  /**
   * <p>Checks that the background color of the component is close to the given value. </p>
   * The color can be given in either hexadecimal ("FF45C0") or human-readable ("red") format.
   *
   * @see <a href="http://www.uispec4j.org/colors">Using colors</a>
   */
  public Assertion backgroundNear(final String expectedColor) {
    return new Assertion() {
      public void check() {
        Color background = getAwtComponent().getBackground();
        if (background == null) {
          background = Color.BLACK;
        }
        ColorUtils.assertSimilar(expectedColor, background);
      }
    };
  }

  private String computeComponentName(Component component) {
    String name = component.getName();
    return (name == null) ? "" : name;
  }

  private boolean isPanelWithNoName(UIComponent component) {
    return ((component instanceof Panel) && computeComponentName(component.getAwtComponent()).equals(""));
  }

  public Panel getContainer() {
    Container parent = getAwtComponent().getParent();
    if (parent == null) {
      return null;
    }
    return new Panel(parent);
  }

  public Panel getContainer(String parentName) {
    Container parent = getAwtComponent().getParent();
    while (parent != null && !parentName.equalsIgnoreCase(parent.getName())) {
      parent = parent.getParent();
    }
    if (parent != null && parentName.equalsIgnoreCase(parent.getName())) {
      return new Panel(parent);
    }
    return null;
  }

  public AbstractUIComponent typeKey(Key key) {
    KeyUtils.enterKeys(getAwtComponent(), key);
    return this;
  }

  public AbstractUIComponent pressKey(Key key) {
    KeyUtils.pressKey(getAwtComponent(), key);
    return this;
  }

  public AbstractUIComponent releaseKey(Key key) {
    KeyUtils.releaseKey(getAwtComponent(), key);
    return this;
  }
}
