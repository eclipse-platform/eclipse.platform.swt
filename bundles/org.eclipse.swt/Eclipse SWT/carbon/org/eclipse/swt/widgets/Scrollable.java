/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.ControlKind;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * This class is the abstract superclass of all classes which
 * represent controls that have standard scroll bars.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>H_SCROLL, V_SCROLL</dd>
 * <dt><b>Events:</b>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class Scrollable extends Control {
 	int scrolledHandle;
	ScrollBar horizontalBar, verticalBar;
	
Scrollable () {
	/* Do nothing */
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
 * @see SWT#H_SCROLL
 * @see SWT#V_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Scrollable (Composite parent, int style) {
	super (parent, style);
}

/**
 * Given a desired <em>client area</em> for the receiver
 * (as described by the arguments), returns the bounding
 * rectangle which would be required to produce that client
 * area.
 * <p>
 * In other words, it returns a rectangle such that, if the
 * receiver's bounds were set to that rectangle, the area
 * of the receiver which is capable of displaying data
 * (that is, not covered by the "trimmings") would be the
 * rectangle described by the arguments (relative to the
 * receiver's parent).
 * </p>
 * 
 * @param x the desired x coordinate of the client area
 * @param y the desired y coordinate of the client area
 * @param width the desired width of the client area
 * @param height the desired height of the client area
 * @return the required bounds to produce the given client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getClientArea
 */
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricScrollBarWidth, outMetric);
	if (horizontalBar != null) height += outMetric [0];
	if (verticalBar != null) width += outMetric [0];
	Rect inset = inset ();
	x -= inset.left;
	y -= inset.top;
	width += inset.left + inset.right;
	height += inset.top + inset.bottom;
	return new Rectangle (x, y, width, height);
}

ScrollBar createScrollBar (int style) {
    return new ScrollBar (this, style);
}

ScrollBar createStandardBar (int style) {
	int barHandle = findStandardBar (style);
	if (barHandle == 0) return null;
	ScrollBar bar = new ScrollBar ();
	bar.parent = this;
	bar.style = style;
	bar.display = display;
	bar.handle = barHandle;
	bar.register ();
	bar.hookEvents ();
	return bar;
}

void createWidget () {
	super.createWidget ();
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
}

void deregister () {
	super.deregister ();
	if (scrolledHandle != 0) display.removeWidget (scrolledHandle);
}

void destroyScrollBar (ScrollBar bar) {
	setScrollBarVisible (bar, false);
	bar.destroyHandle ();
}

int findStandardBar (int style) {
	int parentHandle = scrolledHandle != 0 ? scrolledHandle : handle;
	short [] count = new short [1];
	OS.CountSubControls (parentHandle, count);
	if (count [0] == 0) return 0;
	int barHandle = 0;
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricScrollBarWidth, outMetric);
	int [] outControl = new int [1];
	ControlKind kind = new ControlKind ();
	int [] property = new int [1];
	for (int i = 0; i < count [0]; i++) {
		OS.GetIndexedSubControl (parentHandle, (short) (i + 1), outControl);		
		OS.GetControlKind (outControl [0], kind);
		if (kind.kind == OS.kControlKindScrollBar) {
			property [0] = 0;
			OS.GetControlProperty (outControl [0], Display.SWT0, Display.SWT0, 4, null, property);
			if (property [0] == 0) {
				Point point = getControlSize (outControl [0]);
				if ((style & SWT.H_SCROLL) != 0) {
					if (point.y == outMetric [0]) barHandle = outControl [0];
				} else {
					if (point.x == outMetric [0]) barHandle = outControl [0];
				}
			}
		}
	}
	return barHandle;
}

public int getBorderWidth () {
	checkWidget();
	if ((state & CANVAS) != 0 && hasBorder ()) {
		int [] outMetric = new int [1];
		OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
		return outMetric [0];
	}
	return 0;
}

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data (that is,
 * not covered by the "trimmings").
 * 
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeTrim
 */
public Rectangle getClientArea () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	return new Rectangle (0, 0, rect.right - rect.left, rect.bottom - rect.top);
}

/**
 * Returns the receiver's horizontal scroll bar if it has
 * one, and null if it does not.
 *
 * @return the horizontal scroll bar (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ScrollBar getHorizontalBar () {
	checkWidget();
	return horizontalBar;
}

/**
 * Returns the receiver's vertical scroll bar if it has
 * one, and null if it does not.
 *
 * @return the vertical scroll bar (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ScrollBar getVerticalBar () {
	checkWidget();
	return verticalBar;
}

void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0 && scrolledHandle != 0) {
		int controlProc = display.controlProc;
		int [] mask = new int [] {
			OS.kEventClassControl, OS.kEventControlDraw,
		};
		int controlTarget = OS.GetControlEventTarget (scrolledHandle);
		OS.InstallEventHandler (controlTarget, controlProc, mask.length / 2, mask, scrolledHandle, null);
	}
}

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp) || hooks (SWT.Traverse);
}

Rect inset () {
	if ((state & CANVAS) != 0) {
		Rect rect = new Rect ();
		int [] outMetric = new int [1];
		if (drawFocusRing () && (style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
			rect.left += outMetric [0];
			rect.top += outMetric [0];
			rect.right += outMetric [0];
			rect.bottom += outMetric [0];
		}
		if (hasBorder ()) {
			OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
			rect.left += outMetric [0];
			rect.top += outMetric [0];
			rect.right += outMetric [0];
			rect.bottom += outMetric [0];
		}
		return rect;
	}
	return EMPTY_RECT;
}

boolean isTrimHandle (int trimHandle) {
	if (horizontalBar != null && horizontalBar.handle == trimHandle) return true;
	if (verticalBar != null && verticalBar.handle == trimHandle) return true;
	return trimHandle == scrolledHandle;
}

int kEventMouseWheelMoved (int nextHandler, int theEvent, int userData) {
	int vPosition = verticalBar == null ? 0 : verticalBar.getSelection ();
	int hPosition = horizontalBar == null ? 0 : horizontalBar.getSelection ();
	int result = super.kEventMouseWheelMoved (nextHandler, theEvent, userData);
	boolean redraw = false;
	if (verticalBar != null) {
		int position = verticalBar.getSelection ();
		if (position != vPosition) {
			Event event = new Event ();
			event.detail = position < vPosition ? SWT.PAGE_UP : SWT.PAGE_DOWN; 
			verticalBar.sendEvent (SWT.Selection, event);
			redraw = true;
		}
	}
	if (horizontalBar != null) {
		int position = horizontalBar.getSelection ();
		if (position != hPosition) {
			Event event = new Event ();
			event.detail = position < hPosition ? SWT.PAGE_UP : SWT.PAGE_DOWN; 
			horizontalBar.sendEvent (SWT.Selection, event);
			redraw = true;
		}
	}
	if (redraw) redrawBackgroundImage ();
	return result;
}

void redrawBackgroundImage () {
	if (scrolledHandle == 0) {
		Control control = findBackgroundControl();
		if (control != null && control.backgroundImage != null) {
			redrawWidget (handle, false);
		}
	}
}

void register () {
	super.register ();
	if (scrolledHandle != 0) display.addWidget (scrolledHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	scrolledHandle = 0;
}

void releaseChildren (boolean destroy) {
	if (horizontalBar != null) {
		horizontalBar.release (false);
		horizontalBar = null;
	}
	if (verticalBar != null) {
		verticalBar.release (false);
		verticalBar = null;
	}
	super.releaseChildren (destroy);
}

void resetVisibleRegion (int control) {
	if (verticalBar != null) verticalBar.resetVisibleRegion (control);
	if (horizontalBar != null) horizontalBar.resetVisibleRegion (control);
	super.resetVisibleRegion (control);
}

void resizeClientArea () {
	if (scrolledHandle == 0) return;
	if ((state & CANVAS) == 0) return;
	int vWidth = 0, hHeight = 0;
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricScrollBarWidth, outMetric);
	boolean isVisibleHBar = horizontalBar != null && horizontalBar.getVisible ();
	boolean isVisibleVBar = verticalBar != null && verticalBar.getVisible ();
	if (isVisibleHBar) hHeight = outMetric [0];
	if (isVisibleVBar) vWidth = outMetric [0];
	int width, height;
	CGRect rect = new CGRect (); 
	OS.HIViewGetBounds (scrolledHandle, rect);
	width = (int) rect.width;
	height = (int) rect.height;
	Rect inset = inset ();
	width = Math.max (0, width - vWidth - inset.left - inset.right);
	height = Math.max (0, height - hHeight - inset.top - inset.bottom);
	setBounds (handle, inset.left, inset.top, width, height, true, true, false);
	if (isVisibleHBar) {
		setBounds (horizontalBar.handle, inset.left, inset.top + height, width, hHeight, true, true, false);
	}
	if (isVisibleVBar) {
		setBounds (verticalBar.handle, inset.left + width, inset.top, vWidth, height, true, true, false);
	}
}

boolean sendMouseWheel (short wheelAxis, int wheelDelta) {
	if ((state & CANVAS) != 0) {
		ScrollBar bar = wheelAxis == OS.kEventMouseWheelAxisX ? horizontalBar : verticalBar;
		if (bar != null && bar.getEnabled ()) {
			bar.setSelection (Math.max (0, bar.getSelection () - bar.getIncrement () * wheelDelta));
			Event event = new Event ();
		    event.detail = wheelDelta > 0 ? SWT.PAGE_UP : SWT.PAGE_DOWN;	
			bar.sendEvent (SWT.Selection, event);
			return true;
		}
	}
	return false;
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	int result = super.setBounds(x, y, width, height, move, resize, false);
	if ((result & MOVED) != 0) {
		if (events) sendEvent (SWT.Move);
	}
	if ((result & RESIZED) != 0) {
		resizeClientArea ();
		if (events) sendEvent (SWT.Resize);
	}
	return result;
}

boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	if (scrolledHandle == 0) return false;
	if ((state & CANVAS) == 0) return false;
	resizeClientArea ();
	setVisible (bar.handle, visible);
	return true;
}

void setZOrder () {
	super.setZOrder ();
	if (scrolledHandle != 0) OS.HIViewAddSubview (scrolledHandle, handle);
}

int topHandle () {
	if (scrolledHandle != 0) return scrolledHandle;
	return handle;
}

}
