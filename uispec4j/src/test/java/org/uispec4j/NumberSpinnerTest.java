package org.uispec4j;

import javax.swing.*;

public class NumberSpinnerTest extends SpinnerTestCase {
  private NumberSpinner numberSpinner;

  protected void setUp() throws Exception {
    super.setUp();
    numberSpinner = (NumberSpinner)spinner;
  }

  public String getText() {
    return "10";
  }

  protected SpinnerModel createSpinnerModel() throws Exception {
    return new SpinnerNumberModel(10, 0, 20, 2);
  }

  protected Spinner createSpinner(JSpinner jSpinner) {
    return new NumberSpinner(jSpinner);
  }

  public void testMinAndMax() throws Exception {
    assertTrue(numberSpinner.minEquals(0));
    assertTrue(numberSpinner.maxEquals(20));

    assertFalse(numberSpinner.minEquals(1));
    assertFalse(numberSpinner.maxEquals(2));
  }

  public void testStepSizeEquals() throws Exception {
    assertTrue(numberSpinner.stepSizeEquals(2));
    assertFalse(numberSpinner.stepSizeEquals(3));
  }

  public void testUsingNumberSpinnerWithOtherModelThanSpinnerNumberModelThrowsAnException() throws Exception {
    try {
      new NumberSpinner(new JSpinner(new SpinnerListModel()));
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals("Expected JSpinner using a SpinnerNumberModel", e.getMessage());
    }
  }

}
