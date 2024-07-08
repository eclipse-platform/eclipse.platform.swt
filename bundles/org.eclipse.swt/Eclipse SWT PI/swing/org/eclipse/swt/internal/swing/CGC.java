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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.Map;

public interface CGC {

  public static abstract class CGCGraphics2D implements CGC {
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
      getGraphics().copyArea(x, y, width, height, dx, dy);
    }
    public void dispose() {
      getGraphics().dispose();
    }
    public void draw(Shape s) {
      getGraphics().draw(s);
    }
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
      getGraphics().drawArc(x, y, width, height, startAngle, arcAngle);
    }
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
      return getGraphics().drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }
    public void drawLine(int x1, int y1, int x2, int y2) {
      getGraphics().drawLine(x1, y1, x2, y2);
    }
    public void drawOval(int x, int y, int width, int height) {
      getGraphics().drawOval(x, y, width, height);
    }
    public void drawPolygon(int[] points, int[] points2, int points3) {
      getGraphics().drawPolygon(points, points2, points3);
    }
    public void drawPolyline(int[] points, int[] points2, int points3) {
      getGraphics().drawPolyline(points, points2, points3);
    }
    public void drawRect(int x, int y, int width, int height) {
      getGraphics().drawRect(x, y, width, height);
    }
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
      getGraphics().drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }
    public void drawString(String str, int x, int y) {
      getGraphics().drawString(str, x, y);
    }
    public void fill(Shape s) {
      getGraphics().fill(s);
    }
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
      getGraphics().fillArc(x, y, width, height, startAngle, arcAngle);
    }
    public void fillOval(int x, int y, int width, int height) {
      getGraphics().fillOval(x, y, width, height);
    }
    public void fillPolygon(int[] points, int[] points2, int points3) {
      getGraphics().fillPolygon(points, points2, points3);
    }
    public void fillRect(int x, int y, int width, int height) {
      getGraphics().fillRect(x, y, width, height);
    }
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
      getGraphics().fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }
    public Color getBackground() {
      return getGraphics().getBackground();
    }
    public Color getColor() {
      return getGraphics().getColor();
    }
    public Composite getComposite() {
      return getGraphics().getComposite();
    }
    public Font getFont() {
      return getGraphics().getFont();
    }
    public FontMetrics getFontMetrics() {
      return getGraphics().getFontMetrics();
    }
    public FontRenderContext getFontRenderContext() {
      return getGraphics().getFontRenderContext();
    }
    public Paint getPaint() {
      return getGraphics().getPaint();
    }
    public Object getRenderingHint(Key hintKey) {
      return getGraphics().getRenderingHint(hintKey);
    }
    public RenderingHints getRenderingHints() {
      return getGraphics().getRenderingHints();
    }
    public Stroke getStroke() {
      return getGraphics().getStroke();
    }
    public AffineTransform getTransform() {
      return getGraphics().getTransform();
    }
    public void setBackground(Color background) {
      getGraphics().setBackground(background);
    }
    public void setColor(Color color) {
      getGraphics().setColor(color);
    }
    public void setComposite(Composite comp) {
      getGraphics().setComposite(comp);
    }
    public void setFont(Font font) {
      getGraphics().setFont(font);
    }
    public void setPaint(Paint paint) {
      getGraphics().setPaint(paint);
    }
    public void setPaintMode() {
      getGraphics().setPaintMode();
    }
    public void setRenderingHint(Key hintKey, Object hintValue) {
      getGraphics().setRenderingHint(hintKey, hintValue);
    }
    public void setRenderingHints(Map hints) {
      getGraphics().setRenderingHints(hints);
    }
    public void setStroke(Stroke s) {
      getGraphics().setStroke(s);
    }
    public void setTransform(AffineTransform tx) {
      getGraphics().setTransform(tx);
    }
    public void setXORMode(Color c1) {
      getGraphics().setXORMode(c1);
    }
    public void transform(AffineTransform tx) {
      getGraphics().transform(tx);
    }
    protected Shape userClip;
    protected Shape systemClip;
    public void setUserClip(Shape userClip) {
      if(this.userClip == null && userClip == null) {
        return;
      }
      this.userClip = userClip;
      Graphics2D g = getGraphics();
      if(systemClip == null) {
        systemClip = g.getClip();
      } else {
        g.setClip(systemClip);
      }
      if(userClip != null) {
        g.clip(userClip);
      }
    }
    public Shape getUserClip() {
      return userClip;
    }
  }
  
  public Graphics2D getGraphics();
  
  public void dispose();
  
  public Color getBackground();
  public void setBackground(Color background);
  
  public Color getColor();
  public void setColor(Color color);
  
  public Paint getPaint();
  public void setPaint(Paint paint);
  
  public Font getFont();
  public void setFont(Font font);
  
  public void fill(Shape s);
  
  public void fillOval(int x, int y, int width, int height);
  
  public void fillRect(int x, int y, int width, int height);
  
  public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);
  
  public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);
  
  public void drawRect(int x, int y, int width, int height);
  
  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer);
  
  public void drawLine(int x1, int y1, int x2, int y2);
  
  public void drawOval(int x, int y, int width, int height);
  
  public void draw(Shape s);
  
  public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints);
  public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints);
  
  public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints);
  
  public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);
  
  public FontMetrics getFontMetrics();
  public FontRenderContext getFontRenderContext();
  
  public void drawString(String str, int x, int y);
  
  public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);
  
  public Object getRenderingHint(RenderingHints.Key hintKey);
  public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue);
  
  public RenderingHints getRenderingHints();
  public void setRenderingHints(Map hints);
  
  public Composite getComposite();
  public void setComposite(Composite comp);
  
  public Stroke getStroke();
  public void setStroke(Stroke s);
  
  public void setXORMode(Color c1);
  public void setPaintMode();

  public void transform(AffineTransform Tx);
  public AffineTransform getTransform();
  public void setTransform(AffineTransform Tx);
  
  public void copyArea(int x, int y, int width, int height, int dx, int dy);
  
  public void setUserClip(Shape s);
  public Shape getUserClip();
  public Dimension getDeviceSize();
  
}
