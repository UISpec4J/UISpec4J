package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.finder.ComponentMatcher;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper for JSpinner components.
 */
public class Spinner extends AbstractSwingUIComponent {
  public static final String TYPE_NAME = "spinner";
  public static final Class[] SWING_CLASSES = {JSpinner.class};

  protected JSpinner jSpinner;

  public Spinner(JSpinner jSpinner) {
    this.jSpinner = jSpinner;
  }

  public JSpinner getAwtComponent() {
    return jSpinner;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  /**
   * Checks that the spinner displays the given value
   */
  public Assertion valueEquals(final Object expectedValue) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(expectedValue, jSpinner.getValue());
      }
    };
  }

  /**
   * Checks that the previous value is the given value
   */
  public Assertion previousValueEquals(final Object expectedPreviousValue) {
    return new Assertion() {
      public void check() {
        Object previousValue = jSpinner.getPreviousValue();
        if (previousValue == null) {
          AssertAdapter.fail("No previous value from the start");
        }
        AssertAdapter.assertEquals(expectedPreviousValue, previousValue);
      }
    };
  }

  /**
   * Checks that the next value is the given value
   */
  public Assertion nextValueEquals(final Object expectedNextValue) {
    return new Assertion() {
      public void check() {
        Object nextValue = jSpinner.getNextValue();
        if (nextValue == null) {
          AssertAdapter.fail("No previous value from the end");
        }
        AssertAdapter.assertEquals(expectedNextValue, nextValue);
      }
    };
  }

  /**
   * <p>Changes the displayed text</p>
   * <p>This method will throw an exception if the value is not allowed in the spinner.</p>
   */
  public void setValue(Object value) throws ItemNotFoundException {
    try {
      jSpinner.setValue(value);
    }
    catch (IllegalArgumentException e) {
      throw new ItemNotFoundException("'" + value + "' not found");
    }
  }

  /**
   * Clicks on the button for next value
   */
  public void clickForNextValue() {
    try {
      jSpinner.setValue(jSpinner.getNextValue());
    }
    catch (IllegalArgumentException e) {
      // Ignore it, as in the UI
    }
  }

  /**
   * Clicks on the button for previous value
   */
  public void clickForPreviousValue() {
    try {
      jSpinner.setValue(jSpinner.getPreviousValue());
    }
    catch (IllegalArgumentException e) {
      // Ignore it, as in the UI
    }
  }

  static ComponentMatcher getSpinnerMatcherByModel(final Class spinnerModelClass) {
    return new ComponentMatcher() {
      public boolean matches(Component component) {
        if (component instanceof JSpinner) {
          JSpinner jSpinner = (JSpinner)component;
          return spinnerModelClass.isInstance(jSpinner.getModel());
        }
        return false;
      }
    };
  }
}
