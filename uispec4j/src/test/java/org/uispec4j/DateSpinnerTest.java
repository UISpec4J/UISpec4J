package org.uispec4j;

import static org.uispec4j.DummySpinner.*;

import javax.swing.*;
import java.util.Calendar;

public class DateSpinnerTest extends SpinnerTestCase {
  private DateSpinner dateSpinner;
  private SpinnerDateModel model;

  public DateSpinnerTest() throws Exception {
    model = DummySpinner.dateModel();
  }

  protected void setUp() throws Exception {
    super.setUp();
    dateSpinner = (DateSpinner)spinner;
  }

  public String getText() {
    return ((JSpinner.DateEditor)dateSpinner.getAwtComponent().getEditor())
      .getFormat().format(model.getDate());
  }

  protected SpinnerModel createSpinnerModel() throws Exception {
    return model;
  }

  protected Spinner createSpinner(JSpinner jSpinner) {
    return new DateSpinner(jSpinner);
  }

  public void testStartAndEndDate() throws Exception {
    assertTrue(dateSpinner.startDateEquals(START_DATE));
    assertTrue(dateSpinner.endDateEquals(END_DATE));

    assertFalse(dateSpinner.startDateEquals(OTHER_DATE));
    assertFalse(dateSpinner.endDateEquals(OTHER_DATE));
  }

  public void testCalendarFielsEquals() throws Exception {
    assertTrue(dateSpinner.calendarFieldsEquals(Calendar.MONTH));
    assertFalse(dateSpinner.calendarFieldsEquals(Calendar.YEAR));
  }

  public void testUsingDateSpinnerWithOtherModelThanSpinnerDateModelThrowsAnException() throws Exception {
    try {
      new DateSpinner(new JSpinner());
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals("Expected JSpinner using a SpinnerDateModel", e.getMessage());
    }
  }
}
