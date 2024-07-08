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
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;

class CTabFolderImplementation extends JTabbedPane implements CTabFolder {

  protected TabFolder handle;

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
  
  public CTabFolderImplementation(TabFolder tabFolder, int style) {
    this.handle = tabFolder;
    userAttributeHandler = new UserAttributeHandler(this);
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    } else {
      setBorder(null);
    }
    setTabPlacement((style & SWT.BOTTOM) != 0? BOTTOM: TOP);
    setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
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
  
  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case PREFERRED_BACKGROUND_INHERITANCE:
    case NO_BACKGROUND_INHERITANCE: {
      Object isContentOpaqueObject = UIManager.get("TabbedPane.contentOpaque");
      UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
      updateUI();
      UIManager.put("TabbedPane.contentOpaque", isContentOpaqueObject);
      setOpaque(true);
      break;
    }
    case BACKGROUND_INHERITANCE: {
      Object isContentOpaqueObject = UIManager.get("TabbedPane.contentOpaque");
      UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
      updateUI();
      UIManager.put("TabbedPane.contentOpaque", isContentOpaqueObject);
      setOpaque(false);
      break;
    }
    }
  }
  
  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    int count = getTabCount();
    if(count > 0) {
      Rectangle bounds = getUI().getTabBounds(this, count - 1);
      size.width = Math.max(size.width, bounds.x + bounds.width + 10);
    }
    return size;
  }

}

public interface CTabFolder extends CScrollable {

  public static class Factory {
    private Factory() {}

    public static CTabFolder newInstance(TabFolder tabFolder, int style) {
      return new CTabFolderImplementation(tabFolder, style);
    }

  }

  public void setTitleAt(int index, String title);

  public void setMnemonicAt(int tabIndex, int mnemonic);

  public void setIconAt(int index, Icon icon);

  public int getSelectedIndex();

  public void setSelectedIndex(int index);

  public void setToolTipTextAt(int index, String toolTipText);

}
