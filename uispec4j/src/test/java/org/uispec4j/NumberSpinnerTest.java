package org.uispec4j;

import static org.uispec4j.DummySpinner.*;
import org.junit.Test;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

public class NumberSpinnerTest extends SpinnerTestCase {
  private NumberSpinner numberSpinner;

  public void setUp() throws Exception {
    super.setUp();
    numberSpinner = (NumberSpinner)spinner;
  }

  public String getText() {
    return "10";
  }

  protected SpinnerModel createSpinnerModel() throws Exception {
    return DummySpinner.numberModel(10, 0, 20, 2);
  }

  protected Spinner createSpinner(JSpinner jSpinner) {
    return new NumberSpinner(jSpinner);
  }

  @Test
  public void testMinAndMax() throws Exception {
    assertTrue(numberSpinner.minEquals(0));
    assertTrue(numberSpinner.maxEquals(20));

    assertFalse(numberSpinner.minEquals(1));
    assertFalse(numberSpinner.maxEquals(2));
  }

  @Test
  public void testStepSizeEquals() throws Exception {
    assertTrue(numberSpinner.stepSizeEquals(2));
    assertFalse(numberSpinner.stepSizeEquals(3));
  }

  @Test
  public void testUsingNumberSpinnerWithOtherModelThanSpinnerNumberModelThrowsAnException() throws Exception {
    try {
      new NumberSpinner(new JSpinner(listModel()));
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals("Expected JSpinner using a SpinnerNumberModel", e.getMessage());
    }
  }

}
