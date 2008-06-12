package org.uispec4j;

/**
 * Thrown by UIComponents when a search cannot identify a single result.
 */
class ItemAmbiguityException extends RuntimeException {
  public ItemAmbiguityException(String searchedValue, String[] foundItems) {
    super(computeMessage(foundItems, searchedValue));
  }

  private static String computeMessage(String[] foundItems, String searchedValue) {
    StringBuffer buffer =
      new StringBuffer(Integer.toString(foundItems.length))
        .append(" items are matching the same pattern '")
        .append(searchedValue)
        .append("': [");
    for (int i = 0; i < foundItems.length; i++) {
      buffer.append(foundItems[i]);
      buffer.append((i < foundItems.length - 1) ? "," : "]");
    }
    return buffer.toString();
  }
}
