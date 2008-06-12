package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.DummyActionListener;

import javax.swing.*;

public abstract class TextBoxComponentTestCase extends UIComponentTestCase {
  TextBox textBox;

  protected abstract void createTextBox(String text);

  public void testAssertTextContainsWithArray() throws Exception {
    String text = "Universal <b>rules</b>:" +
                  "<ul>" +
                  "<li>item1</li>" +
                  "<li>item2</li>" +
                  "<li>item3</li>" +
                  "</ul>";
    createTextBox(text);

    assertTrue(textBox.textContains(new String[0]));
    assertTrue(textBox.textContains(new String[]{"item1"}));
    assertTrue(textBox.textContains(new String[]{"item1", "item2", "item3", "ul"}));
    assertTrue(textBox.textContains(new String[]{"item1", "item", "item"}));

    String renderedText = textBox.getText();
    checkAssertTextContainsFails(new String[]{"item2", "item1"},
                                 "The component text does not contain 'item1' at the expected position" +
                                 " - actual content is:" + renderedText);
    checkAssertTextContainsFails(new String[]{"item3", "item"},
                                 "The component text does not contain 'item' at the expected position" +
                                 " - actual content is:" + renderedText);
    checkAssertTextContainsFails(new String[]{"item2", "toto"},
                                 "The component text does not contain 'toto'" +
                                 " - actual content is:" + renderedText);
  }

  private void checkAssertTextContainsFails(String[] texts, String error) {
    try {
      assertTrue(textBox.textContains(texts));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals(error, e.getMessage());
    }
  }

  protected UIComponent createComponent() {
    return textBox;
  }

  protected DummyActionListener initWithTextFieldAndActionListener() {
    JTextField textField = new JTextField();
    DummyActionListener actionListener = new DummyActionListener();
    textField.addActionListener(actionListener);
    textBox = new TextBox(textField);
    return actionListener;
  }
}
