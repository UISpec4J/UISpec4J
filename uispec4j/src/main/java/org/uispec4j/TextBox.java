package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Wrapper for JTextComponent/JLabel components.
 */
public class TextBox extends AbstractSwingUIComponent {
  public static final String TYPE_NAME = "textBox";
  public static final Class[] SWING_CLASSES = {JTextComponent.class, JLabel.class};

  private Handler handler;
  private static final String TEXT_DISABLED_ERROR = "The text component is not enabled - text cannot be entered";

  public TextBox(JTextComponent textComponent) {
    this.handler = TextBoxHandlerForHtmlTextComponent.init(textComponent);
    if (handler == null) {
      this.handler = new TextBoxHandlerForRawTextComponent(textComponent);
    }
  }

  public TextBox(JLabel label) {
    this.handler = new TextBoxHandlerForLabel(label);
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public JComponent getAwtComponent() {
    return handler.getAwtComponent();
  }

  /**
   * Replaces the text box contents and simulates pressing the Enter key.
   */
  public void setText(String text) {
    if (!handler.getAwtComponent().isEnabled()) {
      AssertAdapter.fail(TEXT_DISABLED_ERROR);
    }
    handler.setText(text);
  }

  /**
   * Replaces the text box contents and simulates pressing Enter if pressEnter is set to true.
   */
  public void setText(String text, boolean pressEnter) {
    if (!handler.getAwtComponent().isEnabled()) {
      AssertAdapter.fail(TEXT_DISABLED_ERROR);
    }
    if (pressEnter)
      handler.setText(text);
    else {
      handler.clear();
      handler.appendText(text);
    }
  }

  /**
   * Inserts text at the given position without pressing Enter.
   */
  public void insertText(String text, int position) {
    if (!handler.getAwtComponent().isEnabled()) {
      AssertAdapter.fail(TEXT_DISABLED_ERROR);
    }
    handler.insertText(text, position);
  }

  /**
   * Inserts text at the given position without pressing Enter.
   */
  public void appendText(String text) {
    if (!handler.getAwtComponent().isEnabled()) {
      AssertAdapter.fail(TEXT_DISABLED_ERROR);
    }
    handler.appendText(text);
  }

  /**
   * Clears the text without validating. Use <code>setText("")</code> to achieve the same effect with validation.
   */
  public void clear() {
    if (!handler.getAwtComponent().isEnabled()) {
      AssertAdapter.fail(TEXT_DISABLED_ERROR);
    }
    handler.clear();
  }

  public String getText() {
    return handler.getText();
  }

  public Assertion textIsEmpty() {
    return handler.textIsEmpty();
  }

  /**
   * Checks the displayed text in cases where HTML text is used. This is
   * different from {@link #textEquals(String)} in that whitespaces, carriage return
   * and other formatting adjustments are ignored.
   */
  public Assertion htmlEquals(String html) {
    return handler.htmlEquals(html);
  }

  /**
   * Checks that the text box contains a number of substrings, in a given order.
   * This method is useful for checking key information in the displayed string,
   * without being too dependent on the actual wording.
   */
  public Assertion textContains(final String... orderedTexts) {
    return new Assertion() {
      public void check() {
        String actual = handler.getText();
        int index = 0;
        for (String text : orderedTexts) {
          index = actual.indexOf(text, index);
          if (index < 0) {
            if (actual.indexOf(text) < 0) {
              AssertAdapter.fail("The component text does not contain '" + text + "' " +
                                 "- actual content is:" + actual);
            }
            else {
              AssertAdapter.fail("The component text does not contain '" + text + "' at the expected position " +
                                 "- actual content is:" + actual);
            }
          }
          else {
            index += text.length();
          }
        }
      }
    };
  }

  public Assertion textEquals(String text) {
    return handler.textEquals(text);
  }

  public Assertion textContains(String text) {
    return handler.textContains(text);
  }

  public Assertion textDoesNotContain(String text) {
    return handler.textDoesNotContain(text);
  }

  public Assertion isEditable() {
    return handler.isEditable();
  }

  /**
   * Checks the icon displayed by the component. Please note that equals()
   * not being defined for Icon implementations, you will have to provide a pointer
   * to the actual Icon instance that is used in the production code. This make
   * this method mostly suited to unit testing.
   */
  public Assertion iconEquals(Icon icon) {
    return handler.iconEquals(icon);
  }

  /**
   * Simulates a click on an hyperlink given a part of the link text.
   * An exception will be thrown if zero or more than one hyperlinks are
   * found with this text.
   */
  public void clickOnHyperlink(String link) {
    handler.clickOnHyperlink(link);
  }

  /**
   * @see #clickOnHyperlink(String)
   */
  public Trigger triggerClickOnHyperlink(final String name) {
    return new Trigger() {
      public void run() throws Exception {
        clickOnHyperlink(name);
      }
    };
  }

  /** Simulates losing the focus., by calling directly the focus listeners of the component
   * without going through the focus mechanim. */
  public void focusLost() {
    handler.focusLost();
  }

  interface Handler {
    JComponent getAwtComponent();

    void setText(String text);

    void insertText(String text, int position);

    void appendText(String text);

    void clear();

    String getText();

    Assertion textIsEmpty();

    Assertion textEquals(String text);

    Assertion textContains(String text);

    Assertion textDoesNotContain(String text);

    Assertion isEditable();

    void clickOnHyperlink(String link);

    Assertion iconEquals(Icon icon);

    Assertion htmlEquals(String html);

    void focusLost();
  }
}
