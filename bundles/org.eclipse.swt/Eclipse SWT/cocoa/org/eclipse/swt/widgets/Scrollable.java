/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.cocoa.*;

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
 	SWTScrollView scrollView;
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
	if (scrollView != null) {
		NSSize size = new NSSize();
		size.width = width;
		size.height = height;
		int border = hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder;
		size = NSScrollView.frameSizeForContentSize(size, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0, border);
		width = (int)size.width;
		height = (int)size.height;
		NSRect frame = scrollView.contentView().frame();
		x -= frame.x;
		y -= frame.y;
	}
	return new Rectangle (x, y, width, height);
}

ScrollBar createScrollBar (int style) {
	if (scrollView == null) return null;
	ScrollBar bar = new ScrollBar ();
	bar.parent = this;
	bar.style = style;
	bar.display = display;
	NSScroller scroller;
	int actionSelector;
	if ((style & SWT.H_SCROLL) != 0) {
		scroller = scrollView.horizontalScroller();
		actionSelector = OS.sel_sendHorizontalSelection;
	} else {
		scroller = scrollView.verticalScroller();
		actionSelector = OS.sel_sendVerticalSelection;
	}
	bar.view = scroller;
	bar.createJNIRef();
	scroller.setTag(bar.jniRef);
	if ((state & CANVAS) == 0) {
		bar.target = scroller.target();
		bar.actionSelector = scroller.action();
	}
	scroller.setTarget(scrollView);
	scroller.setAction(actionSelector);
	return bar;
}

void createWidget () {
	super.createWidget ();
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
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
	if (scrollView != null) {
		NSSize size = scrollView.contentSize();
		return new Rectangle(0, 0, (int)size.width, (int)size.height);
	} else {
		NSRect rect = view.bounds();
		return new Rectangle(0, 0, (int)rect.width, (int)rect.height);
	}
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

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp) || hooks (SWT.Traverse);
}


void releaseHandle () {
	super.releaseHandle ();
	if (scrollView != null)  {
		scrollView.setTag(-1);
		scrollView.release();
	}
	scrollView = null;
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

void resizeClientArea () {
//	if (scrolledHandle == 0) return;
//	if ((state & CANVAS) == 0) return;
//	int vWidth = 0, hHeight = 0;
//	int [] outMetric = new int [1];
//	OS.GetThemeMetric (OS.kThemeMetricScrollBarWidth, outMetric);
//	boolean isVisibleHBar = horizontalBar != null && horizontalBar.getVisible ();
//	boolean isVisibleVBar = verticalBar != null && verticalBar.getVisible ();
//	if (isVisibleHBar) hHeight = outMetric [0];
//	if (isVisibleVBar) vWidth = outMetric [0];
//	int width, height;
//	CGRect rect = new CGRect (); 
//	OS.HIViewGetBounds (scrolledHandle, rect);
//	width = (int) rect.width;
//	height = (int) rect.height;
//	Rect inset = inset ();
//	width = Math.max (0, width - vWidth - inset.left - inset.right);
//	height = Math.max (0, height - hHeight - inset.top - inset.bottom);
//	setBounds (handle, inset.left, inset.top, width, height, true, true, false);
//	if (isVisibleHBar) {
//		setBounds (horizontalBar.handle, inset.left, inset.top + height, width, hHeight, true, true, false);
//	}
//	if (isVisibleVBar) {
//		setBounds (verticalBar.handle, inset.left + width, inset.top, vWidth, height, true, true, false);
//	}
}

void sendHorizontalSelection () {
	horizontalBar.sendSelection ();
}

boolean sendMouseWheel (short wheelAxis, int wheelDelta) {
//	if ((state & CANVAS) != 0) {
//		ScrollBar bar = wheelAxis == OS.kEventMouseWheelAxisX ? horizontalBar : verticalBar;
//		if (bar != null && bar.getEnabled ()) {
//			bar.setSelection (Math.max (0, bar.getSelection () - bar.getIncrement () * wheelDelta));
//			Event event = new Event ();
//		    event.detail = wheelDelta > 0 ? SWT.PAGE_UP : SWT.PAGE_DOWN;	
//			bar.sendEvent (SWT.Selection, event);
//			return true;
//		}
//	}
	return false;
}

void sendVerticalSelection () {
	verticalBar.sendSelection ();
}

boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	if (scrollView == null) return false;
	if ((state & CANVAS) == 0) return false;
	if (visible) {
		if ((bar.state & HIDDEN) == 0) return false;
		bar.state &= ~HIDDEN;
	} else {
		if ((bar.state & HIDDEN) != 0) return false;
		bar.state |= HIDDEN;
	}
	resizeClientArea ();
//	setVisible (bar.handle, visible);
	bar.sendEvent (visible ? SWT.Show : SWT.Hide);
	sendEvent (SWT.Resize);
	return true;
}

NSView topView () {
	if (scrollView != null) return scrollView;
	return super.topView ();
}
}
