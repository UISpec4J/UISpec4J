package org.uispec4j.utils;

import org.uispec4j.Key;
import org.uispec4j.UIComponent;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.KeyListener;

public class KeyUtils {


  public static void pressKey(UIComponent component, Key key) {
    pressKey(component.getAwtComponent(), key);
  }

  public static void pressKey(Component component, Key key) {
    dispatchEvent(KEY_PRESSED, key, key.getCode(), component);
    if (key.isPrintable() && key.getChar() != KeyEvent.CHAR_UNDEFINED) {
      dispatchEvent(KEY_TYPED, key, KeyEvent.VK_UNDEFINED, component);
    }
    if (component instanceof RootPaneContainer && key.equals(Key.ENTER)) {
      JButton defaultButton = ((RootPaneContainer)component).getRootPane().getDefaultButton();
      if (defaultButton != null) {
        defaultButton.doClick(20);
      }
    }

    if (JComponent.class.isInstance(component)) {
      KeyStroke keyStroke = KeyStroke.getKeyStroke(key.getCode(), key.getModifier().getCode());
      final ActionListener actionForKeyStroke = ((JComponent)component).getActionForKeyStroke(keyStroke);
      if (actionForKeyStroke != null) {
        actionForKeyStroke.actionPerformed(new ActionEvent(component, KeyEvent.KEY_PRESSED, ""));
      }
    }

    if (JTextComponent.class.isInstance(component) && key.isPrintable()) {
      insertKeyText(key, (JTextComponent)component);
    }
  }

  public static void releaseKey(Component component, Key key) {
    dispatchEvent(KEY_RELEASED, key, key.getCode(), component);
  }

  public static void enterKeys(Component component, Key... keys) {
    for (Key k : keys) {
      pressKey(component, k);
      releaseKey(component, k);
    }
  }

  private static void dispatchEvent(int eventCode, Key key, int keyCode, Component component) {
    int modifier = key.getModifier().getCode();
    KeyEvent event = new KeyEvent(component, eventCode, 0, modifier, keyCode, key.getChar());

    for (KeyListener listener : component.getKeyListeners()) {
      switch (eventCode) {
        case KEY_PRESSED:
          listener.keyPressed(event);
          break;
        case KEY_TYPED:
          listener.keyTyped(event);
          break;
        case KEY_RELEASED:
          listener.keyReleased(event);
          break;
      }
    }
  }

  private static void insertKeyText(Key key, JTextComponent jTextComponent) {
    if (jTextComponent.isEditable()) {
      Document document = jTextComponent.getDocument();
      try {
        int position = jTextComponent.getCaretPosition();
        document.insertString(position,
                              key.getChar().toString(),
                              document.getDefaultRootElement().getAttributes());
        jTextComponent.moveCaretPosition(document.getEndPosition().getOffset() - 1);
      }
      catch (BadLocationException e) {
        AssertAdapter.fail(e.getMessage());
      }
    }
  }
}
