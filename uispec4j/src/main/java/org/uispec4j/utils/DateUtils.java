package org.uispec4j.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for checking date as String formatted by a date format.
 * By default, the date format u
 *
 * @see java.text.DateFormat
 */
public class DateUtils {

  public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm");
  private static DateFormat dateFormat = DEFAULT_DATE_FORMAT;

  private DateUtils() {
    // Instanceless class
  }

  /**
   * Returns the date from the given value parsed by the date format.
   */
  public static Date getDate(String yyyyMMdd) throws ParseException {
    synchronized (dateFormat) {
      return dateFormat.parse(yyyyMMdd);
    }
  }

  /**
   * Returns the Date as String formated by the date format.
   */
  public static String getStandardDate(Date date) {
    synchronized (dateFormat) {
      return dateFormat.format(date);
    }
  }

  /**
   * Sets the date format.
   */
  public static void setDateFormat(DateFormat dateFormat) {
    DateUtils.dateFormat = dateFormat;
  }
}
