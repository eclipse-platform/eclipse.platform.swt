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

public /*final*/ class List extends Scrollable {

public List (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
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

public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
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

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	/**
	 * Feature in Photon - The preferred width calculated by
	 * PtWidgetPreferredSize is the current width.
	 */
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int width = 0, height = dim.h;
	int [] args = new int [] {
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_ITEMS, 0, 0,
		OS.Pt_ARG_LIST_FONT, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	PhRect_t rect = new PhRect_t();
	int [] items = new int [1];
	for (int i = 0; i < args [1]; i++) {
		OS.memmove (items, args [4] + (i * 4), 4);
		int length = OS.strlen (items [0]);
		OS.PfExtentText(rect, null, args [7], items [0], length);
		width = Math.max(width, rect.lr_x - rect.ul_x + 1);
	}
	rect = new PhRect_t ();
	PhArea_t area = new PhArea_t ();
	rect.lr_x = (short) (width + 1);
	OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
	width = area.size_w;
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		rect = new PhRect_t ();
		area = new PhArea_t ();
		rect.lr_x = (short) (wHint - 1);
		rect.lr_y = (short) (hHint - 1);
		OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
		ScrollBar scroll;
		if (wHint != SWT.DEFAULT) {
			width = area.size_w;
			if ((scroll = getVerticalBar()) != null)
				width += scroll.getSize().x;
		}
		if (hHint != SWT.DEFAULT) {
			height = area.size_h;
			if ((scroll = getHorizontalBar()) != null)
				height += scroll.getSize().y;
		}
	}
	return new Point(width, height);
}

void createHandle (int index) {
	Display display = getDisplay ();
	int clazz = display.PtList;
	int parentHandle = parent.handle;
	int mode = OS.Pt_SELECTION_MODE_SINGLE | OS.Pt_SELECTION_MODE_AUTO;
	if ((style & SWT.MULTI) != 0) {
		if ((style & SWT.SIMPLE) != 0) {
			mode = OS.Pt_SELECTION_MODE_MULTIPLE | OS.Pt_SELECTION_MODE_NOCLEAR
				| OS.Pt_SELECTION_MODE_TOGGLE | OS.Pt_SELECTION_MODE_NOMOVE;
		} else {
			mode = OS.Pt_SELECTION_MODE_MULTIPLE | OS.Pt_SELECTION_MODE_AUTO;
		}
	}
	mode |= OS.Pt_SELECTION_MODE_NOFOCUS;
	int [] args = {
		OS.Pt_ARG_SELECTION_MODE, mode, 0,
		OS.Pt_ARG_FLAGS, OS.Pt_SELECTABLE | OS.Pt_SELECT_NOREDRAW, OS.Pt_SELECTABLE | OS.Pt_SELECT_NOREDRAW,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);	
	createScrollBars();
}

byte [] defaultFont () {
	Display display = getDisplay ();
	return display.LIST_FONT;
}

public void deselect (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index < 0) return;
	OS.PtListUnselectPos (handle, index + 1);
}

public void deselect (int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (start > end) return;
	if ((style & SWT.SINGLE) != 0) {
		int [] args = new int [] {OS.Pt_ARG_LIST_SEL_COUNT, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		int count = args [1];
		int index = Math.min (count - 1, end);
		if (index >= start) deselect (index);
		return;
	}
	for (int i=start; i<end; i++) {
		OS.PtListUnselectPos (handle, i + 1);
	}
}

public void deselect (int [] indices) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		if (index != -1) {
			OS.PtListUnselectPos (handle, index + 1);
		}
	}
}

public void deselectAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [1];
	for (int i=0; i<count; i++) {
		OS.PtListUnselectPos (handle, i + 1);
	}
}

public int getFocusIndex () {
	return getSelectionIndex ();
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
	int [] args = new int [] {
		OS.Pt_ARG_LIST_TOTAL_HEIGHT, 0, 0,
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_LIST_FONT, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [4] == 0) {
		int ptr = OS.malloc(1);
		PhRect_t rect = new PhRect_t ();
		OS.PfExtentText(rect, null, args [7], ptr, 1);
		OS.free(ptr);
		int inset = 4;
		return inset + (rect.lr_y - rect.ul_y + 1);
	}
	return args [1] / args [4];
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

public String [] getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] indices = getSelectionIndices ();
	String [] result = new String [indices.length];
	for (int i=0; i<indices.length; i++) {
		result [i] = getItem (indices [i]);
	}
	return result;
}

public int getSelectionCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_LIST_SEL_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public int getSelectionIndex () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {
		OS.Pt_ARG_LIST_SEL_COUNT, 0, 0,
		OS.Pt_ARG_SELECTION_INDEXES, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return -1;
	short [] buffer = new short [1];
	OS.memmove (buffer, args [4], 2);
	return buffer [0] - 1;
}

public int [] getSelectionIndices () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {
		OS.Pt_ARG_LIST_SEL_COUNT, 0, 0,
		OS.Pt_ARG_SELECTION_INDEXES, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	short [] indices = new short [args [1]];
	OS.memmove (indices, args [4], args [1] * 2);
	int [] result = new int [args [1]];
	for (int i=0; i<args [1]; i++) {
		result [i] = indices [i] - 1;
	}
	return result;
}

public int getTopIndex () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_TOP_ITEM_POS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_SELECTION, windowProc, SWT.Selection);
	OS.PtAddCallback (handle, OS.Pt_CB_ACTIVATE, windowProc, SWT.DefaultSelection);
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

public boolean isSelected (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {
		OS.Pt_ARG_LIST_SEL_COUNT, 0, 0,
		OS.Pt_ARG_SELECTION_INDEXES, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	short [] buffer = new short [1];
	for (int i=0; i<args [1]; i++) {
		OS.memmove (buffer, args [4] + (i * 2), 2);
		if (buffer [0] == index + 1) return true;
	}
	return false;
}

int processDefaultSelection (int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_END;
	int[] click_count = new int [1];
	OS.memmove(click_count, cbinfo.cbdata, 4);
	if (click_count [0] > 1) postEvent (SWT.DefaultSelection);
	return OS.Pt_CONTINUE;
}

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtList (), handle, damage);
	return super.processPaint (damage);
}

int processSelection (int info) {
	postEvent (SWT.Selection);
	return OS.Pt_CONTINUE;
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

public void remove (int [] indices) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);

	//NOT DONE
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

public void removeAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.PtListDeleteAllItems (handle);
}


public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void select (int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (start > end) return;
	if ((style & SWT.SINGLE) != 0) {
		int [] args = new int [] {OS.Pt_ARG_LIST_SEL_COUNT, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		int count = args [1];
		int index = Math.min (count - 1, end);
		if (index >= start) select (index);
		return;
	}
	for (int i=start; i<end; i++) {
		OS.PtListSelectPos (handle, start + 1);
	}
	OS.PtListGotoPos (handle, start + 1);
}

public void select (int [] indices) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	for (int i=0; i<indices.length; i++) {
		if (indices [i] >= 0) {
			OS.PtListSelectPos (handle, indices [i] + 1);
		}
	}
	OS.PtListGotoPos (handle, indices [0] + 1);
}

public void select (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index < 0) return;
	OS.PtListSelectPos (handle, index + 1);
	OS.PtListGotoPos (handle, index + 1);
}

public void selectAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SINGLE) != 0) return;
	int [] args = new int [] {OS.Pt_ARG_LIST_SEL_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [1];
	for (int i=0; i<count; i++) {
		OS.PtListSelectPos (handle, i + 1);
	}
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
}

public void setSelection (int start, int end) {
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (start, end);
}

public void setSelection (int index) {
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (index);
}

public void setSelection(int[] indices) {
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (indices);
}

public void setSelection (String [] items) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.MULTI) != 0) deselectAll ();
	for (int i=items.length-1; i>=0; --i) {
		int index = 0;
		String string = items [i];
		if (string != null) {
			while ((index = indexOf (string, index)) != -1) {
				select (index);
				if (((style & SWT.SINGLE) != 0) && isSelected (index)) return;
				index++;
			}
		}
	}
	if ((style & SWT.SINGLE) != 0) deselectAll ();
}

public void setTopIndex (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {OS.Pt_ARG_TOP_ITEM_POS, index + 1, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void showSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = new int [] {
		OS.Pt_ARG_LIST_SEL_COUNT, 0, 0,
		OS.Pt_ARG_SELECTION_INDEXES, 0, 0,
		OS.Pt_ARG_TOP_ITEM_POS, 0, 0,
		OS.Pt_ARG_VISIBLE_COUNT, 0, 0,
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return;
	short [] buffer = new short [1];
	OS.memmove (buffer, args [4], 2);
	int index = buffer [0] - 1;
	int topIndex = args [7] - 1, visibleCount = args [10], count = args [13];
	int bottomIndex = Math.min (topIndex + visibleCount - 1, count);
	if ((topIndex <= index) && (index <= bottomIndex)) return;
	int lastIndex = Math.max (1, count - visibleCount + 1);
	int newTop = Math.min (Math.max (index - (visibleCount / 2), 1), lastIndex);
	args = new int [] {OS.Pt_ARG_TOP_ITEM_POS, newTop, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

}
