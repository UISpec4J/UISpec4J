package org.uispec4j;

import junit.framework.TestCase;

import javax.swing.*;

public class MouseTest extends TestCase {
  TextBox textBox = new TextBox(new JLabel());
  final MouseLogger logger = new MouseLogger(textBox);

  public void testSimpleClickOnComponent() throws Exception {
    Mouse.click(textBox);

    logger.assertEquals("<log>" +
                        "  <mousePressed button='1'/>" +
                        "  <mouseReleased button='1'/>" +
                        "  <mouseClicked button='1'/>" +
                        "</log>");
  }

  public void testDoubleClickOnComponent() throws Exception {
    Mouse.doubleClick(textBox);

    logger.assertEquals("<log>" +
                        "  <mousePressed button='1' clickCount='2'/>" +
                        "  <mouseReleased button='1' clickCount='2'/>" +
                        "  <mouseClicked button='1' clickCount='2'/>" +
                        "</log>");
  }
}
