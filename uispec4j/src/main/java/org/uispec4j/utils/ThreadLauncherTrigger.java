package org.uispec4j.utils;

import org.uispec4j.Trigger;

public class ThreadLauncherTrigger implements Trigger {
  private Thread[] threads;

  public ThreadLauncherTrigger(Thread... threads) {
    this.threads = threads;
  }

  public void run() throws Exception {
    for (Thread thread : threads) {
      thread.start();
    }
  }
}
