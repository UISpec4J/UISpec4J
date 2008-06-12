package org.uispec4j;

/**
 * Interface for adapting a UISpec4J test suite to a given application.
 *
 * @see UISpecTestCase
 */
public interface UISpecAdapter {
  Window getMainWindow();
}
