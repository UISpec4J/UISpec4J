package org.uispec4j.assertion.testlibrairies;

import java.util.ArrayList;
import java.util.List;

enum TestLibraries {
  JUNIT("junit.framework.Assert", JUnitLibrary.class),
  TESTNG("org.testng.Assert", TestNGLibrary.class);

  private String representativeClassPath;
  private TestLibrary dependency;

  private static final String TEST_LIBRARY_PROPERTY = "uispec4j.test.library";

  private TestLibraries(String assertPath, Class<? extends TestLibrary> dependencyClass) {
    this.representativeClassPath = assertPath;
    try {
      this.dependency = dependencyClass.newInstance();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isPresent() {
    ClassLoader classLoader = AssertAdapter.class.getClassLoader();
    try {
      classLoader.loadClass(representativeClassPath);
      return true;
    }
    catch (ClassNotFoundException e) {
      return false;
    }
  }

  public TestLibrary getDependency() {
    return dependency;
  }

  public static TestLibrary loadDependency() {
    String library = System.getProperty(TEST_LIBRARY_PROPERTY);
    if (library != null && library.length() > 0) {
      return getImposedLibrary(library);
    }
    return retrieveDependency();
  }

  private static TestLibrary retrieveDependency() {
    TestLibraries[] candidates = values();
    List<TestLibraries> libraries = new ArrayList<TestLibraries>();
    for (TestLibraries candidate : candidates) {
      if (candidate.isPresent()) {
        libraries.add(candidate);
      }
    }
    if (libraries.size() == 0) {
      return new UISpecLibrary();
    }

    if (libraries.size() > 1) {
      return new InvalidLibrary("UISpec4J found several testing frameworks in your classpath. " +
                              "You must set the '" + TEST_LIBRARY_PROPERTY +
                              "' property to one among " +
                              getList() + " or trim your classpath to keep only one of them.");
    }
    return libraries.get(0).getDependency();
  }

  private static String getList() {
    StringBuffer buffer = new StringBuffer("[");
    TestLibraries[] libraries = values();
    for (int i = 0; i < libraries.length; i++) {
      String str = libraries[i].name().toLowerCase();
      buffer.append(str);
      buffer.append((i == libraries.length - 1) ? "]" : ", ");
    }
    return buffer.toString();
  }

  private static TestLibrary getImposedLibrary(String libraryName) {
    TestLibraries testLibrary = null;
    try {
      testLibrary = valueOf(libraryName.toUpperCase());
    }
    catch (IllegalArgumentException e) {
      return new InvalidLibrary("You required UISpec4J to use " + libraryName + " as your testing framework, but UISpec4J does not recognize it. " +
                              "Please use one of the following, before running your tests: " +
                              getList());
    }
    if (!testLibrary.isPresent()) {
      return new InvalidLibrary("You required UISpec4J to use " + libraryName + " as your testing framework. " +
                              "Please add it to the classpath and retry.");
    }
    return testLibrary.getDependency();
  }
}
