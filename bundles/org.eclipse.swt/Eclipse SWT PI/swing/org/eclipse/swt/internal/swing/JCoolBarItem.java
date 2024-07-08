/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.basic.BasicToolBarUI;

public class JCoolBarItem extends JToolBar {

  protected int xSpacing;

  protected int getXSpacing() {
    return xSpacing;
  }

  protected void setXSpacing(int xSpacing) {
    this.xSpacing = xSpacing;
  }

  protected JCoolBar getCoolBar() {
    return (JCoolBar)super.getParent();
  }

  protected Point mouseLocation;

  public ComponentOrientation getComponentOrientation() {
    Component parent = getParent();
    if(parent != null) {
      return parent.getComponentOrientation();
    }
    return super.getComponentOrientation();
  }

  public JCoolBarItem() {
    // Remove all default mouse listeners to avoid the toolbar from being dragged out
    MouseListener[] listeners = getMouseListeners();
    for(int i=0; i<listeners.length; i++) {
      removeMouseListener(listeners[i]);
    }
    MouseMotionListener[] motionListeners = getMouseMotionListeners();
    for(int i=0; i<motionListeners.length; i++) {
      removeMouseMotionListener(motionListeners[i]);
    }
    // Add custom listeners
    addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        isInGrip = isInGrip(e.getPoint());
      }
      public void mouseExited(MouseEvent e) {
        isInGrip = false;
      }
      public void mousePressed(MouseEvent e) {
        if(!isLocked() && isInGrip && (e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
          isDragging = true;
          if(!isFloating) {
            getCoolBar().processItemMouseEvent(e);
          } else {
            mouseLocation = e.getPoint();
          }
        }
      }
      public void mouseReleased(MouseEvent e) {
        if(isDragging && (e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
          if(!isFloating) {
            getCoolBar().processItemMouseEvent(e);
          } else {
            mouseLocation = null;
          }
          isDragging = false;
        }
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        if(isDragging) {
          if(!isFloating) {
            getCoolBar().processItemMouseEvent(e);
          } else {
            Point mouseLocation = e.getPoint();
            int offsetX = JCoolBarItem.this.mouseLocation.x - mouseLocation.x;
            int offsetY = JCoolBarItem.this.mouseLocation.y - mouseLocation.y;
            Window window = SwingUtilities.getWindowAncestor(JCoolBarItem.this);
            Point location = window.getLocation();
            window.setLocation(location.x - offsetX, location.y - offsetY);
          }
        }
      }
      public void mouseMoved(MouseEvent e) {
        isInGrip = isInGrip(e.getPoint());
      }
    });
  }

  protected boolean isInGrip;
  protected boolean isDragging;

  public Cursor getCursor() {
    if(!isLocked() && (isDragging || isInGrip || getCoolBar().isDragging())) {
      return JCoolBar.GRIP_CURSOR;
    }
    return super.getCursor();
  }

  private int getGripWidth() {
    return getInsets().left - 2;
  }

  /**
   * Indicate if the specified point is located in the grip of the toolbar.
   * @param point The point to consider, in toolbar coordinates.
   * @return True if the point is in the grip.
   */
  private boolean isInGrip(Point point) {
    Rectangle gripRect = new Rectangle();
    int gripWidth = getGripWidth();
    int x = getComponentOrientation().isLeftToRight() ? 0 : getWidth() - gripWidth;
    gripRect.setBounds(x, 0, gripWidth, getHeight());
    return gripRect.contains(point);
  }

  public void setLocked(boolean isLocked) {
    setFloatable(!isLocked);
  }

  public boolean isLocked() {
    return !isFloatable();
  }

  protected boolean isWrapped;

  public void setWrapped(boolean isWrapped) {
    this.isWrapped = isWrapped;
  }

  public boolean isWrapped() {
    return isWrapped;
  }

//  public void addSeparator() {
//    
//  }

  public Dimension getMinimumSize() {
    Dimension size = super.getMinimumSize();
    if(!isLocked()) {
      // TODO: check why this is needed for Eclipse!
      size.width += getGripWidth() + 2;
    }
    return size;
  }

  protected boolean isFloating;

  protected void setFloating(boolean isFloating, Point location) {
    if(isFloating == this.isFloating) {
      return;
    }
    if(getCoolBar() == null) {
      if(!isFloating) {
        this.isFloating = false;
      }
      return;
    }
    ToolBarUI toolBarUI = getUI();
    if(toolBarUI instanceof BasicToolBarUI) {
      this.isFloating = isFloating;
      if(isFloating) {
        if(isWrapped()) {
          setWrapped(false);
          JCoolBar coolBar = getCoolBar();
          int index = coolBar.getItemIndex(this);
          if(index + 1 < coolBar.getComponentCount()) {
            coolBar.getItem(index + 1).setWrapped(true);
          }
        }
      }
      BasicToolBarUI basicToolBarUI = (BasicToolBarUI)toolBarUI;
      basicToolBarUI.setFloating(isFloating, null);
      if(isFloating) {
        Window window = SwingUtilities.getWindowAncestor(JCoolBarItem.this);
        Point offsetPoint = new Point(0, 0);
        if(xSpacing < 0) {
          offsetPoint.x = -xSpacing;
        }
        offsetPoint = SwingUtilities.convertPoint(this, offsetPoint, window);
        System.err.println(location.x + ", " + offsetPoint.x);
        window.setLocation(location.x - offsetPoint.x, location.y - offsetPoint.y);
        setXSpacing(0);
      }
    }
  }
  
  public Dimension getItemPreferredSize() {
    return getPreferredSize();
  }
  
}
