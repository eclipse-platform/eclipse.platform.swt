package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class Scrollable extends Control {
	int scrolledHandle;
	ScrollBar horizontalBar, verticalBar;

Scrollable () {
	/* Do nothing */
}

public Scrollable (Composite parent, int style) {
	super (parent, style);
}

void createScrollBars () {
	/* Search the handle to find the scroll bars */
	int child = OS.PtWidgetChildFront (handle);
	while (child != 0) {
		if (OS.PtWidgetClass (child) == OS.PtScrollbar ()) {
			int [] args = {OS.Pt_ARG_ORIENTATION, 0, 0};
			OS.PtGetResources (child, args.length / 3, args);
			switch (args [1]) {
				case OS.Pt_HORIZONTAL:
					horizontalBar = new ScrollBar (this, SWT.HORIZONTAL, child);
					break;
				case OS.Pt_VERTICAL:
					verticalBar = new ScrollBar (this, SWT.VERTICAL, child);
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

public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	PhRect_t rect = new PhRect_t ();
	PhArea_t area = new PhArea_t ();
	rect.ul_x = (short) x;
	rect.ul_y = (short) y;
	rect.lr_x = (short) (x + width - 1);
	rect.lr_y = (short) (y + height - 1);
	OS.PtSetAreaFromWidgetCanvas (scrolledHandle != 0 ? scrolledHandle : handle, rect, area);
	if (horizontalBar != null && horizontalBar.getVisible ()) {
		Point size = horizontalBar.getSize ();
		area.size_h += size.y;
	}
	if (verticalBar != null && verticalBar.getVisible ()) {
		Point size = verticalBar.getSize ();
		area.size_w += size.x;
	}
	return new Rectangle (area.pos_x, area.pos_y, area.size_w, area.size_h);
}

public Rectangle getClientArea () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	PhRect_t rect = new PhRect_t ();
	int vParent = OS.PtValidParent (handle, OS.PtContainer ());
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	OS.PtCalcCanvas (vParent, rect);
	int width = rect.lr_x - rect.ul_x + 1;
	int height = rect.lr_y - rect.ul_y + 1;
	return new Rectangle (0, 0, width, height);
}

public ScrollBar getHorizontalBar () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return horizontalBar;
}

public ScrollBar getVerticalBar () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return verticalBar;
}

void releaseHandle () {
	super.releaseHandle ();
	scrolledHandle = 0;
}

void resizeClientArea () {
	/* Do nothing */
}

void releaseWidget () {
	if (horizontalBar != null) horizontalBar.releaseWidget ();
	if (verticalBar != null) verticalBar.releaseWidget ();
	horizontalBar = verticalBar = null;
	super.releaseWidget ();
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
