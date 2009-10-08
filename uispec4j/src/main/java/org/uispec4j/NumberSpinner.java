package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.*;

/**
 * Wrapper for JSpinner components implementing a SpinnerNumberModel.
 */
public class NumberSpinner extends Spinner {
  private SpinnerNumberModel model;

  public NumberSpinner(JSpinner jSpinner) {
    super(jSpinner);
    SpinnerModel model = jSpinner.getModel();
    if (!SpinnerNumberModel.class.isInstance(model)) {
      throw new ItemNotFoundException("Expected JSpinner using a SpinnerNumberModel");
    }
    this.model = (SpinnerNumberModel)model;
  }

  /**
   * Checks that the list spinner displays starts with the given value
   */
  public Assertion minEquals(final int expectedMin) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(new Integer(expectedMin), model.getMinimum());
      }
    };
  }

  /**
   * Checks that the list spinner displays starts with the given value
   */
  public Assertion maxEquals(final int expectedMax) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(new Integer(expectedMax), model.getMaximum());
      }
    };
  }

  /**
   * Checks that the list spinner computes previous and next value with the given value.
   */
  public Assertion stepSizeEquals(final int expectedStepSize) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(new Integer(expectedStepSize), model.getStepSize());
      }
    };
  }
}
