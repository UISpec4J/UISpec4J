package org.uispec4j.finder;

import org.uispec4j.ComponentAmbiguityException;
import org.uispec4j.ItemNotFoundException;
import org.uispec4j.UIComponent;
import org.uispec4j.utils.UIComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic utility for retrieving AWT/Swing components in a container.
 */
public class ComponentFinder {
  private Container container;

  public ComponentFinder(Container container) {
    this.container = container;
  }

  public Component getComponent(ComponentMatcher matcher) throws ComponentAmbiguityException, ItemNotFoundException {
    return getComponent(new ComponentMatcher[]{matcher}, null, null, new Class[0]);
  }

  public Component getComponent(String name, Class[] swingClasses, String componentType) throws ComponentAmbiguityException, ItemNotFoundException {
    try {
      return getComponent(getMatchers(name), componentType, name, swingClasses);
    }
    catch (ItemNotFoundException e) {
      Component[] components = findComponents(ComponentMatcher.ALL, swingClasses);
      List<String> names = new ArrayList<String>();
      for (Component component : components) {
        UIComponent uiComponent = UIComponentFactory.createUIComponent(component);
        String componentLabel = uiComponent.getLabel();
        String componentName = uiComponent.getName();
        if (componentLabel != null) {
          names.add(componentLabel);
        }
        else if (componentName != null) {
          names.add(componentName);
        }
      }
      Collections.sort(names);
      String message =
        Messages.computeNotFoundMessage(componentType, name, names);
      throw new ItemNotFoundException(message);
    }
  }

  public Component findComponent(ComponentMatcher matcher) throws ComponentAmbiguityException {
    return findComponent(new ComponentMatcher[]{matcher}, null, null, new Class[0]);
  }

  public Component findComponent(String name, Class[] swingClasses, String componentType) throws ComponentAmbiguityException {
    return findComponent(getMatchers(name), componentType, name, swingClasses);
  }

  public Component[] getComponents(ComponentMatcher matcher) {
    return findComponents(matcher, new Class[0]);
  }

  public Component[] getComponents(String name, Class[] swingClasses) {
    return getComponents(getMatchers(name), swingClasses);
  }

  private Component findComponent(ComponentMatcher[] matchers, String type, String name, Class[] swingClasses) throws ComponentAmbiguityException {
    Component[] foundComponents = getComponents(matchers, swingClasses);
    if (foundComponents.length > 1) {
      throw new ComponentAmbiguityException(Messages.computeAmbiguityMessage(foundComponents, type, name));
    }
    return (foundComponents.length == 0) ? null : foundComponents[0];
  }

  private Component getComponent(ComponentMatcher[] matchers, String type, String name, Class[] swingClasses) throws ItemNotFoundException, ComponentAmbiguityException {
    Component foundComponent = findComponent(matchers, type, name, swingClasses);
    if (foundComponent == null) {
      throw new ItemNotFoundException(Messages.computeNotFoundMessage(type, name, null));
    }
    return foundComponent;
  }

  private Component[] getComponents(ComponentMatcher[] matchers, Class[] swingClasses) {
    for (ComponentMatcher matcher : matchers) {
      Component[] foundComponents = findComponents(matcher, swingClasses);
      if (foundComponents.length > 0) {
        return foundComponents;
      }
    }
    return new Component[0];
  }

  private Component[] findComponents(ComponentMatcher matcher, Class[] swingClasses) {
    List<Component> foundComponents = new ArrayList<Component>();
    retrieveComponents(container, foundComponents, matcher, swingClasses);
    return foundComponents.toArray(new Component[foundComponents.size()]);
  }

  private static void retrieveComponents(Container container,
                                         List<Component> components,
                                         ComponentMatcher matcher,
                                         Class[] swingClasses) {
    if (container instanceof JScrollPane) {
      JScrollPane scroll = (JScrollPane)container;
      retrieveComponents(scroll.getViewport(), components, matcher, swingClasses);
      return;
    }
    if (container == null) {
      return;
    }
    List<Container> containers = new ArrayList<Container>();
    boolean isCardLayout = (container.getLayout() instanceof CardLayout);
    for (int i = 0, max = container.getComponentCount(); i < max; i++) {
      Component component = container.getComponent(i);
      if (isCardLayout && !component.isVisible()) {
        continue;
      }
      if (isClassMatching(component, swingClasses) && matcher.matches(component)) {
        components.add(component);
      }
      if (component instanceof Container) {
        containers.add((Container)component);
      }
    }
    for (Container innerContainer : containers) {
      retrieveComponents(innerContainer, components, matcher, swingClasses);
    }
  }

  private static boolean isClassMatching(Component component, Class[] swingClasses) {
    if (swingClasses.length == 0) {
      return true;
    }
    for (Class expectedClass : swingClasses) {
      if (expectedClass.isInstance(component)) {
        return true;
      }
    }
    return false;
  }

  private static ComponentMatcher[] getMatchers(String name) {
    if (name == null) {
      return new ComponentMatcher[]{ComponentMatcher.ALL};
    }
    else {
      return getPredefinedNameMatchers(name);
    }
  }

  private static ComponentMatcher[] getPredefinedNameMatchers(String name) {
    return new ComponentMatcher[]{
      ComponentMatchers.displayedNameIdentity(name),
      ComponentMatchers.displayedNameSubstring(name),
      ComponentMatchers.componentLabelFor(name),
      ComponentMatchers.innerNameIdentity(name),
      ComponentMatchers.innerNameSubstring(name)
    };
  }
}
