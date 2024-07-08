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


import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.CControl;
import org.eclipse.swt.internal.swing.CGC;
import org.eclipse.swt.internal.swing.Compatibility;
import org.eclipse.swt.internal.swing.DisabledStatePanel;
import org.eclipse.swt.internal.swing.NullGraphics2D;
import org.eclipse.swt.internal.swing.UIThreadUtils;
import org.eclipse.swt.internal.swing.Utils;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b>
 * <dd>FocusIn, FocusOut, Help, KeyDown, KeyUp, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, Move, Paint, Resize, Traverse,
 *     DragDetect, MenuDetect</dd>
 * </dl>
 * </p><p>
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public abstract class Control extends Widget implements Drawable {
	/**
	 * the handle to the OS resource 
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public Container handle;
	Composite parent;
//	Cursor cursor;
	Menu menu;
//	String toolTipText;
	Object layoutData;
	Accessible accessible;
//	int drawCount, foreground, background;
//  long lastPressed = 0;
  Image backgroundImage;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Control () {
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
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
 * @see SWT#BORDER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget ();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
 * interface.
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control gains or loses focus, by sending
 * it one of the messages defined in the <code>FocusListener</code>
 * interface.
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
 * @see FocusListener
 * @see #removeFocusListener
 */
public void addFocusListener (FocusListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.FocusIn,typedListener);
	addListener (SWT.FocusOut,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when help events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>HelpListener</code> interface.
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
 * @see HelpListener
 * @see #removeHelpListener
 */
public void addHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard, by sending
 * it one of the messages defined in the <code>KeyListener</code>
 * interface.
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
 * @see KeyListener
 * @see #removeKeyListener
 */
public void addKeyListener (KeyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.KeyUp,typedListener);
	addListener (SWT.KeyDown,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when mouse buttons are pressed and released, by sending
 * it one of the messages defined in the <code>MouseListener</code>
 * interface.
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
 * @see MouseListener
 * @see #removeMouseListener
 */
public void addMouseListener (MouseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseDown,typedListener);
	addListener (SWT.MouseUp,typedListener);
	addListener (SWT.MouseDoubleClick,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse passes or hovers over controls, by sending
 * it one of the messages defined in the <code>MouseTrackListener</code>
 * interface.
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
 * @see MouseTrackListener
 * @see #removeMouseTrackListener
 */
public void addMouseTrackListener (MouseTrackListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse moves, by sending it one of the
 * messages defined in the <code>MouseMoveListener</code>
 * interface.
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
 * @see MouseMoveListener
 * @see #removeMouseMoveListener
 */
public void addMouseMoveListener (MouseMoveListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseMove,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver needs to be painted, by sending it
 * one of the messages defined in the <code>PaintListener</code>
 * interface.
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
 * @see PaintListener
 * @see #removePaintListener
 */
public void addPaintListener (PaintListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Paint,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when traversal events occur, by sending it
 * one of the messages defined in the <code>TraverseListener</code>
 * interface.
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
 * @see TraverseListener
 * @see #removeTraverseListener
 */
public void addTraverseListener (TraverseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}

//int borderHandle () {
//	return handle;
//}
//
//void checkBorder () {
//	if (getBorderWidth () == 0) style &= ~SWT.BORDER;
//}
//
//boolean checkHandle (int hwnd) {
//	return hwnd == handle;
//}
//
//void checkMirrored () {
//	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
//		int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
//		if ((bits & OS.WS_EX_LAYOUTRTL) != 0) style |= SWT.MIRRORED;
//	}
//}
//
/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @return the preferred size of the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack(boolean)
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}

/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p><p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @param changed <code>true</code> if the control's contents have changed, and <code>false</code> otherwise
 * @return the preferred size of the control.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack(boolean)
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
  //TODO: it seems that for some components we have to take the border into account (Shell), and for others not (Button).
  // Let's assume for a start that we don't take that into account if the component is the same as the client area.
  Container clientArea = ((CControl)handle).getClientArea();
  Dimension size = handle.getPreferredSize();
  Dimension contentSize = clientArea.getPreferredSize();
  if(clientArea != handle) {
    Insets insets = clientArea.getInsets();
    contentSize.width -= insets.left + insets.right;
    contentSize.height -= insets.top + insets.bottom;
  }
  int width = size.width;
  int height = size.height;
  if(wHint != SWT.DEFAULT) {
    width = wHint + width - contentSize.width;
  }
  if(hHint != SWT.DEFAULT) {
    height = hHint + height - contentSize.height;
  }
	return new Point (width, height);
}

Control computeTabGroup () {
	if (isTabGroup ()) return this;
	return parent.computeTabGroup ();
}

Control computeTabRoot () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		int index = 0;
		while (index < tabList.length) {
			if (tabList [index] == this) break;
			index++;
		}
		if (index == tabList.length) {
			if (isTabGroup ()) return this;
		}
	}
	return parent.computeTabRoot ();
}

Control [] computeTabList () {
	if (isTabGroup ()) {
		if (getVisible () && getEnabled ()) {
			return new Control [] {this};
		}
	}
	return new Control [0];
}

/**
 * Like create handle, but overriden class should call its supertype declaration
 */
void createHandleInit () {
  
}

Container createHandle () {
  if(handle != null) return handle;
  error (SWT.ERROR_NO_HANDLES);
  return null;
}

boolean autoAddChildren() {
  return true;
}

void createWidget () {
	checkOrientation (parent);
	createHandleInit ();
	handle = createHandle ();
  if(!(handle instanceof CControl)) throw new IllegalStateException("The widget should implement the CComponent interface!");
  if(handle instanceof Window) {
    Dimension size = ((Window)handle).getSize();
    ((Window)handle).pack();
    handle.setSize(size);
  }
  if(parent != null && !(handle instanceof Window)) {
    if(parent.autoAddChildren()) {
      Container clientArea = ((CControl)parent.handle).getClientArea();
      if(!parent.isLayoutManaged()) {
        clientArea.setLayout(null);
      }
      clientArea.add(handle);
      updateBackgroundMode();
      ((JComponent)clientArea).revalidate();
      clientArea.repaint();
    }
//    if(parent instanceof Shell && parent.getChildren().length == 0) {
//      handle.requestFocus();
//    }
  }
    // TODO: should have a fixInitSize like in old implementation <- Note it seems to be fixed now, but have to check
//    if(handle.getSize().equals(new java.awt.Dimension(0, 0))) {
//      handle.setSize(handle.getPreferredSize());
//      if(handle instanceof JComponent) {
//        JComponent component = (JComponent)handle;
////        component.setOpaque(true);
////        component.revalidate();
////      } else {
////        handle.validate();
//      }
//    }
    register ();
//	subclass ();
//	setDefaultFont ();
//	checkMirrored ();
//	checkBorder ();
}

//int defaultBackground () {
//	if (OS.IsWinCE) return OS.GetSysColor (OS.COLOR_WINDOW);
//	return OS.GetSysColor (OS.COLOR_BTNFACE);
//}
//
//int defaultFont () {
//	return display.systemFont ();
//}
//
//int defaultForeground () {
//	return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
//}

void deregister () {
	display.removeControl (handle);
}

void destroyWidget () {
//	int hwnd = topHandle ();
  releaseHandle ();
//	if (hwnd != 0) {
//		OS.DestroyWindow (hwnd);
//	}
}

//void drawBackground (int hDC) {
//	RECT rect = new RECT ();
//	OS.GetClientRect (handle, rect);
//	drawBackground (hDC, getBackgroundPixel (), rect);
//}
//
//void drawBackground (int hDC, int pixel, RECT rect) {
//	Control control = null;
//	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
//		control = findThemeControl ();
//	}
//	if (control == null) {
//		int hPalette = display.hPalette;
//		if (hPalette != 0) {
//			OS.SelectPalette (hDC, hPalette, false);
//			OS.RealizePalette (hDC);
//		}
//		int hBrush = findBrush (pixel);
//		OS.FillRect (hDC, rect, hBrush);
//	} else {
//		RECT rect2 = new RECT ();
//		OS.GetWindowRect (control.handle, rect2);		
//		OS.MapWindowPoints (0, handle, rect2, 2);
//		control.drawThemeBackground (hDC, rect2);	
//	}
//}
//
//void drawBackground (int hDC, RECT rect) {
//	drawBackground (hDC, getBackgroundPixel (), rect);
//}
//
//void drawThemeBackground (int hDC, RECT rect) {
//	/* Do nothing */
//}
//
//void enableWidget (boolean enabled) {
//  handle.setEnabled(enabled);
//}
//
//int findBrush (int pixel) {
//	return parent.findBrush (pixel);
//}
//
//Cursor findCursor () {
//	if (cursor != null) return cursor;
//	return parent.findCursor ();
//}
//
//Control findThemeControl () {
//	return background == - 1 ? parent.findThemeControl () : null;
//}

Menu [] findMenus (Control control) {
	if (menu != null && this != control) return new Menu [] {menu};
	return new Menu [0];
}

//int findMnemonicIndex (String string) {
//  int index = 0;
//  int length = string.length ();
//  do {
//    while (index < length && string.charAt (index) != '&') index++;
//    if (++index >= length) return -1;
//    if (string.charAt (index) != '&') return index;
//    index++;
//  } while (index < length);
//  return -1;
//}

char findMnemonic (String string) {
	int index = 0;
	int length = string.length ();
	do {
		while (index < length && string.charAt (index) != '&') index++;
		if (++index >= length) return '\0';
		if (string.charAt (index) != '&') return string.charAt (index);
		index++;
	} while (index < length);
 	return '\0';
}

void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	oldShell.fixShell (newShell, this);
	oldDecorations.fixDecorations (newDecorations, this, menus);
}

//void fixFocus (Control focusControl) {
//	Shell shell = getShell ();
//	Control control = this;
//	while ((control = control.parent) != null) {
//		if (control.setFocus ()) return;
//		if (control == shell) break;
//	}
//	shell.setSavedFocus (focusControl);
//	OS.SetFocus (0);
//}

/**
 * Forces the receiver to have the <em>keyboard focus</em>, causing
 * all keyboard events to be delivered to it.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setFocus
 */
public boolean forceFocus () {
	checkWidget ();
//	if (display.focusEvent == SWT.FocusOut) return false;
//	Decorations shell = menuShell ();
//	shell.setSavedFocus (this);
	if (!isEnabled () /*|| !isActive ()*/) return false;
	if(!handle.isFocusable()) return false;
	if (!isVisible ()) {
	  Window window = SwingUtilities.getWindowAncestor(handle);
	  if(window != null && !window.isVisible()) {
	    getShell().setInitialFocusedControl(this);
	    return true;
	  }
	  return false;
	}
	if (isFocusControl ()) return true;
//	shell.setSavedFocus (null);
  // return handle.requestFocusInWindow();
  Window window = SwingUtilities.getWindowAncestor(handle);
  if(window != null) {
    window.setFocusableWindowState(true);
  }
//  if (handle instanceof JComponent) {
//    ((javax.swing.JComponent) handle).grabFocus();
//  } else {
    handle.requestFocus();
//  }
  return true;
  
//  return handle.hasFocus();
//	/*
//	* This code is intentionally commented.
//	*
//	* When setting focus to a control, it is
//	* possible that application code can set
//	* the focus to another control inside of
//	* WM_SETFOCUS.  In this case, the original
//	* control will no longer have the focus
//	* and the call to setFocus() will return
//	* false indicating failure.
//	* 
//	* We are still working on a solution at
//	* this time.
//	*/
////	if (OS.GetFocus () != OS.SetFocus (handle)) return false;
//	OS.SetFocus (handle);
//	if (isDisposed ()) return false;
//	shell.setSavedFocus (this);
//	return isFocusControl ();
}

//void forceResize () {
//	if (parent == null) return;
//	WINDOWPOS [] lpwp = parent.lpwp;
//	if (lpwp == null) return;
//	for (int i=0; i<lpwp.length; i++) {
//		WINDOWPOS wp = lpwp [i];
//		if (wp != null && wp.hwnd == handle) {
//			/*
//			* This code is intentionally commented.  All widgets that
//			* are created by SWT have WS_CLIPSIBLINGS to ensure that
//			* application code does not draw outside of the control.
//			*/	
////			int count = parent.getChildrenCount ();
////			if (count > 1) {
////				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
////				if ((bits & OS.WS_CLIPSIBLINGS) == 0) wp.flags |= OS.SWP_NOCOPYBITS;
////			}
//			SetWindowPos (wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
//			lpwp [i] = null;
//			return;
//		}	
//	}
//}

/**
 * Returns the accessible object for the receiver.
 * If this is the first time this object is requested,
 * then the object is created and returned.
 *
 * @return the accessible object
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Accessible#addAccessibleListener
 * @see Accessible#addAccessibleControlListener
 * 
 * @since 2.0
 */
public Accessible getAccessible () {
	checkWidget ();
	if (accessible == null) accessible = new_Accessible (this);
	return accessible;
}

/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getBackground () {
	checkWidget ();
	return Color.swing_new (display, ((CControl)handle).getUserAttributeHandler().getBackground());
}

//int getBackgroundPixel () {
//	if (background == -1) return defaultBackground ();
//	return background;
//}

/**
 * Returns the receiver's background image.
 *
 * @return the background image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public Image getBackgroundImage () {
  checkWidget ();
  return backgroundImage;
}

/**
 * Returns the receiver's border width.
 *
 * @return the border width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getBorderWidth () {
	checkWidget ();
  java.awt.Rectangle bounds = handle.getBounds();
  java.awt.Rectangle cBounds = ((CControl)handle).getClientArea().getBounds();
  return Math.max((bounds.width - cBounds.width + 1) / 2, (bounds.height - cBounds.height + 1) / 2);
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null),
 * unless the receiver is a shell. In this case, the location is
 * relative to the display.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget ();
  java.awt.Rectangle rect = handle.getBounds();
  Point offset = parent == null? new Point(0, 0): parent.getInternalOffset();
  return new Rectangle (rect.x + offset.x, rect.y + offset.y, rect.width, rect.height);
//  
//	RECT rect = new RECT ();
//	OS.GetWindowRect (topHandle (), rect);
//	int hwndParent = parent == null ? 0 : parent.handle;
//	OS.MapWindowPoints (0, hwndParent, rect, 2);
//	int width = rect.right - rect.left;
//	int height =  rect.bottom - rect.top;
//	return new Rectangle (rect.left, rect.top, width, height);
}

//int getCodePage () {
//	if (OS.IsUnicode) return OS.CP_ACP;
//	int hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
//	LOGFONT logFont = OS.IsUnicode ? (LOGFONT) new LOGFONTW () : new LOGFONTA ();
//	OS.GetObject (hFont, LOGFONT.sizeof, logFont);
//	int cs = logFont.lfCharSet & 0xFF;
//	int [] lpCs = new int [8];
//	if (OS.TranslateCharsetInfo (cs, lpCs, OS.TCI_SRCCHARSET)) {
//		return lpCs [1];
//	}
//	return OS.GetACP ();
//}
//
//String getClipboardText () {
//	String string = "";
//	if (OS.OpenClipboard (0)) {
//		int hMem = OS.GetClipboardData (OS.IsUnicode ? OS.CF_UNICODETEXT : OS.CF_TEXT);
//		if (hMem != 0) {
//			/* Ensure byteCount is a multiple of 2 bytes on UNICODE platforms */
//			int byteCount = OS.GlobalSize (hMem) / TCHAR.sizeof * TCHAR.sizeof;
//			int ptr = OS.GlobalLock (hMem);
//			if (ptr != 0) {
//				/* Use the character encoding for the default locale */
//				TCHAR buffer = new TCHAR (0, byteCount / TCHAR.sizeof);
//				OS.MoveMemory (buffer, ptr, byteCount);
//				string = buffer.toString (0, buffer.strlen ());
//				OS.GlobalUnlock (hMem);
//			}
//		}
//		OS.CloseClipboard ();
//	}
//	return string;
//}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget ();
	return handle.isEnabled();
}

/**
 * Returns the font that the receiver will use to paint textual information.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Font getFont () {
	checkWidget ();	
	return Font.swing_new (display, ((CControl)handle).getUserAttributeHandler().getFont());
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getForeground () {
	checkWidget ();
	return Color.swing_new (display, ((CControl)handle).getUserAttributeHandler().getForeground());
}

//int getForegroundPixel () {
//	if (foreground == -1) return defaultForeground ();
//	return foreground;
//}

/**
 * Returns layout data which is associated with the receiver.
 *
 * @return the receiver's layout data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Object getLayoutData () {
	checkWidget ();
	return layoutData;
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null), unless
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
 *
 * @return the receiver's location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getLocation () {
	checkWidget ();
  java.awt.Point location = handle.getLocation();
  Point offset = parent == null? new Point(0, 0): parent.getInternalOffset();
	return new Point (location.x + offset.x, location.y + offset.y);
}

/**
 * Returns the receiver's pop up menu if it has one, or null
 * if it does not. All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 *
 * @return the receiver's menu
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getMenu () {
	checkWidget ();
	return menu;
}

/**
 * Returns the receiver's monitor.
 * 
 * @return the receiver's monitor
 * 
 * @since 3.0
 */
public Monitor getMonitor () {
	checkWidget ();
  GraphicsConfiguration gc = handle.getGraphicsConfiguration();
  Monitor monitor = new Monitor();
  monitor.handle = gc;
  java.awt.Rectangle bounds = gc.getBounds();
  monitor.x = bounds.x;
  monitor.y = bounds.y;
  monitor.width = bounds.width;
  monitor.height = bounds.height;
  java.awt.Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
  monitor.clientX = bounds.x + insets.left;
  monitor.clientY = bounds.y + insets.top;
  monitor.clientWidth = bounds.width - insets.left - insets.right;
  monitor.clientHeight = bounds.height - insets.top - insets.bottom;
	return monitor;
}

/**
 * Returns the receiver's parent, which must be a <code>Composite</code>
 * or null when the receiver is a shell that was created with null or
 * a display for a parent.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Composite getParent () {
	checkWidget ();
	return parent;
}

Control [] getPath () {
	int count = 0;
	Shell shell = getShell ();
	Control control = this;
	while (control != shell) {
		count++;
		control = control.parent;
	}
	control = this;
	Control [] result = new Control [count];
	while (control != shell) {
		result [--count] = control;
		control = control.parent;
	}
	return result;
}

/**
 * Returns the receiver's shell. For all controls other than
 * shells, this simply returns the control's nearest ancestor
 * shell. Shells return themselves, even if they are children
 * of other shells.
 *
 * @return the receiver's shell
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getParent
 */
public Shell getShell () {
	checkWidget ();
	return parent.getShell ();
}

/**
 * Returns a point describing the receiver's size. The
 * x coordinate of the result is the width of the receiver.
 * The y coordinate of the result is the height of the
 * receiver.
 *
 * @return the receiver's size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSize () {
	checkWidget ();
  java.awt.Dimension size = handle.getSize();
  return new Point(size.width, size.height);
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
	return toolTipText;
}

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget ();
  return handle.isVisible();
}

//boolean hasCursor () {
//	RECT rect = new RECT ();
//	if (!OS.GetClientRect (handle, rect)) return false;
//	OS.MapWindowPoints (handle, 0, rect, 2);
//	POINT pt = new POINT ();
//	return OS.GetCursorPos (pt) && OS.PtInRect (rect, pt);
//}
//
//boolean hasFocus () {
//	/*
//	* If a non-SWT child of the control has focus,
//	* then this control is considered to have focus
//	* even though it does not have focus in Windows.
//	*/
//	int hwndFocus = OS.GetFocus ();
//	while (hwndFocus != 0) {
//		if (hwndFocus == handle) return true;
//		if (display.getControl (hwndFocus) != null) {
//			return false;
//		}
//		hwndFocus = OS.GetParent (hwndFocus);
//	}
//	return false;
//}

/**	 
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data 
 * @return the platform specific GC handle
 */
public CGC internal_new_GC (GCData data) {
	checkWidget();
  if(data != null) {
    int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
    if ((data.style & mask) != 0) {
      data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? false : true;
    } else {
      if (!handle.getComponentOrientation().isLeftToRight()) {
        data.style |= SWT.RIGHT_TO_LEFT | SWT.MIRRORED;
      } else {
        data.style |= SWT.LEFT_TO_RIGHT;
      }
    }
    data.device = display;
    data.foreground = handle.getForeground();
    data.background = handle.getBackground();
    data.hFont = handle.getFont();
  }
//  Component clientArea = ((CControl)handle).getClientArea();
//  final Graphics2D g2D = (Graphics2D)clientArea.getGraphics();
//  if(g2D == null) {
//    return null;
//  }
//  Point internalOffset = getInternalOffset();
//  g2D.translate(-internalOffset.x, -internalOffset.y);
//  java.awt.Point point = new java.awt.Point(0, 0);
//  point = SwingUtilities.convertPoint(component, point, handle);
//  g2D.translate(-point.x, -point.y);
  return new CGC.CGCGraphics2D() {
    protected Graphics2D internalSwingGraphics2D;
    protected Graphics2D graphics2D;
    protected boolean isAdjusting;
    public Graphics2D getGraphics() {
      if(isAdjusting) {
        return graphics2D;
      }
      Container clientArea = ((CControl)handle).getClientArea();
      Graphics2D sGraphics2D = clientArea instanceof JComponent? (Graphics2D)((JComponent)clientArea).getClientProperty(Utils.SWTSwingGraphics2DClientProperty): null;
      boolean resetClip = false;
      // sGraphics2D == null is needed because it seems it is not always the same GC for the native component.
      if(internalSwingGraphics2D != sGraphics2D || sGraphics2D == null || graphics2D == null || graphics2D instanceof NullGraphics2D) {
        Graphics2D newGraphics2D = (Graphics2D)clientArea.getGraphics();
        if(newGraphics2D == null) {
          newGraphics2D = new NullGraphics2D();
          newGraphics2D.setBackground(clientArea.getBackground());
          newGraphics2D.setColor(clientArea.getForeground());
          newGraphics2D.setFont(clientArea.getFont());
        }
        Point internalOffset = getInternalOffset();
        newGraphics2D.translate(-internalOffset.x, -internalOffset.y);
        if(graphics2D != null) {
          resetClip = true;
          newGraphics2D.setBackground(graphics2D.getBackground());
          newGraphics2D.setColor(graphics2D.getColor());
          newGraphics2D.setComposite(graphics2D.getComposite());
          newGraphics2D.setFont(graphics2D.getFont());
          newGraphics2D.setPaint(graphics2D.getPaint());
          newGraphics2D.setRenderingHints(graphics2D.getRenderingHints());
          newGraphics2D.setStroke(graphics2D.getStroke());
          newGraphics2D.setTransform(graphics2D.getTransform());
        }
        graphics2D = newGraphics2D;
        internalSwingGraphics2D = sGraphics2D;
      }
      if(resetClip) {
        isAdjusting = true;
        setUserClip(getUserClip());
        isAdjusting = false;
      }
      return graphics2D;
    }
    public Dimension getDeviceSize() {
      return ((CControl)handle).getClientArea().getSize();
    }
  };
//	int hwnd = handle;
//	if (data != null && data.hwnd != 0) {
//		hwnd = data.hwnd;
//	}
//	int hDC = 0;
//	if (data == null || data.ps == null) {
//		hDC = OS.GetDC (hwnd);
//	} else {
//		hDC = OS.BeginPaint (hwnd, data.ps);
//	}
//	if (hDC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//	if (data != null) {
//		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
//			int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
//			if ((data.style & mask) != 0) {
//				data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.LAYOUT_RTL : 0;
//			} else {
//				int flags = OS.GetLayout (hDC);
//				if ((flags & OS.LAYOUT_RTL) != 0) {
//					data.style |= SWT.RIGHT_TO_LEFT | SWT.MIRRORED;
//				} else {
//					data.style |= SWT.LEFT_TO_RIGHT;
//				}
//			}
//		} else {
//			data.style |= SWT.LEFT_TO_RIGHT;
//		}
//		data.device = display;
//		data.foreground = getForegroundPixel ();
//		data.background = getBackgroundPixel ();
//		data.hFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
//	}
//	return hDC;
}

/**	 
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data 
 */
public void internal_dispose_GC (CGC hDC, GCData data) {
	checkWidget ();
//	Component hwnd = handle;
//	if (data != null && data.hwnd != null) {
//		hwnd = data.hwnd;
//	}
  hDC.dispose();
//	if (data == null || data.ps == null) {
//		OS.ReleaseDC (hwnd, hDC);
//	} else {
//		OS.EndPaint (hwnd, data.ps);
//	}
}
//
//boolean isActive () {
//	Shell dialogShell = display.getModalDialogShell ();
//	if (dialogShell != null && dialogShell != getShell ()) {
//		return false;
//	}
//	Shell shell = null;
//	Shell [] modalShells = display.modalShells;
//	if (modalShells != null) {
//		int bits = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
//		int index = modalShells.length;
//		while (--index >= 0) {
//			Shell modal = modalShells [index];
//			if (modal != null) {
//				if ((modal.style & bits) != 0) {
//					Control control = this;
//					while (control != null) {
//						if (control == modal) break;
//						control = control.parent;
//					}
//					if (control != modal) return false;
//					break;
//				}
//				if ((modal.style & SWT.PRIMARY_MODAL) != 0) {
//					if (shell == null) shell = getShell ();
//					if (modal.parent == shell) return false;
//				}
//			}
//		}
//	}
//	if (shell == null) shell = getShell ();
//	return shell.getEnabled ();
//}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * ancestors up to and including the receiver's nearest ancestor
 * shell are enabled.  Otherwise, <code>false</code> is returned.
 * A disabled control is typically not selectable from the user
 * interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget ();
	return getEnabled () && parent.isEnabled ();
}

/**
 * Returns <code>true</code> if the receiver has the user-interface
 * focus, and <code>false</code> otherwise.
 *
 * @return the receiver's focus state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isFocusControl () {
	checkWidget ();
  return display.findControl(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()) == this;
}

//boolean isFocusAncestor (Control control) {
//	while (control != null && control != this) {
//		control = control.parent;
//	}
//	return control == this;
//}

/**
 * Returns <code>true</code> if the underlying operating
 * system supports this reparenting, otherwise <code>false</code>
 *
 * @return <code>true</code> if the widget can be reparented, otherwise <code>false</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isReparentable () {
	checkWidget ();
	return true;
}

//boolean isShowing () {
//	/*
//	* This is not complete.  Need to check if the
//	* widget is obscurred by a parent or sibling.
//	*/
//	if (!isVisible ()) return false;
//	Control control = this;
//	while (control != null) {
//		Point size = control.getSize ();
//		if (size.x == 0 || size.y == 0) {
//			return false;
//		}
//		control = control.parent;
//	}
//	return true;
//	/*
//	* Check to see if current damage is included.
//	*/
////	if (!OS.IsWindowVisible (handle)) return false;
////	int flags = OS.DCX_CACHE | OS.DCX_CLIPCHILDREN | OS.DCX_CLIPSIBLINGS;
////	int hDC = OS.GetDCEx (handle, 0, flags);
////	int result = OS.GetClipBox (hDC, new RECT ());
////	OS.ReleaseDC (handle, hDC);
////	return result != OS.NULLREGION;
//}

boolean isTabGroup () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return true;
		}
	}
  return handle.isFocusable();
}

boolean isTabItem () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return false;
		}
	}
  // TODO: implement next lines?
//	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//	if ((bits & OS.WS_TABSTOP) != 0) return false;
//	int code = OS.SendMessage (handle, OS.WM_GETDLGCODE, 0, 0);
//	if ((code & OS.DLGC_STATIC) != 0) return false;
//	if ((code & OS.DLGC_WANTALLKEYS) != 0) return false;
//	if ((code & OS.DLGC_WANTARROWS) != 0) return false;
//	if ((code & OS.DLGC_WANTTAB) != 0) return false;
	return !isTabGroup();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * ancestors up to and including the receiver's nearest ancestor
 * shell are visible. Otherwise, <code>false</code> is returned.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget ();
  return handle.isShowing();
}

//void mapEvent (int hwnd, Event event) {
//	if (hwnd != handle) {
//		POINT point = new POINT ();
//		point.x = event.x;
//		point.y = event.y;
//		OS.MapWindowPoints (hwnd, handle, point, 1);
//		event.x = point.x;
//		event.y = point.y;
//	}
//}

void markLayout (boolean changed, boolean all) {
	/* Do nothing */
}

Decorations menuShell () {
	return parent.menuShell ();
}

boolean mnemonicHit (char key) {
	return false;
}

boolean mnemonicMatch (char key) {
	return false;
}

/**
 * Moves the receiver above the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the top of the drawing order. The control at
 * the top of the drawing order will not be covered by other
 * controls even if they occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Control#moveBelow
 * @see Composite#getChildren
 */
public void moveAbove (Control control) {
	checkWidget ();
  Container cParent = handle.getParent();
  if (control != null) {
    if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
    if (handle == control.handle) return;
    if (parent != control.parent) return;
    if(!Compatibility.IS_JAVA_5_OR_GREATER) {
      boolean isFound = false;
      for(int i=cParent.getComponentCount()-1; i>=0; i--) {
        isFound = control.handle == handle;
        if(cParent.getComponent(i) == control.handle) {
          cParent.remove(handle);
          cParent.add(handle, isFound? i: i-1);
          handle.invalidate();
          cParent.validate();
          cParent.repaint();
          break;
        }
      }
      return;
    }
    int cOrder = cParent.getComponentZOrder(control.handle);
    int order = cParent.getComponentZOrder(handle);
    if(order < cOrder) {
      cOrder--;
    }
    cParent.setComponentZOrder(handle, cOrder);
  } else {
    if(!Compatibility.IS_JAVA_5_OR_GREATER) {
      cParent.remove(handle);
      cParent.add(handle, 0);
      handle.invalidate();
      cParent.validate();
      cParent.repaint();
      return;
    }
    cParent.setComponentZOrder(handle, 0);
  }
//	int topHandle = topHandle (), hwndAbove = OS.HWND_TOP;
//	if (control != null) {
//		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
//		if (parent != control.parent) return;
//		int hwnd = control.topHandle ();
//		if (hwnd == 0 || hwnd == topHandle) return;
//		hwndAbove = OS.GetWindow (hwnd, OS.GW_HWNDPREV);
//		/*
//		* Bug in Windows.  For some reason, when GetWindow ()
//		* with GW_HWNDPREV is used to query the previous window
//		* in the z-order with the first child, Windows returns
//		* the first child instead of NULL.  The fix is to detect
//		* this case and move the control to the top.
//		*/
//		if (hwndAbove == 0 || hwndAbove == hwnd) {
//			hwndAbove = OS.HWND_TOP;
//		}
//	}
//	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
//	SetWindowPos (topHandle, hwndAbove, 0, 0, 0, 0, flags);
}

/**
 * Moves the receiver below the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the bottom of the drawing order. The control at
 * the bottom of the drawing order will be covered by all other
 * controls which occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Control#moveAbove
 * @see Composite#getChildren
 */
public void moveBelow (Control control) {
	checkWidget ();
  Container cParent = handle.getParent();
  if (control != null) {
    if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
    if (handle == control.handle) return;
    if (parent != control.parent) return;
    if(!Compatibility.IS_JAVA_5_OR_GREATER) {
      boolean isFound = false;
      for(int i=cParent.getComponentCount()-1; i>=0; i--) {
        isFound = control.handle == handle;
        if(cParent.getComponent(i) == control.handle) {
          cParent.remove(handle);
          cParent.add(handle, isFound? i: i+1);
          handle.invalidate();
          cParent.validate();
          cParent.repaint();
          break;
        }
      }
      return;
    }
    int cOrder = cParent.getComponentZOrder(control.handle);
    int order = cParent.getComponentZOrder(handle);
    if(order < cOrder) {
      cOrder--;
    }
    cParent.setComponentZOrder(handle, cOrder + 1);
  } else {
    if(!Compatibility.IS_JAVA_5_OR_GREATER) {
      cParent.remove(handle);
      cParent.add(handle);
      handle.invalidate();
      cParent.validate();
      cParent.repaint();
      return;
    }
    cParent.setComponentZOrder(handle, cParent.getComponentCount() - 1);
  }
//	int topHandle = topHandle (), hwndAbove = OS.HWND_BOTTOM;
//	if (control != null) {
//		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
//		if (parent != control.parent) return;
//		hwndAbove = control.topHandle ();
//	}
//	if (hwndAbove == 0 || hwndAbove == topHandle) return;
//	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
//	SetWindowPos (topHandle, hwndAbove, 0, 0, 0, 0, flags);
}

Accessible new_Accessible (Control control) {
	return Accessible.internal_new_Accessible (this);
}

GC new_GC (GCData data) {
	return GC.swing_new (this, data);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize(int, int, boolean)
 */
public void pack () {
	checkWidget ();
	pack (true);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 * <p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained. 
 * </p>
 *
 * @param changed whether or not the receiver's contents have changed
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize(int, int, boolean)
 */
public void pack (boolean changed) {
	checkWidget ();
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}

/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted,
 * including the background.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 */
public void redraw () {
	checkWidget ();
  handle.repaint();
//	if (!OS.IsWindowVisible (handle)) return;
//	if (OS.IsWinCE) {
//		OS.InvalidateRect (handle, null, true);
//	} else {
//		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
//		OS.RedrawWindow (handle, null, 0, flags);
//	}
}

/**
 * Causes the rectangular area of the receiver specified by
 * the arguments to be marked as needing to be redrawn. 
 * The next time a paint request is processed, that area of
 * the receiver will be painted, including the background.
 * If the <code>all</code> flag is <code>true</code>, any
 * children of the receiver which intersect with the specified
 * area will also paint their intersecting areas. If the
 * <code>all</code> flag is <code>false</code>, the children
 * will not be painted.
 *
 * @param x the x coordinate of the area to draw
 * @param y the y coordinate of the area to draw
 * @param width the width of the area to draw
 * @param height the height of the area to draw
 * @param all <code>true</code> if children should redraw, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 */
public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget ();
	if (width <= 0 || height <= 0) return;
  // TODO: what about the "all" setting?
//	handle.repaint();
  // TODO: check using the Paint Example why this repaint needs -1 and +2...
  ((CControl)handle).getClientArea().repaint(x-1, y-1, width+2, height+2);
//	if (!OS.IsWindowVisible (handle)) return;
//	RECT rect = new RECT ();
//	OS.SetRect (rect, x, y, x + width, y + height);
//	if (OS.IsWinCE) {
//		OS.InvalidateRect (handle, rect, true);
//	} else {
//		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
//		if (all) flags |= OS.RDW_ALLCHILDREN;
//		OS.RedrawWindow (handle, rect, 0, flags);
//	}
}

void register () {
	display.addControl (handle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	handle = null;
  parent = null;
}

void releaseParent () {
  Container parent = handle.getParent();
  if(parent != null) {
    parent.remove(handle);
    ((JComponent)parent).revalidate();
    parent.repaint();
  }
  this.parent.removeControl (this);
}

void releaseWidget () {
  if(focusLostRunnable != null && getShell().getActiveControl() == this) {
    focusLostRunnable.run();
  }
	super.releaseWidget ();
//	if (OS.IsDBLocale) {
//		OS.ImmAssociateContext (handle, 0);
//	}
//	if (toolTipText != null) {
//		Shell shell = getShell ();
//		shell.setToolTipText (handle, null);
//	}
//	toolTipText = null;
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
//	cursor = null;
	deregister ();
//	unsubclass ();
	layoutData = null;
	if (accessible != null) {
		accessible.internal_dispose_Accessible ();
	}
	accessible = null;
  if(disabledStatePanel != null) {
    disabledStatePanel.release();
    disabledStatePanel = null;
  }
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control gains or loses focus.
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
 * @see FocusListener
 * @see #addFocusListener
 */
public void removeFocusListener(FocusListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.FocusIn, listener);
	eventTable.unhook (SWT.FocusOut, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
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
 * @see HelpListener
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard.
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
 * @see KeyListener
 * @see #addKeyListener
 */
public void removeKeyListener(KeyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.KeyUp, listener);
	eventTable.unhook (SWT.KeyDown, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse passes or hovers over controls.
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
 * @see MouseTrackListener
 * @see #addMouseTrackListener
 */
public void removeMouseTrackListener(MouseTrackListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when mouse buttons are pressed and released.
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
 * @see MouseListener
 * @see #addMouseListener
 */
public void removeMouseListener (MouseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseDown, listener);
	eventTable.unhook (SWT.MouseUp, listener);
	eventTable.unhook (SWT.MouseDoubleClick, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse moves.
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
 * @see MouseMoveListener
 * @see #addMouseMoveListener
 */
public void removeMouseMoveListener(MouseMoveListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseMove, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver needs to be painted.
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
 * @see PaintListener
 * @see #addPaintListener
 */
public void removePaintListener(PaintListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when traversal events occur.
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
 * @see TraverseListener
 * @see #addTraverseListener
 */
public void removeTraverseListener(TraverseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}

//void showWidget (boolean visible) {
//	OS.ShowWindow (handle, visible ? OS.SW_SHOW : OS.SW_HIDE);
//}
//
//boolean sendFocusEvent (int type) {
//	Shell shell = getShell ();
//	
//	/*
//	* Feature in Windows.  During the processing of WM_KILLFOCUS,
//	* when the focus window is queried using GetFocus(), it has
//	* already been assigned to the new window.  The fix is to
//	* remember the control that is losing or gaining focus and
//	* answer it during WM_KILLFOCUS.  If a WM_SETFOCUS occurs
//	* during WM_KILLFOCUS, the focus control needs to be updated
//	* to the current control.  At any other time, the focus
//	* control matches Windows.
//	*/
//	Display display = this.display;
//	display.focusEvent = type;
//	display.focusControl = this;
//	sendEvent (type);
//	// widget could be disposed at this point
//	display.focusEvent = SWT.None;
//	display.focusControl = null;
//
//	/*
//	* It is possible that the shell may be
//	* disposed at this point.  If this happens
//	* don't send the activate and deactivate
//	* events.
//	*/	
//	if (!shell.isDisposed ()) {
//		switch (type) {
//			case SWT.FocusIn:
//				shell.setActiveControl (this);
//				break;
//			case SWT.FocusOut:
//				if (shell != display.getActiveShell ()) {
//					shell.setActiveControl (null);
//				}
//				break;
//		}
//	}
//	return true;
//}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBackground (Color color) {
  checkWidget ();
  if (color != null) {
    if (color.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
    ((CControl)handle).getUserAttributeHandler().setBackground (color.handle);
  } else {
    ((CControl)handle).getUserAttributeHandler().setBackground (null);
  }
  handle.repaint();
}

/**
 * Sets the receiver's background image to the image specified
 * by the argument, or to the default system color for the control
 * if the argument is null.  The background image is tiled to fill
 * the available space.
 *
 * @param image the new image (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 *    <li>ERROR_INVALID_ARGUMENT - if the argument is not a bitmap</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setBackgroundImage (Image image) {
  checkWidget ();
  if (image != null) {
    if (image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
    if (image.type != SWT.BITMAP) error (SWT.ERROR_INVALID_ARGUMENT);
  }
  if (backgroundImage == image) return;
  backgroundImage = image;
  ((CControl)handle).setBackgroundImage(backgroundImage == null? null: backgroundImage.handle);
  handle.repaint();
}

//void setBackgroundPixel (int pixel) {
//	if (background == pixel) return;
//	background = pixel;
//	OS.InvalidateRect (handle, null, true);
//}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the arguments. The <code>x</code> and 
 * <code>y</code> arguments are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the <code>x</code>
 * and <code>y</code> arguments are relative to the display.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (int x, int y, int width, int height) {
	checkWidget ();
  if(width < 0) width = 0;
  if(height < 0) height = 0;
  Point offset = parent == null? new Point(0, 0): parent.getInternalOffset();
  x -= offset.x;
  y -= offset.y;
//  java.awt.Point location = new java.awt.Point(x, y);
//  Container parent = handle.getParent();
//  if(parent != null) {
//    location = SwingUtilities.convertPoint(this.parent.handle, location, parent);
//  }
  java.awt.Rectangle bounds = handle.getBounds();
  if(bounds.x == x && bounds.y == y && bounds.width == width && bounds.height == height) return;
  handle.setBounds(x, y, width, height);
  // The notification has to be immediate in SWT, so we force it
  if(bounds.width != width || bounds.height != height) {
    ((CControl)handle).getClientArea().invalidate();
    handle.validate();
    processEvent(new ComponentEvent(handle, ComponentEvent.COMPONENT_RESIZED));
  }
  // The notification has to be immediate in SWT, so we force it
  if(bounds.x != x || bounds.y != y) {
    processEvent(new ComponentEvent(handle, ComponentEvent.COMPONENT_MOVED));
  }
  handle.repaint();
}

//void setBounds (int x, int y, int width, int height, int flags) {
//	setBounds (x, y, width, height, flags, true);
//}
//
//void setBounds (int x, int y, int width, int height, int flags, boolean defer) {
//	int topHandle = topHandle ();
//	if (defer && parent != null) {
//		forceResize ();
//		WINDOWPOS [] lpwp = parent.lpwp;
//		if (lpwp == null) {
//			/*
//			* This code is intentionally commented.  All widgets that
//			* are created by SWT have WS_CLIPSIBLINGS to ensure that
//			* application code does not draw outside of the control.
//			*/
////			int count = parent.getChildrenCount ();
////			if (count > 1) {
////				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
////				if ((bits & OS.WS_CLIPSIBLINGS) == 0) flags |= OS.SWP_NOCOPYBITS;
////			}
//			SetWindowPos (topHandle, 0, x, y, width, height, flags);
//		} else {
//			int index = 0;
//			while (index < lpwp.length) {
//				if (lpwp [index] == null) break;
//				index ++;
//			}
//			if (index == lpwp.length) {
//				WINDOWPOS [] newLpwp = new WINDOWPOS [lpwp.length + 4];
//				System.arraycopy (lpwp, 0, newLpwp, 0, lpwp.length);
//				parent.lpwp = lpwp = newLpwp;
//			}
//			WINDOWPOS wp = new WINDOWPOS ();
//			wp.hwnd = topHandle;
//			wp.x = x;
//			wp.y = y;
//			wp.cx = width;
//			wp.cy = height;
//			wp.flags = flags;
//			lpwp [index] = wp;
//		}
//	} else {
//		SetWindowPos (topHandle, 0, x, y, width, height, flags);
//	}
//}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the argument. The <code>x</code> and 
 * <code>y</code> fields of the rectangle are relative to
 * the receiver's parent (or its display if its parent is null).
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param rect the new bounds for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (Rectangle rect) {
	checkWidget ();
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}

/**
 * If the argument is <code>true</code>, causes the receiver to have
 * all mouse events delivered to it until the method is called with
 * <code>false</code> as the argument.
 *
 * @param capture <code>true</code> to capture the mouse, and <code>false</code> to release it
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCapture (boolean capture) {
	checkWidget ();
	if (capture) {
	  Utils.capturedControl = this;
	} else {
		if (Utils.capturedControl == this) {
		  Utils.capturedControl = null;
		}
	}
//	if (capture) {
//		OS.SetCapture (handle);
//	} else {
//		if (OS.GetCapture () == handle) {
//			OS.ReleaseCapture ();
//		}
//	}
}

void sendMouseEvent(int eventType, Event event) {
  if(Utils.capturedControl == null || Utils.capturedControl == this) {
    sendEvent(eventType, event);
    return;
  }
  Control control = Utils.capturedControl;
  switch(eventType) {
    case SWT.MouseDown:
    case SWT.MouseUp:
      Utils.capturedControl = null;
      break;
  }
  java.awt.Point point = SwingUtilities.convertPoint(handle, event.x, event.y, control.handle);
  event.x = point.x;
  event.y = point.y;
  control.sendEvent(eventType, event);
}

//void setCursor () {
//	int lParam = OS.HTCLIENT | (OS.WM_MOUSEMOVE << 16);
//	OS.SendMessage (handle, OS.WM_SETCURSOR, handle, lParam);
//}

/**
 * Sets the receiver's cursor to the cursor specified by the
 * argument, or to the default cursor for that kind of control
 * if the argument is null.
 * <p>
 * When the mouse pointer passes over a control its appearance
 * is changed to match the control's cursor.
 * </p>
 *
 * @param cursor the new cursor (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCursor (Cursor cursor) {
	checkWidget ();
	if (cursor != null) {
    if (cursor.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    ((CControl)handle).getUserAttributeHandler().setCursor(cursor.handle);
  } else {
    ((CControl)handle).getUserAttributeHandler().setCursor (null);
  }
//	this.cursor = cursor;
//	if (OS.IsWinCE) {
//		int hCursor = cursor != null ? cursor.handle : 0;
//		OS.SetCursor (hCursor);
//		return;
//	}
//	int hwndCursor = OS.GetCapture ();
//	if (hwndCursor == 0) {
//		POINT pt = new POINT ();
//		if (!OS.GetCursorPos (pt)) return;
//		int hwnd = hwndCursor = OS.WindowFromPoint (pt);
//		while (hwnd != 0 && hwnd != handle) {
//			hwnd = OS.GetParent (hwnd);
//		}
//		if (hwnd == 0) return;
//	}
//	Control control = display.getControl (hwndCursor);
//	if (control == null) control = this;
//	control.setCursor ();
}

//void setDefaultFont () {
//	int hFont = display.systemFont ();
//	OS.SendMessage (handle, OS.WM_SETFONT, hFont, 0);
//}

private DisabledStatePanel disabledStatePanel;

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget ();
  handle.setEnabled(enabled);
  if(enabled) {
    if(disabledStatePanel != null) {
      disabledStatePanel.release();
      disabledStatePanel = null;
    }
  } else {
    if(disabledStatePanel == null) {
      disabledStatePanel = new DisabledStatePanel(this);
    }
  }
//	/*
//	* Feature in Windows.  If the receiver has focus, disabling
//	* the receiver causes no window to have focus.  The fix is
//	* to assign focus to the first ancestor window that takes
//	* focus.  If no window will take focus, set focus to the
//	* desktop.
//	*/
//	Control control = null;
//	boolean fixFocus = false;
//	if (!enabled) {
//		if (display.focusEvent != SWT.FocusOut) {
//			control = display.getFocusControl ();
//			fixFocus = isFocusAncestor (control);
//		}
//	}
//	enableWidget (enabled);
//	if (fixFocus) fixFocus (control);
}

/**
 * Causes the receiver to have the <em>keyboard focus</em>, 
 * such that all keyboard events will be delivered to it.  Focus
 * reassignment will respect applicable platform constraints.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #forceFocus
 */
public boolean setFocus () {
	checkWidget ();
	if ((style & SWT.NO_FOCUS) != 0) return false;
	return forceFocus ();
}

/**
 * Sets the font that the receiver will use to paint textual information
 * to the font specified by the argument, or to the default font for that
 * kind of control if the argument is null.
 *
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setFont (Font font) {
	checkWidget ();
	if (font != null) { 
		if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		((CControl)handle).getUserAttributeHandler().setFont(font.handle);
	} else {
	  ((CControl)handle).getUserAttributeHandler().setFont(null);
	}
  handle.repaint();
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setForeground (Color color) {
	checkWidget ();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		((CControl)handle).getUserAttributeHandler().setForeground(color.handle);
	} else {
	  ((CControl)handle).getUserAttributeHandler().setForeground (null);
  }
  handle.repaint();
}

//void setForegroundPixel (int pixel) {
//	if (foreground == pixel) return;
//	foreground = pixel;
//	OS.InvalidateRect (handle, null, true);
//}

/**
 * Sets the layout data associated with the receiver to the argument.
 * 
 * @param layoutData the new layout data for the receiver.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayoutData (Object layoutData) {
	checkWidget ();
	this.layoutData = layoutData;
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget ();
  Point offset = parent == null? new Point(0, 0): parent.getInternalOffset();
	x -= offset.x;
	y -= offset.y;
  java.awt.Point location = handle.getLocation();
  if(location.x == x && location.y == y) return;
  handle.setLocation(x, y);
//  ((CControl)handle).getClientArea().invalidate();
//  handle.validate();
  // The notification has to be immediate in SWT, so we force it
  if(location.x != x || location.y != y) {
    processEvent(new ComponentEvent(handle, ComponentEvent.COMPONENT_MOVED));
  }
  handle.repaint();
//  Container parent = handle.getParent();
//  if(parent != null) {
//    if(parent instanceof JComponent) {
//      ((JComponent)parent).revalidate();
//    } else {
//      parent.invalidate();
//      parent.validate();
//    }
//    parent.repaint();
//  }
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
 *
 * @param location the new location for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	checkWidget ();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

/**
 * Sets the receiver's pop up menu to the argument.
 * All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 * <p>
 * Note: Disposing of a control that has a pop up menu will
 * dispose of the menu.  To avoid this behavior, set the
 * menu to null before the control is disposed.
 * </p>
 *
 * @param menu the new pop up menu
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenu (Menu menu) {
	checkWidget ();
	if (menu != null) {
		if (menu.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.POP_UP) == 0) {
			error (SWT.ERROR_MENU_NOT_POP_UP);
		}
		if (menu.parent != menuShell ()) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	this.menu = menu;
}

boolean setRadioFocus () {
	return false;
}

boolean setRadioSelection (boolean value) {
	return false;
}

/**
 * If the argument is <code>false</code>, causes subsequent drawing
 * operations in the receiver to be ignored. No drawing of any kind
 * can occur in the receiver until the flag is set to true.
 * Graphics operations that occurred while the flag was
 * <code>false</code> are lost. When the flag is set to <code>true</code>,
 * the entire widget is marked as needing to be redrawn.  Nested calls
 * to this method are stacked.
 * <p>
 * Note: This operation is a hint and may not be supported on some
 * platforms or for some widgets.
 * </p>
 *
 * @param redraw the new redraw state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #redraw(int, int, int, int, boolean)
 * @see #update
 */
public void setRedraw (boolean redraw) {
	checkWidget ();
  handle.setIgnoreRepaint(!redraw);
}

//boolean setSavedFocus () {
//	return forceFocus ();
//}

/**
 * Sets the receiver's size to the point specified by the arguments.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (int width, int height) {
	checkWidget ();
  if(width < 0) width = 0;
  if(height < 0) height = 0;
  java.awt.Dimension size = handle.getSize();
  handle.setSize(width, height);
  // The notification has to be immediate in SWT, so we force it
  if(size.width != width || size.height != height) {
    ((CControl)handle).getClientArea().invalidate();
    handle.validate();
    processEvent(new ComponentEvent(handle, ComponentEvent.COMPONENT_RESIZED));
  }
  handle.repaint();
}

/**
 * Sets the receiver's size to the point specified by the argument.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause them to be
 * set to zero instead.
 * </p>
 *
 * @param size the new size for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (Point size) {
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}

boolean setTabGroupFocus () {
	return setTabItemFocus ();
}

boolean setTabItemFocus () {
	if (!handle.isShowing ()) return false;
	return forceFocus ();
}

String toolTipText;

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget ();
  toolTipText = string;
  ((CControl)handle).setToolTipText(Utils.convertStringToHTML(string));
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
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
  handle.setVisible(visible);
//	if (drawCount != 0) {
//		if (((state & HIDDEN) == 0) == visible) return;
//	} else {
//		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//		if (((bits & OS.WS_VISIBLE) != 0) == visible) return;
//	}
  // TODO: uncomment the next lines, or attach a listener to components?
//	if (visible) {
//		sendEvent (SWT.Show);
//		if (isDisposed ()) return;
//	}
	
//	/*
//	* Feature in Windows.  If the receiver has focus, hiding
//	* the receiver causes no window to have focus.  The fix is
//	* to assign focus to the first ancestor window that takes
//	* focus.  If no window will take focus, set focus to the
//	* desktop.
//	*/
//	Control control = null;
//	boolean fixFocus = false;
//	if (!visible) {
//		if (display.focusEvent != SWT.FocusOut) {
//			control = display.getFocusControl ();
//			fixFocus = isFocusAncestor (control);
//		}
//	}
//	if (drawCount != 0) {
//		state = visible ? state & ~HIDDEN : state | HIDDEN;
//	} else {
//		showWidget (visible);
//		if (isDisposed ()) return;
//	}
//	if (!visible) {
//		sendEvent (SWT.Hide);
//		if (isDisposed ()) return;
//	}
//	if (fixFocus) fixFocus (control);
}

void sort (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (items [j] <= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
}

//void subclass () {
//	int oldProc = windowProc ();
//	int newProc = display.windowProc;
//	if (oldProc == newProc) return;
//	OS.SetWindowLong (handle, OS.GWL_WNDPROC, newProc);
//}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param x the x coordinate to be translated
 * @param y the y coordinate to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public Point toControl (int x, int y) {
	checkWidget ();
  java.awt.Point point = new java.awt.Point(x, y);
  Container container = handle;
  if(container instanceof RootPaneContainer) {
    container = ((RootPaneContainer)container).getContentPane();
  }
  SwingUtilities.convertPointFromScreen(point, container);
  return new Point(point.x, point.y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toControl (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toControl (point.x, point.y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param x the x coordinate to be translated
 * @param y the y coordinate to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public Point toDisplay (int x, int y) {
	checkWidget ();
  java.awt.Point point = new java.awt.Point(x, y);
  Container container = handle;
  if(container instanceof RootPaneContainer) {
    container = ((RootPaneContainer)container).getContentPane();
  }
  SwingUtilities.convertPointToScreen(point, container);
  return new Point(point.x, point.y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toDisplay (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toDisplay (point.x, point.y);
}

//int topHandle () {
//	return handle;
//}

//boolean translateAccelerator (MSG msg) {
//	return menuShell ().translateAccelerator (msg);
//}

boolean translateMnemonic (Event event, Control control) {
	if (control == this) return false;
	if (!isVisible () || !isEnabled ()) return false;
	event.doit = mnemonicMatch (event.character);
	return traverse (event);
}

//boolean translateMnemonic (MSG msg) {
//	if (msg.wParam < 0x20) return false;
//	int hwnd = msg.hwnd;
//	if (OS.GetKeyState (OS.VK_MENU) >= 0) {
//		int code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
//		if ((code & OS.DLGC_WANTALLKEYS) != 0) return false;
//		if ((code & OS.DLGC_BUTTON) == 0) return false;
//	}
//	Decorations shell = menuShell ();
//	if (shell.isVisible () && shell.isEnabled ()) {
//		display.lastAscii = msg.wParam;
//		display.lastNull = display.lastDead = false;
//		Event event = new Event ();
//		event.detail = SWT.TRAVERSE_MNEMONIC;
//		if (setKeyState (event, SWT.Traverse, msg.wParam, msg.lParam)) {
//			return translateMnemonic (event, null) || shell.translateMnemonic (event, this);
//		}
//	}
//	return false;
//}
//
//boolean translateTraversal (MSG msg) {
//	int key = msg.wParam;
//	if (key == OS.VK_MENU) {
//		Shell shell = getShell ();
//		int hwndShell = shell.handle;
//		OS.SendMessage (hwndShell, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
//		return false;
//	}
//	int hwnd = msg.hwnd;
//	int detail = SWT.TRAVERSE_NONE;
//	boolean doit = true, all = false;
//	boolean lastVirtual = false;
//	int lastKey = key, lastAscii = 0;
//	switch (key) {
//		case OS.VK_ESCAPE: {
//			all = true;
//			lastAscii = 27;
//			int code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
//			if ((code & OS.DLGC_WANTALLKEYS) != 0) {
//				/*
//				* Use DLGC_HASSETSEL to determine that the control
//				* is a text widget.  A text widget normally wants
//				* all keys except VK_ESCAPE.  If this bit is not
//				* set, then assume the control wants all keys,
//				* including VK_ESCAPE.
//				*/
//				if ((code & OS.DLGC_HASSETSEL) == 0) doit = false;
//			}
//			detail = SWT.TRAVERSE_ESCAPE;
//			break;
//		}
//		case OS.VK_RETURN: {
//			all = true;
//			lastAscii = '\r';
//			int code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
//			if ((code & OS.DLGC_WANTALLKEYS) != 0) doit = false;
//			detail = SWT.TRAVERSE_RETURN;
//			break;
//		}
//		case OS.VK_TAB: {
//			lastAscii = '\t';
//			boolean next = OS.GetKeyState (OS.VK_SHIFT) >= 0;
//			int code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
//			if ((code & (OS.DLGC_WANTTAB | OS.DLGC_WANTALLKEYS)) != 0) {
//				/*
//				* Use DLGC_HASSETSEL to determine that the control is a
//				* text widget.  If the control is a text widget, then
//				* Ctrl+Tab and Shift+Tab should traverse out of the widget.
//				* If the control is not a text widget, the correct behavior
//				* is to give every character, including Tab, Ctrl+Tab and
//				* Shift+Tab to the control.
//				*/
//				if ((code & OS.DLGC_HASSETSEL) != 0) {
//					if (next && OS.GetKeyState (OS.VK_CONTROL) >= 0) {
//						doit = false;
//					}
//				} else {
//					doit = false;
//				}
//			}
//			if (parent != null && (parent.style & SWT.MIRRORED) != 0) {
//				if (key == OS.VK_LEFT || key == OS.VK_RIGHT) next = !next;
//			}
//			detail = next ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
//			break;
//		}
//		case OS.VK_UP:
//		case OS.VK_LEFT:
//		case OS.VK_DOWN:
//		case OS.VK_RIGHT: {
//			/*
//			* On WinCE SP there is no tab key.  Focus is assigned
//			* using the VK_UP and VK_DOWN keys, not with VK_LEFT
//			* or VK_RIGHT.
//			*/
//			if (OS.IsSP) {
//				if (key == OS.VK_LEFT || key == OS.VK_RIGHT) return false;
//			}
//			lastVirtual = true;
//			int code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
//			if ((code & (OS.DLGC_WANTARROWS /*| OS.DLGC_WANTALLKEYS*/)) != 0) doit = false;
//			boolean next = key == OS.VK_DOWN || key == OS.VK_RIGHT;
//			detail = next ? SWT.TRAVERSE_ARROW_NEXT : SWT.TRAVERSE_ARROW_PREVIOUS;
//			break;
//		}
//		case OS.VK_PRIOR:
//		case OS.VK_NEXT: {
//			all = true;
//			lastVirtual = true;
//			if (OS.GetKeyState (OS.VK_CONTROL) >= 0) return false;
//			int code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
//			if ((code & OS.DLGC_WANTALLKEYS) != 0) {
//				/*
//				* Use DLGC_HASSETSEL to determine that the control is a
//				* text widget.  If the control is a text widget, then
//				* Ctrl+PgUp and Ctrl+PgDn should traverse out of the widget.
//				*/
//				if ((code & OS.DLGC_HASSETSEL) == 0) doit = false;
//			}
//			detail = key == OS.VK_PRIOR ? SWT.TRAVERSE_PAGE_PREVIOUS : SWT.TRAVERSE_PAGE_NEXT;
//			break;
//		}
//		default:
//			return false;
//	}
//	Event event = new Event ();
//	event.doit = doit;
//	event.detail = detail;
//	display.lastKey = lastKey;
//	display.lastAscii = lastAscii;
//	display.lastVirtual = lastVirtual;
//	display.lastNull = display.lastDead = false;
//	if (!setKeyState (event, SWT.Traverse, msg.wParam, msg.lParam)) return false;
//	Shell shell = getShell ();
//	Control control = this;
//	do {
//		if (control.traverse (event)) {
//			int hwndShell = shell.handle;
//			OS.SendMessage (hwndShell, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
//			return true;
//		}
//		if (!event.doit && control.hooks (SWT.Traverse)) return false;
//		if (control == shell) return false;
//		control = control.parent;
//	} while (all && control != null);
//	return false;
//}

boolean traverse (Event event) {
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the traverse
	* event.  If this happens, return true to stop further
	* event processing.
	*/	
	sendEvent (SWT.Traverse, event);
	if (isDisposed ()) return true;
	if (!event.doit) return false;
	switch (event.detail) {
		case SWT.TRAVERSE_NONE:			return true;
		case SWT.TRAVERSE_ESCAPE:			return traverseEscape ();
		case SWT.TRAVERSE_RETURN:			return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:		return traverseGroup (true);
		case SWT.TRAVERSE_TAB_PREVIOUS:	return traverseGroup (false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);
		case SWT.TRAVERSE_MNEMONIC:		return traverseMnemonic (event.character);	
		case SWT.TRAVERSE_PAGE_NEXT:		return traversePage (true);
		case SWT.TRAVERSE_PAGE_PREVIOUS:	return traversePage (false);
	}
	return false;
}

/**
 * Based on the argument, perform one of the expected platform
 * traversal action. The argument should be one of the constants:
 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>, 
 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>, 
 * <code>SWT.TRAVERSE_ARROW_NEXT</code> and <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>.
 *
 * @param traversal the type of traversal
 * @return true if the traversal succeeded
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean traverse (int traversal) {
	checkWidget ();
	Event event = new Event ();
	event.doit = true;
	event.detail = traversal;
	return traverse (event);
}

boolean traverseEscape () {
	return false;
}

boolean traverseGroup (boolean next) {
	Control root = computeTabRoot ();
	Control group = computeTabGroup ();
	Control [] list = root.computeTabList ();
	int length = list.length;
	int index = 0;
	while (index < length) {
		if (list [index] == group) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	if (index == length) return false;
	int start = index, offset = (next) ? 1 : -1;
	while ((index = ((index + offset + length) % length)) != start) {
		Control control = list [index];
		if (!control.isDisposed () && control.setTabGroupFocus ()) {
			return true;
		}
	}
	if (group.isDisposed ()) return false;
	return group.setTabGroupFocus ();
}

boolean traverseItem (boolean next) {
	Control [] children = parent._getChildren ();
	int length = children.length;
	int index = 0;
	while (index < length) {
		if (children [index] == this) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	if (index == length) return false;
	int start = index, offset = (next) ? 1 : -1;
	while ((index = (index + offset + length) % length) != start) {
		Control child = children [index];
		if (!child.isDisposed () && child.isTabItem ()) {
			if (child.setTabItemFocus ()) return true;
		}
	}
	return false;
}

boolean traverseMnemonic (char key) {
	return mnemonicHit (key);
}

boolean traversePage (boolean next) {
	return false;
}

boolean traverseReturn () {
	return false;
}

//void unsubclass () {
//	int newProc = windowProc ();
//	int oldProc = display.windowProc;
//	if (oldProc == newProc) return;
//	OS.SetWindowLong (handle, OS.GWL_WNDPROC, newProc);
//}

/**
 * Forces all outstanding paint requests for the widget
 * to be processed before this method returns.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #redraw(int, int, int, int, boolean)
 * @see PaintListener
 * @see SWT#Paint
 */
public void update () {
	checkWidget ();
  Utils.paintComponentImmediately(handle);
//	update (false);
}

//void update (boolean all) {
////	checkWidget ();
//	if (OS.IsWinCE) {
//		OS.UpdateWindow (handle);
//	} else {
//		int flags = OS.RDW_UPDATENOW;
//		if (all) flags |= OS.RDW_ALLCHILDREN;
//		OS.RedrawWindow (handle, null, 0, flags);
//	}
//}
//
//boolean updateFont (Font oldFont, Font newFont) {
//	boolean sameFont = getFont ().equals (oldFont);
//	/* 
//	* If the font that the control is using is the
//	* same one that was being used from the Control
//	* Panel, then use the new Control Panel font.
//	*/
//	if (sameFont) setFont (newFont);
//	return sameFont;
//}

void updateBackgroundMode() {
  switch(parent.getBackgroundMode()) {
  case SWT.INHERIT_NONE: ((CControl)handle).setBackgroundInheritance(CControl.NO_BACKGROUND_INHERITANCE); break;
  case SWT.INHERIT_DEFAULT: ((CControl)handle).setBackgroundInheritance(CControl.PREFERRED_BACKGROUND_INHERITANCE); break;
  case SWT.INHERIT_FORCE: ((CControl)handle).setBackgroundInheritance(CControl.BACKGROUND_INHERITANCE); break;
  }
  handle.repaint();
}

void updateLayout (boolean resize, boolean all) {
	/* Do nothing */
}

//CREATESTRUCT widgetCreateStruct () {
//	return null;
//}
//
//int widgetExtStyle () {
//	int bits = 0;
//	if (!OS.IsPPC) {
//		if ((style & SWT.BORDER) != 0) bits |= OS.WS_EX_CLIENTEDGE;
//	}
////	if ((style & SWT.BORDER) != 0) {
////		if ((style & SWT.FLAT) == 0) bits |= OS.WS_EX_CLIENTEDGE;
////	}
//	/*
//	* Feature in Windows NT.  When CreateWindowEx() is called with
//	* WS_EX_LAYOUTRTL or WS_EX_NOINHERITLAYOUT, CreateWindowEx()
//	* fails to create the HWND. The fix is to not use these bits.
//	*/
//	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) {
//		return bits;
//	} 
//	bits |= OS.WS_EX_NOINHERITLAYOUT;
//	if ((style & SWT.RIGHT_TO_LEFT) != 0) bits |= OS.WS_EX_LAYOUTRTL;
//	return bits;
//}
//
//int widgetParent () {
//	return parent.handle;
//}
//
//int widgetStyle () {
//	/* Force clipping of siblings by setting WS_CLIPSIBLINGS */
//	int bits = OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_CLIPSIBLINGS;
////	if ((style & SWT.BORDER) != 0) {
////		if ((style & SWT.FLAT) != 0) bits |= OS.WS_BORDER;
////	}
//	if (OS.IsPPC) {
//		if ((style & SWT.BORDER) != 0) bits |= OS.WS_BORDER;
//	}
//	return bits;
//	
//	/*
//	* This code is intentionally commented.  When clipping
//	* of both siblings and children is not enforced, it is
//	* possible for application code to draw outside of the
//	* control.
//	*/
////	int bits = OS.WS_CHILD | OS.WS_VISIBLE;
////	if ((style & SWT.CLIP_SIBLINGS) != 0) bits |= OS.WS_CLIPSIBLINGS;
////	if ((style & SWT.CLIP_CHILDREN) != 0) bits |= OS.WS_CLIPCHILDREN;
////	return bits;
//}

/**
 * Changes the parent of the widget to be the one provided if
 * the underlying operating system supports this feature.
 * Returns <code>true</code> if the parent is successfully changed.
 *
 * @param parent the new parent for the control.
 * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is <code>null</code></li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *  </ul>
 */
public boolean setParent (Composite parent) {
	checkWidget ();
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.parent == parent) return true;
	if (!isReparentable ()) return false;
	releaseParent ();
	Shell newShell = parent.getShell (), oldShell = getShell ();
	Decorations newDecorations = parent.menuShell (), oldDecorations = menuShell ();
	if (oldShell != newShell || oldDecorations != newDecorations) {
		Menu [] menus = oldShell.findMenus (this);
		fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	}
  ((CControl)parent.handle).getClientArea().add(handle);
  updateBackgroundMode();
//	int topHandle = topHandle ();
//	if (OS.SetParent (topHandle, parent.handle) == 0) return false;
//	this.parent = parent;
//	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
//	SetWindowPos (topHandle, OS.HWND_BOTTOM, 0, 0, 0, 0, flags);
	return true;
}

//abstract TCHAR windowClass ();
//
//abstract int windowProc ();
//
//int windowProc (int hwnd, int msg, int wParam, int lParam) {
//	LRESULT result = null;
//	switch (msg) {
//		case OS.WM_ACTIVATE:			result = WM_ACTIVATE (wParam, lParam); break;
//		case OS.WM_CAPTURECHANGED:		result = WM_CAPTURECHANGED (wParam, lParam); break;
//		case OS.WM_CHAR:				result = WM_CHAR (wParam, lParam); break;
//		case OS.WM_CLEAR:				result = WM_CLEAR (wParam, lParam); break;
//		case OS.WM_CLOSE:				result = WM_CLOSE (wParam, lParam); break;
//		case OS.WM_COMMAND:				result = WM_COMMAND (wParam, lParam); break;
//		case OS.WM_CONTEXTMENU:			result = WM_CONTEXTMENU (wParam, lParam); break;
//		case OS.WM_CTLCOLORBTN:
//		case OS.WM_CTLCOLORDLG:
//		case OS.WM_CTLCOLOREDIT:
//		case OS.WM_CTLCOLORLISTBOX:
//		case OS.WM_CTLCOLORMSGBOX:
//		case OS.WM_CTLCOLORSCROLLBAR:
//		case OS.WM_CTLCOLORSTATIC:		result = WM_CTLCOLOR (wParam, lParam); break;
//		case OS.WM_CUT:					result = WM_CUT (wParam, lParam); break;
//		case OS.WM_DESTROY:				result = WM_DESTROY (wParam, lParam); break;
//		case OS.WM_DRAWITEM:			result = WM_DRAWITEM (wParam, lParam); break;
//		case OS.WM_ENDSESSION:			result = WM_ENDSESSION (wParam, lParam); break;
//		case OS.WM_ENTERIDLE:			result = WM_ENTERIDLE (wParam, lParam); break;
//		case OS.WM_ERASEBKGND:			result = WM_ERASEBKGND (wParam, lParam); break;
//		case OS.WM_GETDLGCODE:			result = WM_GETDLGCODE (wParam, lParam); break;
//		case OS.WM_GETFONT:				result = WM_GETFONT (wParam, lParam); break;
//		case OS.WM_GETOBJECT:			result = WM_GETOBJECT (wParam, lParam); break;
//		case OS.WM_GETMINMAXINFO:		result = WM_GETMINMAXINFO (wParam, lParam); break;
//		case OS.WM_HELP:				result = WM_HELP (wParam, lParam); break;
//		case OS.WM_HSCROLL:				result = WM_HSCROLL (wParam, lParam); break;
//		case OS.WM_IME_CHAR:			result = WM_IME_CHAR (wParam, lParam); break;
//		case OS.WM_IME_COMPOSITION:		result = WM_IME_COMPOSITION (wParam, lParam); break;
//		case OS.WM_INITMENUPOPUP:		result = WM_INITMENUPOPUP (wParam, lParam); break;
//		case OS.WM_INPUTLANGCHANGE:		result = WM_INPUTLANGCHANGE (wParam, lParam); break;
//		case OS.WM_HOTKEY:				result = WM_HOTKEY (wParam, lParam); break;		
//		case OS.WM_KEYDOWN:				result = WM_KEYDOWN (wParam, lParam); break;
//		case OS.WM_KEYUP:				result = WM_KEYUP (wParam, lParam); break;
//		case OS.WM_KILLFOCUS:			result = WM_KILLFOCUS (wParam, lParam); break;
//		case OS.WM_LBUTTONDBLCLK:		result = WM_LBUTTONDBLCLK (wParam, lParam); break;
//		case OS.WM_LBUTTONDOWN:			result = WM_LBUTTONDOWN (wParam, lParam); break;
//		case OS.WM_LBUTTONUP:			result = WM_LBUTTONUP (wParam, lParam); break;
//		case OS.WM_MBUTTONDBLCLK:		result = WM_MBUTTONDBLCLK (wParam, lParam); break;
//		case OS.WM_MBUTTONDOWN:			result = WM_MBUTTONDOWN (wParam, lParam); break;
//		case OS.WM_MBUTTONUP:			result = WM_MBUTTONUP (wParam, lParam); break;
//		case OS.WM_MEASUREITEM:			result = WM_MEASUREITEM (wParam, lParam); break;
//		case OS.WM_MENUCHAR:			result = WM_MENUCHAR (wParam, lParam); break;
//		case OS.WM_MENUSELECT:			result = WM_MENUSELECT (wParam, lParam); break;
//		case OS.WM_MOUSEACTIVATE:		result = WM_MOUSEACTIVATE (wParam, lParam); break;
//		case OS.WM_MOUSEHOVER:			result = WM_MOUSEHOVER (wParam, lParam); break;
//		case OS.WM_MOUSELEAVE:			result = WM_MOUSELEAVE (wParam, lParam); break;
//		case OS.WM_MOUSEMOVE:			result = WM_MOUSEMOVE (wParam, lParam); break;
//		case OS.WM_MOUSEWHEEL:			result = WM_MOUSEWHEEL (wParam, lParam); break;
//		case OS.WM_MOVE:				result = WM_MOVE (wParam, lParam); break;
//		case OS.WM_NCACTIVATE:			result = WM_NCACTIVATE (wParam, lParam); break;
//		case OS.WM_NCCALCSIZE:			result = WM_NCCALCSIZE (wParam, lParam); break;
//		case OS.WM_NCHITTEST:			result = WM_NCHITTEST (wParam, lParam); break;
//		case OS.WM_NCLBUTTONDOWN:		result = WM_NCLBUTTONDOWN (wParam, lParam); break;
//		case OS.WM_NOTIFY:				result = WM_NOTIFY (wParam, lParam); break;
//		case OS.WM_PAINT:				result = WM_PAINT (wParam, lParam); break;
//		case OS.WM_PALETTECHANGED:		result = WM_PALETTECHANGED (wParam, lParam); break;
//		case OS.WM_PARENTNOTIFY:		result = WM_PARENTNOTIFY (wParam, lParam); break;
//		case OS.WM_PASTE:				result = WM_PASTE (wParam, lParam); break;
//		case OS.WM_PRINTCLIENT:			result = WM_PRINTCLIENT (wParam, lParam); break;
//		case OS.WM_QUERYENDSESSION:		result = WM_QUERYENDSESSION (wParam, lParam); break;
//		case OS.WM_QUERYNEWPALETTE:		result = WM_QUERYNEWPALETTE (wParam, lParam); break;
//		case OS.WM_QUERYOPEN:			result = WM_QUERYOPEN (wParam, lParam); break;
//		case OS.WM_RBUTTONDBLCLK:		result = WM_RBUTTONDBLCLK (wParam, lParam); break;
//		case OS.WM_RBUTTONDOWN:			result = WM_RBUTTONDOWN (wParam, lParam); break;
//		case OS.WM_RBUTTONUP:			result = WM_RBUTTONUP (wParam, lParam); break;
//		case OS.WM_SETCURSOR:			result = WM_SETCURSOR (wParam, lParam); break;
//		case OS.WM_SETFOCUS:			result = WM_SETFOCUS (wParam, lParam); break;
//		case OS.WM_SETFONT:				result = WM_SETFONT (wParam, lParam); break;
//		case OS.WM_SETTINGCHANGE:		result = WM_SETTINGCHANGE (wParam, lParam); break;
//		case OS.WM_SETREDRAW:			result = WM_SETREDRAW (wParam, lParam); break;
//		case OS.WM_SHOWWINDOW:			result = WM_SHOWWINDOW (wParam, lParam); break;
//		case OS.WM_SIZE:				result = WM_SIZE (wParam, lParam); break;
//		case OS.WM_SYSCHAR:				result = WM_SYSCHAR (wParam, lParam); break;
//		case OS.WM_SYSCOLORCHANGE:		result = WM_SYSCOLORCHANGE (wParam, lParam); break;
//		case OS.WM_SYSCOMMAND:			result = WM_SYSCOMMAND (wParam, lParam); break;
//		case OS.WM_SYSKEYDOWN:			result = WM_SYSKEYDOWN (wParam, lParam); break;
//		case OS.WM_SYSKEYUP:			result = WM_SYSKEYUP (wParam, lParam); break;
//		case OS.WM_TIMER:				result = WM_TIMER (wParam, lParam); break;
//		case OS.WM_UNDO:				result = WM_UNDO (wParam, lParam); break;
//		case OS.WM_VSCROLL:				result = WM_VSCROLL (wParam, lParam); break;
//		case OS.WM_WINDOWPOSCHANGED:	result = WM_WINDOWPOSCHANGED (wParam, lParam); break;
//		case OS.WM_WINDOWPOSCHANGING:	result = WM_WINDOWPOSCHANGING (wParam, lParam); break;
//		case OS.WM_XBUTTONDBLCLK:		result = WM_XBUTTONDBLCLK (wParam, lParam); break;
//		case OS.WM_XBUTTONDOWN:			result = WM_XBUTTONDOWN (wParam, lParam); break;
//		case OS.WM_XBUTTONUP:			result = WM_XBUTTONUP (wParam, lParam); break;
//	}
//	if (result != null) return result.value;
//	return callWindowProc (hwnd, msg, wParam, lParam);
//}
//
//LRESULT WM_ACTIVATE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_CAPTURECHANGED (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_CHAR (int wParam, int lParam) {
//	return wmChar (handle, wParam, lParam);
//}
//
//LRESULT WM_CLEAR (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_CLOSE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_COMMAND (int wParam, int lParam) {
//	/*
//	* When the WM_COMMAND message is sent from a
//	* menu, the HWND parameter in LPARAM is zero.
//	*/
//	if (lParam == 0) {
//		Decorations shell = menuShell ();
//		if (shell.isEnabled ()) {
//			int id = wParam & 0xFFFF;
//			MenuItem item = display.getMenuItem (id);
//			if (item != null && item.isEnabled ()) {
//				return item.wmCommandChild (wParam, lParam);
//			}
//		}
//		return null;
//	}
//	Control control = display.getControl (lParam);
//	if (control == null) return null;
//	return control.wmCommandChild (wParam, lParam);
//}
//
//LRESULT WM_CONTEXTMENU (int wParam, int lParam) {
//	return wmContextMenu (handle, wParam, lParam);
//}
//
//LRESULT WM_CTLCOLOR (int wParam, int lParam) {
//	int hPalette = display.hPalette;
//	if (hPalette != 0) {
//		OS.SelectPalette (wParam, hPalette, false);
//		OS.RealizePalette (wParam);
//	}
//	Control control = display.getControl (lParam);
//	if (control == null) return null;
//	return control.wmColorChild (wParam, lParam);
//}
//
//LRESULT WM_CUT (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_DESTROY (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_DRAWITEM (int wParam, int lParam) {
//	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
//	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
//	if (struct.CtlType == OS.ODT_MENU) {
//		MenuItem item = display.getMenuItem (struct.itemID);
//		if (item == null) return null;
//		return item.wmDrawChild (wParam, lParam);
//	}
//	Control control = display.getControl (struct.hwndItem);
//	if (control == null) return null;
//	return control.wmDrawChild (wParam, lParam);
//}
//
//LRESULT WM_ENDSESSION (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_ENTERIDLE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_ERASEBKGND (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_GETDLGCODE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_GETFONT (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_GETOBJECT (int wParam, int lParam) {
//	if (accessible != null) {
//		int result = accessible.internal_WM_GETOBJECT (wParam, lParam);
//		if (result != 0) return new LRESULT (result);
//	}
//	return null;
//}
//
//LRESULT WM_GETMINMAXINFO (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_HOTKEY (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_HELP (int wParam, int lParam) {
//	if (OS.IsWinCE) return null;
//	HELPINFO lphi = new HELPINFO ();
//	OS.MoveMemory (lphi, lParam, HELPINFO.sizeof);
//	Decorations shell = menuShell ();
//	if (!shell.isEnabled ()) return null;
//	if (lphi.iContextType == OS.HELPINFO_MENUITEM) {
//		MenuItem item = display.getMenuItem (lphi.iCtrlId);
//		if (item != null && item.isEnabled ()) {
//			Widget widget = null;
//			if (item.hooks (SWT.Help)) {
//				widget = item;
//			} else {
//				Menu menu = item.parent;
//				if (menu.hooks (SWT.Help)) widget = menu;
//			}
//			if (widget != null) {
//				int hwndShell = shell.handle;
//				OS.SendMessage (hwndShell, OS.WM_CANCELMODE, 0, 0);
//				widget.postEvent (SWT.Help);
//				return LRESULT.ONE;
//			}
//		}
//		return null;
//	}
//	if (hooks (SWT.Help)) {
//		postEvent (SWT.Help);
//		return LRESULT.ONE;
//	}
//	return null;
//}
//
//LRESULT WM_HSCROLL (int wParam, int lParam) {
//	if (lParam == 0) return null;
//	Control control = display.getControl (lParam);
//	if (control == null) return null;
//	return control.wmScrollChild (wParam, lParam);
//}
//
//LRESULT WM_IME_CHAR (int wParam, int lParam) {
//	return wmIMEChar (handle, wParam, lParam);
//}
//
//LRESULT WM_IME_COMPOSITION (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_INITMENUPOPUP (int wParam, int lParam) {
//	
//	/* Ignore WM_INITMENUPOPUP for an accelerator */
//	if (display.accelKeyHit) return null;
//
//	/*
//	* If the high order word of LPARAM is non-zero,
//	* the menu is the system menu and we can ignore
//	* WPARAM.  Otherwise, use WPARAM to find the menu.
//	*/
//	Shell shell = getShell ();
//	Menu oldMenu = shell.activeMenu, newMenu = null;
//	if ((lParam >> 16) == 0) {
//		newMenu = menuShell ().findMenu (wParam);
//	}	
//	Menu menu = newMenu;
//	while (menu != null && menu != oldMenu) {
//		menu = menu.getParentMenu ();
//	}
//	if (menu == null) {
//		menu = shell.activeMenu;
//		while (menu != null) {
//			/*
//			* It is possible (but unlikely), that application
//			* code could have disposed the widget in the hide
//			* event.  If this happens, stop searching up the
//			* ancestor list because there is no longer a link
//			* to follow.
//			*/
//			menu.sendEvent (SWT.Hide);
//			if (menu.isDisposed ()) break;
//			menu = menu.getParentMenu ();
//			Menu ancestor = newMenu;
//			while (ancestor != null && ancestor != menu) {
//				ancestor = ancestor.getParentMenu ();
//			}
//			if (ancestor != null) break;
//		}
//	}
//	
//	/*
//	* The shell and the new menu may be disposed because of
//	* sending the hide event to the ancestor menus but setting
//	* a field to null in a disposed shell is not harmful.
//	*/
//	if (newMenu != null && newMenu.isDisposed ()) newMenu = null;
//	shell.activeMenu = newMenu;
//	
//	/* Send the show event */
//	if (newMenu != null && newMenu != oldMenu) {
//		newMenu.sendEvent (SWT.Show);
//		// widget could be disposed at this point
//	}
//	return null;
//}
//
//LRESULT WM_INPUTLANGCHANGE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_KEYDOWN (int wParam, int lParam) {
//	return wmKeyDown (handle, wParam, lParam);
//}
//
//LRESULT WM_KEYUP (int wParam, int lParam) {
//	return wmKeyUp (handle, wParam, lParam);
//}
//
//LRESULT WM_KILLFOCUS (int wParam, int lParam) {
//	return wmKillFocus (handle, wParam, lParam);
//}
//
//LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
//	return wmLButtonDblClk (handle, wParam, lParam);
//}
//
//LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
//	return wmLButtonDown (handle, wParam, lParam);
//}
//
//LRESULT WM_LBUTTONUP (int wParam, int lParam) {
//	return wmLButtonUp (handle, wParam, lParam);
//}
//
//LRESULT WM_MBUTTONDBLCLK (int wParam, int lParam) {
//	return wmMButtonDblClk (handle, wParam, lParam);
//}
//
//LRESULT WM_MBUTTONDOWN (int wParam, int lParam) {
//	return wmMButtonDown (handle, wParam, lParam);
//}
//
//LRESULT WM_MBUTTONUP (int wParam, int lParam) {
//	return wmMButtonUp (handle, wParam, lParam);
//}
//
//LRESULT WM_MEASUREITEM (int wParam, int lParam) {
//	MEASUREITEMSTRUCT struct = new MEASUREITEMSTRUCT ();
//	OS.MoveMemory (struct, lParam, MEASUREITEMSTRUCT.sizeof);
//	if (struct.CtlType == OS.ODT_MENU) {
//		MenuItem item = display.getMenuItem (struct.itemID);
//		if (item == null) return null;
//		return item.wmMeasureChild (wParam, lParam);
//	}
//	int hwnd = OS.GetDlgItem (handle, struct.CtlID);
//	Control control = display.getControl (hwnd);
//	if (control == null) return null;
//	return control.wmMeasureChild (wParam, lParam);
//}
//
//LRESULT WM_MENUCHAR (int wParam, int lParam) {
//	/*
//	* Feature in Windows.  When the user types Alt+<key>
//	* and <key> does not match a mnemonic in the System
//	* menu or the menu bar, Windows beeps.  This beep is
//	* unexpected and unwanted by applications that look
//	* for Alt+<key>.  The fix is to detect the case and
//	* stop Windows from beeping by closing the menu.
//	*/
//	int type = wParam >> 16;
//	if (type == 0 || type == OS.MF_SYSMENU) {
//		display.mnemonicKeyHit = false;
//		return new LRESULT (OS.MNC_CLOSE << 16);
//	}
//	return null;
//}
//
//LRESULT WM_MENUSELECT (int wParam, int lParam) {
//	int code = wParam >> 16;
//	Shell shell = getShell ();
//	if (code == -1 && lParam == 0) {
//		Menu menu = shell.activeMenu;
//		while (menu != null) {
//			/*
//			* When the user cancels any menu that is not the
//			* menu bar, assume a mnemonic key was pressed to open
//			* the menu from WM_SYSCHAR.  When the menu was invoked
//			* using the mouse, this assumption is wrong but not
//			* harmful.  This variable is only used in WM_SYSCHAR
//			* and WM_SYSCHAR is only sent after the user has pressed
//			* a mnemonic.
//			*/
//			display.mnemonicKeyHit = true;
//			/*
//			* It is possible (but unlikely), that application
//			* code could have disposed the widget in the hide
//			* event.  If this happens, stop searching up the
//			* parent list because there is no longer a link
//			* to follow.
//			*/
//			menu.sendEvent (SWT.Hide);
//			if (menu.isDisposed ()) break;
//			menu = menu.getParentMenu ();
//		}
//		/*
//		* The shell may be disposed because of sending the hide
//		* event to the last active menu menu but setting a field
//		* to null in a destroyed widget is not harmful.
//		*/
//		shell.activeMenu = null;
//		return null;
//	}
//	if ((code & OS.MF_SYSMENU) != 0) return null;
//	if ((code & OS.MF_HILITE) != 0) {
//		MenuItem item = null;
//		Decorations menuShell = menuShell ();
//		if ((code & OS.MF_POPUP) != 0) {
//			int index = wParam & 0xFFFF;
//			MENUITEMINFO info = new MENUITEMINFO ();
//			info.cbSize = MENUITEMINFO.sizeof;
//			info.fMask = OS.MIIM_SUBMENU;
//			if (OS.GetMenuItemInfo (lParam, index, true, info)) {
//				Menu newMenu = menuShell.findMenu (info.hSubMenu);
//				if (newMenu != null) item = newMenu.cascade;
//			}	
//		} else {
//			Menu newMenu = menuShell.findMenu (lParam);
//			if (newMenu != null) {
//				int id = wParam & 0xFFFF;
//				item = display.getMenuItem (id);
//			}
//			Menu oldMenu = shell.activeMenu;
//			if (oldMenu != null) {
//				Menu ancestor = oldMenu;
//				while (ancestor != null && ancestor != newMenu) {
//					ancestor = ancestor.getParentMenu ();
//				}
//				if (ancestor == newMenu) {
//					ancestor = oldMenu;
//					while (ancestor != newMenu) {
//						/*
//						* It is possible (but unlikely), that application
//						* code could have disposed the widget in the hide
//						* event or the item about to be armed.  If this
//						* happens, stop searching up the ancestor list
//						* because there is no longer a link to follow.
//						*/
//						ancestor.sendEvent (SWT.Hide);
//						if (ancestor.isDisposed ()) break;
//						ancestor = ancestor.getParentMenu ();
//					}
//					/*
//					* The shell and/or the item could be disposed when
//					* processing hide events from above.  If this happens,
//					* ensure that the shell is not accessed and that no
//					* arm event is sent to the item.
//					*/
//					if (!shell.isDisposed ()) {
//						if (newMenu != null && newMenu.isDisposed ()) {
//							newMenu = null;
//						}
//						shell.activeMenu = newMenu;
//					}
//					if (item != null && item.isDisposed ()) item = null;
//				}
//			}
//		}
//		if (item != null) item.sendEvent (SWT.Arm);
//	}
//	return null;
//}
//
//LRESULT WM_MOUSEACTIVATE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_MOUSEHOVER (int wParam, int lParam) {
//	return wmMouseHover (handle, wParam, lParam);
//}
//
//LRESULT WM_MOUSELEAVE (int wParam, int lParam) {
//	return wmMouseLeave (handle, wParam, lParam);
//}
//
//LRESULT WM_MOUSEMOVE (int wParam, int lParam) {
//	return wmMouseMove (handle, wParam, lParam);
//}
//
//LRESULT WM_MOUSEWHEEL (int wParam, int lParam) {
//	return wmMouseWheel (handle, wParam, lParam);
//}
//
//LRESULT WM_MOVE (int wParam, int lParam) {
//	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
//		if (OS.IsWindowVisible (handle)) {
//			if (findThemeControl () != null) {
//				int flags = OS.RDW_ERASE | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
//				OS.RedrawWindow (handle, null, 0, flags);
//			}
//		}
//	}
//	sendEvent (SWT.Move);
//	// widget could be disposed at this point
//	return null;
//}
//
//LRESULT WM_NCACTIVATE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_NCCALCSIZE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_NCHITTEST (int wParam, int lParam) {
//	if (!OS.IsWindowEnabled (handle)) return null;
//	if (!isActive ()) return new LRESULT (OS.HTTRANSPARENT);
//	return null;
//}
//
//LRESULT WM_NCLBUTTONDOWN (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_NOTIFY (int wParam, int lParam) {
//	NMHDR hdr = new NMHDR ();
//	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
//	int hwnd = hdr.hwndFrom;
//	if (hwnd == 0) return null;
//	Control control = display.getControl (hwnd);
//	if (control == null) return null;
//	return control.wmNotifyChild (wParam, lParam);
//}
//
//LRESULT WM_PAINT (int wParam, int lParam) {
//	return super.wmPaint (handle, wParam, lParam);
//}
//
//LRESULT WM_PALETTECHANGED (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_PARENTNOTIFY (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_PASTE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_PRINTCLIENT (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_QUERYENDSESSION (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_QUERYNEWPALETTE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_QUERYOPEN (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_RBUTTONDBLCLK (int wParam, int lParam) {
//	return wmRButtonDblClk (handle, wParam, lParam);
//}
//
//LRESULT WM_RBUTTONDOWN (int wParam, int lParam) {
//	return wmRButtonDown (handle, wParam, lParam);
//}
//
//LRESULT WM_RBUTTONUP (int wParam, int lParam) {
//	return wmRButtonUp (handle, wParam, lParam);
//}
//
//LRESULT WM_SETCURSOR (int wParam, int lParam) {
//	int hitTest = (short) (lParam & 0xFFFF);
// 	if (hitTest == OS.HTCLIENT) {
//		Control control = display.getControl (wParam);
//		if (control == null) return null;
//		Cursor cursor = control.findCursor ();
//		if (cursor != null) {
//			OS.SetCursor (cursor.handle);
//			return LRESULT.ONE;
//		}
//	}
//	return null;
//}
//
//LRESULT WM_SETFOCUS (int wParam, int lParam) {
//	return wmSetFocus (handle, wParam, lParam);
//}
//
//LRESULT WM_SETTINGCHANGE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_SETFONT (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_SETREDRAW (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_SHOWWINDOW (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_SIZE (int wParam, int lParam) {
//	sendEvent (SWT.Resize);
//	// widget could be disposed at this point
//	return null;
//}
//
//LRESULT WM_SYSCHAR (int wParam, int lParam) {
//	return wmSysChar (handle, wParam, lParam);
//}
//
//LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_SYSCOMMAND (int wParam, int lParam) {
//	/*
//	* Check to see if the command is a system command or
//	* a user menu item that was added to the System menu.
//	* When a user item is added to the System menu,
//	* WM_SYSCOMMAND must always return zero.
//	*/
//	if ((wParam & 0xF000) == 0) {
//		Decorations shell = menuShell ();
//		if (shell.isEnabled ()) {
//			MenuItem item = display.getMenuItem (wParam & 0xFFFF);
//			if (item != null) item.wmCommandChild (wParam, lParam);
//		}
//		return LRESULT.ZERO;
//	}
//
//	/* Process the System Command */
//	int cmd = wParam & 0xFFF0;
//	switch (cmd) {
//		case OS.SC_CLOSE:
//			int hwndShell = menuShell ().handle;
//			int bits = OS.GetWindowLong (hwndShell, OS.GWL_STYLE);
//			if ((bits & OS.WS_SYSMENU) == 0) return LRESULT.ZERO;
//			break;
//		case OS.SC_KEYMENU:
//			/*
//			* When lParam is zero, one of F10, Shift+F10, Ctrl+F10 or
//			* Ctrl+Shift+F10 was pressed.  If there is no menu bar and
//			* the focus control is interested in keystrokes, give the
//			* key to the focus control.  Normally, F10 with no menu bar
//			* moves focus to the System menu but this can be achieved
//			* using Alt+Space.  To allow the application to see F10,
//			* avoid running the default window proc.
//			* 
//			* NOTE:  When F10 is pressed, WM_SYSCOMMAND is sent to the
//			* shell, not the focus control.  This is undocumented Windows
//			* behavior.
//			*/
//			if (lParam == 0) {
//				Decorations shell = menuShell ();
//				Menu menu = shell.getMenuBar ();
//				if (menu == null) {
//					Control control = display._getFocusControl ();
//					if (control != null) {
//						if (control.hooks (SWT.KeyDown) || control.hooks (SWT.KeyUp)) {
//							display.mnemonicKeyHit = false;
//							return LRESULT.ZERO;
//						}
//					}
//				}
//			} else {
//				/*
//				* When lParam is not zero, Alt+<key> was pressed.  If the
//				* application is interested in keystrokes and there is a
//				* menu bar, check to see whether the key that was pressed
//				* matches a mnemonic on the menu bar.  Normally, Windows
//				* matches the first character of a menu item as well as
//				* matching the mnemonic character.  To allow the application
//				* to see the keystrokes in this case, avoid running the default
//				* window proc.
//				* 
//				* NOTE: When the user types Alt+Space, the System menu is
//				* activated.  In this case the application should not see
//				* the keystroke.
//				*/
//				if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
//					if (lParam != ' ') {
//						Decorations shell = menuShell ();
//						Menu menu = shell.getMenuBar ();
//						if (menu != null) {
//							char key = Display.mbcsToWcs (lParam);
//							if (key != 0) {
//								key = Character.toUpperCase (key);
//								MenuItem [] items = menu.getItems ();
//								for (int i=0; i<items.length; i++) {
//									MenuItem item = items [i];
//									String text = item.getText ();
//									char mnemonic = findMnemonic (text);
//									if (text.length () > 0 && mnemonic == 0) {
//										char ch = text.charAt (0);
//										if (Character.toUpperCase (ch) == key) {
//											display.mnemonicKeyHit = false;
//											return LRESULT.ZERO;
//										}
//									}
//								}
//							}
//						} else {
//							display.mnemonicKeyHit = false;
//						}
//					}
//				}
//			}
//			// FALL THROUGH
//		case OS.SC_HSCROLL:
//		case OS.SC_VSCROLL:
//			/*
//			* Do not allow keyboard traversal of the menu bar
//			* or scrolling when the shell is not enabled.
//			*/
//			Decorations shell = menuShell ();
//			if (!shell.isEnabled () || !shell.isActive ()) {
//				return LRESULT.ZERO;
//			}
//			break;
//		case OS.SC_MINIMIZE:
//			/* Save the focus widget when the shell is minimized */
//			menuShell ().saveFocus ();
//			break;
//	}
//	return null;
//}
//
//LRESULT WM_SYSKEYDOWN (int wParam, int lParam) {
//	return wmSysKeyDown (handle, wParam, lParam);
//}
//
//LRESULT WM_SYSKEYUP (int wParam, int lParam) {
//	return wmSysKeyUp (handle, wParam, lParam);
//}
//
//LRESULT WM_TIMER (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_UNDO (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_VSCROLL (int wParam, int lParam) {
//	if (lParam == 0) return null;
//	Control control = display.getControl (lParam);
//	if (control == null) return null;
//	return control.wmScrollChild (wParam, lParam);
//}
//
//LRESULT WM_WINDOWPOSCHANGED (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_WINDOWPOSCHANGING (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT WM_XBUTTONDBLCLK (int wParam, int lParam) {
//	return wmXButtonDblClk (handle, wParam, lParam);
//}
//
//LRESULT WM_XBUTTONDOWN (int wParam, int lParam) {
//	return wmXButtonDown (handle, wParam, lParam);
//}
//
//LRESULT WM_XBUTTONUP (int wParam, int lParam) {
//	return wmXButtonUp (handle, wParam, lParam);
//}
//
//LRESULT wmColorChild (int wParam, int lParam) {
//	if (background == -1 && foreground == -1) return null;
//	int forePixel = foreground, backPixel = background;
//	if (forePixel == -1) forePixel = defaultForeground ();
//	if (backPixel == -1) backPixel = defaultBackground ();
//	OS.SetTextColor (wParam, forePixel);
//	OS.SetBkColor (wParam, backPixel);
//	return new LRESULT (findBrush (backPixel));
//}
//
//LRESULT wmCommandChild (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT wmDrawChild (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT wmMeasureChild (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT wmNotifyChild (int wParam, int lParam) {
//	return null;
//}
//
//LRESULT wmScrollChild (int wParam, int lParam) {
//	return null;
//}

private volatile long mouseHoverTimeStamp;
private volatile Thread mouseHoverThread;
private java.awt.event.MouseEvent mouseHoverEvent;

void adjustMouseHoverState(java.awt.event.MouseEvent me) {
  mouseHoverTimeStamp = System.currentTimeMillis() + 500;
  mouseHoverEvent = me;
  if(mouseHoverThread == null) {
    mouseHoverThread = new Thread("Mouse Hover Thread") {
      public void run() {
        while(mouseHoverThread == this) {
          try {
            sleep(Math.max(50, mouseHoverTimeStamp - System.currentTimeMillis()));
          } catch(Exception e) {}
          if(mouseHoverThread != this) {
            return;
          }
          if(mouseHoverTimeStamp - System.currentTimeMillis() < 0) {
            final Thread t = this;
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                if(mouseHoverThread != t) {
                  return;
                }
                if(mouseHoverTimeStamp - System.currentTimeMillis() < 0) {
                  mouseHoverThread = null;
                  java.awt.event.MouseEvent me = mouseHoverEvent;
                  mouseHoverEvent = null;
                  // TODO: the point is incorrect when the wheel mouse is used: cf snippet125
                  if(!isDisposed() && me.getComponent().contains(me.getPoint())) {
                    UIThreadUtils.startExclusiveSection(getDisplay());
                    if(isDisposed()) {
                      UIThreadUtils.stopExclusiveSection();
                      return;
                    }
                    try {
                      sendMouseEvent(SWT.MouseHover, createMouseEvent(me, false));
                    } catch(Throwable t) {
                      UIThreadUtils.storeException(t);
                    } finally {
                      UIThreadUtils.stopExclusiveSection();
                    }
                  }
                }
              }
            });
          }
        }
      }
    };
    mouseHoverThread.setDaemon(true);
    mouseHoverThread.start();
  }
}

/**
 * The entry point for callbacks 
 * (Warning: This method is platform dependent)
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It is not available on all
 * platforms and should never be accessed from application code.
 * </p>
 */
public void processEvent(EventObject e) {
}

boolean isTraversing;
int lastPressedKeyCode;
char lastPressedKeyChar;
//int lastKeyCode;
static Runnable focusLostRunnable;

boolean isAdjustingSize;
static boolean isDragging;

/**
 * The entry point for callbacks 
 * (Warning: This method is platform dependent)
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It is not available on all
 * platforms and should never be accessed from application code.
 * </p>
 */
public void processEvent(AWTEvent e) {
  int id = e.getID();
  switch(id) {
  case java.awt.event.MouseEvent.MOUSE_CLICKED: {
    java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
    if(me.getClickCount() != 2) {
      return;
    }
    if(Utils.capturedControl == null && !hooks(SWT.MouseDoubleClick)) return;
    break;
  }
  case java.awt.event.PaintEvent.PAINT: if(!hooks(SWT.Paint)) return; break;
  case java.awt.event.MouseEvent.MOUSE_DRAGGED: if(Utils.capturedControl == null && !hooks(SWT.DragDetect) && !hooks(SWT.MouseMove) && !hooks(SWT.MouseHover)) return; break;
  case java.awt.event.MouseEvent.MOUSE_MOVED: if(Utils.capturedControl == null && !hooks(SWT.MouseMove) && !hooks(SWT.MouseHover)) return; break;
  case java.awt.event.MouseEvent.MOUSE_PRESSED: {
    if(Utils.capturedControl == null && !hooks(SWT.MouseDown) && menu == null && (!hooks(SWT.MenuDetect) || !((java.awt.event.MouseEvent)e).isPopupTrigger())) return;
    break;
  }
  case java.awt.event.MouseEvent.MOUSE_RELEASED: {
    isDragging = false;
    if(Utils.capturedControl == null && !hooks(SWT.MouseUp) && menu == null && (!hooks(SWT.MenuDetect) || !((java.awt.event.MouseEvent)e).isPopupTrigger())) return;
    break;
  }
  case java.awt.event.MouseEvent.MOUSE_WHEEL: {
    if(Utils.capturedControl == null && !hooks(SWT.MouseWheel)) {
      Object o = e.getSource();
      if(o instanceof Component && !(o instanceof Window)) {
        Component c = ((Component)o);
        Container cParent = c.getParent();
        if(cParent != null) {
          MouseWheelEvent mwe = (MouseWheelEvent)e;
          cParent.dispatchEvent(new MouseWheelEvent(cParent, mwe.getID(), mwe.getWhen(), mwe.getModifiers(), mwe.getX() + cParent.getX(), mwe.getY() + cParent.getY(), mwe.getClickCount(), mwe.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(), mwe.getWheelRotation()));
          mwe.consume();
        }
      }
      adjustMouseHoverState((java.awt.event.MouseEvent)e);
      return;
    }
    break;
  }
  case java.awt.event.MouseEvent.MOUSE_ENTERED: if(!hooks(SWT.MouseEnter)) return; break;
  case java.awt.event.MouseEvent.MOUSE_EXITED: if(!hooks(SWT.MouseExit)) {mouseHoverThread = null; return;} break;
  case java.awt.event.KeyEvent.KEY_PRESSED: {
//    if(!hooks(SWT.KeyDown) && !hooks(SWT.Traverse) && !isTraversalKey((java.awt.event.KeyEvent)e)) {
//      return;
//    }
    break;
  }
  case java.awt.event.KeyEvent.KEY_RELEASED: if(!hooks(SWT.KeyUp)) {lastPressedKeyCode = -1; lastPressedKeyChar = '\0'; return;} break;
  case java.awt.event.KeyEvent.KEY_TYPED: if(!hooks(SWT.KeyDown)) {isTraversing = false; return;} break;
  case ComponentEvent.COMPONENT_RESIZED: if(!hooks(SWT.Resize)) return; break;
  case ComponentEvent.COMPONENT_MOVED: if(!hooks(SWT.Move)) return; break;
  case ComponentEvent.COMPONENT_SHOWN: if(!hooks(SWT.Show)) return; break;
  case ComponentEvent.COMPONENT_HIDDEN: if(!hooks(SWT.Hide)) return; break;
  case java.awt.event.FocusEvent.FOCUS_GAINED: /*if(!hooks(SWT.FocusIn)) return;*/ break;
  case java.awt.event.FocusEvent.FOCUS_LOST: /*if(!hooks(SWT.FocusOut)) return;*/ break;
  default: return;
  }
  if(isDisposed()) {
    return;
  }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    return;
  }
  try {
    switch(id) {
    case java.awt.event.PaintEvent.PAINT: {
      Event event = new Event();
      event.gc = new GC(this);
      Rectangle r = this.getBounds ();
      event.width = r.width;
      event.height = r.height;
      sendEvent(SWT.Paint, event);
      if (this instanceof Canvas) {
        Canvas canvas = (Canvas) this;
        if (canvas.caret != null) {
          canvas.caret.paintCaret (event.gc);
        }
      }
      break;
    }
    case java.awt.event.MouseEvent.MOUSE_DRAGGED: {
      if(!isDragging) {
        isDragging = true;
        java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
        sendEvent(SWT.DragDetect, createMouseEvent(me, false));
      }
      // Fall through
    }
    case java.awt.event.MouseEvent.MOUSE_MOVED: {
      java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
      sendMouseEvent(SWT.MouseMove, createMouseEvent(me, false));
      adjustMouseHoverState(me);
      break;
    }
    case java.awt.event.MouseEvent.MOUSE_PRESSED: {
      java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
      Event event = createMouseEvent(me, true);
      sendMouseEvent(SWT.MouseDown, event);
      if((menu != null || hooks(SWT.MenuDetect)) && me.isPopupTrigger()) {
        java.awt.Point p = new java.awt.Point(event.x, event.y);
        SwingUtilities.convertPointToScreen(p, me.getComponent());
        event.x = p.x;
        event.y = p.y;
        sendEvent(SWT.MenuDetect, event);
        showPopup(me);
      }
      break;
    }
    case java.awt.event.MouseEvent.MOUSE_RELEASED: {
      java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
      Event event = createMouseEvent(me, true);
      sendMouseEvent(SWT.MouseUp, event);
      if((menu != null || hooks(SWT.MenuDetect)) && me.isPopupTrigger()) {
        java.awt.Point p = new java.awt.Point(event.x, event.y);
        SwingUtilities.convertPointToScreen(p, me.getComponent());
        event.x = p.x;
        event.y = p.y;
        sendEvent(SWT.MenuDetect, event);
        showPopup(me);
      }
      break;
    }
    case java.awt.event.MouseEvent.MOUSE_CLICKED: sendMouseEvent(SWT.MouseDoubleClick, createMouseEvent((java.awt.event.MouseEvent)e, false)); break;
    case java.awt.event.MouseEvent.MOUSE_WHEEL: {
      sendMouseEvent(SWT.MouseWheel, createMouseEvent((java.awt.event.MouseEvent)e, false));
      adjustMouseHoverState((java.awt.event.MouseEvent)e);
      break;
    }
    case java.awt.event.MouseEvent.MOUSE_ENTERED: sendEvent(SWT.MouseEnter, createMouseEvent((java.awt.event.MouseEvent)e, false)); break;
    case java.awt.event.MouseEvent.MOUSE_EXITED:
      mouseHoverThread = null;
      sendEvent(SWT.MouseExit, createMouseEvent((java.awt.event.MouseEvent)e, false));
      break;
    case java.awt.event.KeyEvent.KEY_TYPED: {
//      if(!isTraversing) {
//        java.awt.event.KeyEvent ke = (java.awt.event.KeyEvent)e;
//        ke.setKeyCode(lastKeyCode);
//        if (ke.getWhen () > lastPressed) {
//          Event event = createKeyEvent (ke);
//          sendEvent (SWT.KeyDown, event);
//          sendEvent (SWT.KeyUp, event);
//        }
//      }
      java.awt.event.KeyEvent ke = (java.awt.event.KeyEvent)e;
      if((ke.getModifiers() & KeyEvent.ALT_GRAPH_MASK) != 0) {
        if(hooks(SWT.KeyDown)) {
          KeyEvent ke2 = new KeyEvent(ke.getComponent(), KeyEvent.KEY_RELEASED, ke.getWhen(), ke.getModifiers(), lastPressedKeyCode, ke.getKeyChar(), ke.getKeyLocation());
          Event event = createKeyEvent(ke2);
          event.stateMask |= SWT.CTRL | SWT.ALT;
          sendEvent(SWT.KeyDown, event);
        }
//        lastPressedKeyCode = ke.getKeyCode();
        lastPressedKeyChar = ke.getKeyChar();
      }
      isTraversing = false;
      break;
    }
    case java.awt.event.KeyEvent.KEY_PRESSED: {
      java.awt.event.KeyEvent ke = (java.awt.event.KeyEvent)e;
      int pressedKeyCode = ke.getKeyCode();
      if(pressedKeyCode == lastPressedKeyCode) {
        boolean isBlocked = false;
        switch(pressedKeyCode) {
        case java.awt.event.KeyEvent.VK_CONTROL:
        case java.awt.event.KeyEvent.VK_SHIFT:
        case java.awt.event.KeyEvent.VK_ALT:
          isBlocked = true;
        }
        if(isBlocked) break;
      }
      lastPressedKeyCode = pressedKeyCode;
      int AltGrMask = KeyEvent.CTRL_MASK | KeyEvent.ALT_MASK;
      if((ke.getModifiers() & AltGrMask) == AltGrMask) {
        boolean isBlocked = true;
        switch(pressedKeyCode) {
          case java.awt.event.KeyEvent.VK_CONTROL:
          case java.awt.event.KeyEvent.VK_SHIFT:
          case java.awt.event.KeyEvent.VK_ALT:
            isBlocked = false;
        }
        if(isBlocked) break;
      }
      lastPressedKeyChar = ke.getKeyChar();
//      lastKeyCode = lastPressedKeyCode;
      if(isTraversalKey(ke)) {
        isTraversing = processTraversalKey(ke);
      }
      if(!isTraversing) {
        if(hooks(SWT.KeyDown)) {
          sendEvent(SWT.KeyDown, createKeyEvent(ke));
        }
//        lastPressed = ke.getWhen ();
      }
      break;
    }
    case java.awt.event.KeyEvent.KEY_RELEASED: {
      java.awt.event.KeyEvent ke = (java.awt.event.KeyEvent)e;
      int AltGrMask = KeyEvent.CTRL_MASK | KeyEvent.ALT_MASK;
      boolean isSending = true;
      if((ke.getModifiers() & AltGrMask) == AltGrMask) {
        KeyEvent ke2 = new KeyEvent(ke.getComponent(), KeyEvent.KEY_RELEASED, ke.getWhen(), ke.getModifiers(), lastPressedKeyCode, lastPressedKeyChar, ke.getKeyLocation());
        sendEvent(SWT.KeyUp, createKeyEvent(ke2));
        isSending = false;
      }
      lastPressedKeyCode = -1;
      lastPressedKeyChar = '\0';
      if(isSending && !isTraversing) {
        sendEvent(SWT.KeyUp, createKeyEvent(ke));
      }
      break;
    }
    case ComponentEvent.COMPONENT_RESIZED: if(!isAdjustingSize) sendEvent(SWT.Resize); break;
    case ComponentEvent.COMPONENT_MOVED: sendEvent(SWT.Move); break;
    case ComponentEvent.COMPONENT_SHOWN: sendEvent(SWT.Show); break;
    case ComponentEvent.COMPONENT_HIDDEN: sendEvent(SWT.Hide); break;
    case java.awt.event.FocusEvent.FOCUS_GAINED: {
      if(focusLostRunnable != null) {
        focusLostRunnable.run();
      }
      Shell shell = getShell ();
      if(!shell.isDisposed()) {
        shell.setActiveControl(this);
      }
      sendEvent(SWT.FocusIn);
      break;
    }
    case java.awt.event.FocusEvent.FOCUS_LOST: {
      Runnable runnable = new Runnable() {
        public void run() {
          if(focusLostRunnable != this) {
            return;
          }
          focusLostRunnable = null;
          UIThreadUtils.startExclusiveSection(display);
          try {
            Shell shell = getShell();
            Display display = getDisplay();
            if (shell != display.getActiveShell ()) {
              shell.setActiveControl (null);
            }
            sendEvent(SWT.FocusOut);
          } catch(Throwable t) {
            UIThreadUtils.storeException(t);
          } finally {
            UIThreadUtils.stopExclusiveSection();
          }
        }
      };
      focusLostRunnable = runnable;
      break;
    }
    }
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

static final Point DEFAULT_EVENT_OFFSET = new Point(0, 0);

Point getInternalOffset() {
  Component clientArea = ((CControl)handle).getClientArea();
  // Code is duplicated in GC.getGraphics() in inverse
  if(clientArea != handle) {
    if(clientArea.getParent() instanceof JViewport) {
      JViewport viewport = (JViewport)clientArea.getParent();
      int offsetX = 0;
      int offsetY = 0;
      java.awt.Point viewPosition = viewport.getViewPosition();
      offsetX -= viewPosition.x;
      offsetY -= viewPosition.y;
      JViewport columnHeader = ((JScrollPane)viewport.getParent()).getColumnHeader();
      if(columnHeader != null && columnHeader.isVisible()) {
        offsetY += columnHeader.getHeight();
      }
      return new Point(offsetX, offsetY);
    }
  }
  return DEFAULT_EVENT_OFFSET;
}

protected boolean isTraversalKey(java.awt.event.KeyEvent ke) {
  switch(ke.getKeyCode()) {
  case java.awt.event.KeyEvent.VK_TAB:
    return true;
  }
  return false;
}

protected int getTraversalKeyDetail(java.awt.event.KeyEvent ke) {
  switch(ke.getKeyCode()) {
  case java.awt.event.KeyEvent.VK_TAB:
    if ((ke.getModifiersEx() & java.awt.event.KeyEvent.SHIFT_DOWN_MASK) != 0) {
      return SWT.TRAVERSE_TAB_PREVIOUS;
    }
    return SWT.TRAVERSE_TAB_NEXT;
  default:
    return -1;
  }
}

protected boolean getTraversalKeyDefault(java.awt.event.KeyEvent ke) {
  switch(ke.getKeyCode()) {
  case java.awt.event.KeyEvent.VK_TAB:
    return true;
  default:
    return false;
  }
}

private boolean processTraversalKey(java.awt.event.KeyEvent ke) {
  Event event = createKeyEvent(ke);
  event.detail = getTraversalKeyDetail(ke);
  if(event.detail < 0) {
    throw new IllegalArgumentException("The traversal key does not define its action type!");
  }
  event.doit = getTraversalKeyDefault(ke);
  if(hooks(SWT.Traverse)) {
    event.doit = false;
    sendEvent(SWT.Traverse, event);
  }
  validateTraversalKey(ke, event);
  return event.doit;
}

protected void validateTraversalKey(java.awt.event.KeyEvent ke, Event event) {
  if(!event.doit) {
    return;
  }
  switch(ke.getKeyCode()) {
  case java.awt.event.KeyEvent.VK_TAB:
    ke.consume();
    if ((ke.getModifiersEx() & java.awt.event.KeyEvent.SHIFT_DOWN_MASK) != 0) {
      traverse(SWT.TRAVERSE_TAB_PREVIOUS);
    } else {
      traverse(SWT.TRAVERSE_TAB_NEXT);
    }
    break;
  }
}

private void showPopup(java.awt.event.MouseEvent e) {
  if(menu != null) {
    java.awt.Point p = new java.awt.Point(e.getPoint());
    SwingUtilities.convertPointToScreen(p, e.getComponent());
    menu.setLocation(new Point(p.x, p.y));
    menu.setVisible(true);
  }
}

Event createMouseEvent(java.awt.event.MouseEvent me, boolean isPreviousInputState) {
  if(me == null) {
    return null;
  }
  Event event = new Event();
  Container container = handle;
  java.awt.Point point = me.getPoint();
  Component component = me.getComponent();
  if(container instanceof RootPaneContainer) {
    container = ((RootPaneContainer)container).getContentPane();
    point = SwingUtilities.convertPoint(component, point, container);
  } else {
    point = SwingUtilities.convertPoint(component, point, ((CControl)handle).getClientArea());
  }
  Point offset = getInternalOffset();
  event.x = point.x + offset.x;
  event.y = point.y + offset.y;
  if (SwingUtilities.isLeftMouseButton (me)) {
    event.button = 1;
  } else if (SwingUtilities.isRightMouseButton (me)) {
    event.button = 2;
  } else if (SwingUtilities.isMiddleMouseButton (me)) {
    event.button = 3;
  }
  event.stateMask = isPreviousInputState? Display.getPreviousInputState(): Display.getInputState();
  return event;
}

Event createKeyEvent(java.awt.event.KeyEvent ke) {
  if(ke == null) {
    return null;
  }
  Event event = new Event();
  Container container = handle;
  if(container instanceof RootPaneContainer) {
    container = ((RootPaneContainer)container).getContentPane();
  }
  int modifiersEx = ke.getModifiersEx();
  if((modifiersEx & java.awt.event.MouseEvent.BUTTON1_DOWN_MASK) != 0) {
    event.button = 1;
  } else if((modifiersEx & java.awt.event.MouseEvent.BUTTON2_DOWN_MASK) != 0) {
    event.button = 2;
  } else if((modifiersEx & java.awt.event.MouseEvent.BUTTON3_DOWN_MASK) != 0) {
    event.button = 3;
  }
  char c = ke.getKeyChar();
  if(c == java.awt.event.KeyEvent.CHAR_UNDEFINED) {
    c = 0;
  }
//  if((modifiersEx & java.awt.event.KeyEvent.SHIFT_DOWN_MASK) != 0 && (modifiersEx & java.awt.event.KeyEvent.CTRL_DOWN_MASK) != 0) {
    int keyCode = ke.getKeyCode();
    if(keyCode >= java.awt.event.KeyEvent.VK_A && keyCode <= java.awt.event.KeyEvent.VK_Z) {
      event.keyCode = (char)('a' - java.awt.event.KeyEvent.VK_A + keyCode);
    } else if(keyCode >= java.awt.event.KeyEvent.VK_0 && keyCode <= java.awt.event.KeyEvent.VK_9) {
      event.keyCode = (char)('0' - java.awt.event.KeyEvent.VK_0 + keyCode);
    }
//  }
  if(ke.getKeyLocation() != java.awt.event.KeyEvent.KEY_LOCATION_NUMPAD) {
    switch(ke.getKeyCode()) {
    case java.awt.event.KeyEvent.VK_ALT: event.keyCode = SWT.ALT; break;
    case java.awt.event.KeyEvent.VK_SHIFT: event.keyCode = SWT.SHIFT; break;
    case java.awt.event.KeyEvent.VK_CONTROL: event.keyCode = SWT.CONTROL; break;
    case java.awt.event.KeyEvent.VK_WINDOWS: event.keyCode = SWT.COMMAND; break;
    /* Non-Numeric Keypad Keys */
    case java.awt.event.KeyEvent.VK_UP: event.keyCode = SWT.ARROW_UP; break;
    case java.awt.event.KeyEvent.VK_DOWN: event.keyCode = SWT.ARROW_DOWN; break;
    case java.awt.event.KeyEvent.VK_LEFT: event.keyCode = SWT.ARROW_LEFT; break;
    case java.awt.event.KeyEvent.VK_RIGHT: event.keyCode = SWT.ARROW_RIGHT; break;
    case java.awt.event.KeyEvent.VK_PAGE_UP: event.keyCode = SWT.PAGE_UP; break;
    case java.awt.event.KeyEvent.VK_PAGE_DOWN: event.keyCode = SWT.PAGE_DOWN; break;
    case java.awt.event.KeyEvent.VK_HOME: event.keyCode = SWT.HOME; break;
    case java.awt.event.KeyEvent.VK_END: event.keyCode = SWT.END; break;
    case java.awt.event.KeyEvent.VK_INSERT: event.keyCode = SWT.INSERT; break;
    /* Virtual and Ascii Keys */
    case java.awt.event.KeyEvent.VK_BACK_SPACE: event.keyCode = SWT.BS; break;
    case java.awt.event.KeyEvent.VK_ENTER: event.keyCode = SWT.CR; break;
    case java.awt.event.KeyEvent.VK_DELETE: event.keyCode = SWT.DEL; break;
    case java.awt.event.KeyEvent.VK_ESCAPE: event.keyCode = SWT.ESC; break;
//    case java.awt.event.KeyEvent.VK_ENTER: event.keyCode = SWT.LF; break;
    case java.awt.event.KeyEvent.VK_TAB: event.keyCode = SWT.TAB; break;
    /* Functions Keys */
    case java.awt.event.KeyEvent.VK_F1: event.keyCode = SWT.F1; break;
    case java.awt.event.KeyEvent.VK_F2: event.keyCode = SWT.F2; break;
    case java.awt.event.KeyEvent.VK_F3: event.keyCode = SWT.F3; break;
    case java.awt.event.KeyEvent.VK_F4: event.keyCode = SWT.F4; break;
    case java.awt.event.KeyEvent.VK_F5: event.keyCode = SWT.F5; break;
    case java.awt.event.KeyEvent.VK_F6: event.keyCode = SWT.F6; break;
    case java.awt.event.KeyEvent.VK_F7: event.keyCode = SWT.F7; break;
    case java.awt.event.KeyEvent.VK_F8: event.keyCode = SWT.F8; break;
    case java.awt.event.KeyEvent.VK_F9: event.keyCode = SWT.F9; break;
    case java.awt.event.KeyEvent.VK_F10: event.keyCode = SWT.F10; break;
    case java.awt.event.KeyEvent.VK_F11: event.keyCode = SWT.F11; break;
    case java.awt.event.KeyEvent.VK_F12: event.keyCode = SWT.F12; break;
    case java.awt.event.KeyEvent.VK_F13: event.keyCode = SWT.F13; break;
    case java.awt.event.KeyEvent.VK_F14: event.keyCode = SWT.F14; break;
    case java.awt.event.KeyEvent.VK_F15: event.keyCode = SWT.F15; break;
    /* Other keys */
    case java.awt.event.KeyEvent.VK_CAPS_LOCK: event.keyCode = SWT.CAPS_LOCK; break;
    case java.awt.event.KeyEvent.VK_NUM_LOCK: event.keyCode = SWT.NUM_LOCK; break;
    case java.awt.event.KeyEvent.VK_SCROLL_LOCK: event.keyCode = SWT.SCROLL_LOCK; break;
    case java.awt.event.KeyEvent.VK_PAUSE: event.keyCode = SWT.PAUSE; break;
    case java.awt.event.KeyEvent.VK_CANCEL: event.keyCode = SWT.BREAK; break;
    case java.awt.event.KeyEvent.VK_PRINTSCREEN: event.keyCode = SWT.PRINT_SCREEN; break;
    case java.awt.event.KeyEvent.VK_HELP: event.keyCode = SWT.HELP; break;
    }
  } else {
    /* Numeric Keypad Keys */
    switch(ke.getKeyCode()) {
    case java.awt.event.KeyEvent.VK_MULTIPLY: event.keyCode = SWT.KEYPAD_MULTIPLY; break;
    case java.awt.event.KeyEvent.VK_ADD: event.keyCode = SWT.KEYPAD_ADD; break;
    case java.awt.event.KeyEvent.VK_ENTER: event.keyCode = SWT.KEYPAD_CR; break;
    case java.awt.event.KeyEvent.VK_SUBTRACT: event.keyCode = SWT.KEYPAD_SUBTRACT; break;
    case java.awt.event.KeyEvent.VK_DECIMAL: event.keyCode = SWT.KEYPAD_DECIMAL; break;
    case java.awt.event.KeyEvent.VK_DIVIDE: event.keyCode = SWT.KEYPAD_DIVIDE; break;
    case java.awt.event.KeyEvent.VK_NUMPAD0: event.keyCode = SWT.KEYPAD_0; break;
    case java.awt.event.KeyEvent.VK_NUMPAD1: event.keyCode = SWT.KEYPAD_1; break;
    case java.awt.event.KeyEvent.VK_NUMPAD2: event.keyCode = SWT.KEYPAD_2; break;
    case java.awt.event.KeyEvent.VK_NUMPAD3: event.keyCode = SWT.KEYPAD_3; break;
    case java.awt.event.KeyEvent.VK_NUMPAD4: event.keyCode = SWT.KEYPAD_4; break;
    case java.awt.event.KeyEvent.VK_NUMPAD5: event.keyCode = SWT.KEYPAD_5; break;
    case java.awt.event.KeyEvent.VK_NUMPAD6: event.keyCode = SWT.KEYPAD_6; break;
    case java.awt.event.KeyEvent.VK_NUMPAD7: event.keyCode = SWT.KEYPAD_7; break;
    case java.awt.event.KeyEvent.VK_NUMPAD8: event.keyCode = SWT.KEYPAD_8; break;
    case java.awt.event.KeyEvent.VK_NUMPAD9: event.keyCode = SWT.KEYPAD_9; break;
    case java.awt.event.KeyEvent.VK_EQUALS: event.keyCode = SWT.KEYPAD_EQUAL; break;
    }
  }
  event.character = c;
  event.stateMask = Display.getInputState();
  switch(event.keyCode) {
  case SWT.CONTROL:
  case SWT.SHIFT:
  case SWT.ALT:
    if(ke.getID() == KeyEvent.KEY_PRESSED) {
      event.stateMask &= ~event.keyCode;
    } else {
      event.stateMask |= event.keyCode;
    }
    break;
  }
  return event;
}

}

