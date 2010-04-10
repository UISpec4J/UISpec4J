package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.*;

/**
 * Base class for UIComponent implementations that wrap JComponent subclasses.
 */
public abstract class AbstractSwingUIComponent extends AbstractUIComponent implements TooltipComponent {

  public abstract JComponent getAwtComponent();

  public Assertion tooltipEquals(final String text) {
    return new Assertion() {
      public void check() {
        String actualText = getAwtComponent().getToolTipText();
        AssertAdapter.assertEquals(actualText, text);
      }
    };
  }

  public Assertion tooltipContains(final String text) {
    return new Assertion() {
      public void check() {
        String actualText = getAwtComponent().getToolTipText();
        AssertAdapter.assertNotNull("No tooltip set", actualText);
        AssertAdapter.assertTrue("Actual tooltip:" + actualText, actualText.contains(text));
      }
    };
  }
}
