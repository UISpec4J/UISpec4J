package org.uispec4j;

import org.junit.Test;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.DummyActionListener;
import javax.swing.JTextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import junit.framework.AssertionFailedError;

public abstract class TextBoxComponentTestCase extends UIComponentTestCase {
  TextBox textBox;

  protected abstract void createTextBox(String text);

  @Test
  public void testAssertTextContainsWithArray() throws Exception {
    String text = "Universal <b>rules</b>:" +
                  "<ul>" +
                  "<li>item1</li>" +
                  "<li>item2</li>" +
                  "<li>item3</li>" +
                  "</ul>";
    createTextBox(text);

    assertTrue(textBox.textContains());
    assertTrue(textBox.textContains("item1"));
    assertTrue(textBox.textContains("item1", "item2", "item3", "ul"));
    assertTrue(textBox.textContains("item1", "item", "item"));

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

  @Test
  public void testFocusLost() throws Exception {

    createTextBox("text");

    final StringBuilder log = new StringBuilder();
    textBox.getAwtComponent().addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        log.append("focusGained");
      }

      public void focusLost(FocusEvent e) {
        log.append("focusLost");
      }
    });

    textBox.focusLost();
    assertEquals("focusLost", log.toString());
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
