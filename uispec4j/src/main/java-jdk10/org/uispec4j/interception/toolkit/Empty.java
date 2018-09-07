package org.uispec4j.interception.toolkit;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Insets;
import java.awt.MenuBar;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.PaintEvent;
import java.awt.event.FocusEvent.Cause;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodContext;
import java.awt.im.spi.InputMethodDescriptor;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.RenderableImage;
import java.awt.peer.CanvasPeer;
import java.awt.peer.ComponentPeer;
import java.awt.peer.ContainerPeer;
import java.awt.peer.DesktopPeer;
import java.awt.peer.MouseInfoPeer;
import java.awt.peer.PanelPeer;
import java.awt.peer.RobotPeer;
import java.awt.peer.SystemTrayPeer;
import java.awt.peer.TrayIconPeer;
import java.awt.peer.WindowPeer;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;

import sun.awt.image.SunVolatileImage;
import sun.java2d.pipe.Region;

/**
 * Contains a set of empty peer class designed to keep the UISpec peer implementation clean.
 */
///CLOVER:OFF
public final class Empty {

  public static final Insets NULL_INSETS = new Insets(0, 0, 0, 0);
  public static final Dimension NULL_DIMENSION = new Dimension(50, 50);
  public static final Rectangle NULL_RECTANGLE = new Rectangle(50, 50);
  public static final GraphicsConfiguration NULL_GRAPHICS_CONFIGURATION = new DummyGraphicsConfiguration();
  public static final GraphicsDevice NULL_GRAPHICS_DEVICE = new DummyGraphicsDevice();
  public static final Graphics2D NULL_GRAPHICS_2D = new DummyGraphics2D();
  public static final ColorModel NULL_COLOR_MODEL = new DummyColorModel();
  public static final Point NULL_POINT = new Point(0, 0);
  public static final Image NULL_IMAGE = new DummyImage();
  public static final Font NULL_FONT = new JLabel().getFont();
  public static final FontMetrics NULL_FONT_METRICS;
  public static final VolatileImage NULL_VOLATILE_IMAGE = new DummyVolatileImage();
  public static final AffineTransform NULL_AFFINE_TRANSFORM = new AffineTransform();
  public static final int DEFAULT_HEIGHT = 50;
  public static final int DEFAULT_WIDTH = 50;
  public static final Paint NULL_PAINT = new DummyPaint();
  public static final PaintContext NULL_PAINT_CONTEXT = new DummyPaintContext();
  public static final ImageProducer NULL_IMAGE_PRODUCER = new DummyImageProducer();
  public static final Composite NULL_COMPOSITE = new DummyComposite();
  public static final CompositeContext NULL_COMPOSITE_CONTEXT = new DummyCompositeContext();
  public static final Stroke NULL_STROKE = new DummyStroke();
  public static final Icon NULL_ICON = new DummyIcon();

  public static final RobotPeer NULL_ROBOT = new DummyRobotPeer();
  public static final DummyPanelPeer NULL_PANEL_PEER = new DummyPanelPeer();
  public static final DummyCanvasPeer NULL_CANVAS_PEER = new DummyCanvasPeer();
  public static final DesktopPeer NULL_DESKTOP_PEER = new DummyDesktopPeer();
  public static final SystemTrayPeer NULL_SYSTEM_TRAY_PEER = new DummySystemTrayPeer();
  public static final TrayIconPeer NULL_TRAY_ICON_PEER = new DummyTrayIconPeer();

  public static final InputMethodDescriptor NULL_INPUT_METHOD_DESCRIPTOR = new DummyInputMethodDescriptor();
  public static final InputMethod NULL_INPUT_METHOD = new DummyInputMethod();
  public static final MouseInfoPeer NULL_MOUSE_INFO = new DummyMouseInfoPeer();

  static {
    NULL_FONT_METRICS = new DummyFontMetrics(NULL_FONT);
  }

  private Empty() {
  }

  static class WindowPeeer implements WindowPeer {
    public void toBack() {
    }

    public void updateWindow() {
    }

    public void setAlwaysOnTop(boolean b) {
    }

    public void updateAlwaysOnTopState() {
    }

    public void updateFocusableWindowState() {
    }

    public void setModalBlocked(Dialog dialog, boolean b) {
    }

    public void updateMinimumSize() {
    }

    public void updateIconImages() {
    }

    public void toFront() {
    }

    public void beginLayout() {
    }

    public void beginValidate() {
    }

    public void endLayout() {
    }

    public void endValidate() {
    }

    public Insets getInsets() {
      return NULL_INSETS;
    }

    public boolean canDetermineObscurity() {
      return false;
    }

    public int checkImage(Image img, int w, int h, ImageObserver o) {
      return 0;
    }

    public void coalescePaintEvent(PaintEvent e) {
    }

    public void createBuffers(int numBuffers, BufferCapabilities caps)
      throws AWTException {
    }

    public Image createImage(ImageProducer producer) {
      return NULL_IMAGE;
    }

    public Image createImage(int width, int height) {
      return NULL_IMAGE;
    }

    public VolatileImage createVolatileImage(int width, int height) {
      return NULL_VOLATILE_IMAGE;
    }

    public void destroyBuffers() {
    }

    public void reparent(ContainerPeer newContainer) {
    }

    public boolean isReparentSupported() {
      return false;
    }

    public void layout() {
    }

    public void dispose() {
    }

    public Image getBackBuffer() {
      return NULL_IMAGE;
    }

    public ColorModel getColorModel() {
      return NULL_COLOR_MODEL;
    }

    public FontMetrics getFontMetrics(Font font) {
      return NULL_FONT_METRICS;
    }

    public Graphics getGraphics() {
      return NULL_GRAPHICS_2D;
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
      return NULL_GRAPHICS_CONFIGURATION;
    }

    public Point getLocationOnScreen() {
      return NULL_POINT;
    }

    public Dimension getMinimumSize() {
      return NULL_DIMENSION;
    }

    public Dimension getPreferredSize() {
      return NULL_DIMENSION;
    }

    public Toolkit getToolkit() {
      return UISpecToolkit.instance();
    }

    public void handleEvent(AWTEvent e) {
    }

    public boolean handlesWheelScrolling() {
      return false;
    }

    public boolean isFocusable() {
      return false;
    }

    public boolean isObscured() {
      return false;
    }

    public void paint(Graphics g) {
    }

    public boolean prepareImage(Image img, int w, int h, ImageObserver o) {
      return false;
    }

    public void print(Graphics g) {
    }

    public void setBounds(int x, int y, int width, int height, int op) {
    }

    public void setBackground(Color c) {
    }

    public void setEnabled(boolean b) {
    }

    public void setFont(Font f) {
    }

    public void setForeground(Color c) {
    }

    public void setVisible(boolean b) {
      if (b) {
        show();
      }
    }

    public void show() {
    }

    public void updateCursorImmediately() {
    }

    public boolean requestFocus(Component lightweightChild, boolean temporary, boolean focusedWindowChangeAllowed, long time, Cause cause) {
    	return false;
    }

    public void setOpacity(float opacity) {
    }

    public void setOpaque(boolean isOpaque) {
    }

    public void repositionSecurityWarning() {
    }

    public void flip(int x1, int y1, int x2, int y2, BufferCapabilities.FlipContents flipAction) {
    }

    public void applyShape(Region shape) {
    }

    public void setZOrder(ComponentPeer above) {
    }

    public boolean updateGraphicsData(GraphicsConfiguration gc) {
      return false;
    }
  }

  static class FramePeer extends Empty.WindowPeeer implements java.awt.peer.FramePeer {
    public int getState() {
      return 0;
    }

    public void setState(int state) {
    }

    public void setResizable(boolean resizeable) {
    }

    public void setMenuBar(MenuBar mb) {
    }

    public void setMaximizedBounds(Rectangle bounds) {
    }

    public void setBoundsPrivate(int x, int y, int width, int height) {
    }

    public Rectangle getBoundsPrivate() {
      return null;
    }

    public void setTitle(String title) {
    }

    public void emulateActivation(boolean activate) {
    }
  }

  static class DialogPeer extends Empty.WindowPeeer implements java.awt.peer.DialogPeer {
    public void setResizable(boolean resizeable) {
    }

    public void blockWindows(java.util.List<Window> windows) {
    }

    public void setTitle(String title) {
    }
  }

  private static class DummyRobotPeer implements RobotPeer {

    public void keyPress(int keycode) {
    }

    public void keyRelease(int keycode) {
    }

    public void mousePress(int buttons) {
    }

    public void mouseRelease(int buttons) {
    }

    public void mouseWheel(int wheelAmt) {
    }

    public int getRGBPixel(int x, int y) {
      return 0;
    }

    public void mouseMove(int x, int y) {
    }

    public int[] getRGBPixels(Rectangle bounds) {
      return new int[0];
    }

    public void dispose() {
    }
  }

  static class DummyGraphicsConfiguration extends GraphicsConfiguration {
    public BufferedImage createCompatibleImage(int width, int height) {
      return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage createCompatibleImage(int width, int height, int transparency) {
      return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public VolatileImage createCompatibleVolatileImage(int width, int height) {
      return new SunVolatileImage(this, width, height, 0, getImageCapabilities());
    }

    public VolatileImage createCompatibleVolatileImage(int width, int height, int transparency) {
      return NULL_VOLATILE_IMAGE;
    }

    public Rectangle getBounds() {
      return NULL_RECTANGLE;
    }

    public ColorModel getColorModel() {
      return NULL_COLOR_MODEL;
    }

    public ColorModel getColorModel(int transparency) {
      return NULL_COLOR_MODEL;
    }

    public AffineTransform getDefaultTransform() {
      return NULL_AFFINE_TRANSFORM;
    }

    public GraphicsDevice getDevice() {
      return NULL_GRAPHICS_DEVICE;
    }

    public AffineTransform getNormalizingTransform() {
      return NULL_AFFINE_TRANSFORM;
    }
  }

  static class DummyColorModel extends ColorModel {
    public DummyColorModel(int bits) {
      super(bits);
    }

    public DummyColorModel() {
      super(128);
    }

    public int getAlpha(int pixel) {
      return 0;
    }

    public int getBlue(int pixel) {
      return 0;
    }

    public int getGreen(int pixel) {
      return 0;
    }

    public int getRed(int pixel) {
      return 0;
    }
  }

  private static class DummyImage extends Image {

    public void flush() {
    }

    public Graphics getGraphics() {
      return NULL_GRAPHICS_2D;
    }

    public int getHeight(ImageObserver observer) {
      return DEFAULT_HEIGHT;
    }

    public int getWidth(ImageObserver observer) {
      return DEFAULT_WIDTH;
    }

    public ImageProducer getSource() {
      return NULL_IMAGE_PRODUCER;
    }

    public Object getProperty(String name, ImageObserver observer) {
      return "";
    }
  }

  private static class DummyVolatileImage extends VolatileImage {
    public static final ImageCapabilities CAPABILITIES = new ImageCapabilities(false);
    public static final BufferedImage IMAGE = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

    public BufferedImage getSnapshot() {
      return IMAGE;
    }

    public int getWidth() {
      return DEFAULT_WIDTH;
    }

    public int getHeight() {
      return DEFAULT_HEIGHT;
    }

    public Graphics2D createGraphics() {
      return NULL_GRAPHICS_2D;
    }

    public int validate(GraphicsConfiguration gc) {
      return 0;
    }

    public boolean contentsLost() {
      return false;
    }

    public ImageCapabilities getCapabilities() {
      return CAPABILITIES;
    }

    public int getWidth(ImageObserver observer) {
      return DEFAULT_WIDTH;
    }

    public int getHeight(ImageObserver observer) {
      return DEFAULT_HEIGHT;
    }

    public Object getProperty(String name, ImageObserver observer) {
      return "";
    }
  }

  private static class DummyFontMetrics extends FontMetrics {
    public static final int[] WIDTHS = new int[256];

    public DummyFontMetrics(Font font) {
      super(font);
    }

    public int[] getWidths() {
      return WIDTHS;
    }

    public int stringWidth(String str) {
      return 0;
    }
  }

  abstract static class DummyComponentPeer implements ComponentPeer {

    public void destroyBuffers() {
    }

    public void dispose() {
    }

    public void hide() {
    }

    public void updateCursorImmediately() {
    }

    public boolean canDetermineObscurity() {
      return false;
    }

    public boolean handlesWheelScrolling() {
      return false;
    }

    public boolean isFocusable() {
      return false;
    }

    public boolean isObscured() {
      return false;
    }

    public void setEnabled(boolean b) {
    }

    public void setVisible(boolean b) {
    }

    public void handleEvent(AWTEvent e) {
    }

    public void createBuffers(int numBuffers, BufferCapabilities caps) throws AWTException {
    }

    public void setBackground(Color c) {
    }

    public void setForeground(Color c) {
    }

    public Dimension getMinimumSize() {
      return NULL_DIMENSION;
    }

    public Dimension getPreferredSize() {
      return NULL_DIMENSION;
    }

    public void setFont(Font f) {
    }

    public Graphics getGraphics() {
      return NULL_GRAPHICS_2D;
    }

    public void paint(Graphics g) {
    }

    public void print(Graphics g) {
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
      return NULL_GRAPHICS_CONFIGURATION;
    }

    public Image getBackBuffer() {
      return NULL_IMAGE;
    }

    public Image createImage(int width, int height) {
      return NULL_IMAGE;
    }

    public Point getLocationOnScreen() {
      return NULL_POINT;
    }

    public Toolkit getToolkit() {
      return Toolkit.getDefaultToolkit();
    }

    public void coalescePaintEvent(PaintEvent e) {
    }

    public ColorModel getColorModel() {
      return NULL_COLOR_MODEL;
    }

    public VolatileImage createVolatileImage(int width, int height) {
      return NULL_VOLATILE_IMAGE;
    }

    public FontMetrics getFontMetrics(Font font) {
      return NULL_FONT_METRICS;
    }

    public Image createImage(ImageProducer producer) {
      return NULL_IMAGE;
    }

    public int checkImage(Image img, int w, int h, ImageObserver o) {
      return 0;
    }

    public boolean prepareImage(Image img, int w, int h, ImageObserver o) {
      return false;
    }
  }

  private static class DummyContainerPeer extends DummyComponentPeer implements ContainerPeer {
    public void beginLayout() {
    }

    public void beginValidate() {
    }

    public void endLayout() {
    }

    public void endValidate() {
    }

    public Insets getInsets() {
      return NULL_INSETS;
    }

    public void setBounds(int x, int y, int width, int height, int op) {
    }

    public boolean requestFocus(Component lightweightChild, boolean temporary, boolean focusedWindowChangeAllowed, long time, Cause cause) {
      return false;
    }

    public void reparent(ContainerPeer newContainer) {
    }

    public boolean isReparentSupported() {
      return false;
    }

    public void layout() {
    }

    public void show() {
    }

    public void flip(int x1, int y1, int x2, int y2, BufferCapabilities.FlipContents flipAction) {
    }

    public void applyShape(Region shape) {
    }

    public void setZOrder(ComponentPeer above) {
    }

    public boolean updateGraphicsData(GraphicsConfiguration gc) {
      return false;
    }
  }

  private static class DummyPanelPeer extends DummyContainerPeer implements PanelPeer {
  }

  private static class DummyCanvasPeer extends DummyContainerPeer implements CanvasPeer {
    public GraphicsConfiguration getAppropriateGraphicsConfiguration(GraphicsConfiguration gc) {
      return null;
    }
  }

  private static class DummyGraphicsDevice extends GraphicsDevice {
    public static final GraphicsConfiguration[] CONFIGURATION = new GraphicsConfiguration[0];

    public int getType() {
      return 0;
    }

    public GraphicsConfiguration getDefaultConfiguration() {
      return NULL_GRAPHICS_CONFIGURATION;
    }

    public GraphicsConfiguration[] getConfigurations() {
      return CONFIGURATION;
    }

    public String getIDstring() {
      return "id";
    }
  }

  private static class DummyGraphics2D extends Graphics2D {

    public void rotate(double theta) {
    }

    public void scale(double sx, double sy) {
    }

    public void shear(double shx, double shy) {
    }

    public void translate(double tx, double ty) {
    }

    public void rotate(double theta, double x, double y) {
    }

    public void dispose() {
    }

    public void setPaintMode() {
    }

    public void translate(int x, int y) {
    }

    public void clearRect(int x, int y, int width, int height) {
    }

    public void clipRect(int x, int y, int width, int height) {
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
    }

    public void drawOval(int x, int y, int width, int height) {
    }

    public void fillOval(int x, int y, int width, int height) {
    }

    public void fillRect(int x, int y, int width, int height) {
    }

    public void setClip(int x, int y, int width, int height) {
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
    }

    public void drawPolygon(int xPoints[], int yPoints[], int nPoints) {
    }

    public void drawPolyline(int xPoints[], int yPoints[], int nPoints) {
    }

    public void fillPolygon(int xPoints[], int yPoints[], int nPoints) {
    }

    public Color getColor() {
      return Color.BLACK;
    }

    public void setColor(Color c) {
    }

    public void setXORMode(Color c1) {
    }

    public Font getFont() {
      return NULL_FONT;
    }

    public void setFont(Font font) {
    }

    public Graphics create() {
      return NULL_GRAPHICS_2D;
    }

    public Rectangle getClipBounds() {
      return NULL_RECTANGLE;
    }

    public Shape getClip() {
      return NULL_RECTANGLE;
    }

    public void setClip(Shape clip) {
    }

    public Color getBackground() {
      return Color.WHITE;
    }

    public void setBackground(Color color) {
    }

    public Composite getComposite() {
      return NULL_COMPOSITE;
    }

    public void setComposite(Composite comp) {
    }

    public GraphicsConfiguration getDeviceConfiguration() {
      return NULL_GRAPHICS_CONFIGURATION;
    }

    public Paint getPaint() {
      return NULL_PAINT;
    }

    public void setPaint(Paint paint) {
    }

    public RenderingHints getRenderingHints() {
      return new RenderingHints(new HashMap());
    }

    public void clip(Shape s) {
    }

    public void draw(Shape s) {
    }

    public void fill(Shape s) {
    }

    public Stroke getStroke() {
      return NULL_STROKE;
    }

    public void setStroke(Stroke s) {
    }

    public FontRenderContext getFontRenderContext() {
      return new FontRenderContext(NULL_AFFINE_TRANSFORM, false, false);
    }

    public void drawGlyphVector(GlyphVector g, float x, float y) {
    }

    public AffineTransform getTransform() {
      return NULL_AFFINE_TRANSFORM;
    }

    public void setTransform(AffineTransform Tx) {
    }

    public void transform(AffineTransform Tx) {
    }

    public void drawString(String s, float x, float y) {
    }

    public void drawString(String str, int x, int y) {
    }

    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
    }

    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
    }

    public FontMetrics getFontMetrics(Font f) {
      return NULL_FONT_METRICS;
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
      return false;
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
      return false;
    }

    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
      return false;
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
      return false;
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
      return false;
    }

    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
      return false;
    }

    public void addRenderingHints(Map hints) {
    }

    public void setRenderingHints(Map hints) {
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
      return false;
    }

    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
    }

    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
    }

    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
    }

    public Object getRenderingHint(RenderingHints.Key hintKey) {
      return RenderingHints.KEY_ALPHA_INTERPOLATION;
    }

    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
    }

    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
      return false;
    }
  }

  public static class DummyPaint implements Paint {

    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
      return NULL_PAINT_CONTEXT;
    }

    public int getTransparency() {
      return 0;
    }
  }

  public static class DummyPaintContext implements PaintContext {

    public void dispose() {
    }

    public ColorModel getColorModel() {
      return NULL_COLOR_MODEL;
    }

    public Raster getRaster(int x, int y, int w, int h) {
      return Raster.createBandedRaster(0, 0, 0, 0, NULL_POINT);
    }
  }

  public static class DummyImageProducer implements ImageProducer {
    public void addConsumer(ImageConsumer ic) {
    }

    public boolean isConsumer(ImageConsumer ic) {
      return false;
    }

    public void removeConsumer(ImageConsumer ic) {
    }

    public void requestTopDownLeftRightResend(ImageConsumer ic) {
    }

    public void startProduction(ImageConsumer ic) {
    }
  }

  public static class DummyComposite implements Composite {

    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
      return NULL_COMPOSITE_CONTEXT;
    }
  }

  public static class DummyCompositeContext implements CompositeContext {

    public void dispose() {
    }

    public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
    }
  }

  public static class DummyStroke implements Stroke {
    public Shape createStrokedShape(Shape p) {
      return NULL_RECTANGLE;
    }
  }

  public static class DummyIcon implements Icon {

    public void paintIcon(Component c, Graphics g, int x, int y) {
    }

    public int getIconWidth() {
      return 0;
    }

    public int getIconHeight() {
      return 0;
    }
  }


  private static class DummyDesktopPeer implements DesktopPeer {

    public boolean isSupported(Desktop.Action action) {
      return false;
    }

    public void open(File file) throws IOException {
    }

    public void edit(File file) throws IOException {
    }

    public void print(File file) throws IOException {
    }

    public void mail(URI uri) throws IOException {
    }

    public void browse(URI uri) throws IOException {
    }
  }

  private static class DummySystemTrayPeer implements SystemTrayPeer {
    public Dimension getTrayIconSize() {
      return null;
    }
  }

  private static class DummyTrayIconPeer implements TrayIconPeer {
    public void displayMessage(String xml, String xml1, String xml2) {
    }

    public void dispose() {
    }

    public void setToolTip(String xml) {
    }

    public void showPopupMenu(int yyyymm, int yyyymm1) {
    }

    public void updateImage() {
    }
  }

  private static class DummyInputMethodDescriptor implements InputMethodDescriptor {
    public InputMethod createInputMethod() throws Exception {
      return NULL_INPUT_METHOD;
    }

    public Locale[] getAvailableLocales() throws AWTException {
      return new Locale[0];
    }

    public String getInputMethodDisplayName(Locale locale, Locale locale1) {
      return locale.getVariant();
    }

    public Image getInputMethodIcon(Locale locale) {
      return NULL_IMAGE;
    }

    public boolean hasDynamicLocaleList() {
      return false;
    }
  }

  private static class DummyInputMethod implements InputMethod {

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

  private static class DummyMouseInfoPeer implements MouseInfoPeer {
    public int fillPointWithCoords(Point point) {
      return 0;
    }

    public boolean isWindowUnderMouse(Window window) {
      return false;
    }
  }
}
