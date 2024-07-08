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

import javax.swing.JProgressBar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ProgressBar;

class CProgressBarImplementation extends JProgressBar implements CProgressBar {

  protected ProgressBar handle;

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
  
  public CProgressBarImplementation(ProgressBar progressBar, int style) {
    handle = progressBar;
    userAttributeHandler = new UserAttributeHandler(this);
    init(style);
  }

  protected void init(int style) {
    setOrientation((style & SWT.HORIZONTAL) != 0? HORIZONTAL: VERTICAL);
    setIndeterminate((style & SWT.INDETERMINATE) != 0);
    // TODO: find if we have some style for non SMOOTH scrollbars.
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return this;
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

}

public interface CProgressBar extends CControl {

  public static class Factory {
    private Factory() {}

    public static CProgressBar newInstance(ProgressBar progressBar, int style) {
      return new CProgressBarImplementation(progressBar, style);
    }

  }

  public int getMinimum();

  public int getMaximum();

  public int getValue();

  public void setMinimum(int maximum);

  public void setMaximum(int maximum);

  public void setValue(int n);

}
