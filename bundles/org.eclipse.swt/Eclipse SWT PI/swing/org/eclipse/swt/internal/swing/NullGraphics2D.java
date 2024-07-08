/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

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
import java.awt.Toolkit;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;

public class NullGraphics2D extends Graphics2D implements Cloneable {
  
  // TODO: save the attributes for the copyAttributes()
  public void addRenderingHints(Map arg0) {
  }
  public void clip(Shape s) {
  }
  public void draw(Shape s) {
  }
  public void drawGlyphVector(GlyphVector g, float x, float y) {
  }
  public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
  }
  public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
    return true;
  }
  public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
  }
  public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
  }
  public void drawString(AttributedCharacterIterator iterator, float x, float y) {
  }
  public void drawString(AttributedCharacterIterator iterator, int x, int y) {
  }
  public void drawString(String s, float x, float y) {
  }
  public void drawString(String str, int x, int y) {
  }
  public void fill(Shape s) {
  }
  protected Color background;
  public Color getBackground() {
    return background;
  }
  public Composite getComposite() {
    return composite;
  }
  public GraphicsConfiguration getDeviceConfiguration() {
    return null;
  }
  public FontRenderContext getFontRenderContext() {
    if(Compatibility.IS_JAVA_6_OR_GREATER) {
      return getFontMetrics().getFontRenderContext();
    }
    return new FontRenderContext(getTransform(), getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING) != RenderingHints.VALUE_ANTIALIAS_OFF, getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS) != RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
  }
  public Paint getPaint() {
    return paint;
  }
  public Object getRenderingHint(Key hintKey) {
    return null;
  }
  public RenderingHints getRenderingHints() {
    return renderingHints;
  }
  public Stroke getStroke() {
    return stroke;
  }
  public AffineTransform getTransform() {
    return transform == null? new AffineTransform(): new AffineTransform(transform);
  }
  public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
    return false;
  }
  public void rotate(double theta, double x, double y) {
  }
  public void rotate(double theta) {
  }
  public void scale(double sx, double sy) {
  }
  public void setBackground(Color background) {
    this.background = background;
  }
  protected Composite composite;
  public void setComposite(Composite composite) {
    this.composite = composite;
  }
  protected Paint paint;
  public void setPaint(Paint paint) {
    this.paint = paint;
  }
  public void setRenderingHint(Key hintKey, Object hintValue) {
    renderingHints.put(hintKey, hintValue);
  }
  protected RenderingHints renderingHints = new RenderingHints(new HashMap());
  public void setRenderingHints(Map renderingHints) {
    this.renderingHints.putAll(renderingHints);
  }
  protected Stroke stroke;
  public void setStroke(Stroke stroke) {
    this.stroke = stroke;
  }
  protected AffineTransform transform;
  public void setTransform(AffineTransform transform) {
    this.transform = new AffineTransform(transform);
  }
  public void shear(double shx, double shy) {
  }
  public void transform(AffineTransform transform) {
    if(this.transform != null) {
      this.transform.concatenate(transform);
    } else {
      this.transform = new AffineTransform(transform);
    }
  }
  public void translate(double tx, double ty) {
  }
  public void translate(int x, int y) {
  }
  public void clearRect(int x, int y, int width, int height) {
  }
  public void clipRect(int x, int y, int width, int height) {
    Area area = new Area(clip);
    area.intersect(new Area(new Rectangle(x, y, width, height)));
    clip = area;
  }
  public void copyArea(int x, int y, int width, int height, int dx, int dy) {
  }
  public Graphics create() {
    try {
      return (Graphics)clone();
    } catch(Exception e) {
      return null;
    }
  }
  public void dispose() {
  }
  public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
  }
  public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
    return true;
  }
  public void drawLine(int x1, int y1, int x2, int y2) {
  }
  public void drawOval(int x, int y, int width, int height) {
  }
  public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
  }
  public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
  }
  public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
  }
  public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
  }
  public void fillOval(int x, int y, int width, int height) {
  }
  public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
  }
  public void fillRect(int x, int y, int width, int height) {
  }
  public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
  }
  public Shape getClip() {
    return clip;
  }
  public Rectangle getClipBounds() {
    return clip.getBounds();
  }
  public Color getColor() {
    return color;
  }
  protected Font font;
  public Font getFont() {
    return font;
  }
  public FontMetrics getFontMetrics() {
    return getFontMetrics(getFont());
  }
  public FontMetrics getFontMetrics(Font f) {
    return Toolkit.getDefaultToolkit().getFontMetrics(f);
  }
  public void setClip(int x, int y, int width, int height) {
    this.clip = new Rectangle(x, y, width, height);
  }
  protected Shape clip;
  public void setClip(Shape clip) {
    this.clip = clip;
  }
  protected Color color;
  public void setColor(Color color) {
    this.color = color;
  }
  public void setFont(Font font) {
    this.font = font;
  }
  public void setPaintMode() {
  }
  public void setXORMode(Color c1) {
  }
}
