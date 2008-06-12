package org.uispec4j.finder;

/**
 * Miscellaneous utilities used by the component finding mechanism
 */
public class FinderUtils {
  private FinderUtils() {
  }

  public static StringMatcher[] getMatchers(String reference) {
    return new StringMatcher[]{StringMatcher.identity(reference), StringMatcher.substring(reference)};
  }
}
