package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public /*final*/ class Group extends Composite {

public Group (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Point titleSize = getTitleSize();
	Point size;
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize ();
	}
	int width = size.x;  int height = size.y;
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	width = Math.max (trim.width, titleSize.x + 6);
	height = trim.height + titleSize.y;
	return new Point (width, height);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createHandle (int index) {
	state |= HANDLE;
	Display display = getDisplay ();
	int clazz = display.PtPane;
	int parentHandle = parent.handle;
	int [] args = {
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_TITLE, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return "";
	int length = OS.strlen (args [1]);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, args [1], length);
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	return new String (unicode);
}

Point getTitleSize() {
	int width = 0, height = 0;
	int [] args = {
		OS.Pt_ARG_TITLE, 0, 0,
		OS.Pt_ARG_TITLE_FONT, 0, 0,
		OS.Pt_ARG_CONTAINER_FLAGS, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if ((OS.Pt_ARG_CONTAINER_FLAGS & OS.Pt_SHOW_TITLE) != 0) {
		PhRect_t rect = new PhRect_t();
		if (args [1] != 0) {
			int length = OS.strlen (args [1]);
			OS.PfExtentText(rect, null, args [4], args [1], length);
		}
		int inset = 4;
		width = inset + rect.lr_x - rect.ul_x + 1;
		height = inset + rect.lr_y - rect.ul_y + 1;
	}
	return new Point(width, height);
}

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtPane (), handle, damage);
	sendPaintEvent (damage);
	return OS.Pt_CONTINUE;
}

public void setText (String string) {	
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int flags = OS.Pt_SHOW_TITLE | OS.Pt_ETCH_TITLE_AREA | OS.Pt_GRADIENT_TITLE_AREA;
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int [] args = {
		OS.Pt_ARG_TITLE, ptr, 0,
		OS.Pt_ARG_CONTAINER_FLAGS, string.length () == 0 ? 0 : flags, flags,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
}

}
