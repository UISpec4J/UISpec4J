package org.uispec4j.interception;

import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.Window;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.interception.handlers.*;
import org.uispec4j.interception.toolkit.UISpecDisplay;
import org.uispec4j.utils.ComponentUtils;
import org.uispec4j.utils.ExceptionContainer;
import org.uispec4j.utils.TriggerRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Intercepts popped-up windows such as JFrame or JDialog. <p>
 * There are two main usage scenarios for this class: intercepting "frames", i.e. non-modal windows,
 * and intercepting modal dialogs.<p>
 * Non-modal windows can be intercepted and used directly from within the test using the following
 * construct:
 * <pre>
 *   Window window = WindowInterceptor.run(panel.getButton("open").triggerClick());
 * </pre>
 * <p/>
 * Modal dialogs cannot be intercepted this way, because the thread from which the test is run
 * will likely be blocked in the production code until the dialog is closed.
 * To intercept a sequence of popped-up windows, use the following construct:
 * <pre>
 *   WindowInterceptor
 *    .init(new Trigger() {
 *      public void run() throws Exception {
 *        // ... trigger something that will cause the first window to be shown ...
 *      }
 *    })
 *    .process(new WindowHandler("first dialog") {
 *      public Trigger process(Window window) {
 *        // ... perform some operations on the first window ...
 *        return window.getButton("OK").triggerClick(); // return a trigger that will close it
 *      }
 *    })
 *    .process(new WindowHandler("second dialog") {
 *      public Trigger process(Window window) {
 *        // ... perform some operations on the second window ...
 *        return window.getButton("OK").triggerClick(); // return a trigger that will close it
 *      }
 *    })
 *   .run();
 * </pre>
 * </ul>
 * This class uses a timeout (see {@link UISpec4J#setWindowInterceptionTimeLimit}) to make sure
 * that windows appear within a given time limit, and that modal windows are closed before the
 * end of the interception.
 *
 * @see <a href="http://www.uispec4j.org/intercepting-windows">Intercepting windows</a>
 */
public final class WindowInterceptor {

  private Trigger trigger;
  private List handlers = new ArrayList();
  private ExceptionContainer exceptionContainer = new ExceptionContainer();
  private StackTraceElement[] stackReference;

  /**
   * Starts the interception of a modal dialog. The returned interceptor must be used for
   * processing the displayed window, for instance:
   * <pre>
   *   WindowInterceptor
   *    .init(new Trigger() {
   *      public void run() throws Exception {
   *        // ... trigger something that will cause the first window to be shown ...
   *      }
   *    })
   *    .process(new WindowHandler("my dialog") {
   *      public Trigger process(Window window) {
   *        // ... perform some operations on the shown window ...
   *        return window.getButton("OK").triggerClick(); // return a trigger that will close it
   *      }
   *    })
   *   .run();
   * </pre>
   *
   * @see #process(WindowHandler)
   */
  public static WindowInterceptor init(Trigger trigger) {
    return new WindowInterceptor(trigger);
  }

  private WindowInterceptor(Trigger trigger) {
    this.trigger = trigger;
  }

  /**
   * Processes a modal dialog. The provided WindowHandler must return a trigger that will cause
   * the window to be closed, in order to prevent the application to be stopped.
   */
  public WindowInterceptor process(WindowHandler handler) {
    if (handler.getName() == null) {
      handler.setName(Integer.toString(handlers.size() + 1));
    }
    handlers.add(handler);
    return this;
  }

  /**
   * Processes a modal dialog after having checked its title first.
   *
   * @see #process(WindowHandler)
   */
  public WindowInterceptor process(final String title, final WindowHandler handler) {
    handlers.add(new WindowHandler(title) {
      public Trigger process(Window window) throws Exception {
        UISpecAssert.assertTrue(window.titleEquals(title));
        return handler.process(window);
      }
    });
    return this;
  }

  /**
   * Processes a sequence of dialogs (one handler per dialog).
   *
   * @see #process(WindowHandler)
   */
  public WindowInterceptor process(WindowHandler[] handlers) {
    for (WindowHandler handler : handlers) {
      process(handler);
    }
    return this;
  }

  /**
   * Processes a dialog that will be closed automatically, and checks its name.
   */
  public WindowInterceptor processTransientWindow(final String title) {
    return process(new WindowHandler("Transient window: " + title) {
      public Trigger process(Window window) {
        checkTitle(window, title);
        return Trigger.DO_NOTHING;
      }
    });
  }

  /**
   * Processes a dialog that will be closed automatically.
   */
  public WindowInterceptor processTransientWindow() {
    return process(new WindowHandler("Transient window") {
      public Trigger process(Window window) {
        return Trigger.DO_NOTHING;
      }
    });
  }

  /**
   * Processes a dialog by clicking on a given button. This a shortcut that prevents you
   * from implementing your own WindowHandler for simple cases such as confirmation dialogs.
   *
   * @see #process(WindowHandler)
   */
  public WindowInterceptor processWithButtonClick(final String buttonName) {
    handlers.add(new WindowHandler("buttonClickHandler") {
      public Trigger process(Window window) throws Exception {
        return window.getButton(buttonName).triggerClick();
      }
    });
    return this;
  }

  /**
   * Processes a dialog by checking its title and clicking on a given button.
   *
   * @see #processWithButtonClick(String)
   */
  public WindowInterceptor processWithButtonClick(final String title, final String buttonName) {
    handlers.add(new WindowHandler("buttonClickHandler") {
      public Trigger process(Window window) throws Exception {
        checkTitle(window, title);
        return window.getButton(buttonName).triggerClick();
      }
    });
    return this;
  }

  /**
   * Starts the interception prepared with the
   * {@link #init(Trigger)}/{@link #process(WindowHandler)} call sequence.
   * This method will fail if no window was shown by the trigger under the time limit.
   * defined with {@link UISpec4J#setWindowInterceptionTimeLimit(long)}.
   */
  public void run() {
    if (handlers.isEmpty()) {
      AssertAdapter.fail("You must add at least one handler");
    }
    initStackReference();
    try {
      run(new TriggerAccessor() {
        public Trigger getTrigger() {
          return trigger;
        }
      }, new InterceptionHandlerAdapter(handlers.iterator()));
    }
    catch (Throwable e) {
      storeException(e, handlers.size() > 1 ? "Error in first handler: " : "");
    }
    exceptionContainer.rethrowIfNeeded();
    UISpecDisplay.instance().rethrowIfNeeded();
  }

  private void checkTitle(Window window, final String title) {
    AssertAdapter.assertEquals("Invalid window title -", title, window.getTitle());
  }

  private void initStackReference() {
    Exception dummyException = new Exception();
    StackTraceElement[] trace = dummyException.getStackTrace();
    List list = new ArrayList(Arrays.asList(trace));
    list.remove(0);
    list.remove(0);
    this.stackReference = (StackTraceElement[])list.toArray(new StackTraceElement[list.size()]);
  }

  private void storeException(Throwable e, String messagePrefix) {
    if (!exceptionContainer.isSet()) {
      String message = messagePrefix + e.getMessage();
      InterceptionError error = new InterceptionError(message, e);
      error.setStackTrace(stackReference);
      exceptionContainer.set(error);
    }
  }

  private class InterceptionHandlerAdapter implements InterceptionHandler {
    private Iterator handlersIterator;
    private WindowHandler handler;

    public InterceptionHandlerAdapter(Iterator handlersIterator) {
      this.handlersIterator = handlersIterator;
    }

    public void process(final Window window) {
      handler = getNextHandler();
      String name = handler.getName();
      try {
        if (handlersIterator.hasNext()) {
          WindowInterceptor.run(new TriggerAccessor() {
            public Trigger getTrigger() throws Exception {
              return handler.process(window);
            }
          }, this);
        }
        else {
          Trigger trigger = handler.process(window);
          TriggerRunner.runInCurrentThread(trigger);
        }
      }
      catch (WindowNotClosedError e) {
        storeException(e, computeMessagePrefix(handler.getName()));
      }
      catch (Throwable e) {
        storeException(e, computeMessagePrefix(name));
      }
      finally {
        if (exceptionContainer.isSet()) {
          ComponentUtils.close(window);
        }
      }
    }

    private String computeMessagePrefix(String handlerName) {
      return handlers.size() > 1 ? "Error in handler '" + handlerName + "': " : "";
    }

    private WindowHandler getNextHandler() {
      return (WindowHandler)handlersIterator.next();
    }
  }

  /**
   * Intercepts a non-modal window by running a trigger and returning the displayed window.
   * This method will fail if no window was shown by the trigger under the time limit
   * defined with {@link UISpec4J#setWindowInterceptionTimeLimit(long)},
   * or if it is used with a modal dialog (modal dialogs should be intercepted using
   * {@link #init(Trigger)}). <p>
   * Note: the trigger is run in the current thread.
   */
  public static Window run(Trigger trigger) {
    return run(trigger, false);

  }

  /**
   * Performs a "quick&dirty" interception of a modal dialog.<p>
   * <em>Warning</em>: This method should be handled with care and especially avoided in cases
   * where the application code is blocked while the dialog is displayed,
   * because it could result in deadlocks. <p>
   * Modal dialogs should rather be intercepted using {@link #init(Trigger)} <p/>
   * This method will fail if no window was shown by the trigger under the time limit, or if it is
   * used with a non-modal window. <p>
   */
  public static Window getModalDialog(Trigger trigger) {
    return run(trigger, true);
  }

  private static Window run(final Trigger trigger, boolean shouldBeModal) {
    UISpecDisplay.instance().rethrowIfNeeded();
    ShownInterceptionCollectorHandler collector = new ShownInterceptionCollectorHandler();
    ShownInterceptionDetectionHandler showDetector = new ShownInterceptionDetectionHandler(collector, UISpec4J.getWindowInterceptionTimeLimit());
    NewThreadInterceptionHandlerDecorator newThreadHandler = new NewThreadInterceptionHandlerDecorator(showDetector);
    ModalInterceptionCheckerHandler modalChecker = new ModalInterceptionCheckerHandler(newThreadHandler, shouldBeModal);
    UISpecDisplay.instance().add(modalChecker);

    try {
      if (shouldBeModal) {
        TriggerRunner.runInUISpecThread(trigger);
      }
      else {
        TriggerRunner.runInCurrentThread(trigger);
      }

      UISpecDisplay.instance().rethrowIfNeeded();
      showDetector.waitWindow();
      return collector.getWindow();
    }
    finally {
      UISpecDisplay.instance().remove(modalChecker);
      newThreadHandler.join();
      UISpecDisplay.instance().rethrowIfNeeded();
    }
  }

  interface TriggerAccessor {
    Trigger getTrigger() throws Exception;
  }

  private static void run(TriggerAccessor triggerAccessor, InterceptionHandler handler) throws Exception {
    ShownInterceptionDetectionHandler showDetector =
      new ShownInterceptionDetectionHandler(handler, UISpec4J.getWindowInterceptionTimeLimit());
    ClosedInterceptionDetectionHandler closeDetector =
      new ClosedInterceptionDetectionHandler(showDetector, UISpec4J.getWindowInterceptionTimeLimit());
    NewThreadInterceptionHandlerDecorator newThreadHandler = new NewThreadInterceptionHandlerDecorator(closeDetector);
    UISpecDisplay.instance().add(newThreadHandler);
    try {
      sun.awt.AWTAutoShutdown.getInstance().notifyThreadBusy(Thread.currentThread());
      TriggerRunner.runInSwingThread(triggerAccessor.getTrigger());
      showDetector.waitWindow();
      newThreadHandler.complete();
      sun.awt.AWTAutoShutdown.getInstance().notifyThreadFree(Thread.currentThread());
      closeDetector.checkWindowWasClosed();
    }
    finally {
      UISpecDisplay.instance().remove(newThreadHandler);
      newThreadHandler.join();
      closeDetector.stop();
    }
  }
}
