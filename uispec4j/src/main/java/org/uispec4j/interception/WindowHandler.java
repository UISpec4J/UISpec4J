package org.uispec4j.interception;

import org.uispec4j.Trigger;
import org.uispec4j.Window;

/**
 * Interface used for defining modal dialog handlers.
 *
 * @see WindowInterceptor
 * @see <a href="http://www.uispec4j.org/intercepting-windows">Intercepting windows</a>
 */
public abstract class WindowHandler {
  private String name;

  protected WindowHandler() {
  }

  /**
   * Gives a name to the handler, for description purposes only.
   * The name is used to clarify the test code, and it will be used by UISpec4J
   * when displaying error messages.
   */
  protected WindowHandler(String stepName) {
    this.name = stepName;
  }

  /**
   * Handles the shown window and returns a trigger that will close it.
   */
  public abstract Trigger process(Window window) throws Exception;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
