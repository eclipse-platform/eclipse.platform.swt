/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.photon.*;
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

void createStandardScrollBars () {
	/* Search the handle to find the scroll bars */
	int child = OS.PtWidgetChildFront (handle);
	while (child != 0) {
		if (OS.PtWidgetClass (child) == OS.PtScrollbar ()) {
			int [] args = {OS.Pt_ARG_ORIENTATION, 0, 0};
			OS.PtGetResources (child, args.length / 3, args);
			switch (args [1]) {
				case OS.Pt_HORIZONTAL:
					if ((style & SWT.H_SCROLL) != 0) {
						horizontalBar = new ScrollBar (this, SWT.HORIZONTAL, child);
					}
					break;
				case OS.Pt_VERTICAL:
					if ((style & SWT.V_SCROLL) != 0) {
						verticalBar = new ScrollBar (this, SWT.VERTICAL, child);
					}
					break;
			}
		}
		child = OS.PtWidgetBrotherBehind (child);
	}
}

void deregister () {
	super.deregister ();
	if (scrolledHandle != 0) WidgetTable.remove (scrolledHandle);
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
	PhRect_t rect = new PhRect_t ();
	PhArea_t area = new PhArea_t ();
	rect.ul_x = (short) x;
	rect.ul_y = (short) y;
	rect.lr_x = (short) (x + width - 1);
	rect.lr_y = (short) (y + height - 1);
	OS.PtSetAreaFromWidgetCanvas (scrolledHandle != 0 ? scrolledHandle : handle, rect, area);
	if (horizontalBar != null) {
		Point size = horizontalBar.getSize ();
		area.size_h += size.y;
	}
	if (verticalBar != null) {
		Point size = verticalBar.getSize ();
		area.size_w += size.x;
	}
	return new Rectangle (area.pos_x, area.pos_y, area.size_w, area.size_h);
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
	PhRect_t rect = new PhRect_t ();
	int validParent = OS.PtValidParent (handle, OS.PtContainer ());
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	OS.PtCalcCanvas (validParent, rect);
	int width = rect.lr_x - rect.ul_x + 1;
	int height = rect.lr_y - rect.ul_y + 1;
	return new Rectangle (0, 0, width, height);
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

boolean isTabGroup () {
	if ((state & CANVAS) != 0) return true;
	return super.isTabGroup ();
}

void releaseHandle () {
	super.releaseHandle ();
	scrolledHandle = 0;
}

void resizeClientArea () {
	/* Do nothing */
}

void reskinChildren (int flags) {
	if (horizontalBar != null) horizontalBar.reskin (flags);
	if (verticalBar != null) verticalBar.reskin (flags);
	super.reskinChildren (flags);
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

void register () {
	super.register ();
	if (scrolledHandle != 0) WidgetTable.put (scrolledHandle, this);
}

int topHandle () {
	if (scrolledHandle == 0) return handle;
	return scrolledHandle;
}

}
