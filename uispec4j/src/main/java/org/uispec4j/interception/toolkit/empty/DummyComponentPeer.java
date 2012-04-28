package org.uispec4j.interception.toolkit.empty;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.PaintEvent;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ComponentPeer;

abstract class DummyComponentPeer implements ComponentPeer {

  public void destroyBuffers() {
  }

  public void disable() {
  }

  public void dispose() {
  }

  public void enable() {
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

  public void reshape(int x, int y, int width, int height) {
  }

  public void setBounds(int x, int y, int width, int height) {
  }

  public void repaint(long tm, int x, int y, int width, int height) {
  }

  public void setEnabled(boolean b) {
  }

  public void setVisible(boolean b) {
  }

  public void handleEvent(AWTEvent e) {
  }

  public void createBuffers(int numBuffers, BufferCapabilities caps) throws AWTException {
  }

  public void flip(BufferCapabilities.FlipContents flipAction) {
  }

  public void setBackground(Color c) {
  }

  public void setForeground(Color c) {
  }

  public boolean requestFocus(Component lightweightChild, boolean temporary, boolean focusedWindowChangeAllowed, long time) {
    return false;
  }

  public Dimension getMinimumSize() {
    return Empty.NULL_DIMENSION;
  }

  public Dimension getPreferredSize() {
    return Empty.NULL_DIMENSION;
  }

  public Dimension minimumSize() {
    return Empty.NULL_DIMENSION;
  }

  public Dimension preferredSize() {
    return Empty.NULL_DIMENSION;
  }

  public void setFont(Font f) {
  }

  public Graphics getGraphics() {
    return Empty.NULL_GRAPHICS_2D;
  }

  public void paint(Graphics g) {
  }

  public void print(Graphics g) {
  }

  public GraphicsConfiguration getGraphicsConfiguration() {
    return Empty.NULL_GRAPHICS_CONFIGURATION;
  }

  public Image getBackBuffer() {
    return Empty.NULL_IMAGE;
  }

  public Image createImage(int width, int height) {
    return Empty.NULL_IMAGE;
  }

  public Point getLocationOnScreen() {
    return Empty.NULL_POINT;
  }

  public Toolkit getToolkit() {
    return Toolkit.getDefaultToolkit();
  }

  public void coalescePaintEvent(PaintEvent e) {
  }

  public ColorModel getColorModel() {
    return Empty.NULL_COLOR_MODEL;
  }

  public VolatileImage createVolatileImage(int width, int height) {
    return Empty.NULL_VOLATILE_IMAGE;
  }

  public FontMetrics getFontMetrics(Font font) {
    return Empty.NULL_FONT_METRICS;
  }

  public Image createImage(ImageProducer producer) {
    return Empty.NULL_IMAGE;
  }

  public int checkImage(Image img, int w, int h, ImageObserver o) {
    return 0;
  }

  public boolean prepareImage(Image img, int w, int h, ImageObserver o) {
    return false;
  }
}