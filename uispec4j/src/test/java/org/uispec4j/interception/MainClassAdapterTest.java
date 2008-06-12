package org.uispec4j.interception;

import org.uispec4j.TextBox;
import org.uispec4j.Window;

import javax.swing.*;
import java.util.Arrays;

public class MainClassAdapterTest extends InterceptionTestCase {
  public void test() throws Exception {
    MainClassAdapter adapter = new MainClassAdapter(MyClass.class, new String[]{"a", "b"});
    Window window = adapter.getMainWindow();
    TextBox textBox = window.getTextBox();
    assertTrue(textBox.textEquals("[a, b]"));
  }

  public void testReusesTheInterceptedWindowOnSubsequentCalls() throws Exception {
    MyClass.callCount = 0;

    MainClassAdapter adapter = new MainClassAdapter(MyClass.class, new String[]{"a", "b"});
    Window window1 = adapter.getMainWindow();
    Window window2 = adapter.getMainWindow();
    assertSame(window1, window2);
    assertEquals(1, MyClass.callCount);

    adapter.reset();
    Window window3 = adapter.getMainWindow();
    assertNotSame(window1, window3);
    assertEquals(2, MyClass.callCount);
  }

  public void testNoMain() throws Exception {
    try {
      new MainClassAdapter(MainClassAdapterTest.class, new String[]{"a", "b"});
    }
    catch (RuntimeException e) {
      assertEquals("Class org.uispec4j.interception.MainClassAdapterTest has no method: public static void main(String[])",
                   e.getMessage());
    }
  }

  private static class MyClass {
    public static int callCount = 0;

    public static void main(String[] args) {
      callCount++;
      JFrame frame = new JFrame();
      frame.getContentPane().add(new JLabel(Arrays.asList(args).toString()));
      frame.setVisible(true);
    }
  }
}
