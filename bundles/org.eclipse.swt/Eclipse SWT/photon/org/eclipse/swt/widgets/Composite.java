package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
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
		/*
		* Bug in Photon.  We must set Pt_GETS_FOCUS or Photon will
		* segment fault when no widget has focus and a key is pressed.
		*/
		OS.Pt_ARG_FLAGS, OS.Pt_GETS_FOCUS, OS.Pt_GETS_FOCUS,
		OS.Pt_ARG_FLAGS, hasBorder () ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
		OS.Pt_ARG_BASIC_FLAGS, hasBorder () ? etches : 0, etches,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	scrolledHandle = OS.PtCreateWidget (OS.PtContainer (), parentHandle, args.length / 3, args);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	Display display = getDisplay ();
	int clazz = display.PtContainer;
	args = new int [] {
		OS.Pt_ARG_FLAGS, OS.Pt_GETS_FOCUS, OS.Pt_GETS_FOCUS,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, scrolledHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	createScrollBars ();
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

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtContainer (), handle, damage);
	return super.processPaint (damage);
}

int processResize (int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_CONTINUE;
	sendEvent (SWT.Resize);
	if (layout != null) layout (false);
	return OS.Pt_CONTINUE;
}

void releaseWidget () {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (!child.isDisposed ()) {
			child.releaseWidget ();
			child.releaseHandle ();
		}
	}
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
	int vBarWidth = 0, hBarHeight = 0;
	boolean isVisibleHBar = horizontalBar != null && horizontalBar.getVisible ();
	boolean isVisibleVBar = verticalBar != null && verticalBar.getVisible ();
	if (isVisibleHBar) {
		int [] args = {OS.Pt_ARG_HEIGHT, 0, 0};
		OS.PtGetResources (horizontalBar.handle, args.length / 3, args);
		height = height - (hBarHeight = args [1]);
	}
	if (isVisibleVBar) {
		int [] args = {OS.Pt_ARG_WIDTH, 0, 0};
		OS.PtGetResources (verticalBar.handle, args.length / 3, args);
		width = width - (vBarWidth = args [1]);
	}
	
	//NOT DONE - used widget canvas to compute insets
	int left = 0, right = 0;
	if (hasBorder ()) {
		left = 2;
		if (isVisibleHBar && isVisibleVBar) right = 3;
	}
	if (isVisibleHBar) {
		horizontalBar.setBounds (-left, height - left, width + right, hBarHeight); 
	}
	if (isVisibleVBar) {
		verticalBar.setBounds (width - left, -left, vBarWidth, height + right); 
	}
	
	PhArea_t area = new PhArea_t ();
	area.size_w = (short) (Math.max (width - (left * 2), 0));
	area.size_h = (short) (Math.max (height - (left * 2), 0));
	int ptr = OS.malloc (PhArea_t.sizeof);
	OS.memmove (ptr, area, PhArea_t.sizeof);
	int [] args = {OS.Pt_ARG_AREA, ptr, 0};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
}

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	if (resize) resizeClientArea (width, height);
	super.setBounds (x, y, width, height, move, resize);
}

public void setLayout (Layout layout) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.layout = layout;
}

}