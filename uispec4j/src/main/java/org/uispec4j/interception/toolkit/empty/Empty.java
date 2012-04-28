package org.uispec4j.interception.toolkit.empty;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodDescriptor;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.DesktopPeer;
import java.awt.peer.RobotPeer;
import java.awt.peer.SystemTrayPeer;
import java.awt.peer.TrayIconPeer;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * Contains a set of empty peer class designed to keep the UISpec peer implementation clean.
 */
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
  public static final FontMetrics NULL_FONT_METRICS = new DummyFontMetrics(NULL_FONT);
  public static final VolatileImage NULL_VOLATILE_IMAGE = new DummyVolatileImage();
  public static final AffineTransform NULL_AFFINE_TRANSFORM = new AffineTransform();
  public static final int DEFAULT_HEIGHT = 50;
  public static final Paint NULL_PAINT = new DummyPaint();
  public static final PaintContext NULL_PAINT_CONTEXT = new DummyPaintContext();
  public static final ImageProducer NULL_IMAGE_PRODUCER = new DummyImageProducer();
  public static final Composite NULL_COMPOSITE = new DummyComposite();
  public static final CompositeContext NULL_COMPOSITE_CONTEXT = new DummyCompositeContext();
  public static final Stroke NULL_STROKE = new DummyStroke();
  public static final Icon NULL_ICON = new DummyIcon();

  public static final RobotPeer NULL_ROBOT = new DummyRobotPeer();
  public static final DummyLightweightPeer NULL_LIGHTWEIGHT_PEER = new DummyLightweightPeer();
  public static final DummyPanelPeer NULL_PANEL_PEER = new DummyPanelPeer();
  public static final DummyCanvasPeer NULL_CANVAS_PEER = new DummyCanvasPeer();
  public static final DesktopPeer NULL_DESKTOP_PEER = new DummyDesktopPeer();
  public static final RobotPeer NULL_ROBOT_PEER = new DummyRobotPeer();
  public static final SystemTrayPeer NULL_SYSTEM_TRAY_PEER = new DummySystemTrayPeer();
  public static final TrayIconPeer NULL_TRAY_ICON_PEER = new DummyTrayIconPeer();

  public static final InputMethodDescriptor NULL_INPUT_METHOD_DESCRIPTOR = new DummyInputMethodDescriptor();
  public static final InputMethod NULL_INPUT_METHOD = new DummyInputMethod();
  // public static final MouseInfoPeer NULL_MOUSE_INFO = new DummyMouseInfoPeer();
}
