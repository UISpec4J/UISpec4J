package org.uispec4j;

import org.uispec4j.assertion.Assertion;

/**
 * Tags components that support tooltips
 */
public interface TooltipComponent {
  Assertion tooltipEquals(String text);

  Assertion tooltipContains(String text);
}
