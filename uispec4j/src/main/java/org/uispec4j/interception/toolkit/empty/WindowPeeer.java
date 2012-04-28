package org.uispec4j.interception.toolkit.empty;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.PaintEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ContainerPeer;
import java.awt.peer.WindowPeer;

import sun.awt.CausedFocusEvent;
import sun.java2d.pipe.Region;

public abstract class WindowPeeer implements WindowPeer {
  public void toBack() {
  }

  public void updateWindow() {
  }

  public void setAlwaysOnTop(boolean b) {
  }

  public void updateFocusableWindowState() {
  }

  public void updateAlwaysOnTop() {
  }

  public boolean requestWindowFocus() {
    return false;
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
    return Empty.NULL_INSETS;
  }

  public Insets insets() {
    return Empty.NULL_INSETS;
  }

  public boolean isPaintPending() {
    return false;
  }

  public void cancelPendingPaint(int x, int y, int w, int h) {
  }

  public void restack() {
  }

  public boolean isRestackSupported() {
    return false;
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
    return Empty.NULL_IMAGE;
  }

  public Image createImage(int width, int height) {
    return Empty.NULL_IMAGE;
  }

  public VolatileImage createVolatileImage(int width, int height) {
    return Empty.NULL_VOLATILE_IMAGE;
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

  public Rectangle getBounds() {
    return Empty.NULL_RECTANGLE;
  }

  public void disable() {
  }

  public void dispose() {
  }

  public void enable() {
  }

  public void flip(BufferCapabilities.FlipContents flipAction) {
  }

  public Image getBackBuffer() {
    return Empty.NULL_IMAGE;
  }

  public ColorModel getColorModel() {
    return Empty.NULL_COLOR_MODEL;
  }

  public FontMetrics getFontMetrics(Font font) {
    return Empty.NULL_FONT_METRICS;
  }

  public Graphics getGraphics() {
    return Empty.NULL_GRAPHICS_2D;
  }

  public GraphicsConfiguration getGraphicsConfiguration() {
    return Empty.NULL_GRAPHICS_CONFIGURATION;
  }

  public Point getLocationOnScreen() {
    return Empty.NULL_POINT;
  }

  public Dimension getMinimumSize() {
    return Empty.NULL_DIMENSION;
  }

  public Dimension getPreferredSize() {
    return Empty.NULL_DIMENSION;
  }

  public void handleEvent(AWTEvent e) {
  }

  public boolean handlesWheelScrolling() {
    return false;
  }

  public void hide() {
  }

  public boolean isFocusable() {
    return false;
  }

  public boolean isObscured() {
    return false;
  }

  public Dimension minimumSize() {
    return Empty.NULL_DIMENSION;
  }

  public void paint(Graphics g) {
  }

  public Dimension preferredSize() {
    return Empty.NULL_DIMENSION;
  }

  public boolean prepareImage(Image img, int w, int h, ImageObserver o) {
    return false;
  }

  public void print(Graphics g) {
  }

  public void setBounds(int x, int y, int width, int height, int op) {
  }

  public void repaint(long tm, int x, int y, int width, int height) {
  }

  public boolean requestFocus(Component lightweightChild,
      boolean temporary,
      boolean focusedWindowChangeAllowed,
      long time) {
    return false;
  }

  public void reshape(int x, int y, int width, int height) {
  }

  public void setBackground(Color c) {
  }

  public void setBounds(int x, int y, int width, int height) {
  }

  public void setEnabled(boolean b) {
  }

  public void setFont(Font f) {
  }

  public void setForeground(Color c) {
  }

  public void setVisible(boolean b) {
  }

  public void show() {
  }

  public void updateCursorImmediately() {
  }

  public boolean requestFocus(Component component, boolean b, boolean b1, long l, CausedFocusEvent.Cause cause) {
    return false;
  }

  public void setOpacity(float opacity) {
  }

  public void setOpaque(boolean isOpaque) {
  }

  public void updateWindow(BufferedImage backBuffer) {
  }

  public void repositionSecurityWarning() {
  }

  public void flip(int x1, int y1, int x2, int y2, BufferCapabilities.FlipContents flipAction) {
  }

  public void applyShape(Region shape) {
  }
}