package org.uispec4j.finder;

import java.awt.*;

/**
 * Interface used for implementing component searching policies.
 *
 * @see org.uispec4j.Panel
 */
public interface ComponentMatcher {
  boolean matches(Component component);

  public static final ComponentMatcher ALL = new ComponentMatcher() {
    public boolean matches(Component component) {
      return true;
    }
  };
}
