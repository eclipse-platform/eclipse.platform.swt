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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;
import javax.swing.plaf.basic.BasicArrowButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

class CButtonArrow extends ArrowButton implements CButton {

  protected Button handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected static int getDirection(int style) {
    int direction = 0;
    if((style & SWT.UP) != 0) {
      direction = BasicArrowButton.NORTH;
    } else if((style & SWT.DOWN) != 0) {
      direction = BasicArrowButton.SOUTH;
    } else if((style & SWT.LEFT) != 0) {
      direction = BasicArrowButton.WEST;
    } else if((style & SWT.RIGHT) != 0) {
      direction = BasicArrowButton.EAST;
    }
    return direction;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CButtonArrow(Button button, int style) {
    super(getDirection(style));
    this.handle = button;
    userAttributeHandler = new UserAttributeHandler(this);
//    setMargin(new Insets(1, 1, 1, 1));
    init(style);
  }
  
  protected void init(int style) {
    setHorizontalAlignment((style & SWT.TRAIL) != 0? AbstractButton.TRAILING: (style & SWT.CENTER) != 0? AbstractButton.CENTER: AbstractButton.LEADING);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    size.width = size.height;
    return size;
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setDirection(alignment);
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
    super.reshape(x, y, h, h);
  }

}

class CButtonPush extends JButton implements CButton {

  protected Button handle;

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
  
  public CButtonPush(Button button, int style) {
    this.handle = button;
    userAttributeHandler = new UserAttributeHandler(this);
    setMargin(new Insets(2, 4, 2, 4));
    init(style);
  }
  
  protected void init(int style) {
    setHorizontalAlignment((style & SWT.TRAIL) != 0? AbstractButton.TRAILING: (style & SWT.CENTER) != 0? AbstractButton.CENTER: AbstractButton.LEADING);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    });
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

class CButtonCheck extends JCheckBox implements CButton {

  protected Button handle;

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
  
  public CButtonCheck(Button button, int style) {
    this.handle = button;
    userAttributeHandler = new UserAttributeHandler(this);
    setMargin(new Insets(0, 0, 0, 0));
    init(style);
  }
  
  protected void init(int style) {
    setOpaque(false);
    setHorizontalAlignment((style & SWT.TRAIL) != 0? AbstractButton.TRAILING: (style & SWT.CENTER) != 0? AbstractButton.CENTER: AbstractButton.LEADING);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return this;
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
  }
  
  public boolean isOpaque() {
    return userAttributeHandler != null && userAttributeHandler.background != null || super.isOpaque();
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
//    switch(backgroundInheritanceType) {
//    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
//    case PREFERRED_BACKGROUND_INHERITANCE:
//    case BACKGROUND_INHERITANCE: setOpaque(false); break;
//    }
  }

}

class CButtonToggle extends JToggleButton implements CButton {

  protected Button handle;

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
  
  public CButtonToggle(Button button, int style) {
    this.handle = button;
    userAttributeHandler = new UserAttributeHandler(this);
    setMargin(new Insets(2, 4, 2, 4));
    init(style);
  }
  
  protected void init(int style) {
    setHorizontalAlignment((style & SWT.TRAIL) != 0? AbstractButton.TRAILING: (style & SWT.CENTER) != 0? AbstractButton.CENTER: AbstractButton.LEADING);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        handle.processEvent(e);
      }
    });
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

class CButtonRadio extends JIconRadioButton implements CButton {

  protected Button handle;

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
  
  public CButtonRadio(Button button, int style) {
    this.handle = button;
    userAttributeHandler = new UserAttributeHandler(this);
    setMargin(new Insets(0, 0, 0, 0));
    init(style);
  }

  protected void init(int style) {
    setHorizontalAlignment((style & SWT.TRAIL) != 0? AbstractButton.TRAILING: (style & SWT.CENTER) != 0? AbstractButton.CENTER: AbstractButton.LEADING);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handle.processEvent(e);
      }
    });
  }

  public Container getClientArea() {
    return this;
  }
  
  public void requestFocus() {
    super.requestFocus();
    if(!isSelected()) {
      ButtonModel model = getModel();
      if(!model.isPressed()) {
        doClick();
      }
    }
  }

  public void setAlignment(int alignment) {
    setHorizontalAlignment(alignment);
  }
  
  public boolean isOpaque() {
    return userAttributeHandler != null && userAttributeHandler.background != null || super.isOpaque();
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
//    switch(backgroundInheritanceType) {
//    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
//    case PREFERRED_BACKGROUND_INHERITANCE:
//    case BACKGROUND_INHERITANCE: setOpaque(false); break;
//    }
  }

}

/**
 * The button equivalent on the Swing side.
 * @version 1.0 2005.03.14
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CButton extends CControl {

  public static class Factory {
    private Factory() {}

    public static CButton newInstance(Button button, int style) {
      if((style & SWT.ARROW) != 0) {
        return new CButtonArrow(button, style);
      }
      if((style & SWT.PUSH) != 0) {
        return new CButtonPush(button, style);
      }
      if((style & (SWT.CHECK)) != 0) {
        return new CButtonCheck(button, style);
      }
      if((style & (SWT.TOGGLE)) != 0) {
        return new CButtonToggle(button, style);
      }
      if((style & (SWT.RADIO)) != 0) {
        return new CButtonRadio(button, style);
      }
      return null;
    }

  }

  public String getText();

  public boolean isSelected();

  public void setSelected(boolean isSelected);

  public void setAlignment(int alignment);

  public void setIcon(Icon icon);

  public void setText(String text);

  public void doClick();

  public void setDisplayedMnemonicIndex(int mnemonicIndex);

  public void setMnemonic(char mnemonic);

}
