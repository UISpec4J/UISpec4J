package org.uispec4j;

import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.xml.EventLogger;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class TreeSelectionTest extends TreeTestCase {
  protected SelectionListener selectionListener = new SelectionListener();

  protected void initTree() {
    super.initTree();
    jTree.addTreeSelectionListener(selectionListener);
  }

  private void checkSelection(DefaultMutableTreeNode[] expectedSelectedNodes, String expectedSelectionLog) throws Exception {
    TreePath[] expectedPaths = new TreePath[expectedSelectedNodes.length];
    for (int i = 0; i < expectedSelectedNodes.length; i++) {
      DefaultMutableTreeNode expectedNode = expectedSelectedNodes[i];
      expectedPaths[i] = new TreePath(expectedNode.getPath());
    }
    ArrayUtils.assertEquals(expectedPaths, jTree.getSelectionPaths());
    selectionListener.assertEquals(expectedSelectionLog);
    selectionListener.reset();
  }

  public void testSelectRoot() throws Exception {
    assertNoSelection(jTree);
    tree.selectRoot();
    ArrayUtils.assertEquals(new int[]{0}, jTree.getSelectionRows());
    selectionListener.assertEquals("<log>  " +
                                   "  <selection path='[obj:root]'/>" +
                                   "</log>");
  }

  public void testSelectExistingPath() throws Exception {
    assertNoSelection(jTree);
    tree.select("child1");
    checkSelection(new DefaultMutableTreeNode[]{child1Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1]'/>" +
                   "</log>");

    tree.select("child1/child1_1");
    checkSelection(new DefaultMutableTreeNode[]{child1_1Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1, obj:child1_1]'/>" +
                   "</log>");

    tree.select("child2");
    checkSelection(new DefaultMutableTreeNode[]{child2Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child2]'/>" +
                   "</log>");
  }

  public void testPathsCanUseSubstringsOfNodeNames() throws Exception {
    tree.select("d1/d1_1");
    assertTrue(tree.selectionEquals("child1/child1_1"));
    tree.select("2");
    assertTrue(tree.selectionEquals("child2"));
    tree.select("1/1");
    assertTrue(tree.selectionEquals("child1/child1_1"));
  }

  public void testPathsCanRevealNamingAmbiguitiesWhenUsingSubstrings() throws Exception {
    try {
      tree.select("child/child1_1");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Naming ambiguity: there are several 'child' under 'root'", e.getMessage());
    }
  }

  public void testPathsCanRevealNamingAmbiguitiesWhenUsingExactNames() throws Exception {
    rootNode.add(new DefaultMutableTreeNode(child1));
    try {
      tree.select("child1/child1_1");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("Naming ambiguity: there are several 'child1' under 'root'", e.getMessage());
    }
  }

  public void testMultiSelectionOfPaths() throws Exception {
    assertNoSelection(jTree);
    assertTrue(tree.contentEquals("root\n" +
                                  "  child1\n" +
                                  "    child1_1\n" +
                                  "  child2\n"));
    tree.addToSelection("child1");
    tree.addToSelection("child2");
    checkSelection(new DefaultMutableTreeNode[]{child1Node, child2Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1]'/>" +
                   "  <selection path='[obj:root, obj:child2]'/>" +
                   "</log>");
    tree.removeFromSelection("child1");
    checkSelection(new DefaultMutableTreeNode[]{child2Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1]'/>" +
                   "</log>");

    tree.select("child1");
    checkSelection(new DefaultMutableTreeNode[]{child1Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1]'/>" +
                   "</log>");

    tree.addToSelection("child1/child1_1");
    tree.addToSelection("child2");
    checkSelection(new DefaultMutableTreeNode[]{child1Node, child1_1Node, child2Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1, obj:child1_1]'/>" +
                   "  <selection path='[obj:root, obj:child2]'/>" +
                   "</log>");

    tree.select("child1");
    tree.removeFromSelection("child1");
    assertTrue(tree.selectionIsEmpty());
  }

  public void testSelectingMultipleNodes() throws Exception {
    assertNoSelection(jTree);
    assertTrue(tree.contentEquals("root\n" +
                                  "  child1\n" +
                                  "    child1_1\n" +
                                  "  child2\n"));
    tree.select(new String[]{"child1", "child2"});
    checkSelection(new DefaultMutableTreeNode[]{child1Node, child2Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1]'/>" +
                   "  <selection path='[obj:root, obj:child2]'/>" +
                   "</log>");

    tree.select(new String[]{"child1"});
    checkSelection(new DefaultMutableTreeNode[]{child1Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1]'/>" +
                   "</log>");

    tree.select(new String[]{"child1/child1_1", "child2"});
    checkSelection(new DefaultMutableTreeNode[]{child1_1Node, child2Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1, obj:child1_1]'/>" +
                   "  <selection path='[obj:root, obj:child2]'/>" +
                   "</log>");
  }

  public void testSelectMultiplePathsWithInvalidPath() throws Exception {
    try {
      tree.select(new String[]{"child1", "unknown"});
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(Tree.badTreePath("unknown"), e.getMessage());
    }
  }

  public void testSelectNonexistingPath() throws Exception {
    assertNoSelection(jTree);
    String path = "child1/unexistingElement";
    try {
      tree.select(path);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(Tree.badTreePath("child1/unexistingElement"), e.getMessage());
    }
    assertNoSelection(jTree);
  }

  public void testSelectChildIndexUnderParent() throws Exception {
    tree.select("", 0);
    checkSelection(new DefaultMutableTreeNode[]{child1Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1]'/>" +
                   "</log>");

    tree.select("child1", 0);
    checkSelection(new DefaultMutableTreeNode[]{child1_1Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1, obj:child1_1]'/>" +
                   "</log>");

    tree.select("", 1);
    checkSelection(new DefaultMutableTreeNode[]{child2Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child2]'/>" +
                   "</log>");

    tree.select("child1", 0);
    tree.addToSelection("", 1);
    checkSelection(new DefaultMutableTreeNode[]{child1_1Node, child2Node},
                   "<log>  " +
                   "  <selection path='[obj:root, obj:child1, obj:child1_1]'/>" +
                   "  <selection path='[obj:root, obj:child2]'/>" +
                   "</log>");
  }

  public void testCheckSelection() throws Exception {
    tree.select("child1");
    assertTrue(tree.selectionEquals(new String[]{"child1"}));

    tree.select("child1/child1_1");
    tree.addToSelection("child2");
    assertTrue(tree.selectionEquals(new String[]{"child1/child1_1", "child2"}));

    tree.select("child2");
    assertTrue(tree.selectionEquals(new String[]{"child2"}));
  }

  public void testCheckSelectionWhenSelectionIsNull() throws Exception {
    jTree.setSelectionPaths(null);
    assertTrue(tree.selectionEquals(new String[0]));
    tree.selectionIsEmpty();
  }

  public void testCheckSelectionWithBadPath() throws Exception {
    tree.select("child1/child1_1");
    String pathToCheck = "child1/toto";
    try {
      assertTrue(tree.selectionEquals(pathToCheck));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(Tree.badTreePath("child1/toto"), e.getMessage());
    }
  }

  public void testSelectChildrenWithSubstring() throws Exception {
    assertTrue(tree.selectionIsEmpty());
    tree.select("", "child");
    assertTrue(tree.selectionEquals(new String[]{"child1", "child2"}));
  }

  public void testSelectChildrenWithNoMatch() throws Exception {
    try {
      tree.select("", "UNKNOWN");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals("No children found", e.getMessage());
    }
  }

  public void testSelectionWithASpecificTreeCellValueConverter() throws Exception {
    tree.setCellValueConverter(new DummyTreeCellValueConverter());
    String path1_1 = "_obj:child1_/_obj:child1_1_";
    String path2 = "_obj:child2_";
    tree.select(path1_1);
    assertTrue(tree.selectionEquals(path1_1));
    tree.addToSelection(path2);
    assertTrue(tree.selectionEquals(new String[]{path1_1, path2}));
  }

  private static class SelectionListener extends EventLogger implements TreeSelectionListener {
    public void valueChanged(TreeSelectionEvent e) {
      if (e.getNewLeadSelectionPath() != null) {
        log("selection").add("path", e.getPath().toString());
      }
    }
  }
}
