package org.uispec4j.interception.toolkit.empty;

import java.awt.AWTEvent;
import java.awt.Rectangle;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodContext;
import java.util.Locale;

class DummyInputMethod implements InputMethod {

  public void setInputMethodContext(InputMethodContext inputMethodContext) {
  }

  public boolean setLocale(Locale locale) {
    return false;
  }

  public Locale getLocale() {
    return Locale.getDefault();
  }

  public void setCharacterSubsets(Character.Subset[] subsets) {
  }

  public void setCompositionEnabled(boolean b) {
  }

  public boolean isCompositionEnabled() {
    return false;
  }

  public void reconvert() {
  }

  public void dispatchEvent(AWTEvent awtEvent) {
  }

  public void notifyClientWindowChange(Rectangle rectangle) {
  }

  public void activate() {
  }

  public void deactivate(boolean b) {
  }

  public void hideWindows() {
  }

  public void removeNotify() {
  }

  public void endComposition() {
  }

  public void dispose() {
  }

  public Object getControlObject() {
    return null;
  }

}