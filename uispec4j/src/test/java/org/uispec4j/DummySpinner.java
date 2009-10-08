package org.uispec4j;

import org.uispec4j.utils.DateUtils;

import javax.swing.*;
import java.util.Arrays;
import java.util.Calendar;

class DummySpinner {
  public static final String START_DATE = "1964.11.23 19:55";
  public static final String END_DATE = "1984.11.23 19:55";
  public static final String CURRENT_DATE = "1974.11.23 19:55";
  public static final String OTHER_DATE = "1964.11.23 19:56";

  public static SpinnerDateModel dateModel() throws Exception {
    return new DateModel();
  }

  public static SpinnerListModel listModel(String... values) throws Exception {
    return new ListModel(values);
  }

  public static SpinnerNumberModel numberModel() throws Exception {
    return new NumberModel();
  }

  public static SpinnerNumberModel numberModel(int value, int minimum, int maximum, int stepSize) throws Exception {
    return new NumberModel(value, minimum, maximum, stepSize);
  }

  public static class DateModel extends SpinnerDateModel {
    DateModel() throws Exception {
      super(DateUtils.getDate(CURRENT_DATE),
            DateUtils.getDate(START_DATE),
            DateUtils.getDate(END_DATE),
            Calendar.MONTH);
    }
  }

  public static class ListModel extends SpinnerListModel {
    public ListModel(String... values) {
      if (values.length > 0) {
        setList(Arrays.asList(values));
      }
    }
  }

  public static  class NumberModel extends SpinnerNumberModel {
    public NumberModel() {
    }

    public NumberModel(int value, int minimum, int maximum, int stepSize) {
      super(value, minimum, maximum, stepSize);
    }
  }
}
