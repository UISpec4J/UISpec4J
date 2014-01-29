package org.uispec4j.finder;

import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.*;
import org.uispec4j.Panel;
import org.uispec4j.extension.CustomCountingButton;
import org.uispec4j.extension.JCountingButton;
import org.uispec4j.utils.ComponentUtils;
import org.uispec4j.utils.UIComponentAnalyzer;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class PanelComponentSearchTest extends PanelComponentFinderTestCase {
  private static final Class[] COMPONENT_CLASSES = new Class[]{
      Button.class,
      CheckBox.class,
      ComboBox.class,
      org.uispec4j.Desktop.class,
      ListBox.class,
      TabGroup.class,
      TextBox.class,
      PasswordField.class,
      Panel.class,
      Spinner.class,
      Slider.class,
      ProgressBar.class,
      RadioButton.class};
  private Map componentAccessors;

  public void setUp() throws Exception {
    super.setUp();
    componentAccessors = createAccessors(panel);
  }

  @Test
  public void testGetCustomComponent() throws Exception {
    String name = "hello world";
    Component component = createComponentWithText(JCountingButton.class, name);
    jPanel.add(component);

    assertTrue(panel.findUIComponent(CustomCountingButton.class, "hello") instanceof CustomCountingButton);
  }

  @Test
  public void testGetComponentTypeName() {
    assertEquals("panel", UIComponentFactory.createUIComponent(new JPanel()).getDescriptionTypeName());
  }

  @Test
  public void testGetComponentWithText() throws Exception {
    checkGetComponentWithText(JButton.class, Button.TYPE_NAME);
    checkGetComponentWithText(JCheckBox.class, CheckBox.TYPE_NAME);
    checkGetComponentWithText(JLabel.class, TextBox.TYPE_NAME);
    checkGetComponentWithText(JRadioButton.class, RadioButton.TYPE_NAME);
  }

  @Test
  public void testGetComponentWithClassAndName() throws Exception {
    for (Class componentClass : COMPONENT_CLASSES) {
      checkGetComponentWithClassAndName(componentClass);
    }
  }

  @Test
  public void testGetComponentWithClass() throws Exception {
    for (Class componentClass : COMPONENT_CLASSES) {
      if (componentClass != Panel.class) {
        checkGetComponentWithClass(componentClass);
      }
    }
  }

  @Test
  public void testGetComponentWithCustomMatcher() throws Exception {
    for (Class componentClass : COMPONENT_CLASSES) {
      if (componentClass != Panel.class) {
        checkGetComponentWithCustomMatcher(componentClass);
      }
    }
  }

  @Test
  public void testComponentNotFoundErrors() throws Exception {
    for (Class componentClass : COMPONENT_CLASSES) {
      checkComponentNotFound(UIComponentAnalyzer.getTypeName(componentClass));
    }
  }

  @Test
  public void testComponentTypeMismatch() throws Exception {
    checkComponentTypeMismatch("name1", JList.class, Button.TYPE_NAME);
    checkComponentTypeMismatch("name2", JList.class, TextBox.TYPE_NAME);
    checkComponentTypeMismatch("name3", JList.class, CheckBox.TYPE_NAME);
    checkComponentTypeMismatch("name5", JList.class, TextBox.TYPE_NAME);
    checkComponentTypeMismatch("name6", JList.class, TabGroup.TYPE_NAME);
    checkComponentTypeMismatch("name7", JList.class, ComboBox.TYPE_NAME);
    checkComponentTypeMismatch("name8", JList.class, RadioButton.TYPE_NAME);
  }

  @Test
  public void testPasswordField() throws Exception {
    JPasswordField jPasswordField = new JPasswordField();
    jPanel.add(jPasswordField);
    PasswordField passwordField = panel.getPasswordField();
    assertSame(jPasswordField, passwordField.getAwtComponent());
  }

  @Test
  public void testSearchTraversalByClassExploresInDepthAllSubComponents() throws Exception {
    JTextField innerTextField = new JTextField("hello");
    JPanel innerPanel = new JPanel();
    innerPanel.add(innerTextField);
    jPanel.add(innerPanel);

    JTextField textField = new JTextField("world");
    jPanel.add(textField);

    TypedComponentAccessor labelAccessor = getAccessor(TextBox.TYPE_NAME);
    try {
      labelAccessor.getComponent();
      fail();
    }
    catch (Exception e) {
      assertEquals(Messages.computeAmbiguityMessage(new String[]{"world", "hello"}, TextBox.TYPE_NAME, null),
                   e.getMessage());
    }

    TestUtils.assertUIComponentRefersTo(textField, labelAccessor.getComponent("world"));
    TestUtils.assertUIComponentRefersTo(innerTextField, labelAccessor.getComponent("hello"));
  }

  @Test
  public void testSearchTraversalByNameExploresInDepthAllSubComponents() throws Exception {
    JLabel innerLabel = new JLabel();
    innerLabel.setName("Hello Marc");
    JPanel innerPanel = new JPanel();
    innerPanel.add(innerLabel);
    jPanel.add(innerPanel);

    JLabel label = new JLabel();
    addComponentToPanelWithName(label, "Hello Regis");

    TypedComponentAccessor labelAccessor = getAccessor(TextBox.TYPE_NAME);
    try {
      labelAccessor.getComponent("hello");
      fail();
    }
    catch (Exception e) {
      assertEquals(Messages.computeAmbiguityMessage(new String[]{"Hello Regis", "Hello Marc"}, TextBox.TYPE_NAME, "hello"),
                   e.getMessage());
    }
    TestUtils.assertUIComponentRefersTo(innerLabel, labelAccessor.getComponent("Hello marc"));
    TestUtils.assertUIComponentRefersTo(label, labelAccessor.getComponent("Hello regis"));
  }

  @Test
  public void testSearchByNameStrategyIsFirstWithDisplayedNameThenLabelForAndFinallyWithInnerName() throws Exception {
    TypedComponentAccessor checkBoxAccessor = getAccessor(CheckBox.TYPE_NAME);
    try {
      checkBoxAccessor.getComponent("toto");
      fail("Should not have found ay item");
    }
    catch (ItemNotFoundException e) {
    }

    JCheckBox checkBoxWithInnerName = new JCheckBox("do not chose me either");
    checkBoxWithInnerName.setName("toto");
    jPanel.add(checkBoxWithInnerName);
    TestUtils.assertUIComponentRefersTo(checkBoxWithInnerName, checkBoxAccessor.getComponent("toto"));

    JCheckBox checkBoxWithReferencingLabel = new JCheckBox("do not choose me");
    JLabel label = new JLabel("toto");
    label.setLabelFor(checkBoxWithReferencingLabel);
    jPanel.add(checkBoxWithReferencingLabel);
    TestUtils.assertUIComponentRefersTo(checkBoxWithReferencingLabel, checkBoxAccessor.getComponent("toto"));

    JCheckBox checkBox = new JCheckBox("label toto");
    jPanel.add(checkBox);

    TestUtils.assertUIComponentRefersTo(checkBox, checkBoxAccessor.getComponent("toto"));
  }

  @Test
  public void testSearchWithinAComplexPanel() throws Exception {
    JPanel main = new JPanel();
    for (int i = 0; i < 10; i++) {
      JPanel containedPanel = new JPanel();
      containedPanel.add(new JButton("button" + i));
      addWithScrollPane(containedPanel, main);

      JPanel topPanel = new JPanel();
      JPanel bottomPanel = new JPanel();
      JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
      JTable table = new JTable();
      table.setName("table" + i);
      addWithScrollPane(table, topPanel);
      JTree tree = new JTree();
      tree.setName("tree" + i);
      addWithScrollPane(tree, topPanel);
      bottomPanel.add(new JLabel("label" + i));
      addWithScrollPane(split, main);

      for (int j = 0; j < 5; j++) {
        JPanel sub = new JPanel();
        sub.add(new JCheckBox("checkBox" + i + "." + j));
        addWithScrollPane(sub, bottomPanel);

        JPanel subSub = new JPanel();
        subSub.add(new JRadioButton("radio" + i + "." + j));
        addWithScrollPane(subSub, sub);
      }
    }

    Panel mainPanel = new Panel(main);
    assertNotNull(mainPanel.getTextBox("label5"));
    assertNotNull(mainPanel.getButton("button7"));
    assertNotNull(mainPanel.getTable("table6"));
    assertNotNull(mainPanel.getTree("tree5"));
    assertNotNull(mainPanel.getCheckBox("checkBox3.4"));
    assertNotNull(mainPanel.getRadioButton("radio2.3"));
  }

  @Test
  public void testComponentNameAmbiguityException() throws Exception {
    jPanel.add(new JButton("myButton1"));
    jPanel.add(new JButton("myButton2"));
    checkComponentNameAmbiguity("button", Button.TYPE_NAME, new String[]{"myButton1", "myButton2"});

    jPanel.add(new JLabel("myLabel1"));
    jPanel.add(new JLabel("myLabel2"));
    checkComponentNameAmbiguity("label", TextBox.TYPE_NAME, new String[]{"myLabel1", "myLabel2"});

    jPanel.add(new JCheckBox("myCheckBox1"));
    jPanel.add(new JCheckBox("myCheckBox2"));
    checkComponentNameAmbiguity("myCheckBox", CheckBox.TYPE_NAME, new String[]{"myCheckBox1", "myCheckBox2"});

    createTwoComponentsWithSameName(JPanel.class, "myPanel");
    checkComponentNameAmbiguity("panel", Panel.TYPE_NAME, new String[]{"myPanel", "myPanel"});

    createTwoComponentsWithSameName(JTextField.class, "myText");
    checkComponentNameAmbiguity("text", TextBox.TYPE_NAME, new String[]{"myText", "myText"});

    createTwoComponentsWithSameName(JTabbedPane.class, "myTabbed");
    checkComponentNameAmbiguity("myTabbed", TabGroup.TYPE_NAME, new String[]{"myTabbed", "myTabbed"});

    createTwoComponentsWithSameName(JComboBox.class, "myCombo");
    checkComponentNameAmbiguity("myCombo", ComboBox.TYPE_NAME, new String[]{"myCombo", "myCombo"});

    addComponentWithLabel(JCheckBox.class, "this is my checkbox");
    addComponentWithLabel(JCheckBox.class, "this is also my checkbox");
    checkComponentNameAmbiguity("my checkbox", CheckBox.TYPE_NAME, new String[]{"this is my checkbox", "this is also my checkbox"});
  }

  @Test
  public void testAmbiguityExceptionIsDetectedBetweenLabelAndTextComponents() throws Exception {
    JLabel jLabel = new JLabel("myText1");
    jPanel.add(jLabel);
    JTextField jTextField = new JTextField("myText2");
    jTextField.setText("myText2");
    jPanel.add(jTextField);
    checkComponentNameAmbiguity("text", TextBox.TYPE_NAME, new String[]{"myText1", "myText2"});

    panel = new Panel(jPanel);
    try {
      panel.getTextBox();
      fail();
    }
    catch (ComponentAmbiguityException e) {
      assertEquals(Messages.computeAmbiguityMessage(new String[]{"myText1", "myText2"}, TextBox.TYPE_NAME, null),
                   e.getMessage());
    }
  }

  @Test
  public void testComponentsWithSameNameAreFoundAccordingToTheirType() throws Exception {
    JLabel label = new JLabel("test");
    JTable table = new JTable();
    table.setName("test");

    jPanel.add(label);
    jPanel.add(table);

    TestUtils.assertUIComponentRefersTo(label, panel.getTextBox("test"));
    TestUtils.assertUIComponentRefersTo(table, panel.getTable("test"));
  }

  @Test
  public void testComponentsWithSamePatternInNameAreFoundAccordingToTheirType() throws Exception {
    JLabel label = new JLabel("another label");
    JTable table = new JTable();
    table.setName("another table");

    jPanel.add(label);
    jPanel.add(table);

    TestUtils.assertUIComponentRefersTo(label, panel.getTextBox("another"));
    TestUtils.assertUIComponentRefersTo(table, panel.getTable("another"));
  }

  @Test
  public void testComponentThatShouldBeUniqueInPanel() throws Exception {
    checkComponentUnicityAmbiguity(JTable.class, Table.TYPE_NAME, "myTable");
    checkComponentUnicityAmbiguity(JList.class, ListBox.TYPE_NAME, "myList");
    checkComponentUnicityAmbiguity(JDesktopPane.class, org.uispec4j.Desktop.TYPE_NAME, "myDesktop");
    checkComponentUnicityAmbiguity(JTextField.class, TextBox.TYPE_NAME, "myText");
    checkComponentUnicityAmbiguity(JTree.class, Tree.TYPE_NAME, "myTree");
  }

  @Test
  public void testComponentsAreFoundDeepInTheComponentsHierarchy() throws Exception {
    JPanel newPanel = new JPanel();
    JButton button = new JButton("button");
    newPanel.add(button);
    jPanel.add(newPanel);
    TestUtils.assertUIComponentRefersTo(button, panel.getButton("button"));
  }

  @Test
  public void testContainmentSkipsScrollpaneButtons() throws Exception {
    JPanel rootPanel = new JPanel();
    rootPanel.setName("rootPanel");
    JPanel innerUnnamedPanel = new JPanel();
    rootPanel.add(innerUnnamedPanel);
    JScrollPane scroll = new JScrollPane();
    innerUnnamedPanel.add(scroll);
    XmlAssert.assertEquivalent("<panel name='rootPanel'/>",
                               new Panel(rootPanel).getDescription());
  }

  @Test
  public void testContainmentSkipsUnnamedContainers() throws Exception {
    JPanel rootPanel = new JPanel();
    rootPanel.setName("rootPanel");
    JPanel innerUnnamedPanel = new JPanel();
    rootPanel.add(innerUnnamedPanel);
    JScrollPane scroll = new JScrollPane();
    innerUnnamedPanel.add(scroll);
    JButton button = new JButton("ok");
    JPanel viewportPanel = new JPanel();
    viewportPanel.add(button);
    scroll.getViewport().add(viewportPanel);
    XmlAssert.assertEquivalent("<panel name='rootPanel'>" +
                               "  <button label='ok'/>" +
                               "</panel>",
                               new Panel(rootPanel).getDescription());
  }

  @Test
  public void testSelectedPanelIsTheOneVisibleInCardLayout() throws Exception {
    JPanel cardPanel = new JPanel();
    JPanel firstPanel = new JPanel();
    firstPanel.add(new JList());
    JButton firstButton = new JButton("myButton");
    firstPanel.add(firstButton);

    JPanel secondPanel = new JPanel();
    JButton secondButton = new JButton("myButton");
    secondPanel.add(secondButton);

    CardLayout cardLayout = new CardLayout();
    cardPanel.setLayout(cardLayout);
    cardPanel.add(firstPanel, "first");
    cardPanel.add(secondPanel, "second");
    addComponentToPanelWithName(cardPanel, "panelWithCardLayout");

    Panel panelWithCardLayoutChecker = panel.getPanel("panelWithCardLayout");

    cardLayout.show(cardPanel, "first");
    ListBox list1Checker = panelWithCardLayoutChecker.getListBox();
    assertNotNull(list1Checker);
    assertNotNull(panelWithCardLayoutChecker.getButton("myButton"));
    XmlAssert.assertEquivalent("<panel name='panelWithCardLayout'>" +
                               "  <listBox/>" +
                               "  <button label='myButton'/>" +
                               "</panel>",
                               panelWithCardLayoutChecker.getDescription());

    cardLayout.show(cardPanel, "second");
    try {
      panelWithCardLayoutChecker.getListBox();
      fail();
    }
    catch (ItemNotFoundException e) {
    }
    assertNotNull(panelWithCardLayoutChecker.getButton("myButton"));
    XmlAssert.assertEquivalent("<panel name='panelWithCardLayout'>" +
                               "  <button label='myButton'/>" +
                               "</panel>",
                               panelWithCardLayoutChecker.getDescription());
  }

  @Test
  public void testAmbiguityMessageContents() throws Exception {
    JButton apply = new JButton("apply");
    JButton noApply = new JButton(" don't apply");
    Component[] components = new Component[]{
      apply, noApply
    };
    StringBuffer message = new StringBuffer();
    message.append("Several components are of type '")
      .append(Button.TYPE_NAME)
      .append("' in this panel: [");
    for (int i = 0; i < components.length; i++) {
      Component component = components[i];
      String displayedName = ComponentUtils.getDisplayedName(component);
      message.append((displayedName == null) ? component.getName() : displayedName);
      message.append((i < components.length - 1) ? ',' : ']');
    }
    assertEquals("Several components are of type 'button' in this panel: [apply, don't apply]",
                 message.toString());
  }

  @Test
  public void testGetInputTextBoxExcludesJLabels() throws Exception {
    JLabel label = new JLabel("label");
    label.setName("name");
    JTextField textField = new JTextField("textField");
    textField.setName("name");
    jPanel.add(label);
    jPanel.add(textField);

    panel.getInputTextBox("name").textEquals("textField");
    panel.getInputTextBox().textEquals("textField");
  }

  @Test
  public void testGetPanelSearchesForSpecificClasses() throws Exception {
    checkGetPanel(new JPanel());
    checkGetPanel(new JInternalFrame());
  }

  private void checkGetPanel(Container innerPanel) throws Exception {
    jPanel.removeAll();
    JTextField textField = new JTextField();
    textField.setName("innerText");
    jPanel.add(textField);
    innerPanel.setName("innerPanel");
    jPanel.add(innerPanel);
    assertNotNull(panel.getPanel("inner"));
  }

  private void addComponentToPanelWithName(Component component, String name) {
    component.setName(name);
    jPanel.add(component);
  }

  private void createTwoComponentsWithSameName(Class component, String componentName) throws Exception {
    addComponentToPanelWithName(((JComponent)component.newInstance()), componentName);
    addComponentToPanelWithName(((JComponent)component.newInstance()), componentName);
  }

  private void checkComponentNotFound(String uiComponentType) throws Exception {
    try {
      getAccessor(uiComponentType).getComponent();
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals(Messages.computeNotFoundMessage(uiComponentType, null, null),
                   e.getMessage());
    }

    String componentName = "unknown";
    try {
      getAccessor(uiComponentType).getComponent(componentName);
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals(Messages.computeNotFoundMessage(uiComponentType, componentName, null),
                   e.getMessage());
    }

    try {
      getAccessor(uiComponentType).getComponent(new ComponentMatcher() {
        public boolean matches(Component component) {
          return false;
        }
      });
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals(Messages.computeNotFoundMessage(null, null, null),
                   e.getMessage());
    }
  }

  private void checkComponentTypeMismatch(String componentName,
                                          Class actualComponentType,
                                          String uiComponentType) throws Exception {
    addComponentToPanelWithName((Component)actualComponentType.newInstance(), componentName);
    try {
      getAccessor(uiComponentType).getComponent(componentName);
      fail();
    }
    catch (ItemNotFoundException e) {
      assertEquals(Messages.computeNotFoundMessage(uiComponentType, componentName, null),
                   e.getMessage());
    }
  }

  private void checkComponentNameAmbiguity(String componentName, String uiComponentType, String[] candidates) throws Exception {
    try {
      getAccessor(uiComponentType).getComponent(componentName);
      fail();
    }
    catch (ComponentAmbiguityException e) {
      assertEquals(Messages.computeAmbiguityMessage(candidates, uiComponentType, componentName),
                   e.getMessage());
    }
  }

  private void checkComponentUnicityAmbiguity(Class componentClass, String uiComponentType, String componentName)
    throws Exception {
    Component component1 = createSwingInstance(componentClass);
    addComponentToPanelWithName(component1, componentName + "1");
    Component component2 = createSwingInstance(componentClass);
    addComponentToPanelWithName(component2, componentName + "2");
    try {
      getAccessor(uiComponentType).getComponent();
      fail();
    }
    catch (ComponentAmbiguityException e) {
      Component[] components = new Component[]{component1, component2};
      String[] names = new String[components.length];
      for (int i = 0; i < components.length; i++) {
        Component component = components[i];
        String displayedName = ComponentUtils.getDisplayedName(component);
        names[i] = (displayedName == null || displayedName.length() == 0) ? component.getName() : displayedName;
      }
      assertEquals(Messages.computeAmbiguityMessage(names, uiComponentType, null),
                   e.getMessage());
    }
  }

  private void checkGetComponentWithText(Class componentClass,
                                         String uiComponentType) throws Exception {
    String name = "wholename";
    String shortName = "lena";
    Component component = createComponentWithText(componentClass, name);
    jPanel.add(component);
    checkGetComponent(component, getAccessor(uiComponentType), name, shortName);
  }

  private void checkGetComponentWithName(Class componentClass,
                                         String uiComponentType) throws Exception {
    String componentName = componentClass.getName() + "#componentName";
    String shortName = componentClass.getName() + "#c";
    Component component = createSwingInstance(componentClass);
    addComponentToPanelWithName(component, componentName);
    checkGetComponent(component, getAccessor(uiComponentType), componentName, shortName);
  }

  private Component createSwingInstance(Class componentClass) throws InstantiationException, IllegalAccessException {
    if (componentClass.equals(JTextComponent.class)) {
      return new JTextField();
    }
    return (Component)componentClass.newInstance();
  }

  private void checkGetComponentWithClassAndName(Class uiComponentClass) throws Exception {
    String typeName = UIComponentAnalyzer.getTypeName(uiComponentClass);
    Class[] swingClasses = UIComponentAnalyzer.getSwingClasses(uiComponentClass);
    for (Class swingClass : swingClasses) {
      checkGetComponentWithName(swingClass, typeName);
    }
  }

  private void checkGetComponent(Component expectedComponent, TypedComponentAccessor accessor, String name, String shortName)
    throws Exception {
    TestUtils.assertUIComponentRefersTo(expectedComponent, accessor.getComponent(name));
    TestUtils.assertUIComponentRefersTo(expectedComponent, accessor.getComponent(name.toUpperCase()));
    TestUtils.assertUIComponentRefersTo(expectedComponent, accessor.getComponent(name.toLowerCase()));
    TestUtils.assertUIComponentRefersTo(expectedComponent, accessor.getComponent(shortName));
    TestUtils.assertUIComponentRefersTo(expectedComponent, accessor.getComponent(shortName.toUpperCase()));
    TestUtils.assertUIComponentRefersTo(expectedComponent, accessor.getComponent(shortName.toLowerCase()));
    try {
      accessor.getComponent("toto");
      fail();
    }
    catch (Throwable e) {
      // OK
    }
  }

  private void checkGetComponentWithClass(Class uiComponentClass) throws Exception {
    Class[] swingClasses = UIComponentAnalyzer.getSwingClasses(uiComponentClass);
    String uiComponentType = UIComponentAnalyzer.getTypeName(uiComponentClass);
    for (Class swingClass : swingClasses) {
      jPanel.removeAll();
      Component component = createSwingInstance(swingClass);
      jPanel.add(component);
      TestUtils.assertUIComponentRefersTo(component, getAccessor(uiComponentType).getComponent());
    }
  }

  private void checkGetComponentWithCustomMatcher(Class uiComponentClass) throws Exception {
    Class[] swingClasses = UIComponentAnalyzer.getSwingClasses(uiComponentClass);
    String uiComponentType = UIComponentAnalyzer.getTypeName(uiComponentClass);
    for (Class swingClass : swingClasses) {
      jPanel.removeAll();
      Component component = createSwingInstance(swingClass);
      jPanel.add(component);
      ComponentMatcher matcher = new ComponentMatcher() {
        public boolean matches(Component component) {
          return component.isEnabled();
        }
      };
      TestUtils.assertUIComponentRefersTo(component, getAccessor(uiComponentType).getComponent(matcher));
    }
  }

  private Component createComponentWithText(Class componentClass, String componentName) throws Exception {
    Constructor componentConstructor = componentClass.getConstructor(String.class);
    return (Component)componentConstructor.newInstance(componentName);
  }

  private TypedComponentAccessor getAccessor(String typeName) {
    return (TypedComponentAccessor)componentAccessors.get(typeName);
  }

  private static Map createAccessors(final Panel panel) {
    Map<String, ComponentAccessorAdapter> map = new HashMap<String, ComponentAccessorAdapter>();
    map.put(Button.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getButton(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getButton(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getButton();
      }
    });
    map.put(ToggleButton.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getToggleButton(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getToggleButton(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getToggleButton();
      }
    });
    map.put(CheckBox.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getCheckBox(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getCheckBox(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getCheckBox();
      }
    });
    map.put(TextBox.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent() throws Exception {
        return panel.getTextBox();
      }

      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getTextBox(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getTextBox(componentName);
      }
    });
    map.put(TabGroup.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent() throws Exception {
        return panel.getTabGroup();
      }

      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getTabGroup(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getTabGroup(componentName);
      }
    });
    map.put(ComboBox.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent() throws Exception {
        return panel.getComboBox();
      }

      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getComboBox(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getComboBox(componentName);
      }
    });
    map.put(org.uispec4j.Desktop.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent() throws Exception {
        return panel.getDesktop();
      }

      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getDesktop(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getDesktop(componentName);
      }
    });
    map.put(ProgressBar.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent() throws Exception {
        return panel.getProgressBar();
      }

      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getProgressBar(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getProgressBar(componentName);
      }
    });
    map.put(RadioButton.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getRadioButton(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getRadioButton(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getRadioButton();
      }

    });
    map.put(Panel.TYPE_NAME, new ComponentAccessorAdapter() {

      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getPanel(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getPanel(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getPanel();
      }
    });
    map.put(ListBox.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getListBox(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getListBox(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getListBox();
      }
    });
    map.put(Table.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getTable(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getTable(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getTable();
      }
    });
    map.put(Tree.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getTree(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getTree(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getTree();
      }
    });
    map.put(Spinner.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getSpinner(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getSpinner(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getSpinner();
      }
    });
    map.put(Slider.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getSlider(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getSlider(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getSlider();
      }
    });
    map.put(PasswordField.TYPE_NAME, new ComponentAccessorAdapter() {
      public UIComponent getComponent(ComponentMatcher matcher) throws Exception {
        return panel.getPasswordField(matcher);
      }

      public UIComponent getComponent(String componentName) throws Exception {
        return panel.getPasswordField(componentName);
      }

      public UIComponent getComponent() throws Exception {
        return panel.getPasswordField();
      }
    });
    return map;
  }

  private interface TypedComponentAccessor {

    UIComponent getComponent(ComponentMatcher matcher) throws Exception;

    UIComponent getComponent(String componentName) throws Exception;

    UIComponent getComponent() throws Exception;

  }

  private abstract static class ComponentAccessorAdapter implements TypedComponentAccessor {

    public UIComponent getComponent(String componentName) throws Exception {
      throw new RuntimeException("No such method");
    }

  }

  private void addWithScrollPane(Component containedComponent, JPanel main) {
    JScrollPane scroll = new JScrollPane();
    scroll.getViewport().add(containedComponent);
    main.add(scroll);
  }

  private void addComponentWithLabel(Class<? extends Component> componentClass, String label) throws Exception {
    Component component = componentClass.newInstance();
    JLabel label1 = new JLabel(label);
    label1.setLabelFor(component);
    jPanel.add(component);
    jPanel.add(label1);
  }
}
