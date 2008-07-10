package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.text.JTextComponent;

class TextBoxHandlerForRawTextComponent extends AbstractTextBoxHandlerForTextComponent {
  public TextBoxHandlerForRawTextComponent(JTextComponent textComponent) {
    super(textComponent);
  }

  public Assertion textIsEmpty() {
    return new Assertion() {
      public void check() {
        String actualText = jTextComponent.getText();
        AssertAdapter.assertTrue("Text should be empty but contains: " + actualText,
                                  actualText.length() == 0);
      }
    };
  }

  public Assertion textEquals(final String text) {
    return "".equals(text) ? textIsEmpty() :
           new Assertion() {
             public void check() {
               AssertAdapter.assertEquals(text, jTextComponent.getText());
             }
           };
  }

  public void clickOnHyperlink(String link) {
    AssertAdapter.fail("This component does not support hyperlinks.");
  }

  public Assertion htmlEquals(String html) {
    return new Assertion() {
      public void check() {
        AssertAdapter.fail("This component does not support html.");
      }
    };
  }
}
