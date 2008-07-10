package org.uispec4j.interception.handlers;

import org.uispec4j.Window;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

public class ModalInterceptionCheckerHandler extends AbstractInterceptionHandlerDecorator {
  private boolean shouldBeModal;

  public ModalInterceptionCheckerHandler(InterceptionHandler innerHandler, boolean shouldBeModal) {
    super(innerHandler);
    this.shouldBeModal = shouldBeModal;
  }

  public void process(Window window) {
    if (!shouldBeModal) {
      if (window.isModal().isTrue()) {
        AssertAdapter.fail("Window '" + window.getTitle() + "' is modal, it must be intercepted with a WindowHandler");
      }
    }
    else {
      if (!window.isModal().isTrue()) {
        AssertAdapter.fail("Window '" + window.getTitle() + "' is non-modal, it must be intercepted with WindowInterceptor.run(Trigger)");
      }
    }
    super.process(window);
  }
}
