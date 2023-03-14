/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
	long scrolledHandle;
	ScrollBar horizontalBar, verticalBar;

	/** See bug 484682 */
	static final boolean RESIZE_ON_GETCLIENTAREA = OS.isWayland() || Boolean.getBoolean("org.eclipse.swt.resizeOnGetClientArea");

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Scrollable () {}

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

long clientHandle () {
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
	checkWidget();
	Rectangle rect = DPIUtil.autoScaleUp(new Rectangle (x, y, width, height));
	return DPIUtil.autoScaleDown(computeTrimInPixels(rect.x, rect.y, rect.width, rect.height));
}

Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	checkWidget();
	int border = 0;
	if (fixedHandle != 0) border += gtk_container_get_border_width_or_margin (fixedHandle);
	if (scrolledHandle != 0) border += gtk_container_get_border_width_or_margin (scrolledHandle);
	int trimX = x - border, trimY = y - border;
	int trimWidth = width + (border * 2), trimHeight = height + (border * 2);
	trimHeight += hScrollBarWidth ();
	trimWidth  += vScrollBarWidth ();
	if (scrolledHandle != 0) {
		boolean hasFrame;
		if (GTK.GTK4) {
			hasFrame = GTK4.gtk_scrolled_window_get_has_frame(scrolledHandle);
		} else {
			hasFrame = GTK3.gtk_scrolled_window_get_shadow_type (scrolledHandle) != GTK.GTK_SHADOW_NONE;
		}

		if (hasFrame) {
			Point thickness = getThickness (scrolledHandle);
			int xthickness = thickness.x;
			int ythickness = thickness.y;
			trimX -= xthickness;
			trimY -= ythickness;
			trimWidth += xthickness * 2;
			trimHeight += ythickness * 2;
		}
	}
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}

ScrollBar createScrollBar (int style) {
	if (scrolledHandle == 0) return null;
	ScrollBar bar = new ScrollBar ();
	bar.parent = this;
	bar.style = style;
	bar.display = display;
	bar.state |= HANDLE;
	if ((style & SWT.H_SCROLL) != 0) {
		bar.handle = GTK.gtk_scrolled_window_get_hscrollbar (scrolledHandle);
		bar.adjustmentHandle = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
	} else {
		bar.handle = GTK.gtk_scrolled_window_get_vscrollbar (scrolledHandle);
		bar.adjustmentHandle = GTK.gtk_scrolled_window_get_vadjustment (scrolledHandle);
	}
	bar.setOrientation (true);
	bar.hookEvents ();
	bar.register ();
	return bar;
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
}

@Override
void updateBackgroundMode () {
	super.updateBackgroundMode ();
	switch (applyThemeBackground ()) {
		case 0: state &= ~THEME_BACKGROUND; break;
		case 1: state |= THEME_BACKGROUND; break;
		default: /* No change */
	}
	super.updateBackgroundMode ();
}

/**
 * @return
 * 		<li>0 to remove THEME_BACKGROUND</li>
 *      <li>1 to apply THEME_BACKGROUND</li>
 *      <li>otherwise don't change THEME_BACKGROUND state</li>
 */
int applyThemeBackground () {
	return (backgroundAlpha == 0) ? 1 : 0;
}

@Override
void deregister () {
	super.deregister ();
	if (scrolledHandle != 0) display.removeWidget (scrolledHandle);
}

void destroyScrollBar (ScrollBar bar) {
	setScrollBarVisible (bar, false);
	//This code is intentionally commented
	//bar.destroyHandle ();
}

@Override
int getBorderWidthInPixels () {
	checkWidget();
	int border = 0;
	if (fixedHandle != 0) border += gtk_container_get_border_width_or_margin (fixedHandle);
	if (scrolledHandle != 0) {
		border += gtk_container_get_border_width_or_margin (scrolledHandle);

		boolean hasFrame;
		if (GTK.GTK4) {
			hasFrame = GTK4.gtk_scrolled_window_get_has_frame(scrolledHandle);
		} else {
			hasFrame = GTK3.gtk_scrolled_window_get_shadow_type (scrolledHandle) != GTK.GTK_SHADOW_NONE;
		}
		if (hasFrame) {
			border += getThickness (scrolledHandle).x;
		}
	}
	return border;
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
	return DPIUtil.autoScaleDown(getClientAreaInPixels());
}

Rectangle getClientAreaInPixels () {
	checkWidget ();
	if(RESIZE_ON_GETCLIENTAREA) {
		forceResize ();
	}
	long clientHandle = clientHandle ();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (clientHandle, allocation);
	int x = allocation.x;
	int y = allocation.y;
	int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
	return new Rectangle (x, y, width, height);
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
/**
 * Returns the mode of the receiver's scrollbars. This will be
 * <em>bitwise</em> OR of one or more of the constants defined in class
 * <code>SWT</code>.<br>
 * <ul>
 * <li><code>SWT.SCROLLBAR_OVERLAY</code> - if receiver
 * uses overlay scrollbars</li>
 * <li><code>SWT.NONE</code> - otherwise</li>
 * </ul>
 *
 * @return the mode of scrollbars
 *
 * @exception SWTException <ul>
 * <li>ERROR_WIDGET_DISPOSED - if the receiver has been
 * disposed</li>
 * <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
 * thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#SCROLLBAR_OVERLAY
 *
 * @since 3.8
 */
public int getScrollbarsMode () {
	checkWidget();
	if (GTK.gtk_scrolled_window_get_overlay_scrolling(scrolledHandle)) {
		return SWT.SCROLLBAR_OVERLAY;
	}
	return SWT.NONE;
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

@Override
long gtk_draw (long widget, long cairo) {
	if (!GTK.GTK4) {
		/*
		 * Draw events destined for an SwtFixed instance will sometimes
		 * only be redrawing the scrollbars attached to it. GTK will send many
		 * draw events to an SwtFixed instance if:
		 *   1) that instance has overlay scrollbars attached to it, and
		 *   2) the mouse has just left (leave-notify) that SwtFixed widget.
		 *
		 * Such extra draw events cause extra SWT.Paint events to be sent and
		 * reduce performance. The fix is to check if the dirty region in need
		 * of a redraw is the same region that the scroll bars occupy, and ignore
		 * draw events that target such cases. See bug 546248.
		 */
		boolean overlayScrolling = !OS.GTK_OVERLAY_SCROLLING_DISABLED;
		if (overlayScrolling && OS.G_OBJECT_TYPE(widget) == OS.swt_fixed_get_type()) {
			if ((style & SWT.V_SCROLL) != 0 && verticalBar != null) {
				GtkAllocation verticalBarAlloc = new GtkAllocation();
				GTK.gtk_widget_get_allocation(verticalBar.handle, verticalBarAlloc);
				GdkRectangle rect = new GdkRectangle();
				GDK.gdk_cairo_get_clip_rectangle(cairo, rect);
				if (rect.width == verticalBarAlloc.width && rect.height == verticalBarAlloc.height) {
					return 0;
				}
			}
			if ((style & SWT.H_SCROLL) != 0 && horizontalBar != null) {
				GtkAllocation horizontalBarAlloc = new GtkAllocation();
				GTK.gtk_widget_get_allocation(horizontalBar.handle, horizontalBarAlloc);
				GdkRectangle rect = new GdkRectangle();
				GDK.gdk_cairo_get_clip_rectangle(cairo, rect);
				if (rect.width == horizontalBarAlloc.width && rect.height == horizontalBarAlloc.height) {
					return 0;
				}
			}
		}
	}

	return super.gtk_draw(widget, cairo);
}

@Override
long gtk_scroll_event (long widget, long eventPtr) {
	long result = super.gtk_scroll_event (widget, eventPtr);

	/*
	* Feature in GTK.  Scrolled windows do not scroll if the scrollbars
	* are hidden.  This is not a bug, but is inconsistent with other platforms.
	* The fix is to set the adjustment values directly.
	*/
	if ((state & CANVAS) != 0) {
		ScrollBar scrollBar;
		int [] direction = new int[1];
		boolean fetched = GDK.gdk_event_get_scroll_direction(eventPtr, direction);

		if (!fetched) {
			double[] delta_x = new double[1], delta_y = new double [1];
			boolean deltasAvailable = GDK.gdk_event_get_scroll_deltas (eventPtr, delta_x, delta_y);

			if (deltasAvailable) {
				if (delta_x [0] != 0) {
					scrollBar = horizontalBar;
					if (scrollBar != null && !GTK.gtk_widget_get_visible (scrollBar.handle) && scrollBar.getEnabled()) {
						GtkAdjustment adjustment = new GtkAdjustment ();
						gtk_adjustment_get (scrollBar.adjustmentHandle, adjustment);
						double delta = Math.pow(adjustment.page_size, 2.0 / 3.0) * delta_x [0];
						int value = (int) Math.max(adjustment.lower,
								Math.min(adjustment.upper - adjustment.page_size, adjustment.value + delta));
						GTK.gtk_adjustment_set_value (scrollBar.adjustmentHandle, value);
						result = 1;
					}
				}
				if (delta_y [0] != 0) {
					scrollBar = verticalBar;
					if (scrollBar != null && !GTK.gtk_widget_get_visible (scrollBar.handle) && scrollBar.getEnabled()) {
						GtkAdjustment adjustment = new GtkAdjustment ();
						gtk_adjustment_get (scrollBar.adjustmentHandle, adjustment);
						double delta = Math.pow(adjustment.page_size, 2.0 / 3.0) * delta_y [0];
						int value = (int) Math.max(adjustment.lower,
								Math.min(adjustment.upper - adjustment.page_size, adjustment.value + delta));
						GTK.gtk_adjustment_set_value (scrollBar.adjustmentHandle, value);
						result = 1;
					}
				}
			}
		} else {
			if (direction[0] == GDK.GDK_SCROLL_UP || direction[0] == GDK.GDK_SCROLL_DOWN) {
				scrollBar = verticalBar;
			} else {
				scrollBar = horizontalBar;
			}
			if (scrollBar != null && !GTK.gtk_widget_get_visible (scrollBar.handle) && scrollBar.getEnabled()) {
				GtkAdjustment adjustment = new GtkAdjustment ();
				gtk_adjustment_get (scrollBar.adjustmentHandle, adjustment);
				/* Calculate wheel delta to match GTK+ 2.4 and higher */
				int wheel_delta = (int) Math.pow(adjustment.page_size, 2.0 / 3.0);
				if (direction[0] == GDK.GDK_SCROLL_UP || direction[0] == GDK.GDK_SCROLL_LEFT)
					wheel_delta = -wheel_delta;
				int value = (int) Math.max(adjustment.lower,
						Math.min(adjustment.upper - adjustment.page_size, adjustment.value + wheel_delta));
				GTK.gtk_adjustment_set_value (scrollBar.adjustmentHandle, value);
				return 1;
			}
		}
	}
	return result;
}

int hScrollBarWidth() {
	Point hScrollbarSize = hScrollbarSize();
	return hScrollbarSize.y;
}

@Override
void reskinChildren (int flags) {
	if (horizontalBar != null) horizontalBar.reskin (flags);
	if (verticalBar != null) verticalBar.reskin (flags);
	super.reskinChildren (flags);
}

@Override
boolean sendLeaveNotify () {
	return scrolledHandle != 0;
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_TEXT_DIR_RTL : GTK.GTK_TEXT_DIR_LTR;
		if (scrolledHandle != 0) {
			GTK.gtk_widget_set_direction (scrolledHandle, dir);
		}
	}
	if (horizontalBar != null) horizontalBar.setOrientation (create);
	if (verticalBar != null) verticalBar.setOrientation (create);
}

boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	if (scrolledHandle == 0) return false;
	int [] hsp = new int [1], vsp = new int [1];
	GTK.gtk_scrolled_window_get_policy (scrolledHandle, hsp, vsp);
	int policy = visible ? GTK.GTK_POLICY_ALWAYS : GTK.GTK_POLICY_NEVER;
	if (!visible) {
		policy = GTK.GTK_POLICY_EXTERNAL;
	}
	if ((bar.style & SWT.HORIZONTAL) != 0) {
		if (hsp [0] == policy) return false;
		hsp [0] = policy;
	} else {
		if (vsp [0] == policy) return false;
		vsp [0] = policy;
	}
	GTK.gtk_scrolled_window_set_policy (scrolledHandle, hsp [0], vsp [0]);
	return true;
}

void redrawBackgroundImage () {
}

@Override
void redrawWidget (int x, int y, int width, int height, boolean redrawAll, boolean all, boolean trim) {
	super.redrawWidget (x, y, width, height, redrawAll, all, trim);
	if (!GTK.gtk_widget_get_realized (handle)) return;
	if (!trim) return;
	long topHandle = topHandle (), paintHandle = paintHandle ();
	if (topHandle == paintHandle) return;
	GdkRectangle rect = new GdkRectangle ();
	if (redrawAll) {
		GtkAllocation allocation = new GtkAllocation ();
		GTK.gtk_widget_get_allocation (topHandle, allocation);
		rect.width = allocation.width;
		rect.height = allocation.height;
	} else {
		if (GTK.GTK4) {
			double[] destX = new double[1], destY = new double[1];
			GTK4.gtk_widget_translate_coordinates(paintHandle, topHandle, x, y, destX, destY);
			rect.x = (int)destX[0];
			rect.y = (int)destY[0];
		} else {
			int[] destX = new int[1], destY = new int[1];
			GTK3.gtk_widget_translate_coordinates(paintHandle, topHandle, x, y, destX, destY);
			rect.x = destX[0];
			rect.y = destY[0];
		}

		rect.width = Math.max(0, width);
		rect.height = Math.max(0, height);
	}
	if (GTK.GTK4) {
		/* TODO: GTK4 no ability to invalidate surfaces, may need to keep track of
		 * invalid regions ourselves and do gdk_surface_queue_expose. Will need a different way to force redraws.
		 * New "render" signal? */
	} else {
		long window = gtk_widget_get_window (topHandle);
		GDK.gdk_window_invalidate_rect (window, rect, all);
	}
}

@Override
void register () {
	super.register ();
	if (scrolledHandle != 0) display.addWidget (scrolledHandle, this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	scrolledHandle = 0;
}

@Override
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

@Override
void resizeHandle (int width, int height) {
	if (fixedHandle != 0) {
		OS.swt_fixed_resize (GTK.gtk_widget_get_parent(fixedHandle), fixedHandle, width, height);
	}
	long child = scrolledHandle != 0 ? scrolledHandle : handle;
	Point sizes = resizeCalculationsGTK3 (child, width, height);
	width = sizes.x;
	height = sizes.y;
	OS.swt_fixed_resize (GTK.gtk_widget_get_parent(child), child, width, height);
}

@Override
void showWidget () {
	super.showWidget ();
	if (scrolledHandle != 0) GTK.gtk_widget_show (scrolledHandle);
}

@Override
long topHandle () {
	if (fixedHandle != 0) return fixedHandle;
	if (scrolledHandle != 0) return scrolledHandle;
	return super.topHandle ();
}

void updateScrollBarValue (ScrollBar bar) {
	redrawBackgroundImage ();
}

int vScrollBarWidth() {
	Point vScrollBarSize = vScrollBarSize();
	return vScrollBarSize.x;
}

private Point hScrollbarSize() {
	if (horizontalBar == null) return new Point(0, 0);
	long vBarHandle = GTK.gtk_scrolled_window_get_hscrollbar (scrolledHandle);
	return scrollBarSize(vBarHandle);
}

private Point vScrollBarSize() {
	if (verticalBar == null) return new Point(0, 0);
	long vBarHandle = GTK.gtk_scrolled_window_get_vscrollbar (scrolledHandle);
	return scrollBarSize(vBarHandle);
}

private Point scrollBarSize(long scrollBarHandle) {
	if (scrollBarHandle == 0) return new Point(0, 0);
	GtkRequisition requisition = new GtkRequisition();
	/*
	 * Feature in GTK3: sometimes the size reported lags on GTK3.20+.
	 * Calling gtk_widget_queue_resize() before querying the size
	 * fixes this issue.
	 */
	GTK.gtk_widget_queue_resize (scrollBarHandle);
	gtk_widget_get_preferred_size (scrollBarHandle, requisition);
	int [] padding = new int [1];
	// Only GTK3 needs this, GTK4 has the size built-in via gtk_widget_get_preferred_size()
	if (!GTK.GTK4) GTK3.gtk_widget_style_get(scrolledHandle, OS.scrollbar_spacing, padding, 0);
	int spacing = padding[0];
	return new Point(requisition.width + spacing, requisition.height + spacing);
}
}
