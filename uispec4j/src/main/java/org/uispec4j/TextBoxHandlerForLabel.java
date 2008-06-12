package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.dependency.InternalAssert;
import org.uispec4j.utils.KeyUtils;

import javax.swing.*;
import java.awt.*;

class TextBoxHandlerForLabel implements TextBox.Handler {
  private JLabel jLabel;

  public TextBoxHandlerForLabel(JLabel label) {
    this.jLabel = label;
  }

  public Component getAwtComponent() {
    return jLabel;
  }

  public void setText(String text) {
    throwNotEditableError();
  }

  public void insertText(String text, int position) {
    throwNotEditableError();
  }

  public void appendText(String text) {
    throwNotEditableError();
  }

  public void clear() {
    throwNotEditableError();
  }

  public String getText() {
    return jLabel.getText();
  }

  public Assertion textIsEmpty() {
    return new Assertion() {
      public void check() {
        InternalAssert.assertTrue("Text should be empty but contains: " + jLabel.getText(),
                                  jLabel.getText().length() == 0);
      }
    };
  }

  public Assertion textEquals(final String text) {
    return new Assertion() {
      public void check() {
        InternalAssert.assertEquals(text, jLabel.getText());
      }
    };
  }

  public Assertion htmlEquals(String html) {
    return new Assertion() {
      public void check() {
        InternalAssert.fail("This component does not support html.");
      }
    };
  }

  public Assertion textContains(final String text) {
    return new Assertion() {
      public void check() {
        String actualText = jLabel.getText();
        InternalAssert.assertTrue("The component text does not contain '" + text +
                                  "' - actual content is: " + actualText,
                                  actualText.indexOf(text) >= 0);
      }
    };
  }

  public Assertion textDoesNotContain(final String text) {
    return new Assertion() {
      public void check() {
        String actualText = jLabel.getText();
        InternalAssert.assertTrue("The component text should not contain '" + text +
                                  "' - actual content is: " + actualText,
                                  actualText.indexOf(text) < 0);
      }
    };
  }

  public Assertion isEditable() {
    return new Assertion() {
      public void check() {
        InternalAssert.fail("Text is not editable");
      }
    };
  }

  public void clickOnHyperlink(String link) {
    InternalAssert.fail("This component does not support hyperlinks.");
  }

  public Assertion iconEquals(final Icon icon) {
    return new Assertion() {
      public void check() {
        InternalAssert.assertEquals("Unexpected icon", icon, jLabel.getIcon());
      }
    };
  }

  public void pressKey(Key key) {
    KeyUtils.pressKey(jLabel, key);
  }

  private void throwNotEditableError() {
    InternalAssert.fail("The text box is not editable");
  }
}
