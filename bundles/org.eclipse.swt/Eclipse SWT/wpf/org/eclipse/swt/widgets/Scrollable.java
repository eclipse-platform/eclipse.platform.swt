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


import org.eclipse.swt.internal.wpf.*;
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
	ScrollBar horizontalBar, verticalBar;
	int scrolledHandle;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Scrollable () {
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

int clientHandle () {
	return handle;
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
	checkWidget ();
	if (verticalBar != null) width += OS.SystemParameters_VerticalScrollBarWidth ();
	if (horizontalBar != null) height += OS.SystemParameters_HorizontalScrollBarHeight ();
	return new Rectangle (x, y, width, height);
}

ScrollBar createScrollBar (int type) {
	return new ScrollBar (this, type);
}

void createWidget () {
	super.createWidget ();
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
	fixScrollbarVisibility ();
}

void deregister () {
	super.deregister ();
	if (scrolledHandle != 0) display.removeWidget (scrolledHandle);
}

int findScrollViewer (int current, int scrollViewerType) {
	int type = OS.Object_GetType (current);
	boolean found = OS.Object_Equals (scrollViewerType, type);
	OS.GCHandle_Free (type);
	if (found) return current;
	int childCount = OS.VisualTreeHelper_GetChildrenCount (current);
	for (int i = 0; i < childCount; i++) {
		int child = OS.VisualTreeHelper_GetChild (current, i);
		int result = findScrollViewer (child, scrollViewerType);
		if (child != result) OS.GCHandle_Free (child);
		if (result != 0) return result;
	}
	return 0;
}

void fixScrollbarVisibility () {
	if ((style & SWT.H_SCROLL) == 0) {
		OS.ScrollViewer_SetHorizontalScrollBarVisibility (handle, OS.ScrollBarVisibility_Hidden);
	}
	if ((style & SWT.V_SCROLL) == 0) {
		OS.ScrollViewer_SetVerticalScrollBarVisibility (handle, OS.ScrollBarVisibility_Hidden);
	}
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
	checkWidget ();
	int clientHandle = clientHandle ();
	int topHandle = topHandle (); 
	updateLayout (topHandle);
	int width = (int) OS.FrameworkElement_ActualWidth (clientHandle);
	int height = (int) OS.FrameworkElement_ActualHeight (clientHandle);
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
	checkWidget ();
	return horizontalBar;
}

int getScrollBarHandle (int style) {
	int scrollbar = 0;
	if (scrolledHandle != 0) {
		int children = OS.Panel_Children (scrolledHandle);
		int enumerator = OS.UIElementCollection_GetEnumerator (children);
		int scrollType = OS.ScrollBar_typeid ();
		while (OS.IEnumerator_MoveNext (enumerator)) {
			int current = OS.IEnumerator_Current (enumerator);
			if (OS.Type_IsInstanceOfType (scrollType, current)) {
				int orientation = OS.ScrollBar_Orientation (current);
				if ((style & SWT.H_SCROLL) != 0 && orientation == OS.Orientation_Horizontal) {
					scrollbar = current;
					break;
				}
				if ((style & SWT.V_SCROLL) != 0 && orientation == OS.Orientation_Vertical) { 
					scrollbar = current;
					break;
				}
			}
			OS.GCHandle_Free (current);
		}
		OS.GCHandle_Free (scrollType);
		OS.GCHandle_Free (enumerator);
		OS.GCHandle_Free (children);
	} else {
		updateLayout (handle);
		int scrollViewerType = OS.ScrollViewer_typeid ();
		int scrollViewer = findScrollViewer (handle, scrollViewerType);
		int template = OS.Control_Template (scrollViewer);
		int part;
		if ((style & SWT.H_SCROLL) != 0) {
			part = createDotNetString ("PART_HorizontalScrollBar", false);
		} else {
			part = createDotNetString ("PART_VerticalScrollBar", false);
		}
		scrollbar = OS.FrameworkTemplate_FindName (template, part, scrollViewer);
		OS.GCHandle_Free (part);
		OS.GCHandle_Free (template);
		OS.GCHandle_Free (scrollViewer);
		OS.GCHandle_Free (scrollViewerType);
	}
	return scrollbar;
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
	checkWidget ();
	return verticalBar;
}

void HandlePreviewMouseWheel (int sender, int e) {
	super.HandlePreviewMouseWheel (sender, e);
	if (!checkEvent (e)) return;
	if ((state & CANVAS) != 0) {
		if (verticalBar != null) {
			int vHandle = verticalBar.handle;
			int delta = OS.MouseWheelEventArgs_Delta (e);
			int lines = OS.SystemParameters_WheelScrollLines ();
			double value = OS.RangeBase_Value (vHandle);
			double newValue = value;
			Event event = new Event ();
			if (lines != -1) {
				double smallIncrement = OS.RangeBase_SmallChange (vHandle);
				newValue += smallIncrement * (-delta/120) * lines;
				event.detail = delta < 0 ? SWT.ARROW_DOWN : SWT.ARROW_UP;
			} else {
				double largeIncrement = OS.RangeBase_LargeChange (vHandle);
				newValue += largeIncrement * (-delta/120);
				event.detail = delta < 0 ? SWT.PAGE_DOWN : SWT.PAGE_UP;
			}
			OS.RangeBase_Value (vHandle, newValue);
			newValue = OS.RangeBase_Value (vHandle);
			if (value != newValue) {
				verticalBar.postEvent (SWT.Selection, event);
			}
		}
	}
}

void register () {
	super.register ();
	if (scrolledHandle != 0) display.addWidget (scrolledHandle, this);
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

void releaseHandle () {
	super.releaseHandle ();
	if (scrolledHandle != 0) OS.GCHandle_Free (scrolledHandle);
	scrolledHandle = 0;
}

void reskinChildren (int flags) {
	if (horizontalBar != null) horizontalBar.reskin (flags);
	if (verticalBar != null) verticalBar.reskin (flags);
	super.reskinChildren (flags);
}

int topHandle () {
	return scrolledHandle != 0 ? scrolledHandle : super.topHandle ();
}

}
