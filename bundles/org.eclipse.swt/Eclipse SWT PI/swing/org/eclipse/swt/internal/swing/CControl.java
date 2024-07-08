/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import org.eclipse.swt.widgets.Control;

/**
 * The interface shared by all controls.
 * @version 1.0 2005.03.13
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CControl {

  public static final int NO_BACKGROUND_INHERITANCE = 0;
  public static final int PREFERRED_BACKGROUND_INHERITANCE = 1;
  public static final int BACKGROUND_INHERITANCE = 2;

  public Rectangle getBounds();

  public Point getLocation();

  public Dimension getSize();

  public Dimension getMinimumSize();

  public Container getClientArea();

  // TODO: implement so that it traverses the complete hierarchy
  public String getToolTipText();

  // TODO: implement so that it traverses the complete hierarchy
  public void setToolTipText(String string);

  public void setBackgroundImage(Image backgroundImage);

  public void setBackgroundInheritance(int backgroundInheritanceType);

  public Container getSwingComponent();

  public Control getSWTHandle();

  public static class UserAttributeHandler {
    protected Component component;
    public Color background;
    public Color foreground;
    public Font font;
    public Cursor cursor;
    public UserAttributeHandler(Component component) {
      this.component = component;
    }
    public void setBackground(Color background) {
      this.background = background;
    }

    public Color getBackground() {
      return component.getBackground();
    }

    public void setForeground(Color foreground) {
      this.foreground = foreground;
    }
    
    public Color getForeground() {
      return component.getForeground();
    }
    
    public void setFont(Font font) {
      this.font = font;
    }
    
    public Font getFont() {
      return component.getFont();
    }
    
    public void setCursor(Cursor cursor) {
      this.cursor = cursor;
    }
    
    public Cursor getCursor() {
      return component.getCursor();
    }
    
  }
  
  public UserAttributeHandler getUserAttributeHandler();
  
}
