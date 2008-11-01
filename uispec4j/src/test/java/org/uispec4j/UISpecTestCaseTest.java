package org.uispec4j;

import org.uispec4j.interception.WindowInterceptor;
import org.uispec4j.utils.UnitTestCase;
import org.uispec4j.utils.Utils;
import org.uispec4j.xml.EventLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UISpecTestCaseTest extends UnitTestCase {

  public class MyTestCase extends UISpecTestCase {
    public MyTestCase() {
    }

    public void test() throws Exception {
    }
  }

  public static class MyAdapter extends EventLogger implements UISpecAdapter {
    private JFrame frame;

    public MyAdapter(JFrame frame) {
      this.frame = frame;
    }

    public MyAdapter() {
      this(new JFrame("title"));
    }

    public Window getMainWindow() {
      log("getMainWindow");
      return new Window(frame);
    }
  }

  public void testPropertyDefined() throws Exception {
    System.setProperty(UISpecTestCase.ADAPTER_CLASS_PROPERTY, MyAdapter.class.getName());
    MyTestCase testCase = new MyTestCase();
    testCase.setUp();
    assertTrue(testCase.getMainWindow().titleEquals("title"));
  }

  public void testGetMainWindowFailsIfThePropertyWasNotDefined() throws Exception {
    System.getProperties().remove(UISpecTestCase.ADAPTER_CLASS_PROPERTY);
    MyTestCase testCase = new MyTestCase();
    testCase.setUp();
    try {
      testCase.getMainWindow();
      fail();
    }
    catch (UISpecTestCase.AdapterNotFoundException e) {
      assertEquals(UISpecTestCase.PROPERTY_NOT_DEFINED, e.getMessage());
    }
  }

  public void testGetMainWindowFailsIfThePropertyWasInitializedWithAWrongValue() throws Exception {
    System.setProperty(UISpecTestCase.ADAPTER_CLASS_PROPERTY, "unknown");
    MyTestCase testCase = new MyTestCase();
    testCase.setUp();
    try {
      testCase.getMainWindow();
      fail();
    }
    catch (UISpecTestCase.AdapterNotFoundException e) {
      assertEquals("Adapter class 'unknown' not found", e.getMessage());
    }
  }

  public void testSettingTheAdapter() throws Exception {
    MyAdapter adapter1 = new MyAdapter();
    MyAdapter adapter2 = new MyAdapter();

    MyTestCase testCase = new MyTestCase();
    testCase.setAdapter(adapter1);
    testCase.setUp();
    assertTrue(testCase.getMainWindow().titleEquals("title"));
    adapter1.assertEquals("<log>" +
                          "  <getMainWindow/>" +
                          "</log>");
    adapter2.assertEmpty();

    testCase.getMainWindow();
    adapter1.assertEquals("<log>" +
                          "  <getMainWindow/>" +
                          "</log>");
    adapter2.assertEmpty();

    testCase.setAdapter(adapter2);
    testCase.getMainWindow();
    adapter1.assertEmpty();
    adapter2.assertEquals("<log>" +
                          "  <getMainWindow/>" +
                          "</log>");
  }

  public void testTriggerExceptionsAreStoredAndRethrownInTearDownWhenNotCaughtImmediately() throws Exception {
    UISpec4J.setWindowInterceptionTimeLimit(100);
    final JFrame frame = new JFrame("my frame");
    final Exception exception = new RuntimeException("triggerException");

    MyExceptionTestCase exceptionTestCase = new MyExceptionTestCase(frame, exception);
    exceptionTestCase.setAdapter(new MyAdapter(frame));
    exceptionTestCase.setUp();
    exceptionTestCase.test();
    try {
      exceptionTestCase.tearDown();
      fail();
    }
    catch (Exception e) {
      assertSame(exception, e.getCause());
    }
  }

  private JDialog createModalDialog(JFrame frame) {
    final JDialog firstDialog = new JDialog(frame, "dlg", true);
    firstDialog.getContentPane().add(new JButton(new AbstractAction("Close") {
      public void actionPerformed(ActionEvent e) {
        firstDialog.dispose();
      }
    }));
    return firstDialog;
  }

  private class MyExceptionTestCase extends UISpecTestCase {
    final JDialog firstDialog;
    private final Exception exception;

    public MyExceptionTestCase(JFrame frame, Exception exception) {
      this.exception = exception;
      this.firstDialog = createModalDialog(frame);
    }

    public void test() throws Exception {
      Window window = WindowInterceptor.getModalDialog(new Trigger() {
        public void run() throws Exception {
          firstDialog.setVisible(true);
          throw exception;
        }
      });
      window.getButton("Close").click();
      Utils.sleep(1);
    }
  }
}
