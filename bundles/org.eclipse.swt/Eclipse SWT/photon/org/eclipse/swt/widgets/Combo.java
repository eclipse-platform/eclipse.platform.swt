package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class Combo extends Composite {

public Combo (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  It is not possible to create
	* a combo box that has a border using Windows style
	* bits.  All combo boxes draw their own border and
	* do not use the standard Windows border styles.
	* Therefore, no matter what style bits are specified,
	* clear the BORDER bits so that the SWT style will
	* match the Windows widget.
	*
	* The Windows behavior is currently implemented on
	* all platforms.
	*/
	style &= ~SWT.BORDER;
	
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	style = checkBits (style, SWT.DROP_DOWN, SWT.SIMPLE, 0, 0, 0, 0);
	if ((style & SWT.SIMPLE) != 0) return style & ~SWT.READ_ONLY;
	return style;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//NOT DONE: this only works with a DROP_DOWN combo 
	if ((style & SWT.SIMPLE) != 0) return new Point(100, 100);
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int width = dim.w;
	int height = dim.h;
	int text = OS.PtWidgetChildBack(handle);
	OS.PtWidgetPreferredSize(text, dim);
	height += dim.h;
	PhRect_t rect = new PhRect_t ();
	PhArea_t area = new PhArea_t ();
	OS.PtSetAreaFromWidgetCanvas (text, rect, area);
	width += area.size_w;
	int [] args = new int [] {
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_ITEMS, 0, 0,
		OS.Pt_ARG_TEXT_FONT, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	int maxWidth = 0;
	rect = new PhRect_t();
	int [] items = new int [1];
	for (int i = 0; i < args [1]; i++) {
		OS.memmove (items, args [4] + (i * 4), 4);
		int length = OS.strlen (items [0]);
		OS.PfExtentText(rect, null, args [7], items [0], length);
		maxWidth = Math.max(maxWidth, rect.lr_x - rect.ul_x + 1);
	}	
	width += maxWidth;
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		rect = new PhRect_t ();
		area = new PhArea_t ();
		rect.lr_x = (short) (wHint - 1);
		rect.lr_y = (short) (hHint - 1);
		OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
		if (wHint != SWT.DEFAULT) width = area.size_w;
		if (hHint != SWT.DEFAULT) height = area.size_h;
	}
	return new Point(width, height);
}

void createHandle (int index) {
	Display display = getDisplay ();
	int clazz = display.PtComboBox;
	int parentHandle = parent.handle;
	int textFlags = (style & SWT.READ_ONLY) != 0 ? 0 : OS.Pt_EDITABLE;
	int [] args = {
		OS.Pt_ARG_TEXT_FLAGS, textFlags, OS.Pt_EDITABLE,
		OS.Pt_ARG_CBOX_MAX_VISIBLE_COUNT, 5, 0,
		OS.Pt_ARG_CBOX_FLAGS, (style & SWT.SIMPLE) != 0 ? OS.Pt_COMBOBOX_STATIC: 0, OS.Pt_COMBOBOX_STATIC,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

public void deselect (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_CBOX_SELECTION_ITEM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == index) {
		args = new int [] {
			OS.Pt_ARG_TEXT_STRING, 0, 0,
			OS.Pt_ARG_CBOX_SELECTION_ITEM, 0, 0
		};
		OS.PtSetResources (handle, args.length / 3, args);
	}
}

public void deselectAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {
		OS.Pt_ARG_TEXT_STRING, 0, 0,
		OS.Pt_ARG_CBOX_SELECTION_ITEM, 0, 0
	};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void add (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	OS.PtListAddItems (handle, new int [] {ptr}, 1, 0);
	OS.free (ptr);
}

public void add (String string, int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int result = OS.PtListAddItems (handle, new int [] {ptr}, 1, index + 1);
	OS.free (ptr);
	if (result != 0) {
		int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		if (0 <= index && index <= args [1]) error (SWT.ERROR_ITEM_NOT_ADDED);
		error (SWT.ERROR_INVALID_RANGE);
	}
}

public void addModifyListener (ModifyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public void clearSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.PtTextSetSelection (handle, new int [] {0}, new int [] {0});
}

public String getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_ITEMS, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if (!(0 <= index && index < args [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int [] items = new int [1];
	OS.memmove (items, args [4] + (index * 4), 4);
	int length = OS.strlen (items [0]);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, items [0], length);
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	return new String (unicode);
}

public int getItemCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public int getItemHeight () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//NOT DONE - NOT NEEDED
	return 0;
}

public String [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_ITEMS, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	int [] items = new int [args [1]];
	OS.memmove (items, args [4], args [1] * 4);
	String [] result = new String [args [1]];
	for (int i=0; i<args [1]; i++) {
		int length = OS.strlen (items [i]);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, items [i], length);
		char [] unicode = Converter.mbcsToWcs (null, buffer);
		result [i] = new String (unicode);
	}
	return result;

}

public Point getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (((style & SWT.DROP_DOWN) != 0) && ((style & SWT.READ_ONLY) != 0)) {
		int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		int length = 0;
		if (args [1] != 0) length = OS.strlen (args [1]);
		return new Point (0, length);
	}
//	if (textVerify != null) {
//		return new Point (textVerify.start_pos, textVerify.end_pos);
//	}
	int [] start = new int [1], end = new int [1];
	OS.PtTextGetSelection (handle, start, end);
	if (start [0] == -1) {
		int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		start [0] = end [0] = args [1];	
	}
	return new Point (start [0], end [0]);
}

public int getSelectionIndex () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_CBOX_SELECTION_ITEM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return -1;
	return args [1] - 1;
}

public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return "";
	int length = OS.strlen (args [1]);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, args [1], length);
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	return new String (unicode);
}

public int getTextHeight () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//NOT DONE - Only works for DROP_DOWN
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int height = dim.h;
	int text = OS.PtWidgetChildBack(handle);
	OS.PtWidgetPreferredSize(text, dim);
	height += dim.h;
	return height;
}

public int getTextLimit () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_MAX_LENGTH, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_SELECTION, windowProc, SWT.Selection);
	OS.PtAddCallback (handle, OS.Pt_CB_TEXT_CHANGED, windowProc, SWT.Modify);
}

public int indexOf (String string) {
	if (!isValidThread ()) error(SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error(SWT.ERROR_WIDGET_DISPOSED);
	return indexOf (string, 0);
}

public int indexOf (String string, int start) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	// NOT DONE - start is ignored
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	return OS.PtListItemPos(handle, buffer) - 1;
}

int processModify (int info) {
	sendEvent (SWT.Modify);
	return OS.Pt_CONTINUE;
}

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtComboBox (), handle, damage);
	sendPaintEvent (damage);
	return OS.Pt_CONTINUE;
}

int processSelection (int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.reason_subtype == OS.Pt_LIST_SELECTION_FINAL) {
		postEvent(SWT.Selection);
	}
	return OS.Pt_CONTINUE;
}

public void remove (int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (!(0 < start && start <= end && end < args [1])) {
		 error (SWT.ERROR_INVALID_RANGE);
	}
	int count = end - start + 1;
	int result = OS.PtListDeleteItemPos (handle, count, start + 1);
	if (result != 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
}

public void remove (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (!(0 <= index && index < args [1])) error (SWT.ERROR_INVALID_RANGE);
	int result = OS.PtListDeleteItemPos (handle, 1, index + 1);
	if (result != 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
}

public void remove (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_ITEM_NOT_REMOVED);
	remove (index);
}

public void removeAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.PtListDeleteAllItems (handle);
}

public void removeModifyListener (ModifyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
}

public void removeSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void select (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index < 0) return;
	int [] args = new int [] {OS.Pt_ARG_CBOX_SELECTION_ITEM, index + 1, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int newHeight = ((style & SWT.DROP_DOWN) != 0) ? getTextHeight() : height;
	super.setBounds (x, y, width, newHeight, move, resize);
}

public void setItem (int index, String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (!(0 <= index && index < args [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	OS.PtListReplaceItemPos (handle, new int [] {ptr}, 1, index + 1);
	OS.free (ptr);
}

public void setItems (String [] items) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	OS.PtListDeleteAllItems (handle);
	int[] itemsPtr = new int [items.length];
	for (int i=0; i<itemsPtr.length; i++) {
		byte [] buffer = Converter.wcsToMbcs (null, items [i], true);
		int ptr = OS.malloc (buffer.length);
		OS.memmove (ptr, buffer, buffer.length);
		itemsPtr [i] = ptr;
	}
	OS.PtListAddItems (handle, itemsPtr, itemsPtr.length, 0);
	for (int i=0; i<itemsPtr.length; i++) {
		OS.free (itemsPtr [i]);
	}
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setSelection (Point selection) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	OS.PtTextSetSelection (handle, new int [] {selection.x}, new int [] {selection.y});
}

public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	if ((style & SWT.READ_ONLY) != 0) {
		int index = OS.PtListItemPos(handle, buffer);
		if (index > 0) {
			int [] args = new int [] {OS.Pt_ARG_CBOX_SELECTION_ITEM, index, 0};
			OS.PtSetResources (handle, args.length / 3, args);
//			sendEvent (SWT.Modify);
		}
		return;
	}
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int [] args = {OS.Pt_ARG_TEXT_STRING, ptr, 0};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
}

public void setTextLimit (int limit) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	int [] args = new int [] {OS.Pt_ARG_MAX_LENGTH, limit, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

}