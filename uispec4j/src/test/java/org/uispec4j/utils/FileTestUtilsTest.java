package org.uispec4j.utils;

import org.junit.Test;
import java.io.File;
import junit.framework.TestCase;

public class FileTestUtilsTest extends TestCase {
  @Test
  public void testDumpStringToFile() throws Exception {
    String content = "hello world" + Utils.LINE_SEPARATOR + "this a new line!";
    String filename = "example.txt";
    File file = FileTestUtils.dumpStringToFile(filename, content);
    assertTrue(file.exists());
    assertEquals(filename, file.getName());
    assertEquals(content, FileTestUtils.loadTextFileToString(file));
  }
}
