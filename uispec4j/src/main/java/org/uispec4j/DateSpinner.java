package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.utils.DateUtils;

import javax.swing.*;
import java.text.ParseException;
import java.util.Date;

/**
 * Wrapper for JSpinner components implementing a SpinnerDateModel.
 */
public class DateSpinner extends Spinner {
  private SpinnerDateModel model;

  public DateSpinner(JSpinner jSpinner) throws ItemNotFoundException {
    super(jSpinner);
    SpinnerModel model = jSpinner.getModel();
    if (!SpinnerDateModel.class.isInstance(model)) {
      throw new ItemNotFoundException("Expected JSpinner using a SpinnerDateModel");
    }
    this.model = (SpinnerDateModel)model;
  }

  /**
   * Checks that the date spinner displays starts with the given value
   *
   * @see DateUtils to format the date as String
   */
  public Assertion startDateEquals(final String expectedStartDate) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(extractDate(expectedStartDate), model.getStart());
      }
    };
  }

  /**
   * Checks that the date spinner displays ends with the given value
   *
   * @see DateUtils to format the date as String
   */
  public Assertion endDateEquals(final String expectedEndDate) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(extractDate(expectedEndDate), model.getEnd());
      }
    };
  }

  /**
   * Checks that the date spinner computes previous and next value with the given value.
   * {@link java.util.Calendar} constants
   */
  public Assertion calendarFieldsEquals(final int expectedCalendarFields) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(expectedCalendarFields, model.getCalendarField());
      }
    };
  }

  private Date extractDate(String expectedStartDate) {
    Date date = null;
    try {
      date = DateUtils.getDate(expectedStartDate);
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return date;
  }

}
