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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;

class CScaleImplementation extends JSlider implements CScale {

  protected Scale handle;

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
  
  public CScaleImplementation(Scale scale, int style) {
    handle = scale;
    userAttributeHandler = new UserAttributeHandler(this);
    init(style);
  }

  protected void init(int style) {
    setMaximum(100);
    setMinorTickSpacing(1);
    setMajorTickSpacing(10);
    setPaintTicks(true);
    setSnapToTicks(true);
    setValue(0);
    setOrientation((style & SWT.HORIZONTAL) != 0? JSlider.HORIZONTAL: JSlider.VERTICAL);
    if((style & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    }
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return this;
  }

  public Dimension getPreferredSize() {
    if(!isPreferredSizeSet()) {
      return getOrientation() == JSlider.HORIZONTAL? new Dimension(170, super.getPreferredSize().height): new Dimension(super.getPreferredSize().width, 170);
    }
    return super.getPreferredSize();
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

public interface CScale extends CControl {

  public static class Factory {
    private Factory() {}

    public static CScale newInstance(Scale scale, int style) {
      return new CScaleImplementation(scale, style);
    }

  }

  public int getMinimum();

  public int getMaximum();

  public int getValue();

  public void setMinimum(int maximum);

  public void setMaximum(int maximum);

  public void setValue(int n);

  public void setMinorTickSpacing(int n);

  public void setMajorTickSpacing(int n);

  public int getMinorTickSpacing();

  public int getMajorTickSpacing();

}
