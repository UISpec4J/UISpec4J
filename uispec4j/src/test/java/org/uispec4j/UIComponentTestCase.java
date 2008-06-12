package org.uispec4j;

import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.utils.UnitTestCase;

import javax.swing.*;

public abstract class UIComponentTestCase extends UnitTestCase {

  public abstract void testGetComponentTypeName() throws Exception;

  public abstract void testGetDescription() throws Exception;

  public abstract void testFactory() throws Exception;

  public void testGetName() throws Exception {
    UIComponent component = createComponent();
    component.getAwtComponent().setName(null);
    assertEquals(null, component.getName());
    component.getAwtComponent().setName("name");
    assertEquals("name", component.getName());
  }

  protected abstract UIComponent createComponent();

  public static void checkFactory(JComponent jComponent, Class expectedGuiComponent) {
    UIComponent uiComponent = UIComponentFactory.createUIComponent(jComponent);
    assertNotNull(uiComponent);
    assertTrue(expectedGuiComponent.isInstance(uiComponent));
  }
}
