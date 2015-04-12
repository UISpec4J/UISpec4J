package org.uispec4j;

import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.DummyTreeCellRenderer;
import org.uispec4j.utils.Functor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class TreeContentTest extends TreeTestCase {
  public void testContentCheck() throws Exception {
    assertTrue(tree.contentEquals("root\n" +
                                  "  child1\n" +
                                  "    child1_1\n" +
                                  "  child2"));
  }

  public void testContentCheckWithEmptyExpectedStringError() {
    checkContainmentError("  ",
                          "Expected tree description should not be empty");
  }

  public void testContentCheckWithErrors() throws Exception {
    checkContainmentError("root\n" +
                          "  child1\n" +
                          "    error\n" +
                          "  child2");
  }

  public void testExpectedContentStringIsTrimmedInContainmentCheck() throws Exception {
    assertTrue(tree.contentEquals("   root\n" +
                                  "  child1\n" +
                                  "    child1_1\n" +
                                  "  child2\n"));
    assertTrue(tree.contentEquals("   root\n" +
                                  "  child1\n" +
                                  "    child1_1\n" +
                                  "  child2\n \t "));
  }

  public void testContentCheckWithPropertiesSpecification() throws Exception {
    child1_1.setBold(true);
    child1_1.setColor(Color.RED);
    child2.setColor(Color.BLUE);
    assertTrue(tree.contentEquals("root\n" +
                                  "  child1\n" +
                                  "    child1_1 #(bold,color=red)\n" +
                                  "  child2 #(color=blue)"));
  }

  public void testContentCheckWithMissingBoldnessError() throws Exception {
    if (TestUtils.isMacOsX()) {
      //TODO: to be studied - on MacOS, the font of the renderer component does not accept to turn to bold (JDK issue?)
      return;
    }
    child1.setBold(true);
    checkContainmentError("root\n" +
                          "  child1\n" +
                          "    child1_1\n" +
                          "  child2");
  }

  public void testContentCheckWithBoldnessError() throws Exception {
    child1.setBold(false);
    checkContainmentError("root\n" +
                          "  child1 #(bold)\n" +
                          "    child1_1\n" +
                          "  child2");
  }

  public void testContentCheckAcceptsBothNumericAndHumanReadableValues() throws Exception {
    child2.setColor(Color.BLUE);
    assertTrue(tree.contentEquals("root\n" +
                                  "  child1\n" +
                                  "    child1_1\n" +
                                  "  child2 #(color=blue)"));
    assertTrue(tree.contentEquals("root\n" +
                                  "  child1\n" +
                                  "    child1_1\n" +
                                  "  child2 #(color=0000ff)"));
  }

  public void testContentCheckWithMissingColorError() throws Exception {
    child1.setColor(Color.BLUE);
    checkContainmentError("root\n" +
                          "  child1\n" +
                          "    child1_1\n" +
                          "  child2");
  }

  public void testCheckContentsWithColorNameError() throws Exception {
    child1_1.setColor(Color.BLUE);
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        assertTrue(tree.contentEquals("root\n" +
                                      "  child1\n" +
                                      "    child1_1 #(color=ERROR)\n" +
                                      "  child2"));
      }
    }, "'ERROR' does not seem to be a color");
  }

  public void testAssertContains() throws Exception {
    assertTrue(tree.contains("child1/child1_1"));
    try {
      assertTrue(tree.contains("child1/unknown"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Could not find element 'child1/unknown'", e.getMessage());
    }
  }

  public void testAssertContainsReallyChecksTheWholePath() throws Exception {
    child1Node.add(new DefaultMutableTreeNode(new DummyTreeCellRenderer.UserObject("child1_2")));
    assertTrue(tree.contains("child1/child1_2"));
  }

  public void testSeparatorCustomisation() throws Exception {
    DefaultMutableTreeNode child3 =
      new DefaultMutableTreeNode(new DummyTreeCellRenderer.UserObject("child/3"));
    rootNode.add(child3);
    child3.add(new DefaultMutableTreeNode(new DummyTreeCellRenderer.UserObject("child/3 3")));
    tree.setSeparator(" | ");
    checkPath("child/3 | child/3 3");
    tree.setSeparator(" ## ");
    checkPath("child/3 ## child/3 3");
    tree.setSeparator("-");
    checkPath("child/3-child/3 3");
  }

  private void checkPath(String path) {
    tree.contains(path);
    tree.select(path);
    assertTrue(tree.selectionEquals(path));
    tree.clearSelection();
    tree.click(path);
    assertTrue(tree.selectionEquals(path));
  }

  public void testSeparatorCanBeSpecifiedAtTreeCreationTime() throws Exception {
    String previousSeparator = Tree.defaultSeparator;
    System.getProperties().remove(Tree.SEPARATOR_PROPERTY);
    Tree.setDefaultSeparator("-*-");

    Tree tree = new Tree(jTree);
    assertTrue(tree.contains("child1-*-child1_1"));
    System.setProperty(Tree.SEPARATOR_PROPERTY, "#");
    assertTrue(tree.contains("child1-*-child1_1"));
    tree = new Tree(jTree);
    assertTrue(tree.contains("child1#child1_1"));

    System.getProperties().remove(Tree.SEPARATOR_PROPERTY);
    Tree.setDefaultSeparator(previousSeparator);
  }

  public void testSeparatorCannotBeEmpty() throws Exception {
    try {
      tree.setSeparator("");
      throw new AssertionFailureNotDetectedError();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Separator must not be empty", e.getMessage());
    }
    try {
      Tree.setDefaultSeparator("");
      throw new AssertionFailureNotDetectedError();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Separator must not be empty", e.getMessage());
    }

    System.setProperty(Tree.SEPARATOR_PROPERTY, "");
    Tree tree = new Tree(jTree);
    assertEquals("/", tree.getSeparator());
    System.getProperties().remove(Tree.SEPARATOR_PROPERTY);
  }

  public void testSeparatorCannotBeNull() throws Exception {
    try {
      tree.setSeparator(null);
      throw new AssertionFailureNotDetectedError();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Separator must not be null", e.getMessage());
    }
    try {
      Tree.setDefaultSeparator(null);
      throw new AssertionFailureNotDetectedError();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Separator must not be null", e.getMessage());
    }
  }

  public void testPathDefinitionsGivePriorityToExactNames() throws Exception {
    rootNode.add(new DefaultMutableTreeNode(new DummyTreeCellRenderer.UserObject("child1bis")));
    checkPath("child1/child1_1");
  }

  public void testUsingASpecificConverter() throws Exception {
    tree.setCellValueConverter(new DummyTreeCellValueConverter());
    assertTrue(tree.contentEquals("_obj:root_\n" +
                                  "  _obj:child1_\n" +
                                  "    _obj:child1_1_\n" +
                                  "  _obj:child2_"));
    assertTrue(tree.contains("_obj:child1_/_obj:child1_1_"));
  }

  public void testUsingASpecificConverterForColor() throws Exception {
    DummyTreeCellValueConverter converter = new DummyTreeCellValueConverter();
    converter.setRedFontPattern("child1");
    tree.setCellValueConverter(converter);
    assertTrue(tree.contentEquals("_obj:root_\n" +
                                  "  _obj:child1_ #(color=FF0000)\n" +
                                  "    _obj:child1_1_ #(color=FF0000)\n" +
                                  "  _obj:child2_"));
  }

  public void testUsingASpecificConverterForTextStyle() throws Exception {
    DummyTreeCellValueConverter converter = new DummyTreeCellValueConverter();
    converter.setBoldPattern("child");
    tree.setCellValueConverter(converter);
    assertTrue(tree.contentEquals("_obj:root_\n" +
                                  "  _obj:child1_ #(bold)\n" +
                                  "    _obj:child1_1_ #(bold)\n" +
                                  "  _obj:child2_ #(bold)"));
  }

  public void testGetChildCount() throws Exception {
    assertEquals(2, tree.getChildCount(""));
    assertEquals(1, tree.getChildCount("child1"));
  }

  public void testCheckForegroundColor() throws Exception {
    assertTrue(tree.foregroundEquals("", "black"));
    child1.setColor(new Color(250, 10, 10));
    assertTrue(tree.foregroundEquals("child1", "red"));
    try {
      assertTrue(tree.foregroundEquals("child1", "green"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("expected:<GREEN> but was:<FA0A0A>", e.getMessage());
    }
  }

  public void testToString() throws Exception {
    assertEquals("root\n" +
                 "  child1\n" +
                 "    child1_1\n" +
                 "  child2\n",
                 tree.toString());
  }

  private void checkContainmentError(String expectedTree) {
    checkContainmentError(expectedTree, null);
  }

  private void checkContainmentError(String expectedTree, String message) {
    try {
      assertTrue(tree.contentEquals(expectedTree));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      if (message != null) {
        assertEquals(message, e.getMessage());
      }
    }
  }
}
