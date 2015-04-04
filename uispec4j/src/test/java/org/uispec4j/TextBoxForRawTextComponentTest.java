package org.uispec4j;

import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.DummyActionListener;
import org.uispec4j.utils.Functor;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;
import javax.swing.text.*;

public class TextBoxForRawTextComponentTest extends TextBoxComponentTestCase {
  private JTextComponent jTextComponent;

  protected void setUp() throws Exception {
    super.setUp();
    init(new JTextArea());
  }

  private void init(JTextComponent swingComponent) {
    jTextComponent = swingComponent;
    jTextComponent.setName("myText");
    createTextBox("");
  }

  protected void createTextBox(String text) {
    textBox = new TextBox(jTextComponent);
    textBox.setText(text);
  }

  public void testGetComponentTypeName() throws Exception {
    assertEquals("textBox", UIComponentFactory.createUIComponent(new JTextField()).getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<textBox name='myText'/>", textBox.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JTextArea(), TextBox.class);
    checkFactory(new JTextPane(), TextBox.class);
    checkFactory(new JEditorPane(), TextBox.class);
    checkFactory(new JTextField(), TextBox.class);
  }

  public void testAssertTextEquals() throws Exception {
    assertTrue(textBox.textEquals(""));
    jTextComponent.setText("some text");
    assertTrue(textBox.textEquals("some text"));
    assertFalse(textBox.textEquals(""));
    try {
      assertTrue(textBox.textEquals("error"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("expected:<[error]> but was:<[some text]>", e.getMessage());
    }
  }

  public void testAssertTextContains() throws Exception {
    jTextComponent.setText("some text");
    assertTrue(textBox.textContains("some"));
    try {
      assertTrue(textBox.textContains("error"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("The component text does not contain 'error' - actual content is:some text",
                   e.getMessage());
    }
  }

  public void testAssertTextDoesNotContain() throws Exception {
    jTextComponent.setText("some text");
    assertTrue(textBox.textDoesNotContain("xxx"));
    try {
      assertTrue(textBox.textDoesNotContain("some"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("The component text should not contain 'some' - actual content is:some text",
                   e.getMessage());
    }
  }

  public void testAssertTextIsEditable() throws Exception {
    jTextComponent.setEditable(true);
    assertTrue(textBox.isEditable());
    jTextComponent.setEditable(false);
    assertFalse(textBox.isEditable());
  }

  public void testAssertEmptyWithPlainText() throws Exception {
    jTextComponent.setText("");
    assertTrue(textBox.textIsEmpty());
    jTextComponent.setText("a");
    try {
      assertTrue(textBox.textIsEmpty());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Text should be empty but contains: a", e.getMessage());
    }
  }

  public void testSetText() throws Exception {
    textBox.setText("new text");
    assertEquals("new text", jTextComponent.getText());
  }

  public void testSetTextChecksThatTheComponentIsEditable() throws Exception {
    textBox.setText("text");
    jTextComponent.setEditable(false);
    try {
      textBox.setText("new text");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("The text box is not editable", e.getMessage());
    }
    assertEquals("text", jTextComponent.getText());
  }

  public void testInsertText() throws Exception {
    jTextComponent.setEditable(true);
    textBox.insertText("text", 0);
    assertEquals("text", textBox.getText());
    textBox.insertText("this is some ", 0);
    assertEquals("this is some text", textBox.getText());
    textBox.insertText("interesting ", 13);
    assertEquals("this is some interesting text", textBox.getText());
    textBox.insertText(", isn't it?", textBox.getText().length());
    assertEquals("this is some interesting text, isn't it?", textBox.getText());
  }

  public void testInsertTextAtABadPosition() throws Exception {
    textBox.setText("text");
    try {
      textBox.insertText("a", 10);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Position should be between 0 and 4", e.getMessage());
    }
  }

  public void testAppendAndClear() throws Exception {
    jTextComponent.setEditable(true);
    textBox.clear();
    assertEquals("", textBox.getText());
    textBox.clear();
    assertEquals("", textBox.getText());
    textBox.appendText("blah");
    assertEquals("blah", textBox.getText());
    textBox.appendText(" ");
    textBox.appendText("yadda");
    assertEquals("blah yadda", textBox.getText());
    textBox.clear();
    assertEquals("", textBox.getText());
  }

  public void testInsertAppendAndClearDoNotNotifyActionListeners() throws Exception {
    DummyActionListener actionListener = initWithTextFieldAndActionListener();
    textBox.insertText("text", 0);
    textBox.clear();
    textBox.appendText("text");
    assertEquals(0, actionListener.getCallCount());
  }

  public void testInsertAppendAndClearChecksThatTheComponentIsEditable() throws Exception {
    textBox.setText("text");
    jTextComponent.setEditable(false);
    try {
      textBox.insertText("new text", 0);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("The text box is not editable", e.getMessage());
    }
    try {
      textBox.clear();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("The text box is not editable", e.getMessage());
    }
    try {
      textBox.appendText("new text");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("The text box is not editable", e.getMessage());
    }
    assertEquals("text", jTextComponent.getText());
  }

  public void testTextCannotBeEnteredWhenTheComponentIsDisabled() throws Exception {
    jTextComponent.setEnabled(false);

    String message = "The text component is not enabled - text cannot be entered";

    try {
      textBox.setText("aa");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(message, e.getMessage());
    }

    try {
      textBox.insertText("aa", 0);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(message, e.getMessage());
    }

    try {
      textBox.appendText("aa");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(message, e.getMessage());
    }

    try {
      textBox.clear();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(message, e.getMessage());
    }
  }

  public void testGetText() throws Exception {
    jTextComponent.setText("some text");
    assertEquals("some text", textBox.getText());
    textBox.setText("new text");
    assertEquals("new text", textBox.getText());
    textBox.setText("new <b>text</b>");
    assertEquals("new <b>text</b>", textBox.getText());
  }

  public void testSetTextNotifiesActionListenersForJTextField() throws Exception {
    DummyActionListener actionListener = initWithTextFieldAndActionListener();

    textBox.setText("text");
    assertEquals(1, actionListener.getCallCount());
  }

  public void testClickOnHyperlinkIsNotSupported() throws Exception {
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        textBox.clickOnHyperlink("text");
      }
    }, "This component does not support hyperlinks.");
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        textBox.triggerClickOnHyperlink("text").run();
      }
    }, "This component does not support hyperlinks.");
  }

  public void testPressingPrintableKeyAddsItToText() throws Exception {
    JTextField textField = new JTextField();
    TextBox textBox = new TextBox(textField);
    textBox.pressKey(Key.A);
    assertTrue(textBox.textEquals("a"));
    textBox.pressKey(Key.shift(Key.B));
    assertTrue(textBox.textEquals("aB"));
    textBox.pressKey(Key.C);
    assertTrue(textBox.textEquals("aBc"));
  }

  public void testPressingPrintableKeyRejectedByTextField() throws Exception {
    JTextField textField = new JTextField();
    ((AbstractDocument)textField.getDocument()).setDocumentFilter(new DocumentFilter() {
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (!text.equals("a")) {
          fb.replace(offset, length, text, attrs);
        }
      }
    });
    TextBox textBox = new TextBox(textField);
    textBox.pressKey(Key.A);
    assertTrue(textBox.textEquals(""));
    textBox.pressKey(Key.B);
    assertTrue(textBox.textEquals("b"));
    textBox.pressKey(Key.A);
    assertTrue(textBox.textEquals("b"));
    textBox.pressKey(Key.shift(Key.A));
    assertTrue(textBox.textEquals("bA"));
  }

  public void testPressingPrintableKeyInANonEmptyTextBoxStartsAtPosition0() throws Exception {
    JTextField textField = new JTextField("text");
    TextBox textBox = new TextBox(textField);
    textBox.pressKey(Key.A);
    assertTrue(textBox.textEquals("atext"));
  }
}
