package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;

/**
 * Instances of this class provide a surface for drawing
 * arbitrary graphics.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are <em>not</em> constructed
 * from aggregates of other controls. That is, they are either
 * painted using SWT graphics calls or are handled by native
 * methods.
 * </p>
 *
 * @see Composite
 */
public class Canvas extends Composite {
	Caret caret;

Canvas () {}

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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Canvas (Composite parent, int style) {
	super (parent, style);
}

/**
 * Returns the caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 *
 * @return the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Caret getCaret () {
	checkWidget();
	return caret;
}

int processFocusIn (int int0, int int1, int int2) {
	int result = super.processFocusIn (int0, int1, int2);
	if (caret != null) caret.setFocus ();
	return result;
}

int processFocusOut(int int0, int int1, int int2) {
	int result = super.processFocusOut (int0, int1, int2);
	if (caret != null) caret.killFocus ();
	return result;
}

int processPaint (int callData, int int1, int int2) {
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	int result = super.processPaint (callData, int1, int2);
	if (isFocus) caret.setFocus ();
	return result;
}

void redrawWidget (int x, int y, int width, int height, boolean all) {
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	super.redrawWidget (x, y, width, height, all);
	if (isFocus) caret.setFocus ();
}

void releaseWidget () {
	if (caret != null) caret.releaseResources ();
	caret = null;
	super.releaseWidget();
}

/**
 * Scrolls a rectangular area of the receiver by first copying 
 * the source area to the destination and then causing the area
 * of the source which is not covered by the destination to
 * be repainted. Children that intersect the rectangle are
 * optionally moved during the operation. In addition, outstanding
 * paint events are flushed before the source area is copied to
 * ensure that the contents of the canvas are drawn correctly.
 *
 * @param destX the x coordinate of the destination
 * @param destY the y coordinate of the destination
 * @param x the x coordinate of the source
 * @param y the y coordinate of the source
 * @param width the width of the area
 * @param height the height of the area
 * @param all <code>true</code>if children should be scrolled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget();
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;
	if (!isVisible ()) return;
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	
//	update ();
//	GC gc = new GC (this);
//	gc.copyArea (x, y, width, height, destX, destY);
//	gc.dispose ();

	OS.gdk_flush ();
	while ((OS.gtk_events_pending()) != 0) {
		OS.gtk_main_iteration ();
	}
	
	int window = paintWindow ();
	int visibleRegion = OS.gdk_drawable_get_visible_region (window);
	GdkRectangle srcRect = new GdkRectangle ();
	srcRect.x = x;
	srcRect.y = y;
	srcRect.width = width;
	srcRect.height = height;
	int copyRegion = OS.gdk_region_new ();
	OS.gdk_region_union_with_rect (copyRegion, srcRect);
	OS.gdk_region_intersect(copyRegion, visibleRegion);
	int invalidateRegion = OS.gdk_region_new ();
	OS.gdk_region_union_with_rect (invalidateRegion, srcRect);	
	OS.gdk_region_subtract (invalidateRegion, visibleRegion);
	OS.gdk_region_offset (invalidateRegion, deltaX, deltaY);
	GdkRectangle copyRect = new GdkRectangle();
	OS.gdk_region_get_clipbox (copyRegion, copyRect);
	int gdkGC = OS.gdk_gc_new (window);
	OS.gdk_gc_set_exposures (gdkGC, true);
	OS.gdk_draw_drawable (window, gdkGC, window, copyRect.x, copyRect.y, copyRect.x + deltaX, copyRect.y + deltaY, copyRect.width, copyRect.height);
	OS.g_object_unref (gdkGC);
	boolean disjoint = (destX + width < x) || (x + width < destX) || (destY + height < y) || (y + height < destY);
	GdkRectangle rect = new GdkRectangle ();
	if (disjoint) {
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
		OS.gdk_region_union_with_rect (invalidateRegion, rect);
	} else {
		if (deltaX != 0) {
			int newX = destX - deltaX;
			if (deltaX < 0) newX = destX + width;
			rect.x = newX;
			rect.y = y;
			rect.width = Math.abs(deltaX);
			rect.height = height;
			OS.gdk_region_union_with_rect (invalidateRegion, rect);
		}
		if (deltaY != 0) {
			int newY = destY - deltaY;
			if (deltaY < 0) newY = destY + height;
			rect.x = x;
			rect.y = newY;
			rect.width = width;
			rect.height = Math.abs(deltaY);
			OS.gdk_region_union_with_rect (invalidateRegion, rect);
		}
	}	
	OS.gdk_window_invalidate_region(window, invalidateRegion, all);
	OS.gdk_region_destroy (visibleRegion);
	OS.gdk_region_destroy (copyRegion);
	OS.gdk_region_destroy (invalidateRegion);
	
	if (isFocus) caret.setFocus ();
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (isFocus) caret.setFocus ();
	return changed;
}

/**
 * Sets the receiver's caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 * @param caret the new caret for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the caret has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCaret (Caret caret) {
	checkWidget();
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (hasFocus ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) {
			if (newCaret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			newCaret.setFocus ();
		}
	}
}

public boolean setFocus () {
	checkWidget ();
	if ((style & SWT.NO_FOCUS) != 0) return false;
	return super.setFocus ();
}

void updateCaret () {
	if (imHandle == 0) return;
	GdkRectangle rect = new GdkRectangle ();
	rect.x = caret.x;
	rect.y = caret.y;
	rect.width = caret.width;
	rect.height = caret.height;
	OS.gtk_im_context_set_cursor_location (imHandle, rect);
}

}
