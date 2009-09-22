package org.uispec4j.interception;

import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UIComponent;
import org.uispec4j.Window;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.interception.handlers.InterceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Ready-to-use window interception handler, designed for simple dialogs. <p>
 * Sample usage:
 * <pre><code>
 * WindowInterceptor
 *   .init(panel.getButton("Change value").triggerClick())
 *   .process(BasicHandler.init()
 *            .assertContainsText("Enter new value")
 *            .setText("13")
 *            .triggerButtonClick("OK"))
 *   .run();
 * </code></pre>
 * The last call must be {@link #triggerButtonClick(String)}, which returns the created WindowHandler.
 *
 * @see WindowInterceptor
 */
public class BasicHandler {

  private List<InterceptionHandler> handlers = new ArrayList<InterceptionHandler>();

  /**
   * Starts the definition of the handler.
   */
  public static BasicHandler init() {
    return new BasicHandler();
  }

  private BasicHandler() {
  }

  /**
   * Checks that there is a text component in the dialog displaying the given text.
   */
  public BasicHandler assertContainsText(final String text) {
    handlers.add(new InterceptionHandler() {
      public void process(Window window) {
        UIComponent component = window.findUIComponent(TextBox.class, text);
        if (component == null) {
          AssertAdapter.fail("Text not found: " + text);
        }
      }
    });
    return this;
  }


  /**
   * Checks the displayed title is the same as the given text.
   */
  public BasicHandler assertTitleEquals(final String expectedTitle) {
    handlers.add(new InterceptionHandler() {
      public void process(Window window) {
        window.titleEquals(expectedTitle).check();
      }
    });
    return this;
  }

  /**
   * Checks the displayed title contains the given text.
   */
  public BasicHandler assertTitleContains(final String expectedTitle) {
    handlers.add(new InterceptionHandler() {
      public void process(Window window) {
        window.titleContains(expectedTitle).check();
      }
    });
    return this;
  }

  /**
   * Clicks on a button given its displayed label. This method will throw an exception if
   * no button with this text is found.
   */
  public BasicHandler clickButton(final String buttonName) {
    handlers.add(new InterceptionHandler() {
      public void process(Window window) {
        window.getButton(buttonName).click();
      }
    });
    return this;
  }

  /**
   * Enters a text value, provided that there is only one input text field in the dialog.
   */
  public BasicHandler setText(final String text) {
    handlers.add(new InterceptionHandler() {
      public void process(Window window) {
        window.getInputTextBox().setText(text);
      }
    });
    return this;
  }

  /**
   * Returns the created window handler with a trigger for clicking on a button.
   */
  public WindowHandler triggerButtonClick(final String buttonName) {
    return new WindowHandler() {
      public Trigger process(Window window) throws Exception {
        for (InterceptionHandler handler : handlers) {
          handler.process(window);
        }
        return window.getButton(buttonName).triggerClick();
      }
    };
  }
}
