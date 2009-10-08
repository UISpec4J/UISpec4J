package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.utils.Utils;

import javax.swing.*;
import java.util.List;

/**
 * Wrapper for JSpinner components implementing a SpinnerListModel.
 */
public class ListSpinner extends Spinner {
  private SpinnerListModel model;

  public ListSpinner(JSpinner jSpinner) {
    super(jSpinner);
    SpinnerModel model = jSpinner.getModel();
    if (!SpinnerListModel.class.isInstance(model)) {
      throw new ItemNotFoundException("Expected JSpinner using a SpinnerListModel");
    }
    this.model = (SpinnerListModel)model;
  }

  /**
   * Checks the list spinner contents
   */
  public Assertion contentEquals(final Object... expectedContents) {
    return new Assertion() {
      public void check() {
        List list = model.getList();
        Utils.assertEquals(expectedContents, list.toArray(new Object[list.size()]));
      }
    };
  }
}
