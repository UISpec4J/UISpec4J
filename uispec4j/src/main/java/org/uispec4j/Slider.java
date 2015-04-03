package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.utils.ComponentUtils;
import org.uispec4j.utils.Utils;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.TreeMap;

/**
 * <p>Wrapper for JSlider components.</p>
 * This class provides means for checking the contents and the current position of the knob,
 * changing the position, etc.
 */
public class Slider extends AbstractUIComponent {
  public static final String TYPE_NAME = "slider";
  public static final Class[] SWING_CLASSES = {JSlider.class};

  private JSlider jSlider;
  private int precision = 2;

  public Slider(JSlider jSlider) {
    this.jSlider = jSlider;
  }

  public JSlider getAwtComponent() {
    return jSlider;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  /**
   * Checks the slider labels in order.
   */
  public Assertion labelsEqual(final String... expected) {
    return new Assertion() {
      public void check() {
        TreeMap<Integer, String> sortedTree = getSortedTree();
        Utils.assertEquals(expected, sortedTree.values().toArray(new Object[sortedTree.values().size()]));
      }
    };
  }

  /**
   * Moves the knob at the specified label position
   */
  public void setPosition(String label) throws ItemNotFoundException {
    int index = getIndexForLabel(label);
    if (index == -1) {
      throw new ItemNotFoundException("No label '" + label + "' has been found");
    }
    jSlider.setValue(index);
  }

  /**
   * Checks that the current position corresponds to the specified label
   */
  public Assertion positionEquals(final String expectedLabel) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(expectedLabel, getCurrentLabel());
      }
    };
  }

  /**
   * Checks the knob position as a percentage (0-100) of the available range.
   * The actual position must be equal to the given value plus or minus the slider precision.
   *
   * @param expectedValue an int between 0 and 100, or -1 if the status is undeterminate
   * @see #setPrecision(int)
   */
  public Assertion relativePositionEquals(final int expectedValue) {
    return new Assertion() {
      public void check() {
        int relativePosition = getRelativePosition();
        AssertAdapter.assertTrue("Expected " + expectedValue + " but was " + relativePosition,
                                 isRoughlyEqual(expectedValue, relativePosition));
      }
    };
  }

  /**
   * Sets the knob position as a percentage (0-100) of the available range.
   *
   * @param percentage an int between 0 and 100
   */
  public void setRelativePosition(int percentage) {
    if (percentage < 0 || percentage > 100) {
      AssertAdapter.fail("Value must be within [0..100]");
    }
    int max = jSlider.getMaximum();
    int min = jSlider.getMinimum();
    int value = min + (percentage * (max - min)) / 100;
    jSlider.setValue(value);
  }

  /**
   * <p>Sets the precision for the relative position check. This precision is the greatest difference
   * allowed between the actual and expected position values (both are integers between 0
   * and 100).</p>
   * The default precision is 2.
   *
   * @see #relativePositionEquals(int)
   */
  public void setPrecision(int value) {
    this.precision = value;
  }

  private int getRelativePosition() {
    int minimum = jSlider.getMinimum();
    int value = jSlider.getValue() - minimum;
    int length = jSlider.getMaximum() - minimum;
    return value * 100 / length;
  }

  private boolean isRoughlyEqual(int actualValue, int expectedValue) {
    return Math.abs(actualValue - expectedValue) <= precision;
  }

  private int getIndexForLabel(String label) {
    Dictionary dictionary = jSlider.getLabelTable();
    for (Enumeration indices = dictionary.keys(); indices.hasMoreElements();) {
      Integer index = (Integer)indices.nextElement();
      JComponent component = (JComponent)dictionary.get(index);
      if (label.equals(ComponentUtils.getDisplayedName(component))) {
        return index;
      }
    }
    return -1;
  }

  private String getCurrentLabel() {
    int value = jSlider.getValue();
    Dictionary dictionary = jSlider.getLabelTable();
    for (Enumeration indices = dictionary.keys(); indices.hasMoreElements();) {
      Integer index = (Integer)indices.nextElement();
      JComponent component = (JComponent)dictionary.get(index);
      if (Utils.equals(index, value)) {
        return ComponentUtils.getDisplayedName(component);
      }
    }
    return null;
  }

  private TreeMap<Integer, String> getSortedTree() {
    Dictionary dictionary = jSlider.getLabelTable();
    TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>();
    for (Enumeration indices = dictionary.keys(); indices.hasMoreElements();) {
      Integer index = (Integer)indices.nextElement();
      JComponent component = (JComponent)dictionary.get(index);
      treeMap.put(index, ComponentUtils.getDisplayedName(component));
    }
    return treeMap;
  }
}
