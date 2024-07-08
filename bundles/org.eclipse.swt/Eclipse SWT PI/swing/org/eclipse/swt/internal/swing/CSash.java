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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Sash;

class CSashImplementation extends JPanel implements CSash {

  protected Sash handle;

  protected BasicSplitPaneDivider divider;

  public Container getSwingComponent() {
    return divider;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CSashImplementation(Sash sash, int style) {
    super(new BorderLayout(0, 0));
    this.handle = sash;
    userAttributeHandler = new UserAttributeHandler(this);
    JSplitPane splitPane = new JSplitPane((style & SWT.HORIZONTAL) != 0? JSplitPane.VERTICAL_SPLIT: JSplitPane.HORIZONTAL_SPLIT);
    divider = ((BasicSplitPaneUI)splitPane.getUI()).getDivider();
    if((handle.getStyle() & SWT.SMOOTH) == 0) {
      MouseInputListener mouseInputListener = new MouseInputAdapter() {
        protected Window splitterWindow;
        public void mousePressed(MouseEvent e) {
          if((e.getButton() & MouseEvent.BUTTON1) == 0) {
            return;
          }
          dragLocation = (handle.getStyle() & SWT.VERTICAL) != 0? getLocation().x: getLocation().y;
          splitterWindow = new Window(SwingUtilities.getWindowAncestor(CSashImplementation.this)) {
            public void update(Graphics g) {
              paint(g);
            }
            public void paint(Graphics g) {
              super.paint(g);
              Dimension size = getSize();
              Color background = getBackground();
              for(int i=0; i<size.width; i++) {
                for(int j=0; j<size.height; j++) {
                  if((i + j) % 2 == 0) {
                    g.setColor(Color.BLACK);
                    g.drawLine(i, j, i, j);
                  } else {
                    g.setColor(background);
                    g.drawLine(i, j, i, j);
                  }
                }
              }
            }
          };
          splitterWindow.setCursor(divider.getCursor());
          adjustWindowBounds();
          splitterWindow.setVisible(true);
        }
        public void mouseReleased(MouseEvent e) {
          if((e.getButton() & MouseEvent.BUTTON1) == 0) {
            return;
          }
          splitterWindow.dispose();
          splitterWindow = null;
        }
        public void mouseDragged(MouseEvent e) {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              adjustWindowBounds();
            }
          });
        }
        protected void adjustWindowBounds() {
          if(splitterWindow == null) {
            return;
          }
          Dimension size = divider.getSize();
          Point location = divider.getLocation();
          SwingUtilities.convertPointToScreen(location, divider.getParent());
          Point screenDragLocation = new Point(dragLocation, dragLocation);
          SwingUtilities.convertPointToScreen(screenDragLocation, CSashImplementation.this.getParent());
          if((handle.getStyle() & SWT.VERTICAL) != 0) {
            location.x = screenDragLocation.x;
          } else {
            location.y = screenDragLocation.y;
          }
          splitterWindow.setBounds(location.x, location.y, size.width, size.height);
        }
      };
      divider.addMouseListener(mouseInputListener);
      divider.addMouseMotionListener(mouseInputListener);
    }
    add(divider, BorderLayout.CENTER);
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      divider.setBorder(LookAndFeelUtils.getStandardBorder());
    }
    Utils.installMouseListener(divider, handle);
    Utils.installKeyListener(divider, handle);
    Utils.installFocusListener(divider, handle);
    Utils.installComponentListener(this, handle);
  }

  public Container getClientArea() {
    return divider;
  }

  protected int dragLocation = Integer.MIN_VALUE;

  public void setDragLocation(int dragLocation) {
    this.dragLocation = dragLocation;
    repaint();
  }

  public void setBackgroundImage(Image backgroundImage) {
    // TODO: implement
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
    // TODO: implement
  }

}

public interface CSash extends CControl {

  public static class Factory {
    private Factory() {}

    public static CSash newInstance(Sash sash, int style) {
      return new CSashImplementation(sash, style);
    }

  }

  public void setDragLocation(int dragLocation);

}
