package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class Composite extends Scrollable {
	Layout layout;
	
Composite () {
	/* Do nothing */
}

public Composite (Composite parent, int style) {
	super (parent, style);
}
	
Control [] _getChildren () {
	int count = 0;
	int child = OS.PtWidgetChildFront (handle);
	while (child != 0) {
		child = OS.PtWidgetBrotherBehind (child);
		count++;
	}
	Control [] children = new Control [count];
	int i = 0, j = 0;
	child = OS.PtWidgetChildFront (handle);
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

protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Point size;
	if (layout != null) {
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			size = layout.computeSize (this, wHint, hHint, changed);
		} else {
			size = new Point (wHint, hHint);
		}
	} else {
		size = minimumSize ();
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}

void createHandle (int index) {
	state |= HANDLE | CANVAS;
	int parentHandle = parent.handle;
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
	Display display = getDisplay ();
	etches = OS.Pt_TOP_OUTLINE | OS.Pt_LEFT_OUTLINE;
	args = new int [] {
		OS.Pt_ARG_FLAGS, OS.Pt_HIGHLIGHTED, OS.Pt_HIGHLIGHTED,
		OS.Pt_ARG_BASIC_FLAGS, etches, etches,
		OS.Pt_ARG_WIDTH, display.SCROLLBAR_WIDTH, 0,
		OS.Pt_ARG_HEIGHT, display.SCROLLBAR_HEIGHT, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	OS.PtCreateWidget (OS.PtContainer (), scrolledHandle, args.length / 3, args);
	int clazz = display.PtContainer;
	args = new int [] {
		OS.Pt_ARG_CONTAINER_FLAGS, 0, OS.Pt_ENABLE_CUA | OS.Pt_ENABLE_CUA_ARROWS,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, scrolledHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	createScrollBars ();
}

public Rectangle getClientArea () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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

public Control [] getChildren () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return _getChildren ();
}

int getChildrenCount () {
	int count = 0;
	int child = OS.PtWidgetChildFront (handle);
	while (child != 0) {
		child = OS.PtWidgetBrotherBehind (child);
		count++;
	}
	return count;
}

public Layout getLayout () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return layout;
}

boolean hasBorder () {
	return (style & SWT.BORDER) != 0;
}

boolean hasFocus () {
	return OS.PtIsFocused (handle) == 2;
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_RESIZE, windowProc, SWT.Resize);
}

public void layout () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	layout (true);
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

public void layout (boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (layout == null) return;
	int count = getChildrenCount ();
	if (count == 0) return;
	layout.layout (this, changed);
}

int processMouse (int info) {

	/* Set focus for a canvas with no children */
	if (OS.PtWidgetChildFront (handle) == 0) {
		if ((state & CANVAS) != 0 && (style & SWT.NO_FOCUS) == 0) {
			if (info == 0) return OS.Pt_END;
			PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
			OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
			if (cbinfo.event == 0) return OS.Pt_END;
			PhEvent_t ev = new PhEvent_t ();
			OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
			switch (ev.type) {
				case OS.Ph_EV_BUT_PRESS: {
					int data = OS.PhGetData (cbinfo.event);
					if (data == 0) return OS.Pt_END;
					PhPointerEvent_t pe = new PhPointerEvent_t ();
					OS.memmove (pe, data, PhPointerEvent_t.sizeof);
					if (pe.buttons == OS.Ph_BUTTON_SELECT) {
						setFocus ();
					}
				}
			}
		}
	}
	return super.processMouse (info);
}

int processPaint (int damage) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_BACKGROUND) == 0) {
			
			/* Get the clipping tiles for children and siblings */
			int clip_tile = getClipping (handle, topHandle (), true, true);

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
			}
			OS.PgSetMultiClip (clip_rects_count[0], clip_rects);
			OS.free (clip_rects);
			
			/* Draw the widget */
			OS.PtSuperClassDraw (OS.PtContainer (), handle, damage);
			
			/* Reset the clipping */
			OS.PgSetMultiClip (0, 0);
		}
	}
	return super.processPaint (damage);
}

int processResize (int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_CONTINUE;
	PtContainerCallback_t cbdata = new PtContainerCallback_t ();
	OS.memmove(cbdata, cbinfo.cbdata, PtContainerCallback_t.sizeof);
	if (cbdata.new_dim_w == cbdata.old_dim_w && cbdata.new_dim_h == cbdata.old_dim_h) {
		return OS.Pt_CONTINUE;
	}
	sendEvent (SWT.Resize);
	if (layout != null) layout (false);
	return OS.Pt_CONTINUE;
}

void releaseChildren () {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (!child.isDisposed ()) {
			child.releaseWidget ();
			child.releaseHandle ();
		}
	}
}

void releaseWidget () {
	releaseChildren ();
	super.releaseWidget ();
}

void resizeClientArea () {
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (scrolledHandle, args.length / 3, args);
	resizeClientArea (args [1], args [4]);
}

boolean sendResize () {
	return false;
}

void resizeClientArea (int width, int height) {
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
	PhPoint_t pt = new PhPoint_t ();
	pt.x = (short) clientWidth;
	pt.y = (short) clientHeight;
	int ptr = OS.malloc (PhPoint_t.sizeof);
	OS.memmove (ptr, pt, PhPoint_t.sizeof);
	args = new int [] {OS.Pt_ARG_POS, ptr, 0};
	OS.PtSetResources (OS.PtWidgetChildBack (scrolledHandle), args.length / 3, args);
	OS.free (ptr);
}

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	super.setBounds (x, y, width, height, move, resize);
	if (resize) resizeClientArea (width, height);
}

public void setLayout (Layout layout) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.layout = layout;
}

int traversalCode (int key_sym, PhKeyEvent_t ke) {
	if ((state & CANVAS) != 0 && hooks (SWT.KeyDown)) return 0;
	return super.traversalCode (key_sym, ke);
}

}
