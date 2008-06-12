package org.uispec4j;

/**
 * Thrown when several components match a given search specification in {@link Panel}.
 */
public class ComponentAmbiguityException extends RuntimeException {
  public ComponentAmbiguityException(String message) {
    super(message);
  }
}
