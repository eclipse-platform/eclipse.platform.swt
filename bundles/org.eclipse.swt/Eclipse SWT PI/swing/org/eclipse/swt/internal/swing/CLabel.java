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
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

class CSeparator extends JPanel implements CLabel {

  protected Label handle;

  protected JSeparator separator;

  public Container getSwingComponent() {
    return separator;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }

  public CSeparator(Label label, int style) {
    this.handle = label;
    userAttributeHandler = new UserAttributeHandler(this);
    GridBagLayout gridBag = new GridBagLayout();
    setLayout(gridBag);
    GridBagConstraints c = new GridBagConstraints();
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.fill = (style & SWT.HORIZONTAL) != 0? GridBagConstraints.HORIZONTAL: GridBagConstraints.VERTICAL;
    separator = new JSeparator((style & SWT.HORIZONTAL) != 0? JSeparator.HORIZONTAL: JSeparator.VERTICAL);
    gridBag.setConstraints(separator, c);
    add(separator);
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    }
    Utils.installMouseListener(separator, handle);
    Utils.installKeyListener(separator, handle);
    Utils.installFocusListener(separator, handle);
    Utils.installComponentListener(this, handle);
  }

  public boolean isOpaque() {
    return backgroundImageIcon == null && super.isOpaque();
  }
  protected void paintComponent(Graphics g) {
    Utils.paintTiledImage(this, g, backgroundImageIcon);
    super.paintComponent(g);
  }

  public Container getClientArea() {
    return separator;
  }
  
  public int getPreferredWidth() {
    return getPreferredSize().width;
  }

  public void setText(String text, int mnemonicIndex) {
  }

  public void setAlignment(int alignment) {
  }

  public void setIcon(Icon icon) {
  }

  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      separator.setOpaque(true);
      break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      separator.setOpaque(false);
      break;
    }
  }

}

class CLabelImplementation extends JMultiLineLabel implements CLabel {

  protected Label handle;

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

  public CLabelImplementation(Label label, int style) {
    this.handle = label;
    LookAndFeelUtils.applyLabelStyle(this);
    userAttributeHandler = new UserAttributeHandler(this) {
      public void setForeground(Color foreground) {
        super.setForeground(foreground);
        adjustStyles();
      }
      public void setFont(Font font) {
        super.setFont(font);
        adjustStyles();
      }
    };
    init(style);
  }

  protected void init(int style) {
    setFocusable(false);
    setWrapping((style & SWT.WRAP) != 0);
    if((style & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    }
    if((style & SWT.RIGHT) != 0) {
      setAlignment(JMultiLineLabel.RIGHT);
    } else if((style & SWT.CENTER) != 0) {
      setAlignment(JMultiLineLabel.CENTER);
    } else  {
      setAlignment(JMultiLineLabel.LEFT);
    }
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
  }

  public boolean isOpaque() {
    return backgroundImageIcon == null && (userAttributeHandler != null && userAttributeHandler.background != null || super.isOpaque());
  }
  protected void paintComponent(Graphics g) {
    Utils.paintTiledImage(this, g, backgroundImageIcon);
    super.paintComponent(g);
  }
  
  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
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
  
  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
//    switch(backgroundInheritanceType) {
//    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
//    case PREFERRED_BACKGROUND_INHERITANCE:
//    case BACKGROUND_INHERITANCE: setOpaque(false); break;
//    }
  }

}

/**
 * The label equivalent on the Swing side.
 * @version 1.0 2005.08.20
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CLabel extends CControl {

  public static class Factory {
    private Factory() {}

    public static CLabel newInstance(Label label, int style) {
      if((style & SWT.SEPARATOR) != 0) {
        return new CSeparator(label, style);
      }
      return new CLabelImplementation(label, style);
    }

  }

  public int getPreferredWidth();
  
  public void setText(String text, int mnemonicIndex);

  public void setAlignment(int alignment);

  public void setIcon(Icon icon);

}
