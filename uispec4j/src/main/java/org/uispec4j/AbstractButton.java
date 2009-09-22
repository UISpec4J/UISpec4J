package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.utils.TriggerRunner;

import javax.swing.*;

/**
 * Base class for button-like components (toggle buttons, check boxes, etc.)
 */
public abstract class AbstractButton extends AbstractSwingUIComponent {
  private javax.swing.AbstractButton abstractButton;

  protected AbstractButton(javax.swing.AbstractButton abstractButton) {
    this.abstractButton = abstractButton;
  }

  public void click() {
    AssertAdapter.assertTrue("The button is not enabled, it cannot be activated",
                             abstractButton.isEnabled());
    AssertAdapter.assertTrue("The button is not visible, it cannot be activated",
                             abstractButton.isVisible());

    if (SwingUtilities.isEventDispatchThread()) {
      doClick(abstractButton);
    }
    else {
      TriggerRunner.runInSwingThread(triggerClick());
    }
  }

  static void doClick(javax.swing.AbstractButton button) {
    ButtonModel model = button.getModel();
    model.setArmed(true);
    model.setPressed(true);
    model.setPressed(false);
    model.setArmed(false);
  }

  public Assertion textEquals(final String text) {
    return new Assertion() {
      public void check() {
        String label = abstractButton.getText();
        if (label != null) {
          label = label.trim();
        }
        AssertAdapter.assertEquals(text, label);
      }
    };
  }

  /**
   * Checks the icon displayed by the component. Please note that equals()
   * not being defined for Icon implementations, you will have to provide a pointer
   * to the actual Icon instance that is used in the production code. This make
   * this method mostly suited to unit testing.
   */
  public Assertion iconEquals(final Icon expected) {
    return new Assertion() {
      public void check() {
        Icon actual = abstractButton.getIcon();
        if (expected != null) {
          AssertAdapter.assertNotNull("The component contains no icon.", actual);
        }
        AssertAdapter.assertSame("The icon ", expected, actual);
      }
    };
  }

  public String getLabel() {
    return abstractButton.getText();
  }

  public Trigger triggerClick() {
    return new Trigger() {
      public void run() {
        AbstractButton.this.click();
      }
    };
  }
}
