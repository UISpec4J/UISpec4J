package org.uispec4j.extension;

import org.junit.Test;
import org.uispec4j.TestUtils;
import org.uispec4j.utils.UnitTestCase;

import java.io.File;
import java.io.IOException;

public class ExtensionGeneratorTest extends UnitTestCase {
  private File output;

  protected void setUp() throws Exception {
    super.setUp();
    output = new File(findTargetDirectory(), "tmp/extension.jar");
    output.getParentFile().mkdirs();
    output.delete();
  }

  @Test
  public void testStandardGenerationUsageWithCustomClass() throws Exception {
    checkStandardGenerationUsage(CustomCountingButton.class);
  }

  @Test
  public void testStandardGenerationUsageWithDerivedClass() throws Exception {
    checkStandardGenerationUsage(DerivedCountingButton.class);
  }

  @Test
  public void testRunningTheGenerationOverAnExistingJarReplacesThePanelClass() throws Exception {
    checkRunningTheGenerationOverAnExistingJarReplacesThePanelClass(CustomCountingButton.class);
    checkRunningTheGenerationOverAnExistingJarReplacesThePanelClass(DerivedCountingButton.class);
  }

  public void checkStandardGenerationUsage(Class componentClass) throws Exception {
    ExtensionGenerator.main(new String[]{output.getAbsolutePath(),
                                         "CountingButton:" + componentClass.getName() + ":"
                                         + JCountingButton.class.getName()});
    runCheckerClass(componentClass);
  }

  private void checkRunningTheGenerationOverAnExistingJarReplacesThePanelClass(Class componentClass) throws Exception {
    ExtensionGenerator.main(new String[]{output.getAbsolutePath(),
                                         "MyButton:" + componentClass.getName() + ":"
                                         + JCountingButton.class.getName()});
    ExtensionGenerator.main(new String[]{output.getAbsolutePath(),
                                         "CountingButton:" + componentClass.getName()
                                         + ":" + JCountingButton.class.getName()});
    runCheckerClass(componentClass);
  }

  private void runCheckerClass(Class componentClass) throws IOException, InterruptedException {
    String separator = System.getProperty("path.separator");
    String classpath = output.getAbsolutePath() +
                       separator +
                       findTargetDirectory() + "/classes" +
                       separator +
                       findTargetDirectory() + "/test-classes" +
                       separator +
                       System.getProperty("java.class.path");

    if ("Linux".equalsIgnoreCase(System.getProperty("os.name"))) {
      System.out.println("to short " + classpath);
      return;
    }
    Process process =
      Runtime.getRuntime().exec(new String[]{"java",
                                             "-Xmx512m",
                                             "-classpath",
                                             classpath,
                                             GeneratedJarChecker.class.getName(),
                                             componentClass.getName()});
    StreamRecorder output = StreamRecorder.run(process.getInputStream());
    StreamRecorder error = StreamRecorder.run(process.getErrorStream());
    process.waitFor();
    String errorOutput = error.getResult();

    // Workaround for Apple bug
    // http://developer.apple.com/releasenotes/Java/Java50Release4RN/OutstandingIssues/chapter_4_section_3.html
    if (TestUtils.isMacOsX()) {
      errorOutput = cleanUpOutputForMacOSX(errorOutput);
    }
    assertEquals("", errorOutput);
    assertEquals("OK", output.getResult());
  }

  public String cleanUpOutputForMacOSX(String input) {
    return input.replaceAll(".*MagicCam.*[\n]?", "");
  }

  public File findTargetDirectory() {
    return findDirectory(getClass(), "target");
  }

  private static File findDirectory(Class baseClass, String directory) {
    String name = "/" + baseClass.getName().replace('.', '/') + ".class";
    String absolutePath = baseClass.getResource(name).getFile();
    return toDirectory(absolutePath, directory);
  }

  private static File toDirectory(String absolutePath, String directory) {
    String path = absolutePath.substring(0, absolutePath.lastIndexOf("target"));
    return new File(path, directory);
  }
}
