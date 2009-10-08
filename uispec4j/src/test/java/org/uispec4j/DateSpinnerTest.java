package org.uispec4j;

import org.uispec4j.utils.DateUtils;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class DateSpinnerTest extends SpinnerTestCase {
  private DateSpinner dateSpinner;
  private Date currentDate;
  private static final String START_DATE = "1964.11.23 19:55";
  private static final String END_DATE = "1984.11.23 19:55";
  private static final String OTHER_DATE = "1964.11.23 19:56";

  protected void setUp() throws Exception {
    super.setUp();
    dateSpinner = (DateSpinner)spinner;
  }

  public String getText() {
    return ((JSpinner.DateEditor)dateSpinner.getAwtComponent().getEditor())
      .getFormat().format(currentDate);
  }

  protected SpinnerModel createSpinnerModel() throws Exception {
    currentDate = DateUtils.getDate("1974.11.23 19:55");
    return new DummySpinnerDateModel(currentDate);
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

  private static class DummySpinnerDateModel extends SpinnerDateModel {
    private DummySpinnerDateModel(Date currentDate) throws Exception {
      super(currentDate,
            DateUtils.getDate(START_DATE),
            DateUtils.getDate(END_DATE),
            Calendar.MONTH);
    }
  }
}
