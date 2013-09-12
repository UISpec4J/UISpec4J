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

import sun.awt.CausedFocusEvent;
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

    public void setAlwaysOnTop(final boolean b) {
    }

    public void updateFocusableWindowState() {
    }

    public void setModalBlocked(final Dialog dialog, final boolean b) {
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

    public int checkImage(final Image img, final int w, final int h, final ImageObserver o) {
      return 0;
    }

    public void coalescePaintEvent(final PaintEvent e) {
    }

    public void createBuffers(final int numBuffers, final BufferCapabilities caps)
      throws AWTException {
    }

    public Image createImage(final ImageProducer producer) {
      return NULL_IMAGE;
    }

    public Image createImage(final int width, final int height) {
      return NULL_IMAGE;
    }

    public VolatileImage createVolatileImage(final int width, final int height) {
      return NULL_VOLATILE_IMAGE;
    }

    public void destroyBuffers() {
    }

    public void reparent(final ContainerPeer newContainer) {
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

    public FontMetrics getFontMetrics(final Font font) {
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

    public void handleEvent(final AWTEvent e) {
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

    public void paint(final Graphics g) {
    }

    public boolean prepareImage(final Image img, final int w, final int h, final ImageObserver o) {
      return false;
    }

    public void print(final Graphics g) {
    }

    public void setBounds(final int x, final int y, final int width, final int height, final int op) {
    }

    public void setBackground(final Color c) {
    }

    public void setEnabled(final boolean b) {
    }

    public void setFont(final Font f) {
    }

    public void setForeground(final Color c) {
    }

    public void setVisible(final boolean b) {
      if (b) {
        show();
      }
    }

    public void show() {
    }

    public void updateCursorImmediately() {
    }

    public boolean requestFocus(final Component component, final boolean b, final boolean b1, final long l, final CausedFocusEvent.Cause cause) {
      return false;
    }

    public void setOpacity(final float opacity) {
    }

    public void setOpaque(final boolean isOpaque) {
    }

    public void repositionSecurityWarning() {
    }

    public void flip(final int x1, final int y1, final int x2, final int y2, final BufferCapabilities.FlipContents flipAction) {
    }

    public void applyShape(final Region shape) {
    }

    public void setZOrder(final ComponentPeer above) {
    }

    public boolean updateGraphicsData(final GraphicsConfiguration gc) {
      return false;
    }

	public void updateAlwaysOnTopState() {
	}
  }

  static class FramePeer extends Empty.WindowPeeer implements java.awt.peer.FramePeer {
    public int getState() {
      return 0;
    }

    public void setState(final int state) {
    }

    public void setResizable(final boolean resizeable) {
    }

    public void setMenuBar(final MenuBar mb) {
    }

    public void setMaximizedBounds(final Rectangle bounds) {
    }

    public void setBoundsPrivate(final int x, final int y, final int width, final int height) {
    }

    public Rectangle getBoundsPrivate() {
      return null;
    }

    public void setTitle(final String title) {
    }

	public void emulateActivation(final boolean activate) {
	}
  }

  static class DialogPeer extends Empty.WindowPeeer implements java.awt.peer.DialogPeer {
    public void setResizable(final boolean resizeable) {
    }

    public void blockWindows(final java.util.List<Window> windows) {
    }

    public void setTitle(final String title) {
    }
  }

  private static class DummyRobotPeer implements RobotPeer {

    public void keyPress(final int keycode) {
    }

    public void keyRelease(final int keycode) {
    }

    public void mousePress(final int buttons) {
    }

    public void mouseRelease(final int buttons) {
    }

    public void mouseWheel(final int wheelAmt) {
    }

    public int getRGBPixel(final int x, final int y) {
      return 0;
    }

    public void mouseMove(final int x, final int y) {
    }

    public int[] getRGBPixels(final Rectangle bounds) {
      return new int[0];
    }

    public void dispose() {
    }
  }

  static class DummyGraphicsConfiguration extends GraphicsConfiguration {
    @Override
	public BufferedImage createCompatibleImage(final int width, final int height) {
      return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
	public BufferedImage createCompatibleImage(final int width, final int height, final int transparency) {
      return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
	public VolatileImage createCompatibleVolatileImage(final int width, final int height) {
      return new SunVolatileImage(this, width, height, 0, getImageCapabilities());
    }

    @Override
	public VolatileImage createCompatibleVolatileImage(final int width, final int height, final int transparency) {
      return NULL_VOLATILE_IMAGE;
    }

    @Override
	public Rectangle getBounds() {
      return NULL_RECTANGLE;
    }

    @Override
	public ColorModel getColorModel() {
      return NULL_COLOR_MODEL;
    }

    @Override
	public ColorModel getColorModel(final int transparency) {
      return NULL_COLOR_MODEL;
    }

    @Override
	public AffineTransform getDefaultTransform() {
      return NULL_AFFINE_TRANSFORM;
    }

    @Override
	public GraphicsDevice getDevice() {
      return NULL_GRAPHICS_DEVICE;
    }

    @Override
	public AffineTransform getNormalizingTransform() {
      return NULL_AFFINE_TRANSFORM;
    }
  }

  static class DummyColorModel extends ColorModel {
    public DummyColorModel(final int bits) {
      super(bits);
    }

    public DummyColorModel() {
      super(128);
    }

    @Override
	public int getAlpha(final int pixel) {
      return 0;
    }

    @Override
	public int getBlue(final int pixel) {
      return 0;
    }

    @Override
	public int getGreen(final int pixel) {
      return 0;
    }

    @Override
	public int getRed(final int pixel) {
      return 0;
    }
  }

  private static class DummyImage extends Image {

    @Override
	public void flush() {
    }

    @Override
	public Graphics getGraphics() {
      return NULL_GRAPHICS_2D;
    }

    @Override
	public int getHeight(final ImageObserver observer) {
      return DEFAULT_HEIGHT;
    }

    @Override
	public int getWidth(final ImageObserver observer) {
      return DEFAULT_WIDTH;
    }

    @Override
	public ImageProducer getSource() {
      return NULL_IMAGE_PRODUCER;
    }

    @Override
	public Object getProperty(final String name, final ImageObserver observer) {
      return "";
    }
  }

  private static class DummyVolatileImage extends VolatileImage {
    public static final ImageCapabilities CAPABILITIES = new ImageCapabilities(false);
    public static final BufferedImage IMAGE = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

    @Override
	public BufferedImage getSnapshot() {
      return IMAGE;
    }

    @Override
	public int getWidth() {
      return DEFAULT_WIDTH;
    }

    @Override
	public int getHeight() {
      return DEFAULT_HEIGHT;
    }

    @Override
	public Graphics2D createGraphics() {
      return NULL_GRAPHICS_2D;
    }

    @Override
	public int validate(final GraphicsConfiguration gc) {
      return 0;
    }

    @Override
	public boolean contentsLost() {
      return false;
    }

    @Override
	public ImageCapabilities getCapabilities() {
      return CAPABILITIES;
    }

    @Override
	public int getWidth(final ImageObserver observer) {
      return DEFAULT_WIDTH;
    }

    @Override
	public int getHeight(final ImageObserver observer) {
      return DEFAULT_HEIGHT;
    }

    @Override
	public Object getProperty(final String name, final ImageObserver observer) {
      return "";
    }
  }

  private static class DummyFontMetrics extends FontMetrics {
    public static final int[] WIDTHS = new int[256];

    public DummyFontMetrics(final Font font) {
      super(font);
    }

    @Override
	public int[] getWidths() {
      return WIDTHS;
    }

    @Override
	public int stringWidth(final String str) {
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

    public void setEnabled(final boolean b) {
    }

    public void setVisible(final boolean b) {
    }

    public void handleEvent(final AWTEvent e) {
    }

    public void createBuffers(final int numBuffers, final BufferCapabilities caps) throws AWTException {
    }

    public void setBackground(final Color c) {
    }

    public void setForeground(final Color c) {
    }

    public Dimension getMinimumSize() {
      return NULL_DIMENSION;
    }

    public Dimension getPreferredSize() {
      return NULL_DIMENSION;
    }

    public void setFont(final Font f) {
    }

    public Graphics getGraphics() {
      return NULL_GRAPHICS_2D;
    }

    public void paint(final Graphics g) {
    }

    public void print(final Graphics g) {
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
      return NULL_GRAPHICS_CONFIGURATION;
    }

    public Image getBackBuffer() {
      return NULL_IMAGE;
    }

    public Image createImage(final int width, final int height) {
      return NULL_IMAGE;
    }

    public Point getLocationOnScreen() {
      return NULL_POINT;
    }

    public Toolkit getToolkit() {
      return Toolkit.getDefaultToolkit();
    }

    public void coalescePaintEvent(final PaintEvent e) {
    }

    public ColorModel getColorModel() {
      return NULL_COLOR_MODEL;
    }

    public VolatileImage createVolatileImage(final int width, final int height) {
      return NULL_VOLATILE_IMAGE;
    }

    public FontMetrics getFontMetrics(final Font font) {
      return NULL_FONT_METRICS;
    }

    public Image createImage(final ImageProducer producer) {
      return NULL_IMAGE;
    }

    public int checkImage(final Image img, final int w, final int h, final ImageObserver o) {
      return 0;
    }

    public boolean prepareImage(final Image img, final int w, final int h, final ImageObserver o) {
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

    public void setBounds(final int x, final int y, final int width, final int height, final int op) {
    }

    public boolean requestFocus(final Component component, final boolean b, final boolean b1, final long l, final CausedFocusEvent.Cause cause) {
      return false;
    }

    public void reparent(final ContainerPeer newContainer) {
    }

    public boolean isReparentSupported() {
      return false;
    }

    public void layout() {
    }

    public void show() {
    }

    public void flip(final int x1, final int y1, final int x2, final int y2, final BufferCapabilities.FlipContents flipAction) {
    }

    public void applyShape(final Region shape) {
    }

    public void setZOrder(final ComponentPeer above) {
    }

    public boolean updateGraphicsData(final GraphicsConfiguration gc) {
      return false;
    }
  }

  private static class DummyPanelPeer extends DummyContainerPeer implements PanelPeer {
  }

  private static class DummyCanvasPeer extends DummyContainerPeer implements CanvasPeer {
    public GraphicsConfiguration getAppropriateGraphicsConfiguration(final GraphicsConfiguration gc) {
      return null;
    }
  }

  private static class DummyGraphicsDevice extends GraphicsDevice {
    public static final GraphicsConfiguration[] CONFIGURATION = new GraphicsConfiguration[0];

    @Override
	public int getType() {
      return 0;
    }

    @Override
	public GraphicsConfiguration getDefaultConfiguration() {
      return NULL_GRAPHICS_CONFIGURATION;
    }

    @Override
	public GraphicsConfiguration[] getConfigurations() {
      return CONFIGURATION;
    }

    @Override
	public String getIDstring() {
      return "id";
    }
  }

  private static class DummyGraphics2D extends Graphics2D {

    @Override
	public void rotate(final double theta) {
    }

    @Override
	public void scale(final double sx, final double sy) {
    }

    @Override
	public void shear(final double shx, final double shy) {
    }

    @Override
	public void translate(final double tx, final double ty) {
    }

    @Override
	public void rotate(final double theta, final double x, final double y) {
    }

    @Override
	public void dispose() {
    }

    @Override
	public void setPaintMode() {
    }

    @Override
	public void translate(final int x, final int y) {
    }

    @Override
	public void clearRect(final int x, final int y, final int width, final int height) {
    }

    @Override
	public void clipRect(final int x, final int y, final int width, final int height) {
    }

    @Override
	public void drawLine(final int x1, final int y1, final int x2, final int y2) {
    }

    @Override
	public void drawOval(final int x, final int y, final int width, final int height) {
    }

    @Override
	public void fillOval(final int x, final int y, final int width, final int height) {
    }

    @Override
	public void fillRect(final int x, final int y, final int width, final int height) {
    }

    @Override
	public void setClip(final int x, final int y, final int width, final int height) {
    }

    @Override
	public void copyArea(final int x, final int y, final int width, final int height, final int dx, final int dy) {
    }

    @Override
	public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
    }

    @Override
	public void drawRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
    }

    @Override
	public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
    }

    @Override
	public void fillRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
    }

    @Override
	public void drawPolygon(final int xPoints[], final int yPoints[], final int nPoints) {
    }

    @Override
	public void drawPolyline(final int xPoints[], final int yPoints[], final int nPoints) {
    }

    @Override
	public void fillPolygon(final int xPoints[], final int yPoints[], final int nPoints) {
    }

    @Override
	public Color getColor() {
      return Color.BLACK;
    }

    @Override
	public void setColor(final Color c) {
    }

    @Override
	public void setXORMode(final Color c1) {
    }

    @Override
	public Font getFont() {
      return NULL_FONT;
    }

    @Override
	public void setFont(final Font font) {
    }

    @Override
	public Graphics create() {
      return NULL_GRAPHICS_2D;
    }

    @Override
	public Rectangle getClipBounds() {
      return NULL_RECTANGLE;
    }

    @Override
	public Shape getClip() {
      return NULL_RECTANGLE;
    }

    @Override
	public void setClip(final Shape clip) {
    }

    @Override
	public Color getBackground() {
      return Color.WHITE;
    }

    @Override
	public void setBackground(final Color color) {
    }

    @Override
	public Composite getComposite() {
      return NULL_COMPOSITE;
    }

    @Override
	public void setComposite(final Composite comp) {
    }

    @Override
	public GraphicsConfiguration getDeviceConfiguration() {
      return NULL_GRAPHICS_CONFIGURATION;
    }

    @Override
	public Paint getPaint() {
      return NULL_PAINT;
    }

    @Override
	public void setPaint(final Paint paint) {
    }

    @Override
	public RenderingHints getRenderingHints() {
      return new RenderingHints(new HashMap());
    }

    @Override
	public void clip(final Shape s) {
    }

    @Override
	public void draw(final Shape s) {
    }

    @Override
	public void fill(final Shape s) {
    }

    @Override
	public Stroke getStroke() {
      return NULL_STROKE;
    }

    @Override
	public void setStroke(final Stroke s) {
    }

    @Override
	public FontRenderContext getFontRenderContext() {
      return new FontRenderContext(NULL_AFFINE_TRANSFORM, false, false);
    }

    @Override
	public void drawGlyphVector(final GlyphVector g, final float x, final float y) {
    }

    @Override
	public AffineTransform getTransform() {
      return NULL_AFFINE_TRANSFORM;
    }

    @Override
	public void setTransform(final AffineTransform Tx) {
    }

    @Override
	public void transform(final AffineTransform Tx) {
    }

    @Override
	public void drawString(final String s, final float x, final float y) {
    }

    @Override
	public void drawString(final String str, final int x, final int y) {
    }

    @Override
	public void drawString(final AttributedCharacterIterator iterator, final float x, final float y) {
    }

    @Override
	public void drawString(final AttributedCharacterIterator iterator, final int x, final int y) {
    }

    @Override
	public FontMetrics getFontMetrics(final Font f) {
      return NULL_FONT_METRICS;
    }

    @Override
	public boolean drawImage(final Image img, final int dx1, final int dy1, final int dx2, final int dy2, final int sx1, final int sy1, final int sx2, final int sy2, final ImageObserver observer) {
      return false;
    }

    @Override
	public boolean drawImage(final Image img, final int x, final int y, final int width, final int height, final ImageObserver observer) {
      return false;
    }

    @Override
	public boolean drawImage(final Image img, final int x, final int y, final ImageObserver observer) {
      return false;
    }

    @Override
	public boolean drawImage(final Image img, final int dx1, final int dy1, final int dx2, final int dy2, final int sx1, final int sy1, final int sx2, final int sy2, final Color bgcolor, final ImageObserver observer) {
      return false;
    }

    @Override
	public boolean drawImage(final Image img, final int x, final int y, final int width, final int height, final Color bgcolor, final ImageObserver observer) {
      return false;
    }

    @Override
	public boolean drawImage(final Image img, final int x, final int y, final Color bgcolor, final ImageObserver observer) {
      return false;
    }

    @Override
	public void addRenderingHints(final Map hints) {
    }

    @Override
	public void setRenderingHints(final Map hints) {
    }

    @Override
	public boolean hit(final Rectangle rect, final Shape s, final boolean onStroke) {
      return false;
    }

    @Override
	public void drawRenderedImage(final RenderedImage img, final AffineTransform xform) {
    }

    @Override
	public void drawRenderableImage(final RenderableImage img, final AffineTransform xform) {
    }

    @Override
	public void drawImage(final BufferedImage img, final BufferedImageOp op, final int x, final int y) {
    }

    @Override
	public Object getRenderingHint(final RenderingHints.Key hintKey) {
      return RenderingHints.KEY_ALPHA_INTERPOLATION;
    }

    @Override
	public void setRenderingHint(final RenderingHints.Key hintKey, final Object hintValue) {
    }

    @Override
	public boolean drawImage(final Image img, final AffineTransform xform, final ImageObserver obs) {
      return false;
    }
  }

  public static class DummyPaint implements Paint {

    public PaintContext createContext(final ColorModel cm, final Rectangle deviceBounds, final Rectangle2D userBounds, final AffineTransform xform, final RenderingHints hints) {
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

    public Raster getRaster(final int x, final int y, final int w, final int h) {
      return Raster.createBandedRaster(0, 0, 0, 0, NULL_POINT);
    }
  }

  public static class DummyImageProducer implements ImageProducer {
    public void addConsumer(final ImageConsumer ic) {
    }

    public boolean isConsumer(final ImageConsumer ic) {
      return false;
    }

    public void removeConsumer(final ImageConsumer ic) {
    }

    public void requestTopDownLeftRightResend(final ImageConsumer ic) {
    }

    public void startProduction(final ImageConsumer ic) {
    }
  }

  public static class DummyComposite implements Composite {

    public CompositeContext createContext(final ColorModel srcColorModel, final ColorModel dstColorModel, final RenderingHints hints) {
      return NULL_COMPOSITE_CONTEXT;
    }
  }

  public static class DummyCompositeContext implements CompositeContext {

    public void dispose() {
    }

    public void compose(final Raster src, final Raster dstIn, final WritableRaster dstOut) {
    }
  }

  public static class DummyStroke implements Stroke {
    public Shape createStrokedShape(final Shape p) {
      return NULL_RECTANGLE;
    }
  }

  public static class DummyIcon implements Icon {

    public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
    }

    public int getIconWidth() {
      return 0;
    }

    public int getIconHeight() {
      return 0;
    }
  }


  private static class DummyDesktopPeer implements DesktopPeer {

    public boolean isSupported(final Desktop.Action action) {
      return false;
    }

    public void open(final File file) throws IOException {
    }

    public void edit(final File file) throws IOException {
    }

    public void print(final File file) throws IOException {
    }

    public void mail(final URI uri) throws IOException {
    }

    public void browse(final URI uri) throws IOException {
    }
  }

  private static class DummySystemTrayPeer implements SystemTrayPeer {
    public Dimension getTrayIconSize() {
      return null;
    }
  }

  private static class DummyTrayIconPeer implements TrayIconPeer {
    public void displayMessage(final String xml, final String xml1, final String xml2) {
    }

    public void dispose() {
    }

    public void setToolTip(final String xml) {
    }

    public void showPopupMenu(final int yyyymm, final int yyyymm1) {
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

    public String getInputMethodDisplayName(final Locale locale, final Locale locale1) {
      return locale.getVariant();
    }

    public Image getInputMethodIcon(final Locale locale) {
      return NULL_IMAGE;
    }

    public boolean hasDynamicLocaleList() {
      return false;
    }
  }

  private static class DummyInputMethod implements InputMethod {

    public void setInputMethodContext(final InputMethodContext inputMethodContext) {
    }

    public boolean setLocale(final Locale locale) {
      return false;
    }

    public Locale getLocale() {
      return Locale.getDefault();
    }

    public void setCharacterSubsets(final Character.Subset[] subsets) {
    }

    public void setCompositionEnabled(final boolean b) {
    }

    public boolean isCompositionEnabled() {
      return false;
    }

    public void reconvert() {
    }

    public void dispatchEvent(final AWTEvent awtEvent) {
    }

    public void notifyClientWindowChange(final Rectangle rectangle) {
    }

    public void activate() {
    }

    public void deactivate(final boolean b) {
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
    public int fillPointWithCoords(final Point point) {
      return 0;
    }

    public boolean isWindowUnderMouse(final Window window) {
      return false;
    }
  }
}
