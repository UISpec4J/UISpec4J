package org.uispec4j;

import org.uispec4j.utils.Functor;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;

public class TextBoxForLabelTest extends TextBoxComponentTestCase {
  private JLabel jLabel;

  protected void setUp() throws Exception {
    super.setUp();
    jLabel = new JLabel("some text");
    jLabel.setName("myLabel");
    textBox = new TextBox(jLabel);
  }

  protected void createTextBox(String text) {
    jLabel = new JLabel(text);
    textBox = new TextBox(jLabel);
  }

  public void testGetComponentTypeName() throws Exception {
    assertEquals("textBox", UIComponentFactory.createUIComponent(new JLabel()).getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<textBox name='myLabel' text='some text'/>", textBox.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JLabel(), TextBox.class);
  }

  public void testAssertTextEquals() throws Exception {
    assertTrue(textBox.textEquals("some text"));
    checkAssertionFails(textBox.textEquals("unknown"),
                        "expected:<unknown> but was:<some text>");
  }

  public void testAssertTextEqualsWithHtml() throws Exception {
    String text = "My name is <b>Bond</b>";
    jLabel.setText(text);
    assertTrue(textBox.textEquals(text));
    assertFalse(textBox.textEquals("My name is <b>Bond</b>, James Bond"));
  }

  public void testAssertTextContains() throws Exception {
    jLabel.setText("some text");
    assertTrue(textBox.textContains("some"));
    checkAssertionFails(textBox.textContains("error"),
                        "The component text does not contain 'error' - actual content is: some text");
  }

  public void testAssertTextDoesNotContain() throws Exception {
    jLabel.setText("some text");
    assertTrue(textBox.textDoesNotContain("xxx"));
    checkAssertionFails(textBox.textDoesNotContain("some"),
                        "The component text should not contain 'some' - actual content is: some text");
  }

  public void testAssertTextIsEditable() throws Exception {
    assertFalse(textBox.isEditable());
  }

  public void testAssertEmpty() throws Exception {
    jLabel.setText("");
    assertTrue(textBox.textIsEmpty());
    jLabel.setText("a");
    checkAssertionFails(textBox.textIsEmpty(),
                        "Text should be empty but contains: a");
  }

  public void testSetTextIsNotSupported() throws Exception {
    checkAssertionFailedError(new Functor() {
      public void run() throws Exception {
        textBox.setText("text");
      }
    }, "The text box is not editable");
    assertEquals("some text", textBox.getText());
  }

  public void testInsertTextIsNotSupported() throws Exception {
    checkAssertionFailedError(new Functor() {
      public void run() throws Exception {
        textBox.insertText("text", 0);
      }
    }, "The text box is not editable");
    assertEquals("some text", textBox.getText());
  }

  public void testAppendTextIsNotSupported() throws Exception {
    checkAssertionFailedError(new Functor() {
      public void run() throws Exception {
        textBox.appendText("text");
      }
    }, "The text box is not editable");
    assertEquals("some text", textBox.getText());
  }

  public void testClearIsNotSupported() throws Exception {
    checkAssertionFailedError(new Functor() {
      public void run() throws Exception {
        textBox.clear();
      }
    }, "The text box is not editable");
    assertEquals("some text", textBox.getText());
  }

  public void testGetText() throws Exception {
    assertEquals("some text", textBox.getText());
    jLabel.setText("new text");
    assertEquals("new text", textBox.getText());
  }

  public void testClickOnHyperlinkIsNotSupported() throws Exception {
    checkAssertionFailedError(new Functor() {
      public void run() throws Exception {
        textBox.clickOnHyperlink("text");
      }
    }, "This component does not support hyperlinks.");
    checkAssertionFailedError(new Functor() {
      public void run() throws Exception {
        textBox.triggerClickOnHyperlink("text").run();
      }
    }, "This component does not support hyperlinks.");
  }

  public void testAssertIconEquals() throws Exception {
    ImageIcon icon1 = new ImageIcon();
    jLabel.setIcon(icon1);
    assertTrue(textBox.iconEquals(icon1));
    checkAssertionFailedError(new Functor() {
      public void run() throws Exception {
        ImageIcon icon2 = new ImageIcon();
        assertTrue(textBox.iconEquals(icon2));
      }
    });
  }
}
