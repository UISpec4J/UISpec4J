package org.uispec4j.utils;

import org.uispec4j.Key;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyUtils {
  public static void pressKey(Component component, Key key) {
    int keyCode = key.getCode();
    int modifier = key.getModifier().getCode();
    if (component.getKeyListeners().length > 0) {
      KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, modifier, keyCode, (char)keyCode);
      for (int i = 0; i < component.getKeyListeners().length; i++) {
        KeyListener keyListener = component.getKeyListeners()[i];
        keyListener.keyPressed(event);
      }
    }

    if (JComponent.class.isInstance(component)) {
      KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifier);
      final ActionListener actionForKeyStroke = ((JComponent)component).getActionForKeyStroke(keyStroke);
      if (actionForKeyStroke != null) {
        actionForKeyStroke.actionPerformed(new ActionEvent(component, KeyEvent.KEY_PRESSED, ""));
      }
    }
  }
}
