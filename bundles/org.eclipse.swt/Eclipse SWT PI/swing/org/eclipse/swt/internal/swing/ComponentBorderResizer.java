/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

public class ComponentBorderResizer {

  protected ComponentBorderResizer() {}
  
  public static void handle(final Component component, final JComponent borderedComponent) {
    MouseInputAdapter windowMouseInputAdapter = new MouseInputAdapter() {
      protected boolean moveTop;
      protected boolean moveLeft;
      protected boolean moveBottom;
      protected boolean moveRight;
      protected Point originalLocation;
      protected static final int MIN_WIDTH = 100;
      protected static final int MIN_HEIGHT = 60;
      public void mousePressed(MouseEvent e) {
        if(e.getButton() != MouseEvent.BUTTON1) {
          return;
        }
//        if(isMaximized()) {
//          return;
//        }
        originalLocation = e.getPoint();
      }
      public void mouseReleased(MouseEvent e) {
        if(e.getButton() != MouseEvent.BUTTON1) {
          return;
        }
        originalLocation = null;
      }
      public void mouseDragged(MouseEvent e) {
        if(originalLocation == null) {
          return;
        }
        Rectangle bounds = component.getBounds();
        if(moveLeft) {
          int diffX = e.getX() - originalLocation.x;
          bounds.x += diffX;
          bounds.width -= diffX;
          Insets borderInsets = borderedComponent.getBorder().getBorderInsets(borderedComponent);
          int minWidth = MIN_WIDTH + borderInsets.left + borderInsets.right;
          if(bounds.width < minWidth) {
            bounds.x -= minWidth - bounds.width;
            bounds.width = minWidth;
          }
          component.setBounds(bounds);
        } else if(moveRight) {
          int diffX = e.getX() - originalLocation.x;
          originalLocation.x += diffX;
          bounds.width += diffX;
          Insets borderInsets = borderedComponent.getBorder().getBorderInsets(borderedComponent);
          int minWidth = MIN_WIDTH + borderInsets.left + borderInsets.right;
          if(bounds.width < minWidth) {
            originalLocation.x += minWidth - bounds.width;
            bounds.width = minWidth;
          }
          component.setBounds(bounds);
        }
        if(moveTop) {
          int diffY = e.getY() - originalLocation.y;
          bounds.y += diffY;
          bounds.height -= diffY;
          Insets borderInsets = borderedComponent.getBorder().getBorderInsets(borderedComponent);
          int minHeight = MIN_HEIGHT + borderInsets.top + borderInsets.bottom;
          if(bounds.height < minHeight) {
            bounds.y -= minHeight - bounds.height;
            bounds.height = minHeight;
          }
          component.setBounds(bounds);
        } else if(moveBottom) {
          int diffY = e.getY() - originalLocation.y;
          originalLocation.y += diffY;
          bounds.height += diffY;
          Insets borderInsets = borderedComponent.getBorder().getBorderInsets(borderedComponent);
          int minHeight = MIN_HEIGHT + borderInsets.top + borderInsets.bottom;
          if(bounds.height < minHeight) {
            originalLocation.y += minHeight - bounds.height;
            bounds.height = minHeight;
          }
          component.setBounds(bounds);
        }
      }
      public void mouseMoved(MouseEvent e) {
        adjustResizing(e.getPoint());
      }
      public void mouseEntered(MouseEvent e) {
        adjustResizing(e.getPoint());
      }
      public void mouseExited(MouseEvent e) {
        adjustResizing(e.getPoint());
      }
      protected int THRESHOLD = 20;
      protected void adjustResizing(Point point) {
        if(originalLocation != null) {
          return;
        }
        moveTop = false;
        moveLeft = false;
        moveBottom = false;
        moveRight = false;
        Insets borderInsets = borderedComponent.getBorder().getBorderInsets(borderedComponent);
        Dimension size = borderedComponent.getSize();
        if(!new Rectangle(borderInsets.left, borderInsets.top, size.width - borderInsets.left - borderInsets.right, size.height - borderInsets.top - borderInsets.bottom).contains(point)) {
//        if(isMaximized()) {
//          rootPane.setCursor(null);
//          return;
//        }
          if(point.x < THRESHOLD) {
            moveLeft = true;
          } else if(point.x > size.width - THRESHOLD) {
            moveRight = true;
          }
          if(point.y < THRESHOLD) {
            moveTop = true;
          } else if(point.y >= size.height - borderInsets.bottom) {
            moveBottom = true;
          }
        }
        if(moveTop && moveLeft || moveBottom && moveRight) {
          borderedComponent.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
        } else if(moveTop && moveRight || moveBottom && moveLeft) {
          borderedComponent.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
        } else if(moveTop || moveBottom) {
          borderedComponent.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        } else if(moveLeft || moveRight) {
          borderedComponent.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        } else {
          borderedComponent.setCursor(null);
        }
      }
    };
    borderedComponent.addMouseListener(windowMouseInputAdapter);
    borderedComponent.addMouseMotionListener(windowMouseInputAdapter);
  }
  
}
