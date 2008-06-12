package org.uispec4j.utils;

import junit.framework.TestCase;

import java.io.File;

public class FileTestUtilsTest extends TestCase {
  public void testDumpStringToFile() throws Exception {
    String content = "hello world" + Utils.LINE_SEPARATOR + "this a new line!";
    String filename = "example.txt";
    File file = FileTestUtils.dumpStringToFile(filename, content);
    assertTrue(file.exists());
    assertEquals(filename, file.getName());
    assertEquals(content, FileTestUtils.loadTextFileToString(file));
  }
}
