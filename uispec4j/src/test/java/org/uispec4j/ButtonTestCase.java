package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.interception.toolkit.Empty;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.Chrono;
import org.uispec4j.utils.DummyActionListener;
import org.uispec4j.utils.Utils;

import javax.swing.*;

public abstract class ButtonTestCase extends UIComponentTestCase {

  protected abstract AbstractButton getButton();

  protected abstract javax.swing.AbstractButton getSwingButton();

  protected UIComponent createComponent() {
    return getButton();
  }

  public void testEnableDisable() throws Exception {
    checkEnabled(true);
    checkEnabled(false);
  }

  private void checkEnabled(boolean enabled) {
    getSwingButton().setEnabled(enabled);
    Assertion enabledAssertion = getButton().isEnabled();
    assertTrue((enabled) ? enabledAssertion : not(enabledAssertion));
  }

  public void testCheckText() throws Exception {
    getSwingButton().setText("text");
    assertTrue(getButton().textEquals("text"));
    try {
      assertTrue(getButton().textEquals("error"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }

  public void testCheckTextTrimsTheActualButtonText() throws Exception {
    getSwingButton().setText(" text  ");
    assertTrue(getButton().textEquals("text"));
  }

  public void testIcons() throws Exception {
    Icon icon = new Empty.DummyIcon();
    checkAssertionFails(getButton().iconEquals(icon), "The component contains no icon.");

    getSwingButton().setIcon(icon);
    assertTrue(getButton().iconEquals(icon));

    assertFalse(getButton().iconEquals(new Empty.DummyIcon()));
  }

  public void testActivateIsRejectedIfTheButtonIsDisabled() throws Exception {
    getSwingButton().setEnabled(false);
    try {
      getButton().click();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("The button is not enabled, it cannot be activated", e.getMessage());
    }
  }

  public void testClick() throws Exception {
    DummyActionListener listener = new DummyActionListener();
    getSwingButton().addActionListener(listener);
    getButton().click();
    assertEquals(1, listener.getCallCount());
  }

  public void testTriggerClick() throws Exception {
    DummyActionListener listener = new DummyActionListener();
    getSwingButton().addActionListener(listener);
    getButton().triggerClick().run();
    assertEquals(1, listener.getCallCount());
  }

  public void testClickTakesLessTimeThanWithDefaultSwingCalls() throws Exception {
    Chrono chrono = Chrono.start();
    getButton().click();
    chrono.assertElapsedTimeLessThan(30);
  }

  public void testWaitForEnabledState() throws Exception {
    AbstractButton button = getButton();
    final javax.swing.AbstractButton swingButton = (javax.swing.AbstractButton)button.getAwtComponent();
    swingButton.setEnabled(false);
    Thread thread = new Thread(new Runnable() {
      public void run() {
        Utils.sleep(10);
        swingButton.setEnabled(true);
      }
    });
    thread.start();
    assertFalse(button.isEnabled());
    UISpecAssert.waitUntil(button.isEnabled(), 30);
    assertTrue(button.isEnabled());
  }

  public void testCheckButtonIsVisible() throws Exception {
    DummyActionListener listener = new DummyActionListener();
    getSwingButton().addActionListener(listener);
    getButton().getAwtComponent().setVisible(false);
    try {
      getButton().click();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("The button is not visible, it cannot be activated", e.getMessage());
    }
    assertEquals(0, listener.getCallCount());
  }
}
