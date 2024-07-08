/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.PaintEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

class CCompositeImplementation extends JPanel implements CComposite {

  protected Composite handle;
  protected JPanel contentPane;
  protected JScrollPane scrollPane;

  public Container getSwingComponent() {
    return contentPane;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CCompositeImplementation(Composite composite, int style) {
    super(new BorderLayout(0, 0));
    this.handle = composite;
    init(style);
  }
  
  public boolean isFocusable() {
    return contentPane.isFocusable();
  }

  public void requestFocus() {
    contentPane.requestFocus();
  }

  public void setCursor(Cursor cursor) {
    contentPane.setCursor(cursor);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    } else {
      setBorder(null);
    }
    JPanel panel = new JPanel(null) {
      protected Graphics graphics;
      public Graphics getGraphics() {
        Graphics g;
        if(graphics != null) {
          g = graphics.create();
        } else {
          g = super.getGraphics();
        }
        return g;
      }
      public boolean isOptimizedDrawingEnabled() {
        return getComponentCount() < 2 || Utils.isFlatLayout(handle);
      }
      public boolean isOpaque() {
        return (CCompositeImplementation.this == null || backgroundImageIcon == null) && super.isOpaque();
      }
      protected void paintComponent (Graphics g) {
        graphics = g;
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, g);
        if(!(getParent() instanceof JViewport)) {
          Utils.paintTiledImage(this, g, backgroundImageIcon);
        }
        super.paintComponent(g);
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, null);
        graphics = null;
      }
      public Color getBackground() {
        return CCompositeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      public Color getForeground() {
        return CCompositeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      public Font getFont() {
        return CCompositeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      public Cursor getCursor() {
        if(Utils.globalCursor != null) {
          return Utils.globalCursor;
        }
        return CCompositeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
      }
      protected void processEvent(AWTEvent e) {
        if(Utils.redispatchEvent(getSWTHandle(), e)) {
          return;
        }
        super.processEvent(e);
      }
    };
    userAttributeHandler = new UserAttributeHandler(panel);
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new UnmanagedScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
        protected DisconnectedViewport createDisconnectedViewport() {
          return new DisconnectedViewport() {
            protected boolean isCreated = true;
            public boolean isOpaque() {
              return backgroundImageIcon == null && super.isOpaque();
            }
            protected void paintComponent(Graphics g) {
              Utils.paintTiledImage(this, g, backgroundImageIcon);
              super.paintComponent(g);
            }
            public Color getBackground() {
              return isCreated? userAttributeHandler.background: super.getBackground();
            }
          };
        }
      };
      this.scrollPane = scrollPane;
      scrollPane.setBorder(null);
      add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(panel);
    } else {
      add(panel, BorderLayout.CENTER);
    }
    contentPane = panel;
    contentPane.setFocusable (true);
    Utils.installMouseListener(contentPane, handle);
    Utils.installKeyListener(contentPane, handle);
    Utils.installFocusListener(contentPane, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return contentPane;
  }

  public JScrollBar getVerticalScrollBar() {
    return scrollPane == null? null: scrollPane.getVerticalScrollBar();
  }

  public JScrollBar getHorizontalScrollBar() {
    return scrollPane == null? null: scrollPane.getHorizontalScrollBar();
  }

  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case PREFERRED_BACKGROUND_INHERITANCE:
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      contentPane.setOpaque(true);
      if(scrollPane != null) {
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
      }
      break;
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      contentPane.setOpaque(false);
      if(scrollPane != null) {
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
      }
      break;
    }
  }

}

/**
 * The composite equivalent on the Swing side.
 * @version 1.0 2005.08.31
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CComposite extends CScrollable {

  public static class Factory {
    private Factory() {}

    public static CComposite newInstance(Composite composite, int style) {
      return new CCompositeImplementation(composite, style);
    }

  }

}
