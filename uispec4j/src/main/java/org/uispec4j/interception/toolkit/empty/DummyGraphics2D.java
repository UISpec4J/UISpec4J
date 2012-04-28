package org.uispec4j.interception.toolkit.empty;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;

class DummyGraphics2D extends Graphics2D {

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
    return Empty.NULL_FONT;
  }

  public void setFont(Font font) {
  }

  public Graphics create() {
    return Empty.NULL_GRAPHICS_2D;
  }

  public Rectangle getClipBounds() {
    return Empty.NULL_RECTANGLE;
  }

  public Shape getClip() {
    return Empty.NULL_RECTANGLE;
  }

  public void setClip(Shape clip) {
  }

  public Color getBackground() {
    return Color.WHITE;
  }

  public void setBackground(Color color) {
  }

  public Composite getComposite() {
    return Empty.NULL_COMPOSITE;
  }

  public void setComposite(Composite comp) {
  }

  public GraphicsConfiguration getDeviceConfiguration() {
    return Empty.NULL_GRAPHICS_CONFIGURATION;
  }

  public Paint getPaint() {
    return Empty.NULL_PAINT;
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
    return Empty.NULL_STROKE;
  }

  public void setStroke(Stroke s) {
  }

  public FontRenderContext getFontRenderContext() {
    return new FontRenderContext(Empty.NULL_AFFINE_TRANSFORM, false, false);
  }

  public void drawGlyphVector(GlyphVector g, float x, float y) {
  }

  public AffineTransform getTransform() {
    return Empty.NULL_AFFINE_TRANSFORM;
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
    return Empty.NULL_FONT_METRICS;
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