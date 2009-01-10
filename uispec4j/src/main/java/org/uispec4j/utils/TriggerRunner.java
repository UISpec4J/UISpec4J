package org.uispec4j.utils;

import org.uispec4j.Trigger;
import org.uispec4j.interception.toolkit.UISpecDisplay;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class TriggerRunner {
  public static void runInCurrentThread(Trigger trigger) {
    try {
      trigger.run();
    }
    catch (RuntimeException e) {
      throw e;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void runInSwingThread(final Trigger trigger) {
    if (SwingUtilities.isEventDispatchThread()) {
      runInCurrentThread(trigger);
    }
    else {
      final ExceptionContainer container = new ExceptionContainer(new RuntimeException());
      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            try {
              runInCurrentThread(trigger);
            }
            catch (Throwable e) {
              container.set(e);
            }
          }
        });
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e.getCause());
      }
      catch (InvocationTargetException e) {
        throw new RuntimeException(e.getCause());
      }
      container.rethrowIfNeeded();
    }
  }

  public static void runInUISpecThread(final Trigger trigger) {
    final Exception exception = new RuntimeException();
    UISpecDisplay.instance().runInNewThread(new Runnable() {
      public void run() {
        try {
          trigger.run();
        }
        catch (Throwable e) {
          UISpecDisplay.instance().store(exception.initCause(e));
        }
      }
    });
  }
}
