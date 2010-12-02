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
 * Instances of this class are controls which are capable
 * of containing other controls.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE, NO_RADIO_GROUP, EMBEDDED, DOUBLE_BUFFERED</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: The <code>NO_BACKGROUND</code>, <code>NO_FOCUS</code>, <code>NO_MERGE_PAINTS</code>,
 * and <code>NO_REDRAW_RESIZE</code> styles are intended for use with <code>Canvas</code>.
 * They can be used with <code>Composite</code> if you are drawing your own, but their
 * behavior is undefined if they are used with subclasses of <code>Composite</code> other
 * than <code>Canvas</code>.
 * </p><p>
 * Note: The <code>CENTER</code> style, although undefined for composites, has the
 * same value as <code>EMBEDDED</code> which is used to embed widgets from other
 * widget toolkits into SWT.  On some operating systems (GTK, Motif), this may cause
 * the children of this composite to be obscured.
 * </p><p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are constructed from aggregates
 * of other controls.
 * </p>
 *
 * @see Canvas
 * @see <a href="http://www.eclipse.org/swt/snippets/#composite">Composite snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Composite extends Scrollable {
	Layout layout;
	Control [] tabList;
	int cornerHandle;
	int layoutCount, backgroundMode;
	
Composite () {
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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_FOCUS
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_RADIO_GROUP
 * @see SWT#EMBEDDED
 * @see SWT#DOUBLE_BUFFERED
 * @see Widget#getStyle
 */
public Composite (Composite parent, int style) {
	super (parent, style);
}

static int checkStyle (int style) {
	style &= SWT.TRANSPARENT;
	return style;
}

Control [] _getChildren () {
	int count = 0;
	int parentHandle = parentingHandle ();
	int child = OS.PtWidgetChildFront (parentHandle);
	while (child != 0) {
		child = OS.PtWidgetBrotherBehind (child);
		count++;
	}
	Control [] children = new Control [count];
	int i = 0, j = 0;
	child = OS.PtWidgetChildFront (parentHandle);
	while (i < count) {
		Widget widget = WidgetTable.get (child);
		if (widget != null && widget != this) {
			if (widget instanceof Control) {
				children [j++] = (Control) widget;
			}
		}
		i++;
		child = OS.PtWidgetBrotherBehind (child);
	}
	if (i == j) return children;
	Control [] newChildren = new Control [j];
	System.arraycopy (children, 0, newChildren, 0, j);
	return newChildren;
}

Control [] _getTabList () {
	if (tabList == null) return tabList;
	int count = 0;
	for (int i=0; i<tabList.length; i++) {
		if (!tabList [i].isDisposed ()) count++;
	}
	if (count == tabList.length) return tabList;
	Control [] newList = new Control [count];
	int index = 0;
	for (int i=0; i<tabList.length; i++) {
		if (!tabList [i].isDisposed ()) {
			newList [index++] = tabList [i];
		}
	}
	tabList = newList;
	return tabList;
}

/**
 * Clears any data that has been cached by a Layout for all widgets that 
 * are in the parent hierarchy of the changed control up to and including the 
 * receiver.  If an ancestor does not have a layout, it is skipped.
 * 
 * @param changed an array of controls that changed state and require a recalculation of size
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void changed (Control[] changed) {
	checkWidget ();
	if (changed == null) error (SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<changed.length; i++) {
		Control control = changed [i];
		if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		boolean ancestor = false;
		Composite composite = control.parent;
		while (composite != null) {
			ancestor = composite == this;
			if (ancestor) break;
			composite = composite.parent;
		}
		if (!ancestor) error (SWT.ERROR_INVALID_PARENT);
	}
	for (int i=0; i<changed.length; i++) {
		Control child = changed [i];
		Composite composite = child.parent;
		while (child != this) {
			if (composite.layout == null || !composite.layout.flushCache (child)) {
				composite.state |= LAYOUT_CHANGED;
			}
			child = composite;
			composite = child.parent;
		}
	}
}

protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}

Control [] computeTabList () {
	Control result [] = super.computeTabList ();
	if (result.length == 0) return result;
	Control [] list = tabList != null ? _getTabList () : _getChildren ();
	for (int i=0; i<list.length; i++) {
		Control child = list [i];
		Control [] childList = child.computeTabList ();
		if (childList.length != 0) {
			Control [] newResult = new Control [result.length + childList.length];
			System.arraycopy (result, 0, newResult, 0, result.length);
			System.arraycopy (childList, 0, newResult, result.length, childList.length);
			result = newResult;
		}
	}
	return result;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	display.runSkin ();
	Point size;
	if (layout != null) {
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			changed |= (state & LAYOUT_CHANGED) != 0;
			size = layout.computeSize (this, wHint, hHint, changed);
			state &= ~LAYOUT_CHANGED;
		} else {
			size = new Point (wHint, hHint);
		}
	} else {
		size = minimumSize ();
		if (size.x == 0) size.x = DEFAULT_WIDTH;
		if (size.y == 0) size.y = DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}

void createHandle (int index) {
	state |= HANDLE | GRAB | CANVAS;
	int parentHandle = parent.parentingHandle ();
	createScrolledHandle (parentHandle);
}

void createScrollBars () {
	if (scrolledHandle == 0) return;
	if ((style & SWT.H_SCROLL) != 0) {
		horizontalBar = new ScrollBar (this, SWT.HORIZONTAL);
	}
	if ((style & SWT.V_SCROLL) != 0) {
		verticalBar = new ScrollBar (this, SWT.VERTICAL);
	}
}

byte [] defaultFont () {
	return display.TITLE_FONT;
}

void createScrolledHandle (int parentHandle) {
	int etches = OS.Pt_ALL_ETCHES | OS.Pt_ALL_OUTLINES;
	int [] args = new int [] {
		OS.Pt_ARG_FLAGS, hasBorder () ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
		OS.Pt_ARG_BASIC_FLAGS, hasBorder () ? etches : 0, etches,
		OS.Pt_ARG_CONTAINER_FLAGS, 0, OS.Pt_ENABLE_CUA | OS.Pt_ENABLE_CUA_ARROWS,
		OS.Pt_ARG_FILL_COLOR, OS.Pg_TRANSPARENT, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	scrolledHandle = OS.PtCreateWidget (OS.PtContainer (), parentHandle, args.length / 3, args);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.H_SCROLL) != 0 && (style & SWT.V_SCROLL) != 0) {
		etches = OS.Pt_TOP_OUTLINE | OS.Pt_LEFT_OUTLINE;
		args = new int [] {
			OS.Pt_ARG_FLAGS, OS.Pt_HIGHLIGHTED, OS.Pt_HIGHLIGHTED,
			OS.Pt_ARG_BASIC_FLAGS, etches, etches,
			OS.Pt_ARG_WIDTH, display.SCROLLBAR_WIDTH, 0,
			OS.Pt_ARG_HEIGHT, display.SCROLLBAR_HEIGHT, 0,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};
		cornerHandle = OS.PtCreateWidget (OS.PtContainer (), scrolledHandle, args.length / 3, args);
	}
	int clazz = display.PtContainer;
	args = new int [] {
		OS.Pt_ARG_FLAGS, OS.Pt_SELECTABLE | OS.Pt_SELECT_NOREDRAW, OS.Pt_SELECTABLE | OS.Pt_SELECT_NOREDRAW,
		OS.Pt_ARG_CONTAINER_FLAGS, 0, OS.Pt_ENABLE_CUA | OS.Pt_ENABLE_CUA_ARROWS,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, scrolledHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	createScrollBars ();
}

/** 
 * Fills the interior of the rectangle specified by the arguments,
 * with the receiver's background. 
 *
 * <p>The <code>offsetX</code> and <code>offsetY</code> are used to map from
 * the <code>gc</code> origin to the origin of the parent image background. This is useful
 * to ensure proper alignment of the image background.</p>
 * 
 * @param gc the gc where the rectangle is to be filled
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 * @param offsetX the image background x offset 
 * @param offsetY the image background y offset
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.6
 */
public void drawBackground (GC gc, int x, int y, int width, int height, int offsetX, int offsetY) {
	drawBackground(gc, offsetX, offsetY, width, height);
}

void drawBackground (GC gc, int x, int y, int width, int height) {
	Color oldColor = gc.getBackground();
	gc.setBackground(getBackground());
	gc.fillRectangle(x, y, width, height);
	gc.setBackground(oldColor);
}

void drawWidget (int widget, int damage) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_BACKGROUND) == 0) {
			
			/*
			* Note that QNX 6.2.1 provides full widget hierarchy clipping in paint.
			*/
			if (!(OS.QNX_MAJOR > 6 || (OS.QNX_MAJOR == 6 && (OS.QNX_MINOR > 2 || (OS.QNX_MINOR == 2 && OS.QNX_MICRO >= 1))))) {
				/* Get the clipping tiles for children and siblings */
				int clip_tile = getClipping (handle, topHandle (), true, true);
				if (clip_tile == 0) return;
	
				/* Translate the clipping to the current GC coordinates */
				short [] abs_x = new short [1], abs_y = new short [1];
				OS.PtGetAbsPosition (handle, abs_x, abs_y);
				short [] dis_abs_x = new short [1], dis_abs_y = new short [1];
				OS.PtGetAbsPosition (OS.PtFindDisjoint (handle), dis_abs_x, dis_abs_y);
				PhPoint_t delta = new PhPoint_t ();
				delta.x = (short) (abs_x [0] - dis_abs_x [0]);
				delta.y = (short) (abs_y [0] - dis_abs_y [0]);
				OS.PhTranslateTiles(clip_tile, delta);
	
				/* Set the clipping */
				int[] clip_rects_count = new int [1];
				int clip_rects = OS.PhTilesToRects (clip_tile, clip_rects_count);
				OS.PhFreeTiles (clip_tile);	
				if (clip_rects_count [0] == 0) {
					clip_rects_count [0] = 1;
					OS.free (clip_rects);
					clip_rects = OS.malloc (PhRect_t.sizeof);
					OS.memset(clip_rects, 0, PhRect_t.sizeof);
				}
				OS.PgSetMultiClip (clip_rects_count[0], clip_rects);
				OS.free (clip_rects);
			}
			
			/* Draw the widget */
			super.drawWidget (widget, damage);
			
			if (!(OS.QNX_MAJOR > 6 || (OS.QNX_MAJOR == 6 && (OS.QNX_MINOR > 2 || (OS.QNX_MINOR == 2 && OS.QNX_MICRO >= 1))))) {
				/* Reset the clipping */
				OS.PgSetMultiClip (0, 0);
			}
		}
	} else {
		super.drawWidget (widget, damage);
	}
}

Composite findDeferredControl () {
	return layoutCount > 0 ? this : parent.findDeferredControl ();
}

void fixTabList (Control control) {
	if (tabList == null) return;
	int count = 0;
	for (int i=0; i<tabList.length; i++) {
		if (tabList [i] == control) count++;
	}
	if (count == 0) return;
	Control [] newList = null;
	int length = tabList.length - count;
	if (length != 0) {
		newList = new Control [length];
		int index = 0;
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] != control) {
				newList [index++] = tabList [i];
			}
		}
	}
	tabList = newList;
}

public boolean forceFocus () {
	checkWidget();
	if ((state & CANVAS) == 0) return super.forceFocus ();
	/*
	* Bug in Photon. PtContainerGiveFocus() is supposed to give
	* focus to the widget even if the widget's Pt_GET_FOCUS flag
	* is not set. This does not happen when the widget is a
	* PtContainer. The fix is to set the flag before calling it.
	*/
	int flags = OS.PtWidgetFlags (handle);
	OS.PtSetResource (handle, OS.Pt_ARG_FLAGS, OS.Pt_GETS_FOCUS, OS.Pt_GETS_FOCUS);
	boolean result = super.forceFocus ();
	OS.PtSetResource (handle, OS.Pt_ARG_FLAGS, flags, OS.Pt_GETS_FOCUS);
	return result;
}

public Rectangle getClientArea () {
	checkWidget();
	if (scrolledHandle == 0) return super.getClientArea ();
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (handle, area);
	return new Rectangle (area.pos_x, area.pos_y, area.size_w, area.size_h);
}

int getClipping(int widget, int topWidget, boolean clipChildren, boolean clipSiblings) {
	int child_tile = 0;
	int widget_tile = OS.PhGetTile(); // NOTE: PhGetTile native initializes the tile

	PhRect_t rect = new PhRect_t ();
	int args [] = {OS.Pt_ARG_FLAGS, 0, 0, OS.Pt_ARG_BASIC_FLAGS, 0, 0};
	
	/* Get the rectangle of all siblings in front of the widget */
	if (clipSiblings && OS.PtWidgetClass(topWidget) != OS.PtWindow()) {
		int temp_widget = topWidget;
		while ((temp_widget = OS.PtWidgetBrotherInFront(temp_widget)) != 0) {
			if (OS.PtWidgetIsRealized(temp_widget)) {
				int tile = OS.PhGetTile();
				if (child_tile == 0) child_tile = tile;			
				else child_tile = OS.PhAddMergeTiles(tile, child_tile, null);
				OS.PtWidgetExtent(temp_widget, tile); // NOTE: tile->rect
				args [1] = args [4] = 0;
				OS.PtGetResources(temp_widget, args.length / 3, args);
				if ((args [1] & OS.Pt_HIGHLIGHTED) != 0) {
					int basic_flags = args [4];
					OS.memmove(rect, tile, PhRect_t.sizeof);
					if ((basic_flags & OS.Pt_TOP_ETCH) != 0) rect.ul_y++;
					if ((basic_flags & OS.Pt_BOTTOM_ETCH) != 0) rect.lr_y--;
					if ((basic_flags & OS.Pt_RIGHT_ETCH) != 0) rect.ul_x++;
					if ((basic_flags & OS.Pt_LEFT_ETCH) != 0) rect.lr_x--;
					OS.memmove(tile, rect, PhRect_t.sizeof);
				}
			}
		}
		/* Translate the siblings rectangles to the widget's coordinates */
		OS.PtWidgetCanvas(topWidget, widget_tile); // NOTE: widget_tile->rect
		OS.PhDeTranslateTiles(child_tile, widget_tile); // NOTE: widget_tile->rect.ul
	}
			
	/* Get the rectangle of the widget's children */
	if (clipChildren) {
		int temp_widget = OS.PtWidgetChildBack(widget);
		while (temp_widget != 0) {
			if (OS.PtWidgetIsRealized(temp_widget)) {
				int tile = OS.PhGetTile();
				if (child_tile == 0) child_tile = tile;			
				else child_tile = OS.PhAddMergeTiles(tile, child_tile, null);
				OS.PtWidgetExtent(temp_widget, tile); // NOTE: tile->rect
				args [1] = args [4] = 0;
				OS.PtGetResources(temp_widget, args.length / 3, args);
				if ((args [1] & OS.Pt_HIGHLIGHTED) != 0) {
					int basic_flags = args [4];
					OS.memmove(rect, tile, PhRect_t.sizeof);
					if ((basic_flags & OS.Pt_TOP_ETCH) != 0) rect.ul_y++;
					if ((basic_flags & OS.Pt_BOTTOM_ETCH) != 0) rect.lr_y--;
					if ((basic_flags & OS.Pt_RIGHT_ETCH) != 0) rect.ul_x++;
					if ((basic_flags & OS.Pt_LEFT_ETCH) != 0) rect.lr_x--;
					OS.memmove(tile, rect, PhRect_t.sizeof);
				}
			}
			temp_widget = OS.PtWidgetBrotherInFront(temp_widget);
		}
	}

	/* Get the widget's rectangle */
	OS.PtWidgetCanvas(widget, widget_tile); // NOTE: widget_tile->rect
	OS.PhDeTranslateTiles(widget_tile, widget_tile); // NOTE: widget_tile->rect.ul

	/* Clip the widget's rectangle from the child/siblings rectangle's */
	if (child_tile != 0) {
		int clip_tile = OS.PhClipTilings(widget_tile, child_tile, null);
		OS.PhFreeTiles(child_tile);
		return clip_tile;
	}
	return widget_tile;
}

/**
 * Returns the receiver's background drawing mode. This
 * will be one of the following constants defined in class
 * <code>SWT</code>:
 * <code>INHERIT_NONE</code>, <code>INHERIT_DEFAULT</code>,
 * <code>INHERTIT_FORCE</code>.
 *
 * @return the background mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 * 
 * @since 3.2
 */
public int getBackgroundMode () {
	checkWidget ();
	return backgroundMode;
}

/**
 * Returns a (possibly empty) array containing the receiver's children.
 * Children are returned in the order that they are drawn.  The topmost
 * control appears at the beginning of the array.  Subsequent controls
 * draw beneath this control and appear later in the array.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of children, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return an array of children
 * 
 * @see Control#moveAbove
 * @see Control#moveBelow
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control [] getChildren () {
	checkWidget();
	return _getChildren ();
}

int getChildrenCount () {
	int count = 0;
	int parentHandle = parentingHandle ();
	int child = OS.PtWidgetChildFront (parentHandle);
	while (child != 0) {
		child = OS.PtWidgetBrotherBehind (child);
		count++;
	}
	return count;
}

/**
 * Returns layout which is associated with the receiver, or
 * null if one has not been set.
 *
 * @return the receiver's layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Layout getLayout () {
	checkWidget();
	return layout;
}

/**
 * Returns <code>true</code> if the receiver has deferred
 * the performing of layout, and <code>false</code> otherwise.
 *
 * @return the receiver's deferred layout state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setLayoutDeferred(boolean)
 * @see #isLayoutDeferred()
 *
 * @since 3.1
 */
public boolean getLayoutDeferred () {
	checkWidget ();
	return layoutCount > 0 ;
}

boolean hasBorder () {
	return (style & SWT.BORDER) != 0;
}

boolean hasFocus () {
	return OS.PtIsFocused (handle) == 2;
}

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
}

/**
 * Returns <code>true</code> if the receiver or any ancestor 
 * up to and including the receiver's nearest ancestor shell
 * has deferred the performing of layouts.  Otherwise, <code>false</code>
 * is returned.
 *
 * @return the receiver's deferred layout state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setLayoutDeferred(boolean)
 * @see #getLayoutDeferred()
 * 
 * @since 3.1
 */
public boolean isLayoutDeferred () {
	checkWidget ();
	return findDeferredControl () != null;
}

/**
 * Gets the (possibly empty) tabbing order for the control.
 *
 * @return tabList the ordered list of controls representing the tab order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setTabList
 */
public Control [] getTabList () {
	checkWidget ();
	Control [] tabList = _getTabList ();
	if (tabList == null) {
		int count = 0;
		Control [] list =_getChildren ();
		for (int i=0; i<list.length; i++) {
			if (list [i].isTabGroup ()) count++;
		}
		tabList = new Control [count];
		int index = 0;
		for (int i=0; i<list.length; i++) {
			if (list [i].isTabGroup ()) {
				tabList [index++] = list [i];
			}
		}
	}
	return tabList;
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the receiver does not have a layout, do nothing.
 * <p>
 * This is equivalent to calling <code>layout(true)</code>.
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout () {
	checkWidget ();
	layout (true);
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the argument is <code>true</code> the layout must not rely
 * on any information it has cached about the immediate children. If it
 * is <code>false</code> the layout may (potentially) optimize the
 * work it is doing by assuming that none of the receiver's 
 * children has changed state since the last layout.
 * If the receiver does not have a layout, do nothing.
 * <p>
 * If a child is resized as a result of a call to layout, the 
 * resize event will invoke the layout of the child.  The layout
 * will cascade down through all child widgets in the receiver's widget 
 * tree until a child is encountered that does not resize.  Note that 
 * a layout due to a resize will not flush any cached information 
 * (same as <code>layout(false)</code>).
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout (boolean changed) {
	checkWidget ();
	if (layout == null) return;
	layout (changed, false);
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the changed argument is <code>true</code> the layout must not rely
 * on any information it has cached about its children. If it
 * is <code>false</code> the layout may (potentially) optimize the
 * work it is doing by assuming that none of the receiver's 
 * children has changed state since the last layout.
 * If the all argument is <code>true</code> the layout will cascade down
 * through all child widgets in the receiver's widget tree, regardless of
 * whether the child has changed size.  The changed argument is applied to 
 * all layouts.  If the all argument is <code>false</code>, the layout will
 * <em>not</em> cascade down through all child widgets in the receiver's widget 
 * tree.  However, if a child is resized as a result of a call to layout, the 
 * resize event will invoke the layout of the child.  Note that 
 * a layout due to a resize will not flush any cached information 
 * (same as <code>layout(false)</code>).
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 * @param all <code>true</code> if all children in the receiver's widget tree should be laid out, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void layout (boolean changed, boolean all) {
	checkWidget ();
	if (layout == null && !all) return;
	markLayout (changed, all);
	updateLayout (all);
}

/**
 * Forces a lay out (that is, sets the size and location) of all widgets that 
 * are in the parent hierarchy of the changed control up to and including the 
 * receiver.  The layouts in the hierarchy must not rely on any information 
 * cached about the changed control or any of its ancestors.  The layout may 
 * (potentially) optimize the work it is doing by assuming that none of the 
 * peers of the changed control have changed state since the last layout.
 * If an ancestor does not have a layout, skip it.
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 * 
 * @param changed a control that has had a state change which requires a recalculation of its size
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void layout (Control [] changed) {
	checkWidget ();
	if (changed == null) error (SWT.ERROR_INVALID_ARGUMENT);
	layout (changed, SWT.NONE);
}

/**
 * Forces a lay out (that is, sets the size and location) of all widgets that 
 * are in the parent hierarchy of the changed control up to and including the 
 * receiver. 
 * <p>
 * The parameter <code>flags</code> may be a combination of:
 * <dl>
 * <dt><b>SWT.ALL</b></dt>
 * <dd>all children in the receiver's widget tree should be laid out</dd>
 * <dt><b>SWT.CHANGED</b></dt>
 * <dd>the layout must flush its caches</dd>
 * <dt><b>SWT.DEFER</b></dt>
 * <dd>layout will be deferred</dd>
 * </dl>
 * </p>
 * <p>
 * When the <code>changed</code> array is specified, the flags <code>SWT.ALL</code>
 * and <code>SWT.CHANGED</code> have no effect. In this case, the layouts in the 
 * hierarchy must not rely on any information cached about the changed control or
 * any of its ancestors.  The layout may (potentially) optimize the
 * work it is doing by assuming that none of the peers of the changed
 * control have changed state since the last layout.
 * If an ancestor does not have a layout, skip it.
 * </p>
 * <p>
 * When the <code>changed</code> array is not specified, the flag <code>SWT.ALL</code>
 * indicates that the whole widget tree should be laid out. And the flag
 * <code>SWT.CHANGED</code> indicates that the layouts should flush any cached
 * information for all controls that are laid out. 
 * </p>
 * <p>
 * The <code>SWT.DEFER</code> flag always causes the layout to be deferred by
 * calling <code>Composite.setLayoutDeferred(true)</code> and scheduling a call
 * to <code>Composite.setLayoutDeferred(false)</code>, which will happen when
 * appropriate (usually before the next event is handled). When this flag is set,
 * the application should not call <code>Composite.setLayoutDeferred(boolean)</code>.
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 * 
 * @param changed a control that has had a state change which requires a recalculation of its size
 * @param flags the flags specifying how the layout should happen
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the controls in changed is null or has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.6
 */
public void layout (Control [] changed, int flags) {
	checkWidget ();
	if (changed != null) {
		for (int i=0; i<changed.length; i++) {
			Control control = changed [i];
			if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
			if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
			boolean ancestor = false;
			Composite composite = control.parent;
			while (composite != null) {
				ancestor = composite == this;
				if (ancestor) break;
				composite = composite.parent;
			}
			if (!ancestor) error (SWT.ERROR_INVALID_PARENT);
		}
		int updateCount = 0;
		Composite [] update = new Composite [16];
		for (int i=0; i<changed.length; i++) {
			Control child = changed [i];
			Composite composite = child.parent;
			while (child != this) {
				if (composite.layout != null) {
					composite.state |= LAYOUT_NEEDED;
					if (!composite.layout.flushCache (child)) {
						composite.state |= LAYOUT_CHANGED;
					}
				}
				if (updateCount == update.length) {
					Composite [] newUpdate = new Composite [update.length + 16];
					System.arraycopy (update, 0, newUpdate, 0, update.length);
					update = newUpdate;
				}
				child = update [updateCount++] = composite;
				composite = child.parent;
			}
		}
		if ((flags & SWT.DEFER) != 0) {
			setLayoutDeferred (true);
			display.addLayoutDeferred (this);
		}
		for (int i=updateCount-1; i>=0; i--) {
			update [i].updateLayout (false);
		}
	} else {
		if (layout == null && (flags & SWT.ALL) == 0) return;
		markLayout ((flags & SWT.CHANGED) != 0, (flags & SWT.ALL) != 0);
		if ((flags & SWT.DEFER) != 0) {
			setLayoutDeferred (true);
			display.addLayoutDeferred (this);
		}
		updateLayout ((flags & SWT.ALL) != 0);
	}
}

void markLayout (boolean changed, boolean all) {
	if (layout != null) {
		state |= LAYOUT_NEEDED;
		if (changed) state |= LAYOUT_CHANGED;
	}
	if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children [i].markLayout (changed, all);
		}
	}
}

Point minimumSize () {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Rectangle rect = children [i].getBounds ();
		width = Math.max (width, rect.x + rect.width);
		height = Math.max (height, rect.y + rect.height);
	}
	return new Point (width, height);
}

void moveToBack (int child) {
	OS.PtWidgetToBack (child);
}

int parentingHandle () {
	return handle;
}

int Ph_EV_BUT_PRESS (int widget, int info) {
	int result = super.Ph_EV_BUT_PRESS (widget, info);
	if (result != OS.Pt_CONTINUE)return result;
	if ((state & CANVAS) != 0) {
		/* Set focus for a CANVAS with no children */
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			if (OS.PtWidgetChildFront (handle) == 0) {
				if (info == 0) return OS.Pt_END;
				PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
				OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
				if (cbinfo.event == 0) return OS.Pt_END;
				PhEvent_t ev = new PhEvent_t ();
				OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
				int data = OS.PhGetData (cbinfo.event);
				if (data == 0) return OS.Pt_END;
				PhPointerEvent_t pe = new PhPointerEvent_t ();
				OS.memmove (pe, data, PhPointerEvent_t.sizeof);
				if (pe.buttons == OS.Ph_BUTTON_SELECT) setFocus ();
			}
		}
	}
	return result;
}

int Pt_CB_OUTBOUND (int widget, int info) {
	if ((state & CANVAS) != 0) {
		if (info == 0) return OS.Pt_END;
		PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
		OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
		if (cbinfo.event == 0) return OS.Pt_END;
		PhEvent_t ev = new PhEvent_t ();
		OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
		int data = OS.PhGetData (cbinfo.event);
		if (data == 0) return OS.Pt_END;
		PhPointerEvent_t pe = new PhPointerEvent_t ();
		OS.memmove (pe, data, PhPointerEvent_t.sizeof);
	
		/* Grab pointer */
		PhRect_t rect = new PhRect_t ();
		PhPoint_t pos = new PhPoint_t ();
		pos.x = pe.pos_x;
		pos.y = pe.pos_y;
		rect.ul_x = rect.lr_x = (short) (pos.x + ev.translation_x);
		rect.ul_y = rect.lr_y = (short) (pos.y + ev.translation_y);
		int rid = OS.PtWidgetRid (handle);
		int input_group = OS.PhInputGroup (0);
		int flags = OS.Ph_DRAG_KEY_MOTION | OS.Ph_DRAG_TRACK | OS.Ph_TRACK_DRAG;
		OS.PhInitDrag (rid, flags, rect, null, input_group, null, null, null, pos, null);
		
		/* Post drag detect event */
		Event event = new Event ();
		event.x = display.dragStartX;
		event.y = display.dragStartY;
		postEvent (SWT.DragDetect, event);
	}
	return OS.Pt_CONTINUE;
}

void releaseChildren (boolean destroy) {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child != null && !child.isDisposed ()) {
			child.release (false);
		}
	}
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	cornerHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	layout = null;
	tabList = null;
}

void removeControl (Control control) {
	fixTabList (control);
}

void reskinChildren (int flags) {
	super.reskinChildren (flags);
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child != null) child.reskin (flags);
	}
}

void resizeClientArea () {
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (scrolledHandle, args.length / 3, args);
	resizeClientArea (args [1], args [4], true);
}

void resizeClientArea (int width, int height, boolean events) {
	if (scrolledHandle == 0) return;
	
	/* Calculate the insets */
	int [] args = {
		OS.Pt_ARG_BASIC_FLAGS, 0, 0,
		OS.Pt_ARG_BEVEL_WIDTH, 0, 0,
	};
	OS.PtGetResources (scrolledHandle, args.length / 3, args);
	int flags = args [1];
	int bevel = args [4];
	int top = 0, left = 0, right = 0, bottom = 0;
	if ((flags & OS.Pt_TOP_ETCH) != 0) top++;
	if ((flags & OS.Pt_TOP_OUTLINE) != 0) top++;
	if ((flags & OS.Pt_TOP_INLINE) != 0) top++;
	if ((flags & OS.Pt_TOP_BEVEL) != 0) top += bevel;
	if ((flags & OS.Pt_BOTTOM_ETCH) != 0) bottom++;
	if ((flags & OS.Pt_BOTTOM_OUTLINE) != 0) bottom++;
	if ((flags & OS.Pt_BOTTOM_INLINE) != 0) bottom++;
	if ((flags & OS.Pt_BOTTOM_BEVEL) != 0) bottom += bevel;
	if ((flags & OS.Pt_RIGHT_ETCH) != 0) right++;
	if ((flags & OS.Pt_RIGHT_OUTLINE) != 0) right++;
	if ((flags & OS.Pt_RIGHT_INLINE) != 0) right++;
	if ((flags & OS.Pt_RIGHT_BEVEL) != 0) right += bevel;
	if ((flags & OS.Pt_LEFT_ETCH) != 0) left++;
	if ((flags & OS.Pt_LEFT_OUTLINE) != 0) left++;
	if ((flags & OS.Pt_LEFT_INLINE) != 0) left++;
	if ((flags & OS.Pt_LEFT_BEVEL) != 0) left += bevel;
	
	int clientWidth = width - (left + right);
	int clientHeight = height - (top + bottom);

	int vBarWidth = 0, hBarHeight = 0;
	boolean isVisibleHBar = horizontalBar != null && horizontalBar.getVisible ();
	boolean isVisibleVBar = verticalBar != null && verticalBar.getVisible ();
	if (isVisibleHBar) {
		args = new int [] {OS.Pt_ARG_HEIGHT, 0, 0};
		OS.PtGetResources (horizontalBar.handle, args.length / 3, args);
		clientHeight -= (hBarHeight = args [1]);
	}
	if (isVisibleVBar) {
		args = new int [] {OS.Pt_ARG_WIDTH, 0, 0};
		OS.PtGetResources (verticalBar.handle, args.length / 3, args);
		clientWidth -= (vBarWidth = args [1]);
	}
	if (isVisibleHBar) {
		horizontalBar.setBounds (0, clientHeight, clientWidth, hBarHeight);
	}
	if (isVisibleVBar) {
		verticalBar.setBounds (clientWidth, 0, vBarWidth, clientHeight);
	}
	args = new int [] {
		OS.Pt_ARG_WIDTH, Math.max (clientWidth, 0), 0,
		OS.Pt_ARG_HEIGHT, Math.max (clientHeight, 0), 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	if (cornerHandle != 0) {
		PhPoint_t pt = new PhPoint_t ();
		pt.x = (short) clientWidth;
		pt.y = (short) clientHeight;
		int ptr = OS.malloc (PhPoint_t.sizeof);
		OS.memmove (ptr, pt, PhPoint_t.sizeof);
		OS.PtSetResource (cornerHandle, OS.Pt_ARG_POS, ptr, 0);
		OS.free (ptr);
	}
	
	if (events) {
		sendEvent (SWT.Resize);
	}
}

/**
 * Sets the background drawing mode to the argument which should
 * be one of the following constants defined in class <code>SWT</code>:
 * <code>INHERIT_NONE</code>, <code>INHERIT_DEFAULT</code>,
 * <code>INHERIT_FORCE</code>.
 *
 * @param mode the new background mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 * 
 * @since 3.2
 */
public void setBackgroundMode (int mode) {
	checkWidget ();
	backgroundMode = mode;
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	int result = super.setBounds (x, y, width, height, move, resize, false);
	if ((result & MOVED) != 0) {
		if (events) sendEvent (SWT.Move);
	}
	if ((result & RESIZED) != 0) {
		resizeClientArea (width, height, false);
		if (events) sendEvent (SWT.Resize);
		if (layout != null) {
			markLayout (false, false);
			updateLayout (false);
		}
	}
	return result;
}

public boolean setFocus () {
	checkWidget();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.getVisible () && child.setFocus ()) return true;
	}
	return super.setFocus ();
}

/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 *
 * @param layout the receiver's new layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayout (Layout layout) {
	checkWidget();
	this.layout = layout;
}

/**
 * If the argument is <code>true</code>, causes subsequent layout
 * operations in the receiver or any of its children to be ignored.
 * No layout of any kind can occur in the receiver or any of its
 * children until the flag is set to false.
 * Layout operations that occurred while the flag was
 * <code>true</code> are remembered and when the flag is set to 
 * <code>false</code>, the layout operations are performed in an
 * optimized manner.  Nested calls to this method are stacked.
 *
 * @param defer the new defer state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #layout(boolean)
 * @see #layout(Control[])
 *
 * @since 3.1
 */
public void setLayoutDeferred (boolean defer) {
	checkWidget();
	if (!defer) {
		if (--layoutCount == 0) {
			if ((state & LAYOUT_CHILD) != 0 || (state & LAYOUT_NEEDED) != 0) {
				updateLayout (true);
			}
		}
	} else {
		layoutCount++;
	}
}

boolean setTabGroupFocus () {
	if (isTabItem ()) return setTabItemFocus ();
	boolean takeFocus = (style & SWT.NO_FOCUS) == 0;
	if ((state & CANVAS) != 0) takeFocus = hooksKeys ();
	if (takeFocus && setTabItemFocus ()) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.isTabItem () && child.setTabItemFocus ()) return true;
	}
	return false;
}

/**
 * Sets the tabbing order for the specified controls to
 * match the order that they occur in the argument list.
 *
 * @param tabList the ordered list of controls representing the tab order or null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if a widget in the tabList is null or has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if widget in the tabList is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTabList (Control [] tabList) {
	checkWidget ();
	if (tabList == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<tabList.length; i++) {
		Control control = tabList [i];
		if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}
	this.tabList = tabList;
}

int traversalCode (int key_sym, PhKeyEvent_t ke) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return 0;
		if (hooksKeys ()) return 0;
	}
	return super.traversalCode (key_sym, ke);
}

void updateLayout (boolean all) {
	Composite parent = findDeferredControl ();
	if (parent != null) {
		parent.state |= LAYOUT_CHILD;
		return;
	}
	if ((state & LAYOUT_NEEDED) != 0) {
		boolean changed = (state & LAYOUT_CHANGED) != 0;
		state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
		display.runSkin ();
		layout.layout (this, changed);
	}
	if (all) {
		state &= ~LAYOUT_CHILD;
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children [i].updateLayout (all);
		}
	}
}

int widgetClass () {
	if ((state & CANVAS) != 0) return OS.PtContainer ();
	return super.widgetClass ();
}

}
