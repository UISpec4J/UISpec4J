package org.uispec4j.interception.handlers;

import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

/**
 * Internal interface for window handlers used by {@link WindowInterceptor}.
 */
public interface InterceptionHandler {
  /**
   * Called to process a modal dialog when it is shown.
   */
  void process(Window window);
}
