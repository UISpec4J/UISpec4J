package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.finder.ComponentFinder;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.utils.UIComponentAnalyzer;
import org.uispec4j.utils.UIComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.uispec4j.finder.ComponentMatchers.*;

/**
 * General container for UI components.<p>
 * This class offers a set of "getXxx" methods for retrieving the different kinds of UIComponent
 * instances laid out in a GUI panel.<p>
 * It also provides a set of generic find/get methods, with the following naming logic:
 * <ul>
 * <li>'find...' stands for a unitary search that returns null when nothing was found</li>
 * <li>'getXxxComponent' stands for a unitary search that throws an exception
 * when nothing was found</li>
 * <li>'getXxxComponent<em>s</em>' stands for plural search and returns an empty array
 * when nothing was found</li>
 * <li>'getXxxComponent<em>s</em>' stands for plural search and returns an empty array
 * when nothing was found</li>
 * <li>'containsXxxComponent<em>s</em>' returns an assertion for checking the presence
 * of a component</li>
 * </ul>
 * NOTE: A Panel can be created from any AWT Container, but when a Panel is searched with the
 * {@link #getPanel(String)} method only components of type JPanel JInternalFrame, etc. will be
 * considered.
 *
 * @see <a href="http://www.uispec4j.org/findingcomponents">Finding Components</a>
 */
public class Panel extends AbstractUIComponent {
  public static final String TYPE_NAME = "panel";
  public static final Class[] SWING_CLASSES = {JPanel.class, JInternalFrame.class,
                                               JViewport.class, JScrollPane.class,
                                               JRootPane.class};

  private Container container;
  private ComponentFinder finder;

  public Panel(Container container) {
    this.container = container;
    this.finder = new ComponentFinder(container);
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public Container getAwtComponent() {
    return container;
  }

  public Container getAwtContainer() {
    return container;
  }

  public Button getButton(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Button.class, name);
  }

  public Button getButton() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Button.class, null);
  }

  public Button getButton(final ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (Button)getComponent(finder, getMatcherByClass(Button.class, matcher));
  }

  public ToggleButton getToggleButton(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, ToggleButton.class, name);
  }

  public ToggleButton getToggleButton() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, ToggleButton.class, null);
  }

  public ToggleButton getToggleButton(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (ToggleButton)getComponent(finder, getMatcherByClass(ToggleButton.class, matcher));
  }

  public CheckBox getCheckBox(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, CheckBox.class, name);
  }

  public CheckBox getCheckBox() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, CheckBox.class, null);
  }

  public CheckBox getCheckBox(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (CheckBox)getComponent(finder, getMatcherByClass(CheckBox.class, matcher));
  }

  public Panel getPanel() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Panel.class, null);
  }

  public Panel getPanel(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Panel.class, name);
  }

  public Panel getPanel(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (Panel)getComponent(finder, getMatcherByClass(Panel.class, matcher));
  }

  public ProgressBar getProgressBar(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, ProgressBar.class, name);
  }

  public ProgressBar getProgressBar() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, ProgressBar.class, null);
  }

  public ProgressBar getProgressBar(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (ProgressBar)getComponent(finder, getMatcherByClass(ProgressBar.class, matcher));
  }

  public Desktop getDesktop(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Desktop.class, name);
  }

  public Desktop getDesktop() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Desktop.class, null);
  }

  public Desktop getDesktop(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (Desktop)getComponent(finder, getMatcherByClass(Desktop.class, matcher));
  }

  public TextBox getTextBox(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    Class[] swingClasses = UIComponentAnalyzer.getSwingClasses(TextBox.class);
    String typeName = UIComponentAnalyzer.getTypeName(TextBox.class);
    Component swingComponent = finder.getComponent(name, swingClasses, typeName);
    return (TextBox)UIComponentFactory.createUIComponent(swingComponent);
  }

  public TextBox getTextBox() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, TextBox.class, null);
  }

  public TextBox getTextBox(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (TextBox)getComponent(finder, getMatcherByClass(TextBox.class, matcher));
  }

  /**
   * Retrieves input-only text boxes. This is useful for avoiding ambiguity exceptions
   * when the input text boxes are laid out near labels, as in most forms.<p>
   * "Input text boxes" are defined as subclasses of the JTextComponent class - in other words,
   * JLabel components are excluded from the search. Please note that the is is not necessarily
   * visible from the user, since JTextComponent subclasses can be customized to look as ordinary,
   * read-only labels.
   */
  public TextBox getInputTextBox(String name) throws ComponentAmbiguityException, ItemNotFoundException {
    java.util.List<Class> inputComponentClasses = new ArrayList<Class>();
    Class[] swingClasses = TextBox.SWING_CLASSES;
    for (Class swingClass : swingClasses) {
      if (!swingClass.equals(JLabel.class)) {
        inputComponentClasses.add(swingClass);
      }
    }
    Class[] inputClassesArray =
      inputComponentClasses.toArray(new Class[inputComponentClasses.size()]);
    return getComponent(finder, TextBox.class, inputClassesArray, name);
  }

  /**
   * Retrieves input-only text boxes.
   *
   * @see #getInputTextBox(String)
   */
  public TextBox getInputTextBox() throws ComponentAmbiguityException, ItemNotFoundException {
    return getInputTextBox(null);
  }

  public TabGroup getTabGroup(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, TabGroup.class, name);
  }

  public TabGroup getTabGroup() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, TabGroup.class, null);
  }

  public TabGroup getTabGroup(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (TabGroup)getComponent(finder, getMatcherByClass(TabGroup.class, matcher));
  }

  public ComboBox getComboBox(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, ComboBox.class, name);
  }

  public ComboBox getComboBox() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, ComboBox.class, null);
  }

  public ComboBox getComboBox(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (ComboBox)getComponent(finder, getMatcherByClass(ComboBox.class, matcher));
  }

  public Spinner getSpinner() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Spinner.class, null);
  }

  public Spinner getSpinner(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Spinner.class, name);
  }

  public Spinner getSpinner(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (Spinner)getComponent(finder, getMatcherByClass(Spinner.class, matcher));
  }

  public DateSpinner getDateSpinner() throws ItemNotFoundException, ComponentAmbiguityException {
    Spinner component = (Spinner)getComponent(finder, Spinner.getSpinnerMatcherByModel(SpinnerDateModel.class));
    return new DateSpinner(component.getAwtComponent());
  }

  public DateSpinner getDateSpinner(String componentName) {
    ComponentMatcher matcher = and(Spinner.getSpinnerMatcherByModel(SpinnerDateModel.class),
                                   getMatcherFromName(componentName));
    Spinner component = (Spinner)getComponent(finder, matcher);
    return new DateSpinner(component.getAwtComponent());
  }

  public DateSpinner getDateSpinner(ComponentMatcher matcher) {
    ComponentMatcher intersection = and(Spinner.getSpinnerMatcherByModel(SpinnerDateModel.class), matcher);
    Spinner component = (Spinner)getComponent(finder, intersection);
    return new DateSpinner(component.getAwtComponent());
  }

  public ListSpinner getListSpinner() throws ItemNotFoundException, ComponentAmbiguityException {
    Spinner component = (Spinner)getComponent(finder, Spinner.getSpinnerMatcherByModel(SpinnerListModel.class));
    return new ListSpinner(component.getAwtComponent());
  }

  public ListSpinner getListSpinner(String componentName) {
    ComponentMatcher matcher = and(Spinner.getSpinnerMatcherByModel(SpinnerListModel.class), getMatcherFromName(componentName));
    Spinner component = (Spinner)getComponent(finder, matcher);
    return new ListSpinner(component.getAwtComponent());
  }

  public ListSpinner getListSpinner(ComponentMatcher matcher) {
    ComponentMatcher intersection = and(Spinner.getSpinnerMatcherByModel(SpinnerListModel.class), matcher);
    Spinner component = (Spinner)getComponent(finder, intersection);
    return new ListSpinner(component.getAwtComponent());
  }

  public NumberSpinner getNumberSpinner() throws ItemNotFoundException, ComponentAmbiguityException {
    Spinner component = (Spinner)getComponent(finder, Spinner.getSpinnerMatcherByModel(SpinnerNumberModel.class));
    return new NumberSpinner(component.getAwtComponent());
  }

  public NumberSpinner getNumberSpinner(String componentName) {
    ComponentMatcher matcher =
      and(Spinner.getSpinnerMatcherByModel(SpinnerNumberModel.class),
          getMatcherFromName(componentName));
    Spinner component = (Spinner)getComponent(finder, matcher);
    return new NumberSpinner(component.getAwtComponent());
  }

  public NumberSpinner getNumberSpinner(ComponentMatcher matcher) {
    ComponentMatcher intersection = and(Spinner.getSpinnerMatcherByModel(SpinnerNumberModel.class), matcher);
    Spinner component = (Spinner)getComponent(finder, intersection);
    return new NumberSpinner(component.getAwtComponent());
  }

  public Slider getSlider() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Slider.class, null);
  }

  public Slider getSlider(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Slider.class, name);
  }

  public Slider getSlider(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (Slider)getComponent(finder, getMatcherByClass(Slider.class, matcher));
  }

  public Table getTable(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Table.class, name);
  }

  public Table getTable() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Table.class, null);
  }

  public Table getTable(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (Table)getComponent(finder, getMatcherByClass(Table.class, matcher));
  }

  public Tree getTree(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Tree.class, name);
  }

  public Tree getTree() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, Tree.class, null);
  }

  public Tree getTree(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (Tree)getComponent(finder, getMatcherByClass(Tree.class, matcher));
  }

  public RadioButton getRadioButton(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, RadioButton.class, name);
  }

  public RadioButton getRadioButton() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, RadioButton.class, null);
  }

  public RadioButton getRadioButton(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (RadioButton)getComponent(finder, getMatcherByClass(RadioButton.class, matcher));
  }

  public ListBox getListBox(String name) throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, ListBox.class, name);
  }

  public ListBox getListBox() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, ListBox.class, null);
  }

  public ListBox getListBox(ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return (ListBox)getComponent(finder, getMatcherByClass(ListBox.class, matcher));
  }

  public PasswordField getPasswordField() throws ItemNotFoundException, ComponentAmbiguityException {
    return getComponent(finder, PasswordField.class, null);
  }

  public PasswordField getPasswordField(ComponentMatcher matcher) {
    return (PasswordField)getComponent(finder, getMatcherByClass(PasswordField.class, matcher));
  }

  public PasswordField getPasswordField(String componentName) {
    return getComponent(finder, PasswordField.class, componentName);
  }

  public UIComponent[] getUIComponents(Class uiComponentClass) {
    return getComponents(finder, uiComponentClass, null);
  }

  public UIComponent[] getUIComponents(Class uiComponentClass, String name) {
    return getComponents(finder, uiComponentClass, name);
  }

  public UIComponent[] getUIComponents(ComponentMatcher matcher) {
    return getComponents(finder, matcher);
  }

  public <T extends UIComponent> T findUIComponent(Class<T> uiComponentClass) throws ComponentAmbiguityException {
    return findComponent(finder, uiComponentClass, null);
  }

  public <T extends UIComponent> T findUIComponent(Class<T> uiComponentClass, String name) throws ComponentAmbiguityException {
    return findComponent(finder, uiComponentClass, name);
  }

  public UIComponent findUIComponent(ComponentMatcher matcher) throws ComponentAmbiguityException {
    Component swingComponent = finder.findComponent(matcher);
    if (swingComponent == null) {
      return null;
    }
    return UIComponentFactory.createUIComponent(swingComponent);
  }

  public Component[] getSwingComponents(Class swingComponentClass) {
    return finder.getComponents(null, new Class[]{swingComponentClass});
  }

  public Component[] getSwingComponents(Class swingComponentClass, String name) {
    return finder.getComponents(name, new Class[]{swingComponentClass});
  }

  public Component[] getSwingComponents(ComponentMatcher matcher) {
    return finder.getComponents(matcher);
  }

  public <T extends Component> T findSwingComponent(Class<T> swingComponentClass) throws ComponentAmbiguityException {
    return (T)finder.findComponent(null, new Class[]{swingComponentClass}, swingComponentClass.getName());
  }

  public <T extends Component> T findSwingComponent(Class<T> swingComponentClass, String componentName) throws ComponentAmbiguityException {
    return (T)finder.findComponent(componentName, new Class[]{swingComponentClass}, swingComponentClass.getName());
  }

  public Component findSwingComponent(ComponentMatcher matcher) throws ComponentAmbiguityException {
    return finder.findComponent(matcher);
  }

  private <T extends UIComponent> ComponentMatcher getMatcherByClass(final Class<T> uiComponentClass, final ComponentMatcher matcher) {
    final Class[] swingClasses = UIComponentAnalyzer.getSwingClasses(uiComponentClass);
    ComponentMatcher[] classMatchers = new ComponentMatcher[swingClasses.length];
    for (int i = 0; i < classMatchers.length; i++) {
      classMatchers[i] = fromClass(swingClasses[i]);
    }
    return and(matcher, or(classMatchers));
  }

  private static UIComponent getComponent(ComponentFinder finder, ComponentMatcher matcher) throws ItemNotFoundException, ComponentAmbiguityException {
    return UIComponentFactory.createUIComponent(finder.getComponent(matcher));
  }

  private static <T extends UIComponent> T getComponent(ComponentFinder finder, Class<T> uiComponentClass, String componentName)
    throws ComponentAmbiguityException, ItemNotFoundException {
    Class[] swingClasses = UIComponentAnalyzer.getSwingClasses(uiComponentClass);
    return getComponent(finder, uiComponentClass, swingClasses, componentName);
  }

  private static <T extends UIComponent> T getComponent(ComponentFinder finder, Class<T> uiComponentClass, Class[] swingClasses, String componentName)
    throws ComponentAmbiguityException, ItemNotFoundException {
    UIComponentFactory.register(uiComponentClass);
    String typeName = UIComponentAnalyzer.getTypeName(uiComponentClass);
    Component swingComponent = finder.getComponent(componentName, swingClasses, typeName);
    return (T)UIComponentFactory.createUIComponent(swingComponent);
  }

  private static <T extends UIComponent> T findComponent(ComponentFinder finder,
                                                         Class<T> uiComponentClass, String name)
    throws ComponentAmbiguityException {
    UIComponentFactory.register(uiComponentClass);
    Class[] swingClasses = UIComponentAnalyzer.getSwingClasses(uiComponentClass);
    String typeName = UIComponentAnalyzer.getTypeName(uiComponentClass);
    Component swingComponent = finder.findComponent(name, swingClasses, typeName);
    return (swingComponent == null) ? null : (T)UIComponentFactory.createUIComponent(swingComponent);
  }

  private static UIComponent[] getComponents(ComponentFinder finder, ComponentMatcher matcher) {
    Component[] components = finder.getComponents(matcher);
    return UIComponentFactory.createUIComponents(components);
  }

  private static UIComponent[] getComponents(ComponentFinder finder, Class uiComponentClass, String name) {
    UIComponentFactory.register(uiComponentClass);
    Class[] swingClasses = UIComponentAnalyzer.getSwingClasses(uiComponentClass);
    Component[] swingComponents = finder.getComponents(name, swingClasses);
    return UIComponentFactory.createUIComponents(swingComponents);
  }

  public <T extends UIComponent> Assertion containsUIComponent(final Class<T> uicomponentClass) {
    return new Assertion() {
      public void check() {
        UIComponent[] uiComponents = getUIComponents(uicomponentClass);
        AssertAdapter.assertTrue(uiComponents.length > 0);
      }
    };
  }

  public <T extends Component> Assertion containsSwingComponent(final Class<T> swingComponentClass) {
    return new Assertion() {
      public void check() {
        Component[] swingComponents = getSwingComponents(swingComponentClass);
        AssertAdapter.assertTrue(swingComponents.length > 0);
      }
    };
  }

  public <T extends UIComponent> Assertion containsUIComponent(final Class<T> uiComponentClass, final String name) {
    return new Assertion() {
      public void check() {
        UIComponent[] uiComponents = getUIComponents(uiComponentClass, name);
        AssertAdapter.assertTrue(uiComponents.length > 0);
      }
    };
  }

  public <T extends Component> Assertion containsSwingComponent(final Class<T> swingComponentClass, final String name) {
    return new Assertion() {
      public void check() {
        Component[] swingComponents = getSwingComponents(swingComponentClass, name);
        AssertAdapter.assertTrue(swingComponents.length > 0);
      }
    };
  }

  public Assertion containsComponent(final ComponentMatcher matcher) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue(getSwingComponents(matcher).length > 0);
      }
    };
  }

  /**
   * Checks that the panel contains a given non-editable text.
   * This method is mainly suited for checking displayed messages in popped-up dialogs.
   * NB: Only JLabel components are taken into account.
   */
  public Assertion containsLabel(final String text) {
    return new Assertion() {
      public void check() {
        Component[] result = getSwingComponents(and(fromClass(JLabel.class), displayedNameSubstring(text)));
        if (result.length == 0) {
          AssertAdapter.fail("No label found with text '" + text + "'");
        }
      }
    };
  }

  private static ComponentMatcher getMatcherFromName(String componentName) {
    return or(innerNameIdentity(componentName),
              innerNameSubstring(componentName),
              innerNameRegexp(componentName));
  }
}
