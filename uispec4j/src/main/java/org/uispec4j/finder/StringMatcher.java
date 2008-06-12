package org.uispec4j.finder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String matching policy used withing the component searching mechanism.
 * Three ready-to-use strategies are provided (identity/substring/regexp),
 * but you can provide your own by implementing this class.
 */
public abstract class StringMatcher {
  public abstract boolean matches(String toCompare);

  public static StringMatcher identity(final String reference) {
    return new StringMatcher() {
      public boolean matches(String toCompare) {
        if (reference == null) {
          return toCompare == null;
        }
        return reference.equalsIgnoreCase(toCompare);
      }
    };
  }

  public static StringMatcher substring(final String reference) {
    final String uppercasedReference = reference.toUpperCase();
    return new StringMatcher() {
      public boolean matches(String toCompare) {
        if ((toCompare == null)) {
          return false;
        }
        String realToCompare = toCompare.toUpperCase();
        return (realToCompare.indexOf(uppercasedReference) >= 0);
      }
    };
  }

  public static StringMatcher regexp(String reference) {
    final Pattern pattern = Pattern.compile(reference);
    return new StringMatcher() {
      public boolean matches(String toCompare) {
        String realToCompare = (toCompare == null) ? "" : toCompare;
        Matcher matcher = pattern.matcher(realToCompare);
        return matcher.matches();
      }
    };
  }
}
