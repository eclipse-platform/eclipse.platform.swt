/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class DisabledStatePanel extends JPanel {

  protected Control control;

  protected HierarchyListener hierarchyListener = new HierarchyListener() {
    public void hierarchyChanged(HierarchyEvent e) {
      long changedFlags = e.getChangeFlags();
      if((changedFlags & HierarchyEvent.HIERARCHY_CHANGED) != 0) {
        Component component = control.handle;
        Container parent = component.getParent();
        boolean isRevalidationNeeded = false;
        if(parent != null) {
          parent.add(component);
          checkFocus();
          isRevalidationNeeded = true;
        } else {
          parent = getParent();
          if(parent != null) {
            parent.remove(DisabledStatePanel.this);
            isRevalidationNeeded = true;
          }
        }
        if(isRevalidationNeeded) {
          if(parent instanceof JComponent) {
            ((JComponent)parent).revalidate();
          } else {
            parent.invalidate();
            parent.validate();
            parent.repaint();
          }
        }
      }
    }
  };

  protected ComponentListener componentListener = new ComponentAdapter() {
    public void componentResized(ComponentEvent e) {
      adjustBounds();
    }
    public void componentMoved(ComponentEvent e) {
      adjustBounds();
    }
  };

  public DisabledStatePanel(Control control) {
    super(null);
    this.control = control;
    enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    setOpaque(false);
    adjustBounds();
    Component component = control.handle;
    Container parent = component.getParent();
    if(parent != null) {
      if(parent instanceof RootPaneContainer) {
        ((RootPaneContainer)parent).getContentPane().add(this, 0);
      } else {
        parent.add(this, 0);
      }
      revalidate();
      checkFocus();
    }
    component.addHierarchyListener(hierarchyListener);
    component.addComponentListener(componentListener);
  }

  protected void checkFocus() {
    Component component = control.handle;
    Container parent = component.getParent();
    if(component.isFocusOwner()) {
      parent.transferFocus();
    } else if(component instanceof Container) {
      Window window = SwingUtilities.getWindowAncestor(parent);
      if(window != null) {
        Component focusOwner = window.getFocusOwner();
        if(focusOwner != null && ((Container)component).isAncestorOf(focusOwner)) {
          parent.transferFocus();
        }
      }
    }
  }

  protected void processEvent(AWTEvent e) {
    if(control.isDisposed()) {
      return;
    }
    Composite parent = control.getParent();
    if(e instanceof MouseEvent) {
      MouseEvent me = (MouseEvent)e;
      Point point = SwingUtilities.convertPoint(control.handle, me.getPoint(), parent.handle);
      if(e instanceof MouseWheelEvent) {
        MouseWheelEvent mwe = (MouseWheelEvent)e;
        parent.processEvent(new MouseWheelEvent(parent.handle, mwe.getID(), mwe.getWhen(), mwe.getModifiersEx(), point.x, point.y, mwe.getClickCount(), mwe.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(), mwe.getWheelRotation()));
      } else {
        parent.processEvent(new MouseEvent(parent.handle, me.getID(), me.getWhen(), me.getModifiersEx(), point.x, point.y, me.getClickCount(), me.isPopupTrigger(), me.getButton()));
      }
    } else if(e instanceof KeyEvent) {
      KeyEvent ke = (KeyEvent)e;
      parent.processEvent(new KeyEvent(parent.handle, ke.getID(), ke.getWhen(), ke.getModifiersEx(), ke.getKeyCode(), ke.getKeyChar(), ke.getKeyLocation()));
    }
  }

  protected void adjustBounds() {
    if(control != null && control.handle != null) {
      setBounds(control.handle.getBounds());
    }
  }

  public void release() {
    Component component = control.handle;
    Container parent = component.getParent();
    if(parent != null) {
      parent.remove(this);
      revalidate();
    }
    component.removeHierarchyListener(hierarchyListener);
    component.removeComponentListener(componentListener);
  }

}
