package org.uispec4j.interception;

import org.uispec4j.Trigger;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.Window;
import org.uispec4j.utils.MainClassTrigger;

/**
 * <p>Adapter that intercepts the window displayed by the main() of a given class.</p>
 * This adapter keeps the reference of the intercepted window, so that main() is not called on
 * subsequent calls. If you need to run main() again, you can either call {@link #reset()} or create a new
 * adapter.
 */
public class MainClassAdapter implements UISpecAdapter {
  private Window window;
  private Trigger trigger;

  public MainClassAdapter(Class mainClass, String... args) {
    this.trigger = new MainClassTrigger(mainClass, args);
  }

  public Window getMainWindow() {
    if (window == null) {
      window = WindowInterceptor.run(trigger);
    }
    return window;
  }

  public void reset() {
    window = null;
  }
}
