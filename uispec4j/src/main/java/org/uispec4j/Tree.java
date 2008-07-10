package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.utils.ColorUtils;
import org.uispec4j.utils.Utils;

import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wrapper for JTree components.<p>
 * The nodes of a tree are identified using stringified paths. For instance, for the tree
 * below:
 * <pre><code>
 * root
 *   |
 *   +- child
 *   |  |
 *   |  +- subChild
 *   |
 *   +- otherChild
 *      |
 *      +- otherSubChild
 * </code></pre>
 * the "subChild" element is identified with the following path:
 * <pre><code>
 *   child/subChild
 * </code></pre>
 * Note that when defining paths the root element name is always omitted. The root
 * node path itself is denoted by an empty string ("").<p>
 * The default path separator is "/". However, this separator can be customized as follows:
 * <ul>
 * <li>By setting it on a given Tree instance using {@link Tree#setSeparator(String)}</li>
 * <li>By setting it on all new Tree instances using {@link Tree#setDefaultSeparator(String)}</li>
 * <li>By setting it on all new Tree instances using the <code>uispec4j.tree.separator</code>
 * property.</li>
 * </ul>
 * When using paths, it is also possible to use substrings from the displayed node names.
 * For instance, instead of writing:
 * <pre><code>
 *   otherChild/otherSubChild
 * </code></pre>
 * one can write:
 * <pre><code>
 *   other/sub
 * </code></pre>
 * <p/>
 * The contents of the tree can be checked with {@link #contentEquals(String)},
 * which is used as follows:
 * <pre><code>
 * assertTrue(jTree.contentEquals("root\n" +
 *                                "  child1\n" +
 *                                "    child1_1\n" +
 *                                "  child2"));
 * </code></pre>
 * <p/>
 * The conversion between the values (Strings) given in the tests and the values
 * actually displayed by the JTree renderer is performed by a dedicated
 * {@link TreeCellValueConverter}, which retrieves the graphical component that draws
 * the tree nodes and determines the displayed value accordingly.
 * A {@link DefaultTreeCellValueConverter} is used by default by the Tree component.
 */
public class Tree extends AbstractUIComponent {

  public static final String TYPE_NAME = "tree";
  public static final Class[] SWING_CLASSES = {JTree.class};
  static final String SEPARATOR_PROPERTY = "uispec4j.tree.separator";

  private JTree jTree;
  static String defaultSeparator = "/";
  private String separator;
  private TreeCellValueConverter cellValueConverter = new DefaultTreeCellValueConverter();

  private static final Pattern COLOR_PROPERTY_PATTERN =
    Pattern.compile(" #\\(.*color=([\\w]+)\\)");

  public Tree(JTree jTree) {
    this.jTree = jTree;
    this.separator = initSeparator();
  }

  private static String initSeparator() {
    String property = System.getProperty(Tree.SEPARATOR_PROPERTY);
    if ((property != null) && (property.length() > 0)) {
      return property;
    }
    return Tree.defaultSeparator;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public JTree getAwtComponent() {
    return jTree;
  }

  /**
   * Returns the JTree wrapped by this component.
   */
  public JTree getJTree() {
    return jTree;
  }

  /**
   * Sets the separator to be used for specifying node paths in this jTree instance.
   */
  public void setSeparator(String separator) {
    checkSeparator(separator);
    this.separator = separator;
  }

  private static void checkSeparator(String separator) {
    if (separator == null) {
      throw new IllegalArgumentException("Separator must not be null");
    }
    else if (separator.length() == 0) {
      throw new IllegalArgumentException("Separator must not be empty");
    }
  }

  /**
   * Returns the separator currently used for specifying node paths in this jTree instance.
   */
  public String getSeparator() {
    return separator;
  }

  /**
   * Sets the separator to be used for specifying node paths in new jTree instances.
   */
  public static void setDefaultSeparator(String separator) {
    checkSeparator(separator);
    defaultSeparator = separator;
  }

  /**
   * Sets a new converter for retrieving the text displayed on the tree cells.
   */
  public void setCellValueConverter(TreeCellValueConverter converter) {
    this.cellValueConverter = converter;
  }

  /**
   * Checks the nodes structure displayed by the jTree.<p>
   * The expected contents is a newline (\n) separated string where nodes are
   * indented with two-space steps.
   * For instance:
   * <code><pre>
   * assertTrue(jTree.contentEquals("root\n" +
   *                                "  child1\n" +
   *                                "    child1_1\n" +
   *                                "  child2"));
   * </pre></code>
   * Text display properties such as boldness and color can be checked using a "#(...)"
   * specifier.
   * For instance:
   * <code><pre>
   * assertTrue(jTree.contentEquals("root\n" +
   *                                "  child1 #(bold)\n" +
   *                                "    child1_1 #(bold,color=red)\n" +
   *                                "  child2"));
   * </pre></code>
   * The properties are defined as follows:
   * <ul>
   * <li>The "bold" property must be present if and only if the node text is bold</li>
   * <li>The "color" property value can be numeric ("0000ee") or approximative ("blue")
   * (see the <a href="http://www.uispec4j.org/usingcolors.html">Using colors</a> page
   * for more information)</li>
   * <li>The "bold" property, if present, must be placed before the "color" property</li>
   * </ul>
   */
  public Assertion contentEquals(final String expectedContents) {
    return new Assertion() {
      public void check() {
        String trimmedExpected = expectedContents.trim();
        AssertAdapter.assertTrue("Expected tree description should not be empty",
                                  (trimmedExpected != null) && (trimmedExpected.length() > 0));
        checkContents(trimmedExpected);
      }
    };
  }

  /**
   * Checks that a node identified by the given path is present in the jTree.
   */
  public Assertion contains(final String path) {
    return new Assertion() {
      public void check() {
        getTreePath(path);
      }
    };
  }

  /**
   * Selects the root node of the jTree.
   */
  public void selectRoot() {
    jTree.setSelectionPath(new TreePath(jTree.getModel().getRoot()));
  }

  /**
   * Expands the current jTree selection with a given node.
   */
  public void addToSelection(String path) {
    jTree.addSelectionPath(getTreePath(path));
  }

  /**
   * Removes the given node from the current jTree selection.
   */
  public void removeFromSelection(String path) {
    TreePath jTreePath = getTreePath(path);
    jTree.removeSelectionPath(jTreePath);
  }

  /**
   * Expands the current jTree selection with a node identified by its position in its parent node.
   * <p>This method is preferred over {@link #addToSelection(String)} when there are several nodes
   * with the same name under a given parent.
   */
  public void addToSelection(String parentPath, int childIndex) {
    jTree.addSelectionPath(computeChildTreePath(parentPath, childIndex));
  }

  /**
   * Removes the current selection.
   */
  public void clearSelection() {
    jTree.clearSelection();
  }

  /**
   * Sets the selection on the given node.
   */
  public void select(String path) {
    jTree.clearSelection();
    jTree.setSelectionPath(getTreePath(path));
  }

  /**
   * Sets the jTree selection on a node identified by its position in its parent node.
   * <p>This method is preferred over {@link #select(String)} when there are several nodes
   * with the same name under a given parent.
   */
  public void select(String parentPath, int childIndex) {
    int childCount = getChildCount(parentPath);
    if (childIndex < 0 || childCount <= childIndex) {
      throw new RuntimeException("No child found under '" +
                                 parentPath +
                                 "' for index '" + childIndex + "'");
    }
    jTree.clearSelection();
    jTree.addSelectionPath(computeChildTreePath(parentPath, childIndex));
  }

  /**
   * Selects under a given parent all the nodes whose name contains a given substring.
   * This method will throw an exception if no the parent path was invalid or if no children
   * were found.
   */
  public void select(String parentPath, String childSubstring) {
    TreePath jTreePath = getTreePath(parentPath);
    TreeModel model = jTree.getModel();
    Object node = jTreePath.getLastPathComponent();
    List<TreePath> subPaths = new ArrayList<TreePath>();
    for (int i = 0, max = model.getChildCount(node); i < max; i++) {
      Object child = model.getChild(node, i);
      String text = getShownText(child);
      if (text.indexOf(childSubstring) >= 0) {
        subPaths.add(jTreePath.pathByAddingChild(child));
      }
    }
    if (subPaths.isEmpty()) {
      AssertAdapter.fail("No children found");
    }

    TreePath[] result = subPaths.toArray(new TreePath[subPaths.size()]);
    jTree.setSelectionPaths(result);
  }

  public void select(String[] paths) {
    jTree.clearSelection();
    for (String path : paths) {
      jTree.addSelectionPath(getTreePath(path));
    }
  }

  /**
   * Simulates a user left-click on a given node.
   */
  public void click(String path) {
    TreePath jTreePath = getTreePath(path);
    jTree.setSelectionPath(jTreePath);
    clickOnTreePath(getTreePath(path), false, Key.Modifier.NONE);
  }

  /**
   * Simulates a user right-click on a given node.
   */
  public void rightClick(String path) {
    TreePath jTreePath = getTreePath(path);
    jTree.setSelectionPath(jTreePath);
    clickOnTreePath(jTreePath, true, Key.Modifier.NONE);
  }

  /**
   * Right-clicks on the first selected node.
   */
  public void rightClickInSelection() {
    TreePath selectionPath = jTree.getSelectionPath();
    AssertAdapter.assertNotNull("There is no current selection", selectionPath);
    clickOnTreePath(selectionPath, true, Key.Modifier.NONE);
  }

  public Trigger triggerClick(final String path) {
    return new Trigger() {
      public void run() throws Exception {
        click(path);
      }
    };
  }

  public Trigger triggerRightClick(final String path) {
    return new Trigger() {
      public void run() throws Exception {
        rightClick(path);
      }
    };
  }

  public Trigger triggerRightClickInSelection() {
    return new Trigger() {
      public void run() throws Exception {
        rightClickInSelection();
      }
    };
  }

  /**
   * Returns the number of children of a given node.
   */
  public int getChildCount(String path) {
    TreePath jTreePath = getTreePath(path);
    return jTree.getModel().getChildCount(jTreePath.getLastPathComponent());
  }

  /**
   * Checks that a given node is selected, and that is is the only selection.
   */
  public Assertion selectionEquals(final String path) {
    return new Assertion() {
      public void check() {
        TreePath selectionPath = jTree.getSelectionPath();
        AssertAdapter.assertNotNull("The current tree selection is null.", selectionPath);
        TreePath expectedPath = getTreePath(path);
        AssertAdapter.assertNotNull("The expected path is not valid.", expectedPath);
        AssertAdapter.assertEquals(path, pathToString(selectionPath, separator));
      }
    };
  }

  /**
   * Checks the selection contents.
   */
  public Assertion selectionEquals(final String[] paths) {
    return new Assertion() {
      public void check() {
        String[] expectedPaths = paths.clone();
        TreePath[] selectionPaths = jTree.getSelectionPaths();
        if (selectionPaths == null) {
          selectionPaths = new TreePath[0];
        }
        String[] actual = new String[selectionPaths.length];
        for (int i = 0; i < selectionPaths.length; i++) {
          TreePath selectionPath = selectionPaths[i];
          AssertAdapter.assertNotNull("The tree has an unexpectedly null selection path.", selectionPath);
          TreePath expectedPath = getTreePath(paths[i]);
          actual[i] = pathToString(expectedPath, separator);
        }
        Arrays.sort(actual);
        ArrayUtils.assertEquals(expectedPaths, actual);
      }
    };
  }

  /**
   * Checks that the selection is empty.
   */
  public Assertion selectionIsEmpty() {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(0, jTree.getSelectionCount());
      }
    };
  }

  /**
   * Checks the font color used on a given node.
   */
  public Assertion foregroundEquals(final String path, final String color) {
    return new Assertion() {
      public void check() {
        Object userObject = getTreePath(path).getLastPathComponent();
        ColorUtils.assertEquals(color, getShownColor(userObject));
      }
    };
  }

  /**
   * Checks that the a given node of the jTree is expanded - i.e. that its children are made visible.
   *
   * @param path a String identifying the path to be expanded or collapsed
   */
  public Assertion pathIsExpanded(final String path) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue(jTree.isExpanded(getTreePath(path)));
      }
    };
  }

  /**
   * Expands or collapses a given node.
   *
   * @param path   a String identifying the path to be expanded or collapsed
   * @param expand if true, expand the node, and collapse it otherwise
   */
  public void expand(String path, boolean expand) {
    TreePath jTreePath = getTreePath(path);
    if (expand) {
      jTree.expandPath(jTreePath);
    }
    else {
      jTree.collapsePath(jTreePath);
    }
  }

  /**
   * Expands all the nodes of the jTree.
   */
  public void expandAll() {
    expandSubTree(new TreePath(jTree.getModel().getRoot()));
  }

  public String toString() {
    return getContent();
  }

  private TreePath computeChildTreePath(String parentPath, int childIndex) {
    TreePath jTreePath = getTreePath(parentPath);
    Object child = jTree.getModel().getChild(jTreePath.getLastPathComponent(), childIndex);
    return jTreePath.pathByAddingChild(child);
  }

  private void clickOnTreePath(TreePath path,
                               boolean useRightClick,
                               Key.Modifier keyModifier) {
    jTree.expandPath(path.getParentPath());
    Rectangle rect = jTree.getRowBounds(jTree.getRowForPath(path));
    if (rect != null) {
      Mouse.doClickInRectangle(jTree, rect, useRightClick, keyModifier);
    }
  }

  private String getShownText(Object object) {
    return cellValueConverter.getValue(getRenderedComponent(object), object);
  }

  private Color getShownColor(Object object) {
    return cellValueConverter.getForeground(getRenderedComponent(object), object);
  }

  private boolean isBold(Object object) {
    return cellValueConverter.isBold(getRenderedComponent(object), object);
  }

  private Component getRenderedComponent(Object object) {
    TreeCellRenderer renderer = jTree.getCellRenderer();
    return renderer.getTreeCellRendererComponent(jTree, object,
                                                 false, false, false, 0, false);
  }

  private TreePath getTreePath(String path) {
    TreePath jTreePath = findTreePath(path);
    if (jTreePath == null) {
      AssertAdapter.fail(badTreePath(path));
    }
    return jTreePath;
  }

  private TreePath findTreePath(String path) {
    String[] pathArray = toArray(path, separator);
    TreeModel model = jTree.getModel();
    Object[] objects = new Object[pathArray.length + 1];
    Object node = model.getRoot();
    objects[0] = node;
    for (int i = 0; i < pathArray.length; i++) {
      Object exactMatch = null;
      Object substringMatch = null;
      boolean substringAmbiguity = false;
      for (int j = 0; (j < model.getChildCount(node)); j++) {
        Object child = model.getChild(node, j);
        String shownText = getShownText(child);
        if (pathArray[i].equals(shownText)) {
          if (exactMatch != null) {
            AssertAdapter.fail("Naming ambiguity: there are several '" +
                                pathArray[i] + "' under '" +
                                getShownText(node) + "'");
          }
          exactMatch = child;
        }
        else if (shownText.indexOf(pathArray[i]) >= 0) {
          if (substringMatch != null) {
            substringAmbiguity = true;
          }
          substringMatch = child;
        }
      }
      Object result = null;
      if (exactMatch != null) {
        result = exactMatch;
      }
      else if (substringAmbiguity) {
        AssertAdapter.fail("Naming ambiguity: there are several '" +
                                       pathArray[i] + "' under '" +
                                       getShownText(node) + "'");
      }
      else {
        result = substringMatch;
      }
      if (result == null) {
        return null;
      }
      objects[i + 1] = result;
      node = result;
    }
    return new TreePath(objects);
  }

  private static String[] toArray(String path, String separator) {
    List<String> result = new ArrayList<String>();
    for (int index = 0; index < path.length();) {
      int nextSeparatorPosition = path.indexOf(separator, index);
      if (nextSeparatorPosition == -1) {
        nextSeparatorPosition = path.length();
      }
      result.add(path.substring(index, nextSeparatorPosition));
      index = nextSeparatorPosition + separator.length();
    }
    return result.toArray(new String[result.size()]);
  }

  private void expandSubTree(TreePath path) {
    TreeModel jTreeModel = jTree.getModel();
    Object node = path.getLastPathComponent();
    for (int i = 0; i < jTreeModel.getChildCount(node); i++) {
      Object child = jTreeModel.getChild(node, i);
      TreePath childPath = path.pathByAddingChild(child);
      if (!isLeaf(childPath)) {
        expandSubTree(childPath);
      }
    }
    jTree.expandPath(path);
  }

  private boolean isLeaf(TreePath path) {
    return jTree.getModel().isLeaf(path.getLastPathComponent());
  }

  private String pathToString(TreePath jTreePath, String separator) {
    Object[] path = jTreePath.getPath();
    StringBuffer buffer = new StringBuffer();
    for (int i = 1; i < path.length; i++) {
      buffer.append(getShownText(path[i]));
      if (i < path.length - 1) {
        buffer.append(separator);
      }
    }
    return buffer.toString();
  }

  private void checkContents(String trimmedExpected) {
    compareContents(trimmedExpected, getContent());
  }

  private String getContent() {
    TreeModel model = jTree.getModel();
    Object root = model.getRoot();
    StringBuffer buffer = new StringBuffer();
    fillBuffer(root, model, buffer, "");
    return buffer.toString();
  }

  private void compareContents(String expected, String actual) {
    if (expected.equals(actual)) {
      return;
    }
    if (!areLinesEqual(toLines(expected), toLines(actual))) {
      AssertAdapter.assertEquals(expected, actual);
    }
  }

  private boolean areLinesEqual(List expected, List actual) {
    if (expected.size() != actual.size()) {
      return false;
    }
    for (Iterator expectedIter = expected.iterator(), actualIter = actual.iterator();
         expectedIter.hasNext() && actualIter.hasNext();) {
      if (!areLinesEqual((String)expectedIter.next(),
                         (String)actualIter.next())) {
        return false;
      }
    }
    return true;
  }

  private boolean areLinesEqual(String expected, String actual) {
    if (expected.equals(actual)) {
      return true;
    }
    Matcher expectedMatcher = COLOR_PROPERTY_PATTERN.matcher(expected);
    Matcher actualMatcher = COLOR_PROPERTY_PATTERN.matcher(actual);
    String expectedWithoutColor = expectedMatcher.replaceFirst("C");
    String actualWithoutColor = actualMatcher.replaceFirst("C");
    if (!expectedWithoutColor.equals(actualWithoutColor)) {
      return false;
    }
    String expectedColor = expectedMatcher.group(1);
    String actualColor = actualMatcher.group(1);
    return ColorUtils.equals(expectedColor, ColorUtils.getColor(actualColor));
  }

  private List<String> toLines(String text) {
    StringTokenizer tokenizer = new StringTokenizer(text, "\n");
    List<String> result = new ArrayList<String>();
    while (tokenizer.hasMoreTokens()) {
      result.add(tokenizer.nextToken());
    }
    return result;
  }

  private void fillBuffer(Object obj,
                          TreeModel model,
                          StringBuffer buffer,
                          String indent) {
    String text = getShownText(obj);
    buffer.append(indent).append(text);
    boolean bold = isBold(obj);
    fillNodeProperties(bold, getShownColor(obj), buffer);
    buffer.append('\n');
    for (int i = 0, max = model.getChildCount(obj); i < max; i++) {
      Object child = model.getChild(obj, i);
      fillBuffer(child, model, buffer, indent + "  ");
    }
  }

  private void fillNodeProperties(boolean bold, Color shownColor, StringBuffer buffer) {
    String shownColorDescription = getShownColorDescription(shownColor);
    if (bold || (shownColorDescription != null)) {
      buffer.append(" #(");
      if (bold) {
        buffer.append("bold");
        if (shownColorDescription != null) {
          buffer.append(',');
        }
      }
      if (shownColorDescription != null) {
        buffer.append("color=").append(shownColorDescription);
      }
      buffer.append(")");
    }
  }

  private String getShownColorDescription(Color shownColor) {
    return isDefaultColor(shownColor) ? null : ColorUtils.getColorDescription(shownColor);
  }

  private boolean isDefaultColor(Color color) {
    return Color.BLACK.equals(color) || Utils.equals(color, getDefaultForegroundColor());
  }

  private Color getDefaultForegroundColor() {
    AccessibleContext context = jTree.getAccessibleContext();
    if (context instanceof AccessibleComponent) {
      return ((AccessibleComponent)context).getForeground();
    }
    return null;
  }

  static String badTreePath(String path) {
    return "Could not find element '" + path + "'";
  }
}