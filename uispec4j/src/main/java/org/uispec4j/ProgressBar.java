package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.dependency.InternalAssert;

import javax.swing.*;

/**
 * Wrapper for JProgressBar components.
 */
public class ProgressBar extends AbstractUIComponent {
  public static final String TYPE_NAME = "progressBar";
  public static final Class[] SWING_CLASSES = new Class[]{JProgressBar.class};
  public static final int DEFAULT_PRECISION = 2;

  private JProgressBar jProgressBar;
  private int precision = DEFAULT_PRECISION;

  public ProgressBar(JProgressBar progressBar) {
    this.jProgressBar = progressBar;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public JProgressBar getAwtComponent() {
    return jProgressBar;
  }

  /**
   * Checks the completion value as a percentage (0-100) of the available range.
   * The actual completion must be equal to the given value plus or minus the progress bar precision.
   *
   * @param expectedValue an int between 0 and 100, or -1 if the status is undeterminate
   * @see #setPrecision
   */
  public Assertion completionEquals(final int expectedValue) {
    return new Assertion() {
      public void check() {
        if (expectedValue == -1) {
          InternalAssert.assertTrue("The progress bar status is not undeterminate",
                                    jProgressBar.isIndeterminate());
          return;
        }
        if ((expectedValue < 0) || (expectedValue > 100)) {
          InternalAssert.fail("Expected value should be in range [0,100]");
        }
        int actualValue = getActualValue();
        if (!isRoughlyEqual(actualValue, expectedValue)) {
          InternalAssert.assertEquals("Unexpected completion rate -", expectedValue, actualValue);
        }
      }
    };
  }

  /**
   * Checks the completion of the progress bar.
   */
  public Assertion isCompleted() {
    return completionEquals(100);
  }

  public Assertion displayedValueEquals(final String expectedProgressString) {
    return new Assertion() {
      public void check() {
        InternalAssert.assertEquals(expectedProgressString, jProgressBar.getString());
      }
    };
  }

  /**
   * Sets the precision for the completion check. This precision is the greatest difference
   * allowed between the actual and expected completion values (both are integers between 0
   * and 100).<p/>
   * The default precision is 2.
   *
   * @see #completionEquals
   */
  public void setPrecision(int value) {
    this.precision = value;
  }

  private boolean isRoughlyEqual(int actualValue, int expectedValue) {
    return Math.abs(actualValue - expectedValue) <= precision;
  }

  private int getActualValue() {
    if (jProgressBar.isIndeterminate()) {
      return -1;
    }
    checkRange();
    return (int)(jProgressBar.getPercentComplete() * 100);
  }

  private int checkRange() {
    int range = jProgressBar.getMaximum() - jProgressBar.getMinimum();
    if (range <= 0) {
      InternalAssert.fail("Invalid range [" + jProgressBar.getMinimum() + "," +
                          jProgressBar.getMaximum() + "]");
    }
    return range;
  }
}
