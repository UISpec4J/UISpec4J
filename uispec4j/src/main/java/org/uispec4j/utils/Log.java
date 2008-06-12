package org.uispec4j.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Simple log class used sporadically for debugging sessions
 */
///CLOVER:OFF
public class Log {
  private static LogWriter writer = new NullWriter();
  public static final int THREAD_NAME_SIZE = 45;

  public static void reset() {
    writer.reset();
  }

  public static void dump() {
    writer.dump();
  }

  public static void write(String message) {
    writer.write(Utils.normalize(Thread.currentThread().getName(), THREAD_NAME_SIZE));
    writer.write("  ");
    writer.write(message);
    writer.write(Utils.LINE_SEPARATOR);
  }

  public static void writeStack(String name) {
    writer.writeStack(name);
  }

  private static interface LogWriter {
    void reset();

    void write(String message);

    void dump();

    void writeStack(String name);
  }

  private static abstract class AbstractLogWriter implements LogWriter {
    public void writeStack(String name) {
      Exception e = new Exception();
      StringWriter stringWriter = new StringWriter();
      PrintWriter writer = new PrintWriter(stringWriter);
      e.printStackTrace(writer);
      write(name + Utils.LINE_SEPARATOR + stringWriter.toString());
    }
  }

  private static class SystemOutWriter extends AbstractLogWriter {
    public void reset() {
    }

    public void write(String message) {
      System.out.print(message);
    }

    public void dump() {
    }
  }

  private static class StringBufferWriter extends AbstractLogWriter {
    private StringBuffer buffer = new StringBuffer();

    public void reset() {
      buffer.setLength(0);
    }

    public void write(String message) {
      buffer.append(message);
    }

    public void dump() {
      System.out.println(buffer.toString());
    }
  }

  private static class NullWriter implements LogWriter {
    public void reset() {
    }

    public void write(String message) {
    }

    public void dump() {
    }

    public void writeStack(String name) {
    }
  }
}
