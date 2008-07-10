package org.uispec4j.xml;

import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.utils.Utils;

public class EventLogger {

  private StringBuffer buffer = new StringBuffer();
  private Log lastLog;

  public EventLogger() {
    reset();
  }

  public void assertEquals(String expected) {
    assertEquals(expected, true);
  }

  public void assertEquals(EventLogger expected) {
    AssertAdapter.assertEquals(expected.closeStream(), closeStream());
  }

  public void assertEquivalent(String expected) {
    assertEquals(expected, false);
  }

  public void assertEmpty() throws Exception {
    assertEquals("<log/>");
  }

  public void reset() {
    buffer.setLength(0);
    buffer.append("<log>").append(Utils.LINE_SEPARATOR);
    lastLog = null;
  }

  public Log log(String eventName) {
    endLastLog();
    lastLog = new Log(eventName);
    return lastLog;
  }

  public class Log {
    private Log(String eventName) {
      buffer.append('<').append(eventName);
    }

    public Log add(String key, String value) {
      buffer.append(' ').append(key).append("=\"").append(value).append("\"");
      return this;
    }

    public Log add(String key, int value) {
      return add(key, Integer.toString(value));
    }

    private void end() {
      buffer.append("/>").append(Utils.LINE_SEPARATOR);
    }
  }

  private void endLastLog() {
    if (lastLog != null) {
      lastLog.end();
    }
  }

  private String closeStream() {
    endLastLog();
    buffer.append("</log>");
    return buffer.toString();
  }

  private void assertEquals(String expected, boolean orderIsSignificant) {
    String actual = closeStream();
    if (expected.length() == 0) {
      fail(expected, actual);
    }
    try {
      if (orderIsSignificant) {
        XmlAssert.assertEquals(expected.replace('\'', '"'), actual.replaceAll("'", ""));
      }
      else {
        XmlAssert.assertEquivalent(expected.replace('\'', '"'), actual.replaceAll("'", ""));
      }
    }
    catch (Error e) {
      AssertAdapter.assertEquals(expected.replace('\'', '"'), actual.replaceAll("'", ""));
      fail(expected, actual);
    }
    reset();
  }

  private void fail(String expected, String actual) {
    AssertAdapter.fail(new StringBuffer()
      .append("Expected:").append(Utils.LINE_SEPARATOR)
      .append(expected).append(Utils.LINE_SEPARATOR)
      .append("...but was:").append(Utils.LINE_SEPARATOR)
      .append(actual).append(Utils.LINE_SEPARATOR).toString());
  }
}
