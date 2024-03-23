package org.uispec4j;

import java.util.Arrays;

import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;

public abstract class SpinnerTestCase extends UIComponentTestCase {
  protected JSpinner jSpinner;
  protected Spinner spinner;

  protected abstract SpinnerModel createSpinnerModel() throws Exception;

  protected abstract Spinner createSpinner(JSpinner jSpinner);

  protected void setUp() throws Exception {
    init();
  }

  public final void testGetComponentTypeName() throws Exception {
    assertEquals("spinner", spinner.getDescriptionTypeName());
  }

  public final void testGetDescription() throws Exception {
    if (isJavaVersionAtLeast("1.6")) {
      XmlAssert.assertEquivalent("<spinner name='marcel'>" +
                                 "  <button name='Spinner.nextButton'/>" +
                                 "  <button name='Spinner.previousButton'/>" +
                                 "  <textBox name='Spinner.formattedTextField' text='" + getText() + "'/>" +
                                 "</spinner>", spinner.getDescription());
    }
    else {
      XmlAssert.assertEquivalent("<spinner name='marcel'>" +
                                 "  <button/>" +
                                 "  <button/>" +
                                 "  <textBox name='Spinner.formattedTextField' text='" + getText() + "'/>" +
                                 "</spinner>", spinner.getDescription());
    }
  }

  protected final UIComponent createComponent() {
    return spinner;
  }

  protected final void checkPreviousValueFails(String wrongPreviousValue) {
    try {
      assertTrue(spinner.previousValueEquals(wrongPreviousValue));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("No previous value from the start", e.getMessage());
    }
  }

  protected final void checkNextValueFails(String wrongNextValue) {
    try {
      assertTrue(spinner.nextValueEquals(wrongNextValue));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("No previous value from the end", e.getMessage());
    }
  }

  protected final void init() throws Exception {
    jSpinner = new JSpinner(createSpinnerModel());
    jSpinner.setName("marcel");
    spinner = createSpinner(jSpinner);
  }

  public void testFactory() throws Exception {
    checkFactory(jSpinner, Spinner.class);
  }

  public abstract String getText();
}
