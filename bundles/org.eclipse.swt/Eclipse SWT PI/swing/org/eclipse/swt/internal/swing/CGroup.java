/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

class CGroupImplementation extends JPanel implements CGroup {

  protected TitledBorder titledBorder;
  protected Container contentPane;
  protected JScrollPane scrollPane;

  protected Group handle;

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
  
  public CGroupImplementation(Group group, int style) {
    super(new BorderLayout(0, 0));
    this.handle = group;
    userAttributeHandler = new UserAttributeHandler(this);
    init(style);
  }

  protected void init(int style) {
    // TODO: support styles
    titledBorder = new TitledBorder("") {
      public Font getTitleFont() {
        return CGroupImplementation.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getTitleFont();
      }
    };
    setBorder(titledBorder);
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new JScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.scrollPane = scrollPane;
      add(scrollPane, BorderLayout.CENTER);
      contentPane = scrollPane.getViewport();
    } else {
      contentPane = new JPanel(null);
      ((JComponent)contentPane).setOpaque(false);
      add(contentPane, BorderLayout.CENTER);
    }
    Utils.installMouseListener(contentPane, handle);
    Utils.installKeyListener(contentPane, handle);
    Utils.installFocusListener(contentPane, handle);
    Utils.installComponentListener(this, handle);
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Utils.paintTiledImage(this, g, backgroundImageIcon);
  }
  
  public Container getClientArea() {
    return contentPane;
  }

  public String getText () {
    return titledBorder.getTitle();
  }

  public void setText(String string) {
    titledBorder.setTitle(string);
    repaint();
  }

  public JScrollBar getVerticalScrollBar() {
    return scrollPane == null? null: scrollPane.getVerticalScrollBar();
  }

  public JScrollBar getHorizontalScrollBar() {
    return scrollPane == null? null: scrollPane.getHorizontalScrollBar();
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
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
//      ((JComponent)contentPane).setOpaque(true);
      if(scrollPane != null) {
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
      }
      break;
    case PREFERRED_BACKGROUND_INHERITANCE:
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
//      ((JComponent)contentPane).setOpaque(false);
      if(scrollPane != null) {
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
      }
      break;
    }
  }

}

/**
 * The group equivalent on the Swing side.
 * @version 1.0 2005.03.13
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CGroup extends CScrollable {

  public static class Factory {
    private Factory() {}

    public static CGroup newInstance(Group group, int style) {
      return new CGroupImplementation(group, style);
    }

  }

  public String getText();

  public void setText(String string);

}
