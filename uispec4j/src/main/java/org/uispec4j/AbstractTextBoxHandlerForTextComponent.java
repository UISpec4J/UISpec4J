package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

abstract class AbstractTextBoxHandlerForTextComponent implements TextBox.Handler {
  protected JTextComponent jTextComponent;

  public AbstractTextBoxHandlerForTextComponent(JTextComponent textComponent) {
    this.jTextComponent = textComponent;
  }

  public JComponent getAwtComponent() {
    return jTextComponent;
  }

  public void setText(String text) {
    UISpecAssert.assertTrue(isEditable());
    jTextComponent.setText(text);
    if (jTextComponent instanceof JTextField) {
      ((JTextField)jTextComponent).postActionEvent();
    }
  }

  public void insertText(String text, int position) {
    UISpecAssert.assertTrue(isEditable());
    Document document = jTextComponent.getDocument();
    try {
      document.insertString(position, text, document.getDefaultRootElement().getAttributes());
    }
    catch (BadLocationException e) {
      AssertAdapter.fail("Position should be between 0 and " + document.getLength());
    }
  }

  public void appendText(String text) {
    insertText(text, jTextComponent.getDocument().getLength());
  }

  public void clear() {
    UISpecAssert.assertTrue(isEditable());
    Document document = jTextComponent.getDocument();
    try {
      document.remove(0, document.getLength());
    }
    catch (BadLocationException e) {
      AssertAdapter.fail("Clear failed: " + e.getMessage());
    }
  }

  public String getText() {
    return jTextComponent.getText();
  }

  public Assertion textContains(final String text) {
    return new Assertion() {
      public void check() {
        String actual = jTextComponent.getText().replaceAll("\n    ", "").replaceAll("\n  </body>", "</body>");
        AssertAdapter.assertTrue("The component text does not contain '" + text + "' - actual content is:" + actual,
                                 actual.indexOf(text.trim()) >= 0);
      }
    };
  }

  public Assertion textDoesNotContain(final String text) {
    return new Assertion() {
      public void check() {
        String actual = jTextComponent.getText();
        AssertAdapter.assertTrue("The component text should not contain '" + text +
                                 "' - actual content is:" + actual,
                                 actual.indexOf(text) < 0);
      }
    };
  }

  public Assertion isEditable() {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue("The text box is not editable", jTextComponent.isEditable());
      }
    };
  }

  public Assertion iconEquals(Icon icon) {
    throw new UnsupportedOperationException("assertIconEquals is not supported for JTextComponent components");
  }

  public void focusLost() {
    for (FocusListener listener : jTextComponent.getFocusListeners()) {
      listener.focusLost(new FocusEvent(jTextComponent, 0));
    }
  }
}
