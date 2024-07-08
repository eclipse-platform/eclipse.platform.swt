/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.CControl;
import org.eclipse.swt.internal.swing.Compatibility;
import org.eclipse.swt.internal.swing.JTracker;
import org.eclipse.swt.internal.swing.Utils;

/**
 *  Instances of this class implement rubber banding rectangles that are
 *  drawn onto a parent <code>Composite</code> or <code>Display</code>.
 *  These rectangles can be specified to respond to mouse and key events
 *  by either moving or resizing themselves accordingly.  Trackers are
 *  typically used to represent window geometries in a lightweight manner.
 *  
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, UP, DOWN, RESIZE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Move, Resize</dd>
 * </dl>
 * <p>
 * Note: Rectangle move behavior is assumed unless RESIZE is specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tracker extends Widget {
  
	protected final class JTrackerExtension extends JTracker {
    
	  protected final Window window;

    private JTrackerExtension(Component component, boolean isResizeType, Window window) {
      super(component, isResizeType);
      this.window = window;
    }

    protected TrackerWindow createTrackerWindow(Window ownerWindow) {
      return new TrackerWindow(ownerWindow) {
        public java.awt.Cursor getCursor() {
          return Utils.globalCursor != null? Utils.globalCursor: super.getCursor();
        }
      };
    }

    protected Window createSharedOwnerWindow() {
      return window != null? window: super.createSharedOwnerWindow();
    }

    protected void releaseSharedOwnerWindow(Window sharedOwnerWindow) {
      if(window == null) {
        super.releaseSharedOwnerWindow(sharedOwnerWindow);
      }
    }

    protected void processMouseEvent(MouseEvent me) {
      if(!Compatibility.IS_JAVA_5_OR_GREATER) {
        Utils.trackMouseProperties(me);
      }
      super.processMouseEvent(me);
      if(Utils.capturedControl != null) {
        Component target = ((CControl)Utils.capturedControl.handle).getClientArea();
        java.awt.Point point = SwingUtilities.convertPoint((Component)me.getSource(), me.getX(), me.getY(), target);
        if(me.getID() == MouseEvent.MOUSE_WHEEL) {
          MouseWheelEvent mwe = (MouseWheelEvent)me;
          me = new MouseWheelEvent(target, mwe.getID(), mwe.getWhen(), mwe.getModifiers(), point.x, point.y, mwe.getClickCount(), mwe.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(), mwe.getWheelRotation());
        } else {
          me = new MouseEvent(target, me.getID(), me.getWhen(), me.getModifiers(), point.x, point.y, me.getClickCount(), me.isPopupTrigger());
        }
        target.dispatchEvent(me);
      }
    }
    
    public void updateCursor() {
      Window ownerWindow = getSharedOwnerWindow();
      if(ownerWindow == null) {
        return;
      }
      Window w = new Window(ownerWindow) {
        public boolean isFocusable() {
          return false;
        }
        public java.awt.Cursor getCursor() {
          return Utils.globalCursor != null? Utils.globalCursor: super.getCursor();
        }
      };
      w.setFocusableWindowState(false);
      Point mouseLocation;
      if(Compatibility.IS_JAVA_5_OR_GREATER) {
        mouseLocation = MouseInfo.getPointerInfo().getLocation();
      } else {
        mouseLocation = getLastMouseLocation();
      }
      if(mouseLocation != null) {
        w.setBounds(mouseLocation.x, mouseLocation.y, 1, 1);
        w.setVisible(true);
        w.dispose();
      }
    }
    
    public void hide() {
      Utils.setGlobalCursor(null);
      updateCursor();
      super.hide();
    }
    
  }

  Control parent;
	JTrackerExtension handle;
	boolean stippled;
	Cursor cursor;
//	boolean tracking, cancelled;
//	Rectangle [] rectangles, proportions;
//	Rectangle bounds;
//	int resizeCursor, clientCursor, cursorOrientation = SWT.NONE;
//	boolean inEvent = false;
//	int oldProc, oldX, oldY;

//	/*
//	* The following values mirror step sizes on Windows
//	*/
//	final static int STEPSIZE_SMALL = 1;
//	final static int STEPSIZE_LARGE = 9;

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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#UP
 * @see SWT#DOWN
 * @see SWT#RESIZE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tracker (Composite parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
  createWidget ();
}

/**
 * Constructs a new instance of this class given the display
 * to create it on and a style value describing its behavior
 * and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p><p>
 * Note: Currently, null can be passed in for the display argument.
 * This has the effect of creating the tracker on the currently active
 * display if there is one. If there is no current display, the 
 * tracker is created on a "default" display. <b>Passing in null as
 * the display argument is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param display the display to create the tracker on
 * @param style the style of control to construct
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 * 
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#UP
 * @see SWT#DOWN
 */
public Tracker (Display display, int style) {
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.style = checkStyle (style);
	this.display = display;
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
public void addControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize, typedListener);
	addListener (SWT.Move, typedListener);
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

//Point adjustMoveCursor () {
//	int newX = bounds.x + bounds.width / 2;
//	int newY = bounds.y;
//  Utils.notImplemented(); return null;
////	POINT pt = new POINT ();
////	pt.x = newX;  pt.y = newY;
////	/*
////	 * Convert to screen coordinates iff needed
//// 	 */
////	if (parent != null) {
////		OS.ClientToScreen (parent.handle, pt);
////	}
////	OS.SetCursorPos (pt.x, pt.y);
////	return new Point (pt.x, pt.y);
//}

//Point adjustResizeCursor () {
//	int newX, newY;
//
//	if ((cursorOrientation & SWT.LEFT) != 0) {
//		newX = bounds.x;
//	} else if ((cursorOrientation & SWT.RIGHT) != 0) {
//		newX = bounds.x + bounds.width;
//	} else {
//		newX = bounds.x + bounds.width / 2;
//	}
//
//	if ((cursorOrientation & SWT.UP) != 0) {
//		newY = bounds.y;
//	} else if ((cursorOrientation & SWT.DOWN) != 0) {
//		newY = bounds.y + bounds.height;
//	} else {
//		newY = bounds.y + bounds.height / 2;
//	}
//
//  Utils.notImplemented(); return null;
//	POINT pt = new POINT ();
//	pt.x = newX;  pt.y = newY;
//	/*
//	 * Convert to screen coordinates iff needed
//	 */
//	if (parent != null) {
//		OS.ClientToScreen (parent.handle, pt);
//	}
//	OS.SetCursorPos (pt.x, pt.y);
//
//	/*
//	* If the client has not provided a custom cursor then determine
//	* the appropriate resize cursor.
//	*/
//	if (clientCursor == 0) {
//		int newCursor = 0;
//		switch (cursorOrientation) {
//			case SWT.UP:
//				newCursor = OS.LoadCursor (0, OS.IDC_SIZENS);
//				break;
//			case SWT.DOWN:
//				newCursor = OS.LoadCursor (0, OS.IDC_SIZENS);
//				break;
//			case SWT.LEFT:
//				newCursor = OS.LoadCursor (0, OS.IDC_SIZEWE);
//				break;
//			case SWT.RIGHT:
//				newCursor = OS.LoadCursor (0, OS.IDC_SIZEWE);
//				break;
//			case SWT.LEFT | SWT.UP:
//				newCursor = OS.LoadCursor (0, OS.IDC_SIZENWSE);
//				break;
//			case SWT.RIGHT | SWT.DOWN:
//				newCursor = OS.LoadCursor (0, OS.IDC_SIZENWSE);
//				break;
//			case SWT.LEFT | SWT.DOWN:
//				newCursor = OS.LoadCursor (0, OS.IDC_SIZENESW);
//				break;
//			case SWT.RIGHT | SWT.UP:
//				newCursor = OS.LoadCursor (0, OS.IDC_SIZENESW);
//				break;
//			default:
//				newCursor = OS.LoadCursor (0, OS.IDC_SIZEALL);
//				break;
//		}
//		OS.SetCursor (newCursor);
//		if (resizeCursor != 0) {
//			OS.DestroyCursor (resizeCursor);
//		}
//		resizeCursor = newCursor;
//	}
//		
//	return new Point (pt.x, pt.y);
//}

static int checkStyle (int style) {
	if ((style & (SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN)) == 0) {
		style |= SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN;
	}
	return style;
}

/**
 * Stops displaying the tracker rectangles.  Note that this is not considered
 * to be a cancelation by the user.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void close () {
	checkWidget ();
	handle.hide();
//	tracking = false;
}

//Rectangle computeBounds () {
//	int xMin = rectangles [0].x;
//	int yMin = rectangles [0].y;
//	int xMax = rectangles [0].x + rectangles [0].width;
//	int yMax = rectangles [0].y + rectangles [0].height;
//	
//	for (int i = 1; i < rectangles.length; i++) {
//		if (rectangles [i].x < xMin) xMin = rectangles [i].x;
//		if (rectangles [i].y < yMin) yMin = rectangles [i].y;
//		int rectRight = rectangles [i].x + rectangles [i].width;
//		if (rectRight > xMax) xMax = rectRight;		
//		int rectBottom = rectangles [i].y + rectangles [i].height;
//		if (rectBottom > yMax) yMax = rectBottom;
//	}
//	
//	return new Rectangle (xMin, yMin, xMax - xMin, yMax - yMin);
//}

//Rectangle [] computeProportions (Rectangle [] rects) {
//	Rectangle [] result = new Rectangle [rects.length];
//	bounds = computeBounds ();
//	for (int i = 0; i < rects.length; i++) {
//		int x = 0, y = 0, width = 0, height = 0;
//		if (bounds.width != 0) {
//			x = (rects [i].x - bounds.x) * 100 / bounds.width;
//			width = rects [i].width * 100 / bounds.width;
//		} else {
//			width = 100;
//		}
//		if (bounds.height != 0) {
//			y = (rects [i].y - bounds.y) * 100 / bounds.height;
//			height = rects [i].height * 100 / bounds.height;
//		} else {
//			height = 100;
//		}
//		result [i] = new Rectangle (x, y, width, height);			
//	}
//	return result;
//}

///**
// * Draw the rectangles displayed by the tracker.
// */
//void drawRectangles (Rectangle [] rects, boolean stippled) {
//	int bandWidth = 1;
//	int hwndTrack = OS.GetDesktopWindow ();
//	if (parent != null) hwndTrack = parent.handle;
//	int hDC = OS.GetDCEx (hwndTrack, 0, OS.DCX_CACHE);
//	int hBitmap = 0, hBrush = 0, oldBrush = 0;
//	if (stippled) {
//		bandWidth = 3;
//		byte [] bits = {-86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0};
//		hBitmap = OS.CreateBitmap (8, 8, 1, 1, bits);
//		hBrush = OS.CreatePatternBrush (hBitmap);
//		oldBrush = OS.SelectObject (hDC, hBrush);
//	}
//	for (int i=0; i<rects.length; i++) {
//		Rectangle rect = rects [i];
//		OS.PatBlt (hDC, rect.x, rect.y, rect.width, bandWidth, OS.PATINVERT);
//		OS.PatBlt (hDC, rect.x, rect.y + bandWidth, bandWidth, rect.height - (bandWidth * 2), OS.PATINVERT);
//		OS.PatBlt (hDC, rect.x + rect.width - bandWidth, rect.y + bandWidth, bandWidth, rect.height - (bandWidth * 2), OS.PATINVERT);
//		OS.PatBlt (hDC, rect.x, rect.y + rect.height - bandWidth, rect.width, bandWidth, OS.PATINVERT);
//	}
//	if (stippled) {
//		OS.SelectObject (hDC, oldBrush);
//		OS.DeleteObject (hBrush);
//		OS.DeleteObject (hBitmap);
//	}
//	OS.ReleaseDC (hwndTrack, hDC);
//}

void createWidget () {
  Control cursorControl = display.getCursorControl();
  final Window window;
  if(cursorControl != null && !Compatibility.IS_JAVA_5_OR_GREATER) {
    window = SwingUtilities.getWindowAncestor(cursorControl.handle);
  } else {
    window = null;
  }
  Component component = parent == null? null: ((CControl)parent.handle).getClientArea();
  handle = new JTrackerExtension(component, (style & SWT.RESIZE) != 0, window);
  handle.addTrackerListener(new JTracker.TrackerListener() {
    public void trackerMoved(JTracker.TrackerEvent e) {
      Event event = new Event();
      event.x = e.getX();
      event.y = e.getY();
      sendEvent(SWT.Move, event);
    }
    public void trackerResized(JTracker.TrackerEvent e) {
      Event event = new Event();
      event.x = e.getX();
      event.y = e.getY();
      sendEvent(SWT.Resize, event);
    }
  });
  int constraints = 0;
  if((style & SWT.UP) != 0) {
    constraints |= JTracker.UP_MASK;
  }
  if((style & SWT.LEFT) != 0) {
    constraints |= JTracker.LEFT_MASK;
  }
  if((style & SWT.DOWN) != 0) {
    constraints |= JTracker.DOWN_MASK;
  }
  if((style & SWT.RIGHT) != 0) {
    constraints |= JTracker.RIGHT_MASK;
  }
  handle.setConstraints(constraints);
}

/**
 * Returns the bounds that are being drawn, expressed relative to the parent
 * widget.  If the parent is a <code>Display</code> then these are screen
 * coordinates.
 *
 * @return the bounds of the Rectangles being drawn
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle [] getRectangles () {
	checkWidget();
	java.awt.Rectangle[] rectangles = handle.getRectangles();
	int length = 0;
	if (rectangles != null) length = rectangles.length;
	Rectangle [] result = new Rectangle [length];
	for (int i = 0; i < length; i++) {
		java.awt.Rectangle current = rectangles [i];
		result [i] = new Rectangle (current.x, current.y, current.width, current.height);
	}
	return result;
}

/**
 * Returns <code>true</code> if the rectangles are drawn with a stippled line, <code>false</code> otherwise.
 *
 * @return the stippled effect of the rectangles
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getStippled () {
	checkWidget ();
	return stippled;
}

//void moveRectangles (int xChange, int yChange) {
//	if (xChange < 0 && ((style & SWT.LEFT) == 0)) xChange = 0;
//	if (xChange > 0 && ((style & SWT.RIGHT) == 0)) xChange = 0;
//	if (yChange < 0 && ((style & SWT.UP) == 0)) yChange = 0;
//	if (yChange > 0 && ((style & SWT.DOWN) == 0)) yChange = 0;
//	if (xChange == 0 && yChange == 0) return;
//	bounds.x += xChange; bounds.y += yChange;
//	for (int i = 0; i < rectangles.length; i++) {
//		rectangles [i].x += xChange;
//		rectangles [i].y += yChange;
//	}
//}

/**
 * Displays the Tracker rectangles for manipulation by the user.  Returns when
 * the user has either finished manipulating the rectangles or has cancelled the
 * Tracker.
 * 
 * @return <code>true</code> if the user did not cancel the Tracker, <code>false</code> otherwise
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean open () {
	checkWidget ();
	if (!hasRectangles) return false;
//	cancelled = false;
//	tracking = true;

//	/*
//	* If exactly one of UP/DOWN is specified as a style then set the cursor
//	* orientation accordingly (the same is done for LEFT/RIGHT styles below).
//	*/
//	int vStyle = style & (SWT.UP | SWT.DOWN);
//	if (vStyle == SWT.UP || vStyle == SWT.DOWN) {
//		cursorOrientation |= vStyle;
//	}
//	int hStyle = style & (SWT.LEFT | SWT.RIGHT);
//	if (hStyle == SWT.LEFT || hStyle == SWT.RIGHT) {
//		cursorOrientation |= hStyle;
//	}
  Utils.setGlobalCursor(cursor == null? null: cursor.handle);
	boolean isValid = handle.show();
  Utils.setGlobalCursor(null);
  return isValid;
//	/*
//	* If this tracker is being created without a mouse drag then
//	* we need to create a transparent window that fills the screen
//	* in order to get all mouse/keyboard events that occur
//	* outside of our visible windows (ie.- over the desktop).
//	*/
//	int hwndTransparent = 0;
//	Callback newProc = null;
//	boolean mouseDown = OS.GetKeyState(OS.VK_LBUTTON) < 0;
//	if (!mouseDown) {
//		int width = OS.GetSystemMetrics (OS.SM_CXSCREEN);
//		int height = OS.GetSystemMetrics (OS.SM_CYSCREEN);
//		hwndTransparent = OS.CreateWindowEx (
//			OS.WS_EX_TRANSPARENT,
//			display.windowClass,
//			null,
//			OS.WS_POPUP | OS.WS_VISIBLE,
//			0, 0,
//			width, height,
//			0,
//			0,
//			OS.GetModuleHandle (null),
//			null);
//		oldProc = OS.GetWindowLong (hwndTransparent, OS.GWL_WNDPROC);
//		newProc = new Callback (this, "transparentProc", 4); //$NON-NLS-1$
//		OS.SetWindowLong (hwndTransparent, OS.GWL_WNDPROC, newProc.getAddress ());
//	}
//
//	update ();
//	drawRectangles (rectangles, stippled);
//	Point cursorPos;
//	if (mouseDown) {
//		POINT pt = new POINT ();
//		OS.GetCursorPos (pt);
//		cursorPos = new Point (pt.x, pt.y);
//	} else {
//		if ((style & SWT.RESIZE) != 0) {
//			cursorPos = adjustResizeCursor ();
//		} else {
//			cursorPos = adjustMoveCursor ();
//		}
//	}
//	oldX = cursorPos.x;
//	oldY = cursorPos.y;
//
//	/* Tracker behaves like a Dialog with its own OS event loop. */
//	MSG msg = new MSG ();
//	while (tracking && !cancelled) {
//		if (parent != null && parent.isDisposed ()) break;
//		OS.GetMessage (msg, 0, 0, 0);
//		OS.TranslateMessage (msg);
//		switch (msg.message) {
//			case OS.WM_LBUTTONUP:
//			case OS.WM_MOUSEMOVE:
//				wmMouse (msg.message, msg.wParam, msg.lParam);
//				break;
//			case OS.WM_IME_CHAR: wmIMEChar (msg.hwnd, msg.wParam, msg.lParam); break;
//			case OS.WM_CHAR: wmChar (msg.hwnd, msg.wParam, msg.lParam); break;
//			case OS.WM_KEYDOWN: wmKeyDown (msg.hwnd, msg.wParam, msg.lParam); break;
//			case OS.WM_KEYUP: wmKeyUp (msg.hwnd, msg.wParam, msg.lParam); break;
//			case OS.WM_SYSCHAR: wmSysChar (msg.hwnd, msg.wParam, msg.lParam); break;
//			case OS.WM_SYSKEYDOWN: wmSysKeyDown (msg.hwnd, msg.wParam, msg.lParam); break;
//			case OS.WM_SYSKEYUP: wmSysKeyUp (msg.hwnd, msg.wParam, msg.lParam); break;
//		}
//		if (OS.WM_KEYFIRST <= msg.message && msg.message <= OS.WM_KEYLAST) continue;
//		if (OS.WM_MOUSEFIRST <= msg.message && msg.message <= OS.WM_MOUSELAST) continue;
//		if (msg.message == OS.WM_PAINT) {
//			update ();
//			drawRectangles (rectangles, stippled);
//		}
//		OS.DispatchMessage (msg);
//		if (msg.message == OS.WM_PAINT) {
//			drawRectangles (rectangles, stippled);
//		}
//	}
//	if (mouseDown) OS.ReleaseCapture ();
//	if (!isDisposed()) {
//		update ();
//		drawRectangles (rectangles, stippled);
//	}
//	/*
//	* Cleanup: If a transparent window was created in order to capture events then
//	* destroy it and its callback object now.
//	*/
//	if (hwndTransparent != 0) {
//		OS.DestroyWindow (hwndTransparent);
//	}
//	if (newProc != null) {
//		newProc.dispose ();
//		oldProc = 0;
//	}
//	/*
//	* Cleanup: If this tracker was resizing then the last cursor that it created
//	* needs to be destroyed.
//	*/
//	if (resizeCursor != 0) {
//		OS.DestroyCursor (resizeCursor);
//		resizeCursor = 0;
//	}
//	tracking = false;
//	return !cancelled;
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
	eventTable.unhook (SWT.Resize, listener);
	eventTable.unhook (SWT.Move, listener);
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

//void resizeRectangles (int xChange, int yChange) {
//	/*
//	* If the cursor orientation has not been set in the orientation of
//	* this change then try to set it here.
//	*/
//	if (xChange < 0 && ((style & SWT.LEFT) != 0) && ((cursorOrientation & SWT.RIGHT) == 0)) {
//		cursorOrientation |= SWT.LEFT;
//	}
//	if (xChange > 0 && ((style & SWT.RIGHT) != 0) && ((cursorOrientation & SWT.LEFT) == 0)) {
//		cursorOrientation |= SWT.RIGHT;
//	}
//	if (yChange < 0 && ((style & SWT.UP) != 0) && ((cursorOrientation & SWT.DOWN) == 0)) {
//		cursorOrientation |= SWT.UP;
//	}
//	if (yChange > 0 && ((style & SWT.DOWN) != 0) && ((cursorOrientation & SWT.UP) == 0)) {
//		cursorOrientation |= SWT.DOWN;
//	}
//	
//	/*
//	 * If the bounds will flip about the x or y axis then apply the adjustment
//	 * up to the axis (ie.- where bounds width/height becomes 0), change the
//	 * cursor's orientation accordingly, and flip each Rectangle's origin (only
//	 * necessary for > 1 Rectangles) 
//	 */
//	if ((cursorOrientation & SWT.LEFT) != 0) {
//		if (xChange > bounds.width) {
//			if ((style & SWT.RIGHT) == 0) return;
//			cursorOrientation |= SWT.RIGHT;
//			cursorOrientation &= ~SWT.LEFT;
//			bounds.x += bounds.width;
//			xChange -= bounds.width;
//			bounds.width = 0;
//			if (proportions.length > 1) {
//				for (int i = 0; i < proportions.length; i++) {
//					Rectangle proportion = proportions [i];
//					proportion.x = 100 - proportion.x - proportion.width;
//				}
//			}
//		}
//	} else if ((cursorOrientation & SWT.RIGHT) != 0) {
//		if (bounds.width < -xChange) {
//			if ((style & SWT.LEFT) == 0) return;
//			cursorOrientation |= SWT.LEFT;
//			cursorOrientation &= ~SWT.RIGHT;
//			xChange += bounds.width;
//			bounds.width = 0;
//			if (proportions.length > 1) {
//				for (int i = 0; i < proportions.length; i++) {
//					Rectangle proportion = proportions [i];
//					proportion.x = 100 - proportion.x - proportion.width;
//				}
//			}
//		}
//	}
//	if ((cursorOrientation & SWT.UP) != 0) {
//		if (yChange > bounds.height) {
//			if ((style & SWT.DOWN) == 0) return;
//			cursorOrientation |= SWT.DOWN;
//			cursorOrientation &= ~SWT.UP;
//			bounds.y += bounds.height;
//			yChange -= bounds.height;
//			bounds.height = 0;
//			if (proportions.length > 1) {
//				for (int i = 0; i < proportions.length; i++) {
//					Rectangle proportion = proportions [i];
//					proportion.y = 100 - proportion.y - proportion.height;
//				}
//			}
//		}
//	} else if ((cursorOrientation & SWT.DOWN) != 0) {
//		if (bounds.height < -yChange) {
//			if ((style & SWT.UP) == 0) return;
//			cursorOrientation |= SWT.UP;
//			cursorOrientation &= ~SWT.DOWN;
//			yChange += bounds.height;
//			bounds.height = 0;
//			if (proportions.length > 1) {
//				for (int i = 0; i < proportions.length; i++) {
//					Rectangle proportion = proportions [i];
//					proportion.y = 100 - proportion.y - proportion.height;
//				}
//			}
//		}
//	}
//	
//	// apply the bounds adjustment
//	if ((cursorOrientation & SWT.LEFT) != 0) {
//		bounds.x += xChange;
//		bounds.width -= xChange;
//	} else if ((cursorOrientation & SWT.RIGHT) != 0) {
//		bounds.width += xChange;
//	}
//	if ((cursorOrientation & SWT.UP) != 0) {
//		bounds.y += yChange;
//		bounds.height -= yChange;
//	} else if ((cursorOrientation & SWT.DOWN) != 0) {
//		bounds.height += yChange;
//	}
//	
//	Rectangle [] newRects = new Rectangle [rectangles.length];
//	for (int i = 0; i < rectangles.length; i++) {
//		Rectangle proportion = proportions[i];
//		newRects[i] = new Rectangle (
//			proportion.x * bounds.width / 100 + bounds.x,
//			proportion.y * bounds.height / 100 + bounds.y,
//			proportion.width * bounds.width / 100,
//			proportion.height * bounds.height / 100);
//	}
//	rectangles = newRects;	
//}

/**
 * Sets the <code>Cursor</code> of the Tracker.  If this cursor is <code>null</code>
 * then the cursor reverts to the default.
 *
 * @param newCursor the new <code>Cursor</code> to display
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCursor(Cursor newCursor) {
	checkWidget();
	if(cursor == newCursor) {
	  return;
	}
	cursor = newCursor;
	if(handle.isVisible()) {
	  Utils.setGlobalCursor(cursor == null? null: cursor.handle);
	}
	handle.updateCursor();
}

boolean hasRectangles;

/**
 * Specifies the rectangles that should be drawn, expressed relative to the parent
 * widget.  If the parent is a Display then these are screen coordinates.
 *
 * @param rectangles the bounds of the rectangles to be drawn
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of rectangles is null or contains a null rectangle</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setRectangles (Rectangle [] rectangles) {
	checkWidget ();
	if (rectangles == null) error (SWT.ERROR_NULL_ARGUMENT);
//	int length = rectangles.length;
//	this.rectangles = new Rectangle [length];
//	for (int i = 0; i < length; i++) {
//		Rectangle current = rectangles [i];
//		if (current == null) error (SWT.ERROR_NULL_ARGUMENT);
//		this.rectangles [i] = new Rectangle (current.x, current.y, current.width, current.height);
//	}
	java.awt.Rectangle[] recs = new java.awt.Rectangle[rectangles.length];
	for(int i=0; i<rectangles.length; i++) {
	  Rectangle current = rectangles [i];
	  if (current == null) error (SWT.ERROR_NULL_ARGUMENT);
	  recs[i] = new java.awt.Rectangle(current.x, current.y, current.width, current.height);
	}
	hasRectangles = true;
	handle.setRectangles(recs);
//	proportions = computeProportions (rectangles);
}

/**
 * Changes the appearance of the line used to draw the rectangles.
 *
 * @param stippled <code>true</code> if rectangle should appear stippled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setStippled (boolean stippled) {
	checkWidget ();
	this.stippled = stippled;
	handle.setAppearance(stippled? JTracker.THICK_BORDER_APPEARANCE: JTracker.SINGLE_LINE_APPEARANCE);
}

//int transparentProc (int hwnd, int msg, int wParam, int lParam) {
//	switch (msg) {
//		/*
//		* We typically do not want to answer that the transparent window is
//		* transparent to hits since doing so negates the effect of having it
//		* to grab events.  However, clients of the tracker should not be aware
//		* of this transparent window.  Therefore if there is a hit query
//		* performed as a result of client code then answer that the transparent
//		* window is transparent to hits so that its existence will not impact
//		* the client.
//		*/
//		case OS.WM_NCHITTEST:
//			if (inEvent) return OS.HTTRANSPARENT;
//			break;
//		case OS.WM_SETCURSOR:
//			if (clientCursor != 0) {
//				OS.SetCursor (clientCursor);
//				return 1;
//			}
//			if (resizeCursor != 0) {
//				OS.SetCursor (resizeCursor);
//				return 1;
//			}
//	}
//	return OS.CallWindowProc (oldProc, hwnd, msg, wParam, lParam);
//}
//
//void update () {
//	if (parent != null) {
//		if (parent.isDisposed ()) return;
//		Shell shell = parent.getShell ();
//		shell.update (true);
//	} else {
//		display.update ();
//	}
//}
//
//LRESULT wmKeyDown (int hwnd, int wParam, int lParam) {
//	LRESULT result = super.wmKeyDown (hwnd, wParam, lParam);
//	if (result != null) return result;
//	boolean isMirrored = parent != null && (parent.style & SWT.MIRRORED) != 0;
//	int stepSize = OS.GetKeyState (OS.VK_CONTROL) < 0 ? STEPSIZE_SMALL : STEPSIZE_LARGE;
//	int xChange = 0, yChange = 0;
//	switch (wParam) {
//		case OS.VK_ESCAPE:
//			cancelled = true;
//			tracking = false;
//			break;
//		case OS.VK_RETURN:
//			tracking = false;
//			break;
//		case OS.VK_LEFT:
//			xChange = isMirrored ? stepSize : -stepSize;
//			break;
//		case OS.VK_RIGHT:
//			xChange = isMirrored ? -stepSize : stepSize;
//			break;
//		case OS.VK_UP:
//			yChange = -stepSize;
//			break;
//		case OS.VK_DOWN:
//			yChange = stepSize;
//			break;
//	}
//	if (xChange != 0 || yChange != 0) {
//		Rectangle [] oldRectangles = rectangles;
//		boolean oldStippled = stippled;
//		Rectangle [] rectsToErase = new Rectangle [rectangles.length];
//		for (int i = 0; i < rectangles.length; i++) {
//			Rectangle current = rectangles [i];
//			rectsToErase [i] = new Rectangle (current.x, current.y, current.width, current.height);
//		}
//		Event event = new Event ();
//		event.x = oldX + xChange;
//		event.y = oldY + yChange;
//		Point cursorPos;
//		if ((style & SWT.RESIZE) != 0) {
//			resizeRectangles (xChange, yChange);
//			inEvent = true;
//			sendEvent (SWT.Resize, event);
//			inEvent = false;
//			/*
//			* It is possible (but unlikely) that application
//			* code could have disposed the widget in the resize
//			* event.  If this happens return false to indicate
//			* that the tracking has failed.
//			*/
//			if (isDisposed ()) {
//				cancelled = true;
//				return LRESULT.ONE;
//			}
//			boolean draw = false;
//			/*
//			 * It is possible that application code could have
//			 * changed the rectangles in the resize event.  If this
//			 * happens then only redraw the tracker if the rectangle
//			 * values have changed.
//			 */
//			if (rectangles != oldRectangles) {
//				int length = rectangles.length;
//				if (length != rectsToErase.length) {
//					draw = true;
//				} else {
//					for (int i = 0; i < length; i++) {
//						if (!rectangles [i].equals (rectsToErase [i])) {
//							draw = true;
//							break;
//						}
//					}
//				}
//			} else {
//				draw = true;
//			}
//			if (draw) {
//				drawRectangles (rectsToErase, oldStippled);
//				update ();
//				drawRectangles (rectangles, stippled);
//			}
//			cursorPos = adjustResizeCursor ();
//		} else {
//			moveRectangles (xChange, yChange);
//			inEvent = true;
//			sendEvent (SWT.Move, event);
//			inEvent = false;
//			/*
//			* It is possible (but unlikely) that application
//			* code could have disposed the widget in the move
//			* event.  If this happens return false to indicate
//			* that the tracking has failed.
//			*/
//			if (isDisposed ()) {
//				cancelled = true;
//				return LRESULT.ONE;
//			}
//			boolean draw = false;
//			/*
//			 * It is possible that application code could have
//			 * changed the rectangles in the move event.  If this
//			 * happens then only redraw the tracker if the rectangle
//			 * values have changed.
//			 */
//			if (rectangles != oldRectangles) {
//				int length = rectangles.length;
//				if (length != rectsToErase.length) {
//					draw = true;
//				} else {
//					for (int i = 0; i < length; i++) {
//						if (!rectangles [i].equals (rectsToErase [i])) {
//							draw = true;
//							break;
//						}
//					}
//				}
//			} else {
//				draw = true;
//			}
//			if (draw) {
//				drawRectangles (rectsToErase, oldStippled);
//				update ();
//				drawRectangles (rectangles, stippled);
//			}
//			cursorPos = adjustMoveCursor ();
//		}
//		oldX = cursorPos.x;
//		oldY = cursorPos.y;
//	}
//	return result;
//}
//
//LRESULT wmSysKeyDown (int hwnd, int wParam, int lParam) {
//	LRESULT result = super.wmSysKeyDown (hwnd, wParam, lParam);
//	if (result != null) return result;
//	cancelled = true;			
//	tracking = false;
//	return result;
//}
//
//LRESULT wmMouse (int message, int wParam, int lParam) {
//	boolean isMirrored = parent != null && (parent.style & SWT.MIRRORED) != 0;
//	int newPos = OS.GetMessagePos ();
//	int newX = (short) (newPos & 0xFFFF);
//	int newY = (short) (newPos >> 16);	
//	if (newX != oldX || newY != oldY) {
//		Rectangle [] oldRectangles = rectangles;
//		boolean oldStippled = stippled;
//		Rectangle [] rectsToErase = new Rectangle [rectangles.length];
//		for (int i = 0; i < rectangles.length; i++) {
//			Rectangle current = rectangles [i];
//			rectsToErase [i] = new Rectangle (current.x, current.y, current.width, current.height);
//		}
//		Event event = new Event ();
//		event.x = newX;
//		event.y = newY;
//		if ((style & SWT.RESIZE) != 0) {
//			if (isMirrored) {
//			   resizeRectangles (oldX - newX, newY - oldY);
//			} else {
//			   resizeRectangles (newX - oldX, newY - oldY);
//			}
//			inEvent = true;
//			sendEvent (SWT.Resize, event);
//			inEvent = false;
//			/*
//			* It is possible (but unlikely), that application
//			* code could have disposed the widget in the resize
//			* event.  If this happens, return false to indicate
//			* that the tracking has failed.
//			*/
//			if (isDisposed ()) {
//				cancelled = true;
//				return LRESULT.ONE;
//			}
//			boolean draw = false;
//			/*
//			 * It is possible that application code could have
//			 * changed the rectangles in the resize event.  If this
//			 * happens then only redraw the tracker if the rectangle
//			 * values have changed.
//			 */
//			if (rectangles != oldRectangles) {
//				int length = rectangles.length;
//				if (length != rectsToErase.length) {
//					draw = true;
//				} else {
//					for (int i = 0; i < length; i++) {
//						if (!rectangles [i].equals (rectsToErase [i])) {
//							draw = true;
//							break;
//						}
//					}
//				}
//			}
//			else {
//				draw = true;
//			}
//			if (draw) {
//				drawRectangles (rectsToErase, oldStippled);
//				update ();
//				drawRectangles (rectangles, stippled);
//			}
//			Point cursorPos = adjustResizeCursor ();
//			newX = cursorPos.x;  newY = cursorPos.y;
//		} else {
//			if (isMirrored) {
//				moveRectangles (oldX - newX, newY - oldY); 
//			} else { 
//				moveRectangles (newX - oldX, newY - oldY);
//			}
//			inEvent = true;
//			sendEvent (SWT.Move, event);
//			inEvent = false;
//			/*
//			* It is possible (but unlikely), that application
//			* code could have disposed the widget in the move
//			* event.  If this happens, return false to indicate
//			* that the tracking has failed.
//			*/
//			if (isDisposed ()) {
//				cancelled = true;
//				return LRESULT.ONE;
//			}
//			boolean draw = false;
//			/*
//			 * It is possible that application code could have
//			 * changed the rectangles in the move event.  If this
//			 * happens then only redraw the tracker if the rectangle
//			 * values have changed.
//			 */
//			if (rectangles != oldRectangles) {
//				int length = rectangles.length;
//				if (length != rectsToErase.length) {
//					draw = true;
//				} else {
//					for (int i = 0; i < length; i++) {
//						if (!rectangles [i].equals (rectsToErase [i])) {
//							draw = true;
//							break;
//						}
//					}
//				}
//			} else {
//				draw = true;
//			}
//			if (draw) {
//				drawRectangles (rectsToErase, oldStippled);
//				update ();
//				drawRectangles (rectangles, stippled);
//			}
//		}
//		oldX = newX;
//		oldY = newY;
//	}
//	tracking = message != OS.WM_LBUTTONUP;
//	return null;
//}

}
