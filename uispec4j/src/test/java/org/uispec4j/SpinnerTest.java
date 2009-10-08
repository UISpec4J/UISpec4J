package org.uispec4j;

import static org.uispec4j.DummySpinner.listModel;

import javax.swing.*;

public class SpinnerTest extends SpinnerTestCase {

  protected SpinnerModel createSpinnerModel() throws Exception {
    return listModel("1", "2", "3");
  }

  protected Spinner createSpinner(JSpinner jSpinner) {
    return new Spinner(jSpinner);
  }

  public void testFactory() throws Exception {
    checkFactory(new JSpinner(), Spinner.class);
  }

  public String getText() {
    return "1";
  }

  public void testCurrentPreviousAndNextValues() throws Exception {
    assertTrue(spinner.valueEquals("1"));
    assertTrue(spinner.nextValueEquals("2"));

    spinner.setValue("3");
    assertTrue(spinner.valueEquals("3"));
    assertTrue(spinner.previousValueEquals("2"));

    spinner.setValue("2");
    assertTrue(spinner.valueEquals("2"));
    assertTrue(spinner.previousValueEquals("1"));
    assertTrue(spinner.nextValueEquals("3"));
  }

  public void testSettingAValueNotInTheSequenceFails() throws Exception {
    try {
      spinner.setValue("4");
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals("'4' not found", e.getMessage());
    }
  }

  public void testFailureWithCurrentPreviousAndNextValues() throws Exception {
    spinner.setValue("1");
    checkPreviousValueFails("4");
    checkPreviousValueFails(null);

    spinner.setValue("3");
    checkNextValueFails("2");
    checkNextValueFails(null);
  }

  public void testClickButtons() throws Exception {
    spinner.setValue("1");
    spinner.clickForNextValue();
    assertTrue(spinner.valueEquals("2"));

    spinner.clickForNextValue();
    assertTrue(spinner.valueEquals("3"));

    spinner.clickForPreviousValue();
    assertTrue(spinner.valueEquals("2"));

    spinner.clickForPreviousValue();
    assertTrue(spinner.valueEquals("1"));
  }

  public void testClickButtonsDoesNotFailIfNotValueAvailable() throws Exception {
    spinner.setValue("1");
    spinner.clickForPreviousValue();
    assertTrue(spinner.valueEquals("1"));

    spinner.setValue("3");
    spinner.clickForNextValue();
    assertTrue(spinner.valueEquals("3"));
  }
}
