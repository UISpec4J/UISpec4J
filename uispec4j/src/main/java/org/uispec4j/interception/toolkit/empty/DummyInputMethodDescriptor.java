package org.uispec4j.interception.toolkit.empty;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodDescriptor;
import java.util.Locale;

class DummyInputMethodDescriptor implements InputMethodDescriptor {
  public InputMethod createInputMethod() throws Exception {
    return Empty.NULL_INPUT_METHOD;
  }

  public Locale[] getAvailableLocales() throws AWTException {
    return new Locale[0];
  }

  public String getInputMethodDisplayName(Locale locale, Locale locale1) {
    return locale.getVariant();
  }

  public Image getInputMethodIcon(Locale locale) {
    return Empty.NULL_IMAGE;
  }

  public boolean hasDynamicLocaleList() {
    return false;
  }
}