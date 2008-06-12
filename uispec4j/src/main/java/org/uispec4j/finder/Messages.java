package org.uispec4j.finder;

import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.utils.ComponentUtils;

import java.awt.*;
import java.util.List;

class Messages {
  public static String computeAmbiguityMessage(Component[] matchingComponents, String type, String name) {
    String[] componentNames = new String[matchingComponents.length];
    for (int i = 0; i < matchingComponents.length; i++) {
      componentNames[i] = ComponentUtils.extractName(matchingComponents[i]);
    }
    return computeAmbiguityMessage(componentNames, type, name);
  }

  public static String computeAmbiguityMessage(String[] componentNames, String type, String name) {
    StringBuffer message = new StringBuffer("Several components");
    if (type != null) {
      message.append(" of type '").append(type).append("'");
    }
    if (name != null) {
      message.append(" match the pattern '").append(name).append("'");
    }
    else {
      message.append(" have been found");
    }
    message.append(" in this panel: [");
    for (int i = 0; i < componentNames.length; i++) {
      message.append(componentNames[i]);
      message.append((i < componentNames.length - 1) ? ',' : ']');
    }
    return message.toString();
  }

  public static String computeNotFoundMessage(String type, String name, List availableNames) {
    if (name != null) {
      return "Component '" + name + "' of type '" + type + "' not found" + getPostfix(availableNames);
    }
    if (type != null) {
      return "No component of type '" + type + "' found" + getPostfix(availableNames);
    }
    return "No component found";
  }

  private static String getPostfix(List availableNames) {
    if ((availableNames == null) || availableNames.isEmpty()) {
      return "";
    }
    return " - available names: " + ArrayUtils.toString(availableNames);
  }
}
