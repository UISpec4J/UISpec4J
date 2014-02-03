package org.uispec4j;

import org.junit.Test;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.xml.XmlAssert;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import java.util.Arrays;
import junit.framework.AssertionFailedError;

public abstract class SpinnerTestCase extends UIComponentTestCase {
  protected JSpinner jSpinner;
  protected Spinner spinner;

  protected abstract SpinnerModel createSpinnerModel() throws Exception;

  protected abstract Spinner createSpinner(JSpinner jSpinner);

  @Override
  public void setUp() throws Exception {
    init();
  }

  @Override
  @Test
  public final void testGetComponentTypeName() throws Exception {
    assertEquals("spinner", spinner.getDescriptionTypeName());
  }

  @Override
  @Test
  public final void testGetDescription() throws Exception {
    String property = System.getProperty("java.specification.version");
    if (Arrays.asList("", "1.6", "1.7").contains(property)) {
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
    catch (AssertionFailedError e) {
      assertEquals("No previous value from the start", e.getMessage());
    }
  }

  protected final void checkNextValueFails(String wrongNextValue) {
    try {
      assertTrue(spinner.nextValueEquals(wrongNextValue));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
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
