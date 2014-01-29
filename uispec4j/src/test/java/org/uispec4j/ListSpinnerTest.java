package org.uispec4j;

import org.junit.Test;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

public class ListSpinnerTest extends SpinnerTestCase {
  private ListSpinner listSpinner;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    listSpinner = (ListSpinner)spinner;
  }

  @Override
  public String getText() {
    return "1";
  }

  @Override
  protected SpinnerModel createSpinnerModel() throws Exception {
    return DummySpinner.listModel("1", "2", "3");
  }

  @Override
  protected Spinner createSpinner(JSpinner jSpinner) {
    return new ListSpinner(jSpinner);
  }

  @Test
  public void testContentsEquals() throws Exception {
    assertTrue(listSpinner.contentEquals("1", "2", "3"));
    assertFalse(listSpinner.contentEquals("2", "3", "1"));
  };

  @Test
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
