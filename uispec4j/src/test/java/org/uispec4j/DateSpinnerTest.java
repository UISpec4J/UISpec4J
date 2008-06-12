package org.uispec4j;

import org.uispec4j.utils.DateUtils;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class DateSpinnerTest extends SpinnerTestCase {
  private DateSpinner dateSpinner;
  private Date currentDate;

  protected void setUp() throws Exception {
    super.setUp();
    dateSpinner = (DateSpinner) spinner;
  }

  public String getText() {
    return ((JSpinner.DateEditor) dateSpinner.getAwtComponent().getEditor())
      .getFormat().format(currentDate);
  }

  protected SpinnerModel createSpinnerModel() throws Exception {
    Date startDate = DateUtils.getDate("1964.11.23 19:55");
    currentDate = DateUtils.getDate("1974.11.23 19:55");
    Date endDate = DateUtils.getDate("1984.11.23 19:55");
    return new SpinnerDateModel(currentDate, startDate, endDate, Calendar.MONTH);
  }

  protected Spinner createSpinner(JSpinner jSpinner) {
    return new DateSpinner(jSpinner);
  }

  public void testStartAndEndDate() throws Exception {
    assertTrue(dateSpinner.startDateEquals("1964.11.23 19:55"));
    assertTrue(dateSpinner.endDateEquals("1984.11.23 19:55"));

    assertFalse(dateSpinner.startDateEquals("1964.11.23 19:56"));
    assertFalse(dateSpinner.endDateEquals("1964.11.23 19:56"));
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
