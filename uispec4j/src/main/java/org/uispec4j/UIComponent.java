package org.uispec4j;

import org.uispec4j.assertion.Assertion;

import java.awt.*;

/**
 * Interface for all UI (user interface) components.
 */
public interface UIComponent {

  /**
   * Returns the Java GUI component represented by this object.
   */
  Component getAwtComponent();

  /**
   * Returns an XML representation of the component and its subcomponents.
   */
  String getDescription();

  /**
   * Returns the name of the component as it will appear in the XML representation
   * returned by {@link UIComponent#getDescription()}.
   */
  String getDescriptionTypeName();

  Assertion isEnabled();

  Assertion isVisible();

  /**
   * Returns the label displayed on this component, or null if this has no sense for this kind of components.
   */
  String getLabel();

  /**
   * Returns the internal name with which can be used to identify the component. This can be null
   * if no name was set by the developers.
   */
  String getName();

  /**
   * Returns the first container named parentName or null if not found
   */
  Panel getContainer(String parentName);

  /**
   * Simulates typing a key while the focus is on the component.
   */
  UIComponent typeKey(Key key);

  /**
   * Simulates pressing a key while the focus is on the component.
   */
  UIComponent pressKey(Key key);

  /**
   * Simulates releasing a key while the focus is on the component.
   */
  UIComponent releaseKey(Key key);
}
