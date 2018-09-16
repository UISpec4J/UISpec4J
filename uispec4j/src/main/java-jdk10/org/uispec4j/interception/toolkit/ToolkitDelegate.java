package org.uispec4j.interception.toolkit;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.JobAttributes;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.PageAttributes;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.PrintJob;
import java.awt.Robot;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.AWTEventListener;
import java.awt.im.InputMethodHighlight;
import java.awt.im.spi.InputMethodDescriptor;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.peer.ButtonPeer;
import java.awt.peer.CanvasPeer;
import java.awt.peer.CheckboxMenuItemPeer;
import java.awt.peer.CheckboxPeer;
import java.awt.peer.ChoicePeer;
import java.awt.peer.DesktopPeer;
import java.awt.peer.DialogPeer;
import java.awt.peer.FileDialogPeer;
import java.awt.peer.FontPeer;
import java.awt.peer.FramePeer;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.awt.peer.LabelPeer;
import java.awt.peer.ListPeer;
import java.awt.peer.MenuBarPeer;
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuPeer;
import java.awt.peer.PanelPeer;
import java.awt.peer.PopupMenuPeer;
import java.awt.peer.RobotPeer;
import java.awt.peer.ScrollPanePeer;
import java.awt.peer.ScrollbarPeer;
import java.awt.peer.SystemTrayPeer;
import java.awt.peer.TextAreaPeer;
import java.awt.peer.TextFieldPeer;
import java.awt.peer.TrayIconPeer;
import java.awt.peer.WindowPeer;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import sun.awt.ComponentFactory;
import sun.awt.LightweightFrame;
import sun.awt.SunToolkit;
import sun.awt.datatransfer.DataTransferer;

/**
 * Delegates to the underlying Toolkit.
 * <p>Note: this does not work if the underlying toolkit is not a Sun one.</p>
 */
///CLOVER:OFF
public abstract class ToolkitDelegate extends SunToolkit implements ComponentFactory {

  protected static Toolkit underlyingToolkit;

  public int getMaximumCursorColors() throws HeadlessException {
    return getUnderlyingToolkit().getMaximumCursorColors();
  }

  public int getMenuShortcutKeyMask() throws HeadlessException {
    return getUnderlyingToolkit().getMenuShortcutKeyMask();
  }

  public int getScreenResolution() throws HeadlessException {
    return getUnderlyingToolkit().getScreenResolution();
  }

  public void beep() {
    getUnderlyingToolkit().beep();
  }

  public void sync() {
    getUnderlyingToolkit().sync();
  }

  public boolean isDynamicLayoutActive() throws HeadlessException {
    return getUnderlyingToolkit().isDynamicLayoutActive();
  }

  public boolean getLockingKeyState(int keyCode)
    throws UnsupportedOperationException {
    return getUnderlyingToolkit().getLockingKeyState(keyCode);
  }

  public boolean isFrameStateSupported(int state)
    throws HeadlessException {
    return getUnderlyingToolkit().isFrameStateSupported(state);
  }

  public void setLockingKeyState(int keyCode, boolean on)
    throws UnsupportedOperationException {
    getUnderlyingToolkit().setLockingKeyState(keyCode, on);
  }

  public void setDynamicLayout(boolean dynamic)
    throws HeadlessException {
    getUnderlyingToolkit().setDynamicLayout(dynamic);
  }

  public Dimension getScreenSize() throws HeadlessException {
    return getUnderlyingToolkit().getScreenSize();
  }

  public Dimension getBestCursorSize(int preferredWidth, int preferredHeight)
    throws HeadlessException {
    return getUnderlyingToolkit().getBestCursorSize(preferredWidth, preferredHeight);
  }

  public EventQueue getSystemEventQueueImpl() {
    return getUnderlyingToolkit().getSystemEventQueue();
  }

  public Image createImage(byte[] imagedata) {
    return getUnderlyingToolkit().createImage(imagedata);
  }

  public Image createImage(byte[] imagedata, int imageoffset, int imagelength) {
    return getUnderlyingToolkit().createImage(imagedata, imageoffset, imagelength);
  }

  public Clipboard getSystemClipboard() throws HeadlessException {
    return getUnderlyingToolkit().getSystemClipboard();
  }

  public AWTEventListener[] getAWTEventListeners() {
    return getUnderlyingToolkit().getAWTEventListeners();
  }

  public AWTEventListener[] getAWTEventListeners(long eventMask) {
    return getUnderlyingToolkit().getAWTEventListeners(eventMask);
  }

  public void removeAWTEventListener(AWTEventListener listener) {
    getUnderlyingToolkit().removeAWTEventListener(listener);
  }

  public void addAWTEventListener(AWTEventListener listener, long eventMask) {
    getUnderlyingToolkit().addAWTEventListener(listener, eventMask);
  }

  public ColorModel getColorModel() throws HeadlessException {
    return getUnderlyingToolkit().getColorModel();
  }

  public PropertyChangeListener[] getPropertyChangeListeners() {
    return getUnderlyingToolkit().getPropertyChangeListeners();
  }

  public String[] getFontList() {
    return getUnderlyingToolkit().getFontList();
  }

  public DataTransferer getDataTransferer() {
     return asSun().getDataTransferer();
  }

  public FontMetrics getFontMetrics(Font font) {
    return getUnderlyingToolkit().getFontMetrics(font);
  }

  public Image createImage(ImageProducer producer) {
    return getUnderlyingToolkit().createImage(producer);
  }

  public Image createImage(String filename) {
    return getUnderlyingToolkit().createImage(filename);
  }

  public Image getImage(String filename) {
    return getUnderlyingToolkit().getImage(filename);
  }

  public Image createImage(URL url) {
    return getUnderlyingToolkit().createImage(url);
  }

  public Image getImage(URL url) {
    return getUnderlyingToolkit().getImage(url);
  }

  public Insets getScreenInsets(GraphicsConfiguration gc)
    throws HeadlessException {
    return Empty.NULL_INSETS;
  }

  public int checkImage(Image image, int width, int height, ImageObserver observer) {
    return getUnderlyingToolkit().checkImage(image, width, height, observer);
  }

  public boolean prepareImage(Image image, int width, int height, ImageObserver observer) {
    return getUnderlyingToolkit().prepareImage(image, width, height, observer);
  }

  public ButtonPeer createButton(Button target)
    throws HeadlessException {
    return asSun().createButton(target);
  }

  public CanvasPeer createCanvas(Canvas target) {
    return asSun().createCanvas(target);
  }

  public CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem target)
    throws HeadlessException {
    return asSun().createCheckboxMenuItem(target);
  }

  public CheckboxPeer createCheckbox(Checkbox target)
    throws HeadlessException {
    return asSun().createCheckbox(target);
  }

  public ChoicePeer createChoice(Choice target)
    throws HeadlessException {
    return asSun().createChoice(target);
  }

  public DialogPeer createDialog(Dialog target)
    throws HeadlessException {
    return asSun().createDialog(target);
  }

  public FileDialogPeer createFileDialog(FileDialog target)
    throws HeadlessException {
    return asSun().createFileDialog(target);
  }

  public FontPeer getFontPeer(String name, int style) {
    return asSun().getFontPeer(name, style);
  }

  public FramePeer createFrame(Frame target)
    throws HeadlessException {
    return asSun().createFrame(target);
  }

  public LabelPeer createLabel(Label target)
    throws HeadlessException {
    return asSun().createLabel(target);
  }

  public ListPeer createList(List target) throws HeadlessException {
    return asSun().createList(target);
  }

  public MenuBarPeer createMenuBar(MenuBar target)
    throws HeadlessException {
    return asSun().createMenuBar(target);
  }

  public MenuItemPeer createMenuItem(MenuItem target)
    throws HeadlessException {
    return asSun().createMenuItem(target);
  }

  public MenuPeer createMenu(Menu target) throws HeadlessException {
    return asSun().createMenu(target);
  }

  public PanelPeer createPanel(Panel target) {
    return asSun().createPanel(target);
  }

  public PopupMenuPeer createPopupMenu(PopupMenu target)
    throws HeadlessException {
    return asSun().createPopupMenu(target);
  }

  public ScrollPanePeer createScrollPane(ScrollPane target)
    throws HeadlessException {
    return asSun().createScrollPane(target);
  }

  public ScrollbarPeer createScrollbar(Scrollbar target)
    throws HeadlessException {
    return asSun().createScrollbar(target);
  }

  public TextAreaPeer createTextArea(TextArea target)
    throws HeadlessException {
    return asSun().createTextArea(target);
  }

  public TextFieldPeer createTextField(TextField target)
    throws HeadlessException {
    return asSun().createTextField(target);
  }

  public FramePeer createLightweightFrame(LightweightFrame target) {
    return asSun().createLightweightFrame(target);
  }

  public WindowPeer createWindow(Window target)
    throws HeadlessException {
    return asSun().createWindow(target);
  }

  public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
    return getUnderlyingToolkit().getPropertyChangeListeners(propertyName);
  }

  public void addPropertyChangeListener(String name, PropertyChangeListener pcl) {
    if(!DESKTOPFONTHINTS.equals(name)){
      getUnderlyingToolkit().addPropertyChangeListener(name, pcl);
    }
  }

  public void removePropertyChangeListener(String name, PropertyChangeListener pcl) {
    getUnderlyingToolkit().removePropertyChangeListener(name, pcl);
  }

  public Map<java.awt.font.TextAttribute,?> mapInputMethodHighlight(InputMethodHighlight highlight)
    throws HeadlessException {
      return getUnderlyingToolkit().mapInputMethodHighlight(highlight);
  }

  public Cursor createCustomCursor(Image cursor, Point hotSpot, String name)
    throws IndexOutOfBoundsException, HeadlessException {
    return getUnderlyingToolkit().createCustomCursor(cursor, hotSpot, name);
  }

  public PrintJob getPrintJob(Frame frame, String jobtitle, Properties props) {
    return getUnderlyingToolkit().getPrintJob(frame, jobtitle, props);
  }

  public PrintJob getPrintJob(Frame frame, String jobtitle,
                              JobAttributes jobAttributes, PageAttributes pageAttributes) {
    return getUnderlyingToolkit().getPrintJob(frame, jobtitle, jobAttributes,
                                              pageAttributes);
  }

  public DesktopPeer createDesktopPeer(Desktop desktop) throws HeadlessException {
    return Empty.NULL_DESKTOP_PEER;
  }

  public RobotPeer createRobot(Robot robot, GraphicsDevice graphicsDevice) throws AWTException, HeadlessException {
    return Empty.NULL_ROBOT;
  }

  public KeyboardFocusManagerPeer getKeyboardFocusManagerPeer() throws HeadlessException {
    return null;
  }

  protected boolean syncNativeQueue(long l) {
    return true;
  }

  public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType modalExclusionType) {
    return true;
  }

  public boolean isModalityTypeSupported(Dialog.ModalityType modalityType) {
    return true;
  }

  public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T>  abstractRecognizerClass,
                                                           DragSource ds,
                                                           Component cp,
                                                           int srcActions,
                                                           DragGestureListener dgl) {
    return getUnderlyingToolkit().createDragGestureRecognizer(abstractRecognizerClass, ds,
                                                              cp, srcActions, dgl);
  }

  private SunToolkit asSun() {
    return (SunToolkit)underlyingToolkit;
  }

  public Toolkit getUnderlyingToolkit() {
    return underlyingToolkit;
  }

  public SystemTrayPeer createSystemTray(SystemTray systemTray) {
    return Empty.NULL_SYSTEM_TRAY_PEER;
  }

  public TrayIconPeer createTrayIcon(TrayIcon trayIcon) throws HeadlessException, AWTException {
    return Empty.NULL_TRAY_ICON_PEER;
  }

  public InputMethodDescriptor getInputMethodAdapterDescriptor() throws AWTException {
    return Empty.NULL_INPUT_METHOD_DESCRIPTOR;
  }

  protected int getScreenHeight() {
    return 0;
  }

  protected int getScreenWidth() {
    return 0;
  }

  public void grab(Window window) {
  }

  public boolean isDesktopSupported() {
    return false;
  }

  public boolean isTraySupported() {
    return false;
  }

  public void ungrab(Window window) {
  }

  public boolean isWindowShapingSupported() {
    return false;
  }

  public boolean isWindowTranslucencySupported() {
    return false;
  }
}
