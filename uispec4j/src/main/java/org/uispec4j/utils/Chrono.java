package org.uispec4j.utils;

import org.uispec4j.assertion.dependency.InternalAssert;

import java.util.Date;

public class Chrono {
  private Date startDate;

  public static Chrono start() {
    return new Chrono();
  }

  private Chrono() {
    startDate = new Date();
  }

  public void assertElapsedTimeLessThan(long max) {
    long elapsed = getElapsedTime();
    InternalAssert.assertTrue("Maximum elapsed time reached: " + elapsed + " >= " + max,
                              elapsed < max);
  }

  public long getElapsedTime() {
    Date currentDate = new Date();
    return currentDate.getTime() - startDate.getTime();
  }
}
