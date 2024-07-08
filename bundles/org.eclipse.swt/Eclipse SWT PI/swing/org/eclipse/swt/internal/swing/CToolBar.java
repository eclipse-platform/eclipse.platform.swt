/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JScrollBar;
import javax.swing.JToolBar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;

class CToolBarImplementation extends JToolBar implements CToolBar {

  protected ToolBar handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CToolBarImplementation(ToolBar toolBar, int style) {
    super((style & SWT.VERTICAL) != 0? JToolBar.VERTICAL: JToolBar.HORIZONTAL);
    this.handle = toolBar;
    userAttributeHandler = new UserAttributeHandler(this);
    init(style);
  }

  protected void init(int style) {
    setFloatable(false);
    if((style & SWT.BORDER) != 0) {
      setBorder(javax.swing.BorderFactory.createEtchedBorder());
    }
    if((style & SWT.WRAP) != 0) {
      setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
//    } else {
//      setLayout(null);
    }
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return this;
  }

  public JScrollBar getHorizontalScrollBar() {
    return null;
  }

  public JScrollBar getVerticalScrollBar() {
    return null;
  }

  public Color getBackground() {
    return userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
  }
  public Color getForeground() {
    return userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
  }
  public Font getFont() {
    return userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
  }
  public Cursor getCursor() {
    if(Utils.globalCursor != null) {
      return Utils.globalCursor;
    }
    return userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
  }
  
  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE: setOpaque(false); break;
    }
  }

  public void reshape(int x, int y, int w, int h) {
    super.reshape(x, y, w, getComponentCount() == 0? h: getPreferredSize().height);
  }

}

public interface CToolBar extends CScrollable {

  public static class Factory {
    private Factory() {}

    public static CToolBar newInstance(ToolBar toolBar, int style) {
      return new CToolBarImplementation(toolBar, style);
    }

  }

}
