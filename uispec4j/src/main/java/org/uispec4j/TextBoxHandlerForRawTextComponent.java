package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.dependency.InternalAssert;

import javax.swing.text.JTextComponent;

class TextBoxHandlerForRawTextComponent extends AbstractTextBoxHandlerForTextComponent {
  public TextBoxHandlerForRawTextComponent(JTextComponent textComponent) {
    super(textComponent);
  }

  public Assertion textIsEmpty() {
    return new Assertion() {
      public void check() {
        String actualText = jTextComponent.getText();
        InternalAssert.assertTrue("Text should be empty but contains: " + actualText,
                                  actualText.length() == 0);
      }
    };
  }

  public Assertion textEquals(final String text) {
    return "".equals(text) ? textIsEmpty() :
           new Assertion() {
             public void check() {
               InternalAssert.assertEquals(text, jTextComponent.getText());
             }
           };
  }

  public void clickOnHyperlink(String link) {
    InternalAssert.fail("This component does not support hyperlinks.");
  }

  public Assertion htmlEquals(String html) {
    return new Assertion() {
      public void check() {
        InternalAssert.fail("This component does not support html.");
      }
    };
  }
}
