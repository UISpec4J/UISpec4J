package org.uispec4j.interception;

import org.uispec4j.Trigger;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.Window;

import java.lang.reflect.Method;

/**
 * Adapter that intercepts the window displayed by the main() of a given class.<p/>
 * This adapter keeps the reference of the intercepted window, so that main() is not called on
 * subsequent calls. If you need to run main() again, you can either call {@link #reset()} or create a new
 * adapter.
 */
public class MainClassAdapter implements UISpecAdapter {
  private Method main;
  private String[] args;
  private Window window;

  public MainClassAdapter(Class mainClass, String... args) {
    this.args = args;
    try {
      main = mainClass.getMethod("main", new Class[]{
        args.getClass()
      });
    }
    catch (NoSuchMethodException e) {
      throw new RuntimeException("Class " + mainClass.getName() + " has no method: public static void main(String[])");
    }
  }

  public Window getMainWindow() {
    if (window == null) {
      window = intercept();
    }
    return window;
  }

  private Window intercept() {
    return WindowInterceptor.run(new Trigger() {
      public void run() throws Exception {
        main.invoke(null, new Object[]{args});
      }
    });
  }

  public void reset() {
    window = null;
  }
}
