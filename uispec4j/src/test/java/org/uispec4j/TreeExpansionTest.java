package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.utils.AssertionFailureNotDetectedError;

import javax.swing.tree.TreePath;

public class TreeExpansionTest extends TreeTestCase {

  public void testExpandAndCollapsePath() throws Exception {
    TreePath path = new TreePath(child1Node.getPath());
    jTree.expandPath(path);
    checkExpanded("child1", true);
    jTree.collapsePath(path);
    checkExpanded("child1", false);
  }

  public void testAssertPathExpanded() throws Exception {
    tree.expand("child1", true);
    checkExpanded("child1", true);
    tree.expand("child1", false);
    checkExpanded("child1", false);
  }

  public void testAssertPathExpandedNeedsAValidPath() throws Exception {
    try {
      tree.expand("unknown", true);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals(Tree.badTreePath("unknown"), e.getMessage());
    }
  }

  private void checkExpanded(String path, boolean expanded) {
    assertEquals(expanded, tree.pathIsExpanded(path));
    try {
      assertEquals(!expanded, tree.pathIsExpanded(path));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }
}
