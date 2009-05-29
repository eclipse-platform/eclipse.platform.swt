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

 
import org.eclipse.swt.internal.motif.*;
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
	int scrolledHandle, formHandle;
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
	int border = getBorderWidth ();
	int trimX = x - border, trimY = y - border;
	int trimWidth = width + (border * 2), trimHeight = height + (border * 2);
	if (horizontalBar != null) {
		trimY -= display.scrolledInsetY;
		trimHeight += display.scrolledInsetY + display.scrolledMarginY;
		if (verticalBar == null) {
			trimX -= display.scrolledInsetX;
			trimWidth += display.scrolledInsetX * 2;
			trimHeight -= display.scrolledInsetY * 2;
		}
	}
	if (verticalBar != null) {
		trimX -= display.scrolledInsetX;
		trimWidth += display.scrolledInsetX + display.scrolledMarginX;
		if (horizontalBar == null) {
			trimY -= display.scrolledInsetY;
			trimHeight += display.scrolledInsetY * 2;
			trimWidth -= display.scrolledInsetX * 2;
		}
	}
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}
ScrollBar createScrollBar (int type) {
	return new ScrollBar (this, type);
}
ScrollBar createStandardBar (int style) {
	if (scrolledHandle == 0) return null;
	ScrollBar bar = new ScrollBar ();
	bar.parent = this;
	bar.style = style;
	bar.display = display;
	int [] argList = {OS.XmNhorizontalScrollBar, 0, OS.XmNverticalScrollBar, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	if (style == SWT.H_SCROLL) bar.handle = argList [1];
	if (style == SWT.V_SCROLL) bar.handle = argList [3];
	bar.hookEvents ();
	bar.register ();
	return bar;
}
void createWidget (int index) {
	super.createWidget (index);
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
}
void destroyScrollBar (ScrollBar bar) {
	setScrollBarVisible (bar, false);
	if ((state & CANVAS) != 0) bar.destroyHandle ();
}
void deregister () {
	super.deregister ();
	if (formHandle != 0) display.removeWidget (formHandle);
	if (scrolledHandle != 0) display.removeWidget (scrolledHandle);
}
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	if (formHandle != 0) enableHandle (enabled, formHandle);
	if (scrolledHandle != 0) enableHandle (enabled, scrolledHandle);
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
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return new Rectangle (0, 0, argList [1], argList [3]);
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
void manageChildren () {
	if (scrolledHandle != 0) {	
		OS.XtSetMappedWhenManaged (scrolledHandle, false);
		OS.XtManageChild (scrolledHandle);
	}
	if (formHandle != 0) {
		OS.XtSetMappedWhenManaged (formHandle, false);
		OS.XtManageChild (formHandle);
	}
	super.manageChildren ();
	if (formHandle != 0) {
		int [] argList = {OS.XmNborderWidth, 0};
		OS.XtGetValues (formHandle, argList, argList.length / 2);
		OS.XtResizeWidget (formHandle, 1, 1, argList [1]);
		OS.XtSetMappedWhenManaged (formHandle, true);
	}
	if (scrolledHandle != 0) {	
		int [] argList = {OS.XmNborderWidth, 0};
		OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
		OS.XtResizeWidget (scrolledHandle, 1, 1, argList [1]);
		OS.XtSetMappedWhenManaged (scrolledHandle, true);
	}
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	if (formHandle != 0) propagateHandle (enabled, formHandle, OS.None);
	if (scrolledHandle != 0) {
		propagateHandle (enabled, scrolledHandle, OS.None);
		if (horizontalBar != null) horizontalBar.propagateWidget (enabled);
		if (verticalBar != null) verticalBar.propagateWidget (enabled);
	}
}
void register () {
	super.register ();
	if (formHandle != 0) display.addWidget (formHandle, this);
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
	scrolledHandle = formHandle = 0;
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	/*
	* Uncomment this code to force scrollbars to change color.
	*/
//	if (scrolledHandle != 0) {
//		int [] argList1 = {
//			OS.XmNhorizontalScrollBar, 0,
//			OS.XmNverticalScrollBar, 0,
//		};
//		OS.XtGetValues (scrolledHandle, argList1, argList1.length / 2);
//		if (argList1 [1] != 0) OS.XmChangeColor (argList1 [1], pixel);
//		if (argList1 [3] != 0) OS.XmChangeColor (argList1 [3], pixel);
//	}
}
void redrawWidget (int x, int y, int width, int height, boolean redrawAll, boolean allChildren, boolean trim) {
	super.redrawWidget (x, y, width, height, redrawAll, allChildren, trim);
	if (!trim) return;
	if (formHandle == 0 && scrolledHandle == 0) return;
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (handle, (short) x, (short) y, root_x, root_y);
	if (formHandle != 0) {
		short [] form_x = new short [1], form_y = new short [1];
		OS.XtTranslateCoords (formHandle, (short) 0, (short) 0, form_x, form_y);
		redrawHandle (root_x [0] - form_x [0], root_y [0] - form_y [0], width, height, redrawAll, formHandle);
	}
	if (scrolledHandle != 0) {
		short [] scrolled_x = new short [1], scrolled_y = new short [1];
		OS.XtTranslateCoords (scrolledHandle, (short) 0, (short) 0, scrolled_x, scrolled_y);
		redrawHandle (root_x [0] - scrolled_x [0], root_y [0] - scrolled_y [0], width, height, redrawAll, scrolledHandle);
		if (horizontalBar != null && horizontalBar.getVisible ()) {
			int horizontalHandle = horizontalBar.handle;
			short [] hscroll_x = new short [1], hscroll_y = new short [1];
			OS.XtTranslateCoords (horizontalHandle, (short) 0, (short) 0, hscroll_x, hscroll_y);
			redrawHandle (root_x [0] - hscroll_x [0], root_y [0] - hscroll_y [0], width, height, redrawAll, horizontalHandle);
		}
		if (verticalBar != null && verticalBar.getVisible ()) {
			int verticalHandle = verticalBar.handle;
			short [] vscroll_x = new short [1], vscroll_y = new short [1];
			OS.XtTranslateCoords (verticalHandle, (short) 0, (short) 0, vscroll_x, vscroll_y);
			redrawHandle (root_x [0] - vscroll_x [0], root_y [0] - vscroll_y [0], width, height, redrawAll, verticalHandle);
		}
	}
}
boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	if (scrolledHandle == 0) return false;
	int barHandle = bar.handle;
	boolean managed = OS.XtIsManaged (barHandle);
	if (managed == visible) return false;

	/*
	* Feature in Motif.  Hiding or showing a scroll bar
	* can cause the widget to automatically resize in
	* the OS.  This behavior is unwanted.  The fix is
	* to force the widget to resize to original size.
	*/
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	
	int [] argList1 = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	
	/* Hide or show the scroll bar */
	if (visible) {
		OS.XtManageChild (barHandle);
	} else {
		OS.XtUnmanageChild (barHandle);
	}
	if ((state & CANVAS) != 0) {
		if (formHandle != 0) {	
			boolean showBorder = (style & SWT.BORDER) != 0;
			int margin = showBorder || visible ? 3 : 0;
			if ((bar.style & SWT.V_SCROLL) != 0) {
				int [] argList2 = new int [] {OS.XmNmarginWidth, margin};
				OS.XtSetValues (formHandle, argList2, argList2.length/2);
			}
			if ((bar.style & SWT.H_SCROLL) != 0) {
				int [] argList2 = new int [] {OS.XmNmarginHeight, margin};
				OS.XtSetValues (formHandle, argList2, argList2.length/2);
			}
		}
	}
	
	/*
	* Feature in Motif.  When XtSetValues() is used to restore the width and
	* height of the widget, the new width and height are sometimes ignored.
	* The fix is to use XtResizeWidget().
	*/
	OS.XtResizeWidget (scrolledHandle, argList [1], argList [3], argList [5]);

	bar.sendEvent (visible ? SWT.Show : SWT.Hide);
	int [] argList3 = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList3, argList3.length / 2);
	return argList1 [1] != argList3 [1] || argList1 [3] != argList3 [3];
}
int topHandle () {
	if (scrolledHandle != 0) return scrolledHandle;
	if (formHandle != 0) return formHandle;
	return handle;
}
}
