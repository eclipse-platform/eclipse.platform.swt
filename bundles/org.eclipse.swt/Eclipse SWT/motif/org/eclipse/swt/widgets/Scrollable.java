package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class Scrollable extends Control {
	int scrolledHandle, formHandle;
	ScrollBar horizontalBar, verticalBar;
Scrollable () {
	/* Do nothing */
}
public Scrollable (Composite parent, int style) {
	super (parent, style);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int border = getBorderWidth ();
	int trimX = x - border, trimY = y - border;
	int trimWidth = width + (border * 2), trimHeight = height + (border * 2);
	boolean hasHBar = (horizontalBar != null) && (horizontalBar.getVisible ());
	boolean hasVBar = (verticalBar != null) && (verticalBar.getVisible ());
	if (hasHBar) {
		Display display = getDisplay ();
		trimY -= display.scrolledInsetY;
		trimHeight += display.scrolledInsetY + display.scrolledMarginY;
		if (!hasVBar) {
			trimX -= display.scrolledInsetX;
			trimWidth += display.scrolledInsetX * 2;
			trimHeight -= display.scrolledInsetY * 2;
		}
	}
	if (hasVBar) {
		Display display = getDisplay ();
		trimX -= display.scrolledInsetX;
		trimWidth += display.scrolledInsetX + display.scrolledMarginX;
		if (!hasHBar) {
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
	bar.state |= HANDLE;
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
void deregister () {
	super.deregister ();
	if (formHandle != 0) WidgetTable.remove (formHandle);
	if (scrolledHandle != 0) WidgetTable.remove (scrolledHandle);
}
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	if (formHandle != 0) enableHandle (enabled, formHandle);
	if (scrolledHandle != 0) {
		enableHandle (enabled, scrolledHandle);
		int [] argList = {
			OS.XmNhorizontalScrollBar, 0,
			OS.XmNverticalScrollBar, 0,
		};
		OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
		if (argList [1] != 0) enableHandle (enabled, argList [1]);
		if (argList [3] != 0) enableHandle (enabled, argList [3]);
	}
}
public Rectangle getClientArea () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return new Rectangle (0, 0, argList [1], argList [3]);
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
	if (formHandle != 0) propagateHandle (enabled, formHandle);
	if (scrolledHandle != 0) {
		propagateHandle (enabled, scrolledHandle);
		int [] argList = {
			OS.XmNhorizontalScrollBar, 0,
			OS.XmNverticalScrollBar, 0,
		};
		OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
		if (argList [1] != 0) propagateHandle (enabled, argList [1]);
		if (argList [3] != 0) propagateHandle (enabled, argList [3]);
	}
}
void redrawWidget (int x, int y, int width, int height, boolean all) {
	super.redrawWidget (x, y, width, height, all);
	/*
	* Uncomment this code to force the window trimmings to redraw.
	*/
//	if (formHandle == 0 && scrolledHandle == 0) return;
//	short [] root_x = new short [1], root_y = new short [1];
//	OS.XtTranslateCoords (handle, (short) x, (short) y, root_x, root_y);
//	if (formHandle != 0) {
//		short [] form_x = new short [1], form_y = new short [1];
//		OS.XtTranslateCoords (formHandle, (short) 0, (short) 0, form_x, form_y);
//		redrawHandle (root_x [0] - form_x [0], root_y [0] - form_y [0], width, height, formHandle);
//	}
//	if (scrolledHandle != 0) {
//		short [] scrolled_x = new short [1], scrolled_y = new short [1];
//		OS.XtTranslateCoords (scrolledHandle, (short) 0, (short) 0, scrolled_x, scrolled_y);
//		redrawHandle (root_x [0] - scrolled_x [0], root_y [0] - scrolled_y [0], width, height, scrolledHandle);
//		if (horizontalBar != null && horizontalBar.getVisible ()) {
//			int horizontalHandle = horizontalBar.handle;
//			short [] hscroll_x = new short [1], hscroll_y = new short [1];
//			OS.XtTranslateCoords (horizontalHandle, (short) 0, (short) 0, hscroll_x, hscroll_y);
//			redrawHandle (root_x [0] - hscroll_x [0], root_y [0] - hscroll_y [0], width, height, horizontalHandle);
//		}
//		if (verticalBar != null && verticalBar.getVisible ()) {
//			int verticalHandle = verticalBar.handle;
//			short [] vscroll_x = new short [1], vscroll_y = new short [1];
//			OS.XtTranslateCoords (verticalHandle, (short) 0, (short) 0, vscroll_x, vscroll_y);
//			redrawHandle (root_x [0] - vscroll_x [0], root_y [0] - vscroll_y [0], width, height, verticalHandle);
//		}
//	}
}
void register () {
	super.register ();
	if (formHandle != 0) WidgetTable.put (formHandle, this);
	if (scrolledHandle != 0) WidgetTable.put (scrolledHandle, this);
}
void releaseHandle () {
	super.releaseHandle ();
	scrolledHandle = formHandle = 0;
}
void releaseWidget () {
	if (horizontalBar != null) {
		horizontalBar.releaseWidget ();
		horizontalBar.releaseHandle ();
	}
	if (verticalBar != null) {
		verticalBar.releaseWidget ();
		verticalBar.releaseHandle ();
	}
	horizontalBar = verticalBar = null;
	super.releaseWidget ();
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	if (scrolledHandle != 0) {
		int [] argList1 = {
			OS.XmNhorizontalScrollBar, 0,
			OS.XmNverticalScrollBar, 0,
		};
		OS.XtGetValues (scrolledHandle, argList1, argList1.length / 2);
		if (argList1 [1] != 0) OS.XmChangeColor (argList1 [1], pixel);
		if (argList1 [3] != 0) OS.XmChangeColor (argList1 [3], pixel);
	}
}
int topHandle () {
	if (scrolledHandle != 0) return scrolledHandle;
	if (formHandle != 0) return formHandle;
	return handle;
}
}
