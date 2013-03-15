package org.uispec4j.interception.ui;

import javax.swing.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class UISpecLF {
  public static void init() {
    UIManager.put("ButtonUI", UISpecButtonUI.class.getName());
    UIManager.put("CheckBoxMenuItemUI", UISpecCheckBoxMenuItemUI.class.getName());
    UIManager.put("CheckBoxUI", UISpecCheckBoxUI.class.getName());
    UIManager.put("ColorChooserUI", UISpecColorChooserUI.class.getName());
    UIManager.put("ComboBoxUI", UISpecComboBoxUI.class.getName());
    UIManager.put("DesktopIconUI", UISpecDesktopIconUI.class.getName());
    UIManager.put("DesktopPaneUI", UISpecDesktopPaneUI.class.getName());
    UIManager.put("EditorPaneUI", UISpecEditorPaneUI.class.getName());
    UIManager.put("FileChooserUI", UISpecFileChooserUI.class.getName());
    UIManager.put("FormattedTextFieldUI", UISpecFormattedTextFieldUI.class.getName());
    UIManager.put("InternalFrameUI", UISpecInternalFrameUI.class.getName());
    UIManager.put("LabelUI", UISpecLabelUI.class.getName());
    UIManager.put("ListUI", UISpecListUI.class.getName());
    UIManager.put("MenuBarUI", UISpecMenuBarUI.class.getName());
    UIManager.put("MenuItemUI", UISpecMenuItemUI.class.getName());
    UIManager.put("MenuUI", UISpecMenuUI.class.getName());
    UIManager.put("OptionPaneUI", UISpecOptionPaneUI.class.getName());
    UIManager.put("PanelUI", UISpecPanelUI.class.getName());
    UIManager.put("PasswordFieldUI", UISpecPasswordFieldUI.class.getName());
    UIManager.put("PopupMenuSeparatorUI", UISpecPopupMenuSeparatorUI.class.getName());
    UIManager.put("PopupMenuUI", UISpecPopupMenuUI.class.getName());
    UIManager.put("ProgressBarUI", UISpecProgressBarUI.class.getName());
    UIManager.put("RadioButtonMenuItemUI", UISpecRadioButtonMenuItemUI.class.getName());
    UIManager.put("RadioButtonUI", UISpecRadioButtonUI.class.getName());
    UIManager.put("RootPaneUI", UISpecRootPaneUI.class.getName());
    UIManager.put("ScrollBarUI", UISpecScrollBarUI.class.getName());
    UIManager.put("ScrollPaneUI", UISpecScrollPaneUI.class.getName());
    UIManager.put("SeparatorUI", UISpecSeparatorUI.class.getName());
    UIManager.put("SliderUI", UISpecSliderUI.class.getName());
    UIManager.put("SpinnerUI", UISpecSpinnerUI.class.getName());
    UIManager.put("SplitPaneUI", UISpecSplitPaneUI.class.getName());
    UIManager.put("TableHeaderUI", UISpecTableHeaderUI.class.getName());
    UIManager.put("TableUI", UISpecTableUI.class.getName());
    UIManager.put("TextAreaUI", UISpecTextAreaUI.class.getName());
    UIManager.put("TextFieldUI", UISpecTextFieldUI.class.getName());
    UIManager.put("TextPaneUI", UISpecTextPaneUI.class.getName());
    UIManager.put("ToggleButtonUI", UISpecToggleButtonUI.class.getName());
    UIManager.put("ToolBarSeparatorUI", UISpecToolBarSeparatorUI.class.getName());
    UIManager.put("ToolBarUI", UISpecToolBarUI.class.getName());
    UIManager.put("ToolTipUI", UISpecToolTipUI.class.getName());
    UIManager.put("TreeUI", UISpecTreeUI.class.getName());
  }

  ///CLOVER:OFF
  public static void main(String[] args) {
    init();
    UIDefaults defaults = UIManager.getDefaults();
    Set set = new TreeSet();
    for (Enumeration dfkeys = defaults.keys(); dfkeys.hasMoreElements();) {
      String key = dfkeys.nextElement().toString();
      if (key.endsWith("UI")) {
        set.add(key);
      }
    }
    for (Iterator iterator = set.iterator(); iterator.hasNext();) {
      String key = (String)iterator.next();
      String compName = key.substring(0, key.length() - 2);
      System.out.println("    UIManager.put(\"" + key + "\", UISpec" + key + ".class.getName());");
    }
    System.out.println("\n");
  }

  private static void printUIManager() {
    UIDefaults defaults = UIManager.getDefaults(); // returns a HashTable
    Enumeration dfkeys = defaults.keys(); // returns an Enumeration

    System.out.println("KEY / VALUE list");
    while (dfkeys.hasMoreElements()) {
      String key = (String)dfkeys.nextElement();
      String line = key + " => " + defaults.get(key);
      if (line.indexOf("UI") >= 0) {
        System.out.println(line);
      }
    }
    System.out.println("\n");
  }
}
