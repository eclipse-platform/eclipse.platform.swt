/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.swing.*;

/**
 * Instances of this class represent icons that can be placed on the
 * system tray or task bar status area.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, MenuDetect, Selection</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @since 3.0
 */
public class TrayItem extends Item {
  Tray parent;
  ToolTip toolTip;
  boolean visible = true;
  TrayIcon trayIcon;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tray</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TrayItem (Tray parent, int style) {
  super (parent, style);
  this.parent = parent;
  parent.createItem (this, parent.getItemCount ());
  createWidget();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the receiver is selected
 * <code>widgetDefaultSelected</code> is called when the receiver is double-clicked
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
  checkWidget ();
  if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
  TypedListener typedListener = new TypedListener (listener);
  addListener (SWT.Selection,typedListener);
  addListener (SWT.DefaultSelection,typedListener);
}

@Override
protected void checkSubclass () {
  if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createWidget () {
  trayIcon = new TrayIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
//  trayIcon.setImageAutoSize(true);
  trayIcon.addMouseListener(new MouseAdapter() {
    protected boolean processMenuEvent(MouseEvent e) {
      switch(e.getClickCount()) {
      case 1:
        if(e.isPopupTrigger()) {
          if (hooks (SWT.MenuDetect)) {
            UIThreadUtils.startExclusiveSection(display);
            if(isDisposed()) {
              UIThreadUtils.stopExclusiveSection();
              return true;
            }
            sendEvent (SWT.MenuDetect);
            UIThreadUtils.stopExclusiveSection();
            return true;
          }
        }
      }
      return false;
    }
    @Override
	public void mousePressed(MouseEvent e) {
      if(processMenuEvent(e)) return;
    }
    @Override
	public void mouseReleased(MouseEvent e) {
      if(processMenuEvent(e)) return;
      // We can't use action listener because it does not make a difference between single click and double click...
      switch(e.getClickCount()) {
      case 1:
        if (hooks (SWT.Selection)) {
          UIThreadUtils.startExclusiveSection(display);
          if(isDisposed()) {
            UIThreadUtils.stopExclusiveSection();
            return;
          }
          try {
            postEvent (SWT.Selection);
          } catch(Throwable t) {
            UIThreadUtils.storeException(t);
          } finally {
            UIThreadUtils.stopExclusiveSection();
          }
        }
        break;
      case 2:
        if (hooks (SWT.DefaultSelection)) {
          UIThreadUtils.startExclusiveSection(display);
          if(isDisposed()) {
            UIThreadUtils.stopExclusiveSection();
            return;
          }
          postEvent (SWT.DefaultSelection);
          UIThreadUtils.stopExclusiveSection();
        }
        break;
      }
    }
  });
  try {
    SystemTray.getSystemTray().add(trayIcon);
  } catch(Exception e) {
    e.printStackTrace();
  }
}

@Override
void destroyWidget () {
  parent.destroyItem (this);
  releaseHandle ();
}

/**
 * Returns the receiver's parent, which must be a <code>Tray</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public Tray getParent () {
  checkWidget ();
  return parent;
}

/**
 * Returns the receiver's tool tip, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public ToolTip getToolTip () {
  checkWidget ();
  return toolTip;
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
  checkWidget ();
  return trayIcon.getToolTip();
}

/**
 * Returns <code>true</code> if the receiver is visible and
 * <code>false</code> otherwise.
 *
 * @return the receiver's visibility
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
  checkWidget ();
  return visible;
}

//int messageProc (int hwnd, int msg, int wParam, int lParam) {
//  /*
//  * Feature in Windows.  When the user clicks on the tray
//  * icon, another application may be the foreground window.
//  * This means that the event loop is not running and can
//  * cause problems.  For example, if a menu is shown, when
//  * the user clicks outside of the menu to cancel it, the
//  * menu is not hidden until an event is processed.  If
//  * another application is the foreground window, then the
//  * menu is not hidden.  The fix is to force the tray icon
//  * message window to the foreground when sending an event.
//  */
//  switch (lParam) {
//    case OS.WM_LBUTTONDOWN:
//      if (hooks (SWT.Selection)) {
//        OS.SetForegroundWindow (hwnd);
//        postEvent (SWT.Selection);
//      }
//      break;
//    case OS.WM_LBUTTONDBLCLK:
//    case OS.WM_RBUTTONDBLCLK:
//      if (hooks (SWT.DefaultSelection)) {
//        OS.SetForegroundWindow (hwnd);
//        postEvent (SWT.DefaultSelection);
//      }
//      break;
//    case OS.WM_RBUTTONUP: {
//      if (hooks (SWT.MenuDetect)) {
//        OS.SetForegroundWindow (hwnd);
//        sendEvent (SWT.MenuDetect);
//        // widget could be disposed at this point
//        if (isDisposed()) return 0;
//      }
//      break;
//    }
//    case OS.NIN_BALLOONSHOW:
//      if (toolTip != null && !toolTip.visible) {
//        toolTip.visible = true;
//        if (toolTip.hooks (SWT.Show)) {
//          OS.SetForegroundWindow (hwnd);
//          toolTip.sendEvent (SWT.Show);
//          // widget could be disposed at this point
//          if (isDisposed()) return 0;
//        }
//      }
//      break;
//    case OS.NIN_BALLOONHIDE:
//    case OS.NIN_BALLOONTIMEOUT:
//    case OS.NIN_BALLOONUSERCLICK:
//      if (toolTip != null) {
//        if (toolTip.visible) {
//          toolTip.visible = false;
//          if (toolTip.hooks (SWT.Hide)) {
//            OS.SetForegroundWindow (hwnd);
//            toolTip.sendEvent (SWT.Hide);
//            // widget could be disposed at this point
//            if (isDisposed()) return 0;
//          }
//        }
//        if (lParam == OS.NIN_BALLOONUSERCLICK) {
//          if (toolTip.hooks (SWT.Selection)) {
//            OS.SetForegroundWindow (hwnd);
//            toolTip.postEvent (SWT.Selection);
//            // widget could be disposed at this point
//            if (isDisposed()) return 0;
//          }
//        }
//      }
//      break;
//  }
//  display.wakeThread ();
//  return 0;
//}
//
//void recreate () {
//  createWidget ();
//  if (!visible) setVisible (false);
//  if (text.length () != 0) setText (text);
//  if (image != null) setImage (image);
//  if (toolTipText != null) setToolTipText (toolTipText);
//}

@Override
void releaseHandle () {
  super.releaseHandle ();
  parent = null;
}

@Override
void releaseWidget () {
  super.releaseWidget ();
  if (toolTip != null) toolTip.item = null;
  toolTip = null;
  if(visible) {
    SystemTray.getSystemTray().remove(trayIcon);
  }
  // TODO: send hide event?
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver is selected.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
  checkWidget ();
  if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
  if (eventTable == null) return;
  eventTable.unhook (SWT.Selection, listener);
  eventTable.unhook (SWT.DefaultSelection,listener);
}

/**
 * Sets the receiver's image.
 *
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setImage (Image image) {
  checkWidget ();
  if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
  super.setImage (image);
  trayIcon.setImage(image.handle);
}

/**
 * Sets the receiver's tool tip to the argument, which
 * may be null indicating that no tool tip should be shown.
 *
 * @param toolTip the new tool tip (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setToolTip (ToolTip toolTip) {
  checkWidget ();
  ToolTip oldTip = this.toolTip, newTip = toolTip;
  if (oldTip != null) oldTip.item = null;
  this.toolTip = newTip;
  if (newTip != null) newTip.item = this;
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param value the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String value) {
  checkWidget ();
  trayIcon.setToolTip(value);
}

/**
 * Makes the receiver visible if the argument is <code>true</code>,
 * and makes it invisible otherwise.
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
  checkWidget ();
  if(this.visible == visible) return;
  this.visible = visible;
  if(visible) {
    try {
      SystemTray.getSystemTray().add(trayIcon);
      sendEvent (SWT.Show);
    } catch(Exception e) {
      e.printStackTrace();
    }
  } else {
    SystemTray.getSystemTray().remove(trayIcon);
    sendEvent (SWT.Hide);
  }
}

}
