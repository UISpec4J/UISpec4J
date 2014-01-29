package org.uispec4j.finder;

import org.uispec4j.Panel;
import org.uispec4j.utils.UnitTestCase;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class PanelComponentFinderTestCase extends UnitTestCase {
  protected JPanel jPanel;
  protected Panel panel;
  protected List components = new ArrayList();

  public void setUp() throws Exception {
    super.setUp();
    jPanel = new JPanel();
    jPanel.setName("myPanel");
    panel = new Panel(jPanel);
  }

  protected <T extends Component> T addComponent(Class<T> awtComponentclass, String name) throws Exception {
    T component = createComponent(awtComponentclass, name);
    components.add(component);
    jPanel.add(component);
    return component;
  }

  private <T extends Component> T createComponent(Class<T> awtComponentclass, String name) throws Exception {
    Component component;
    try {
      Constructor constructor = awtComponentclass.getConstructor(new Class[]{String.class});
      component = (Component) constructor.newInstance(new Object[]{name});
    }
    catch (NoSuchMethodException e) {
      component = (Component) awtComponentclass.newInstance();
    }
    component.setName(name);
    return (T)component;
  }
}
