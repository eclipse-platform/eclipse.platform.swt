/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

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
 */
public abstract class Scrollable extends Control {
 	int scrolledHandle;
	int hScrollBar, vScrollBar;
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

ScrollBar createScrollBar (int type) {
    return new ScrollBar (this, type);
}

ScrollBar createStandardBar (int style) {
	short [] count = new short [1];
	OS.CountSubControls (handle, count);
	if (count [0] == 0) return null;
	int [] outControl = new int [1];
	int index = (style & SWT.HORIZONTAL) != 0 ? 1 : 2;
	int status = OS.GetIndexedSubControl (handle, (short)index, outControl);
	if (status != OS.noErr) return null;
	ScrollBar bar = new ScrollBar ();
	bar.parent = this;
	bar.style = style;
	bar.display = display;
	bar.handle = outControl [0];
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

boolean hasBorder () {
	return (style & SWT.BORDER) != 0;
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
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
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
	int result = super.kEventMouseWheelMoved (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if ((state & CANVAS) != 0) {
		short [] wheelAxis = new short [1];
		OS.GetEventParameter (theEvent, OS.kEventParamMouseWheelAxis, OS.typeMouseWheelAxis, null, 2, null, wheelAxis);
		ScrollBar bar = wheelAxis [0] == OS.kEventMouseWheelAxisX ? horizontalBar : verticalBar;
		if (bar != null && bar.getVisible ()) {
			int [] wheelDelta = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamMouseWheelDelta, OS.typeSInt32, null, 4, null, wheelDelta);
			bar.setSelection (Math.max (0, bar.getSelection () - bar.getIncrement () * wheelDelta [0]));
			Event event = new Event ();
		    event.detail = wheelDelta [0] > 0 ? SWT.PAGE_UP : SWT.PAGE_DOWN;	
			bar.sendEvent (SWT.Selection, event);
//			Display display = getDisplay ();
//			display.update ();
			update ();
		}
		/*
		* Feature in the Macintosh.   For some reason, when eventNotHandledErr
		* is returned from kEventMouseWheelMoved the event is sent twiced to
		* the same control with the same mouse wheel data.  The fix is to return
		* noErr to stop further event processing.
		*/
		return OS.noErr;
	}
	int vPosition = verticalBar == null ? 0 : verticalBar.getSelection ();
	int hPosition = horizontalBar == null ? 0 : horizontalBar.getSelection ();
	result = OS.CallNextEventHandler (nextHandler, theEvent);
	if (verticalBar != null) {
		int position = verticalBar.getSelection ();
		if (position != vPosition) {
			Event event = new Event ();
			event.detail = position < vPosition ? SWT.PAGE_UP : SWT.PAGE_DOWN; 
			verticalBar.sendEvent (SWT.Selection, event);
		}
	}
	if (horizontalBar != null) {
		int position = horizontalBar.getSelection ();
		if (position != hPosition) {
			Event event = new Event ();
			event.detail = position < vPosition ? SWT.PAGE_UP : SWT.PAGE_DOWN; 
			horizontalBar.sendEvent (SWT.Selection, event);
		}
	}
	return result;
}

void layoutControl (boolean events) {
	if (scrolledHandle == 0) return;
	int vWidth = 0, hHeight = 0;
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricScrollBarWidth, outMetric);
	boolean isVisibleHBar = horizontalBar != null && horizontalBar.getVisible ();
	boolean isVisibleVBar = verticalBar != null && verticalBar.getVisible ();
	if (isVisibleHBar) hHeight = outMetric [0];
	if (isVisibleVBar) vWidth = outMetric [0];
	Rect rect = new Rect ();
	OS.GetControlBounds (scrolledHandle, rect);
	Rect inset = inset ();
	int width = Math.max (0, rect.right - rect.left - vWidth - inset.left - inset.right);
	int height = Math.max (0, rect.bottom - rect.top - hHeight - inset.top - inset.bottom);
	setBounds (handle, inset.left, inset.top, width, height, true, true, false);
	if (isVisibleHBar) {
		setBounds (horizontalBar.handle, inset.left, inset.top + height, width, hHeight, true, true, false);
	}
	if (isVisibleVBar) {
		setBounds (verticalBar.handle, inset.left + width, inset.top, vWidth, height, true, true, false);
	}
	if (events) {
		sendEvent (SWT.Resize);
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

void releaseWidget () {
	if (horizontalBar != null) horizontalBar.releaseResources ();
	if (verticalBar != null) verticalBar.releaseResources ();
	horizontalBar = verticalBar = null;
	super.releaseWidget ();
}

void resetVisibleRegion (int control) {
	if (verticalBar != null) verticalBar.resetVisibleRegion (control);
	if (horizontalBar != null) horizontalBar.resetVisibleRegion (control);
	super.resetVisibleRegion (control);
}

int setBounds (int control, int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	int result = super.setBounds(control, x, y, width, height, move, resize, false);
	if ((result & MOVED) != 0) {
		if (events) sendEvent (SWT.Move);
	}
	if ((result & RESIZED) != 0) {
		if (control == scrolledHandle) layoutControl (false);
		if (events) sendEvent (SWT.Resize);
	}
	return result;
}

int topHandle () {
	if (scrolledHandle != 0) return scrolledHandle;
	return handle;
}

}
