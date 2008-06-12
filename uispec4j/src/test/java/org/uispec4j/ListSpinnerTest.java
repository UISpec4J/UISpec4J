package org.uispec4j;

import javax.swing.*;

public class ListSpinnerTest extends SpinnerTestCase {
  private ListSpinner listSpinner;

  protected void setUp() throws Exception {
    super.setUp();
    listSpinner = (ListSpinner)spinner;
  }

  public String getText() {
    return "1";
  }

  protected SpinnerModel createSpinnerModel() throws Exception {
    return new SpinnerListModel(new String[] {"1", "2", "3"});
  }

  protected Spinner createSpinner(JSpinner jSpinner) {
    return new ListSpinner(jSpinner);
  }

  public void testContentsEquals() throws Exception {
    assertTrue(listSpinner.contentEquals("1", "2", "3"));
    assertFalse(listSpinner.contentEquals("2", "3", "1"));
  };

  public void testUsingListSpinnerWithOtherModelThanSpinnerListModelThrowsAnException() throws Exception {
    try {
      new ListSpinner(new JSpinner());
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals("Expected JSpinner using a SpinnerListModel", e.getMessage());
    }
  }
}
