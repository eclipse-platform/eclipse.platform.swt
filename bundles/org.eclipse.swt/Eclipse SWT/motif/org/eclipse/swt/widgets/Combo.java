package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/**
*	The combo class represents a selectable user interface object
* that combines a text field and a list and issues notificiation
* when an item is selected from the list.
*
* Styles
*
*	SIMPLE,
*	DROP_DOWN,
*	READ_ONLY,
*
* Events
*
*	Selection
*
**/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/* Class Definition */
public /*final*/ class Combo extends Composite {
	public static int LIMIT;
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
	}

/**
* Creates a new instance of the widget.
*/
public Combo (Composite parent, int style) {
	super (parent, checkStyle (style));
}
/**
* Adds an item.
* <p>
* The item is placed at the end of the list.
* Indexing is zero based.
* 
* This operation can fail when the item cannot
* be added in the OS.
*
* @param string the new item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
* @exception SWTError(ERROR_ITEM_NOT_ADDED)
*	when the operation fails in the OS
*/
public void add (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);

	byte [] buffer = Converter.wcsToMbcs (null, encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.XmComboBoxAddItem(handle, xmString, -1, false);
	OS.XmStringFree (xmString);
}
/**
* Adds an item at an index.
* <p>
* The item is placed at an index in the list.
* Indexing is zero based.
*
* This operation will fail when the index is
* out of range or the item cannot be added in
* the OS.
*
* @param string the new item
* @param index the index for the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when the string is null
* @exception SWTError(ERROR_ITEM_NOT_ADDED)
*	when the item cannot be added
*/
public void add (String string, int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	
	/*
	* Feature in Motif.  When an index is out of range,
	* the list widget adds the item at the end.  This
	* behavior is not wrong but it is unwanted.  The
	* fix is to check the range before adding the item.
	*/
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index <= argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (null, encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.XmComboBoxAddItem(handle, xmString, index + 1, false);
	OS.XmStringFree (xmString);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addModifyListener (ModifyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
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
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
/**
* Clears the current selection.
* <p>
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void clearSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextClearSelection (argList[1], OS.XtLastTimestampProcessed (xDisplay));
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {
		OS.XmNlist, 0, 
		OS.XmNtextField, 0,
		OS.XmNitemCount, 0,
		OS.XmNmarginWidth, 0,
		OS.XmNshadowThickness, 0, 
		OS.XmNhighlightThickness, 0,
		OS.XmNarrowSize, 0, 
		OS.XmNarrowSpacing, 0,
	};
	OS.XtGetValues(handle, argList, argList.length / 2);	
	XtWidgetGeometry result = new XtWidgetGeometry ();
	result.request_mode = OS.CWWidth;	
	OS.XtQueryGeometry (argList[1], null, result);
	int width = result.width, height = getTextHeight();
	int[] argList2 = {OS.XmNmarginWidth, 0, OS.XmNshadowThickness, 0};
	OS.XtGetValues(argList[3], argList2, argList2.length / 2);
	if ((style & SWT.READ_ONLY) == 0) width += (2 * argList[7]);
	if ((style & SWT.DROP_DOWN) != 0) {
		width += argList[13] + argList[15];
	} else {
		int itemCount = (argList[5] == 0) ? 5 : argList[5];
		height += (getItemHeight () * itemCount);
	}
	width += (2 * argList[9]) 
		+ (2 * argList[11])
		+ (2 * argList2[1])
		+ (2 * argList2[3]);
	if (argList[5] == 0) width = DEFAULT_WIDTH;
	if (hHint != SWT.DEFAULT) height = hHint;
	if (wHint != SWT.DEFAULT) width = wHint; 
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}
void createHandle (int index) {
	state |= HANDLE;
	
	/*
	* Feature in Motif.  When items are added or removed
	* from a combo, it may request and be granted, a new
	* preferred size.  This behavior is unwanted.  The fix
	* is to create a parent for the list that will disallow
	* geometry requests.
	*/
	formHandle = OS.XmCreateForm (parent.handle, null, null, 0);
	if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int comboBoxType = OS.XmDROP_DOWN_COMBO_BOX;
	if ((style & SWT.SIMPLE) != 0) {
		comboBoxType = OS.XmCOMBO_BOX;
	} else if ((style & SWT.READ_ONLY) != 0) {
		comboBoxType = OS.XmDROP_DOWN_LIST;
	}
	int [] argList = {
		OS.XmNcomboBoxType, comboBoxType,
		OS.XmNtopAttachment, OS.XmATTACH_FORM,
		OS.XmNbottomAttachment, OS.XmATTACH_FORM,
		OS.XmNleftAttachment, OS.XmATTACH_FORM,
		OS.XmNrightAttachment, OS.XmATTACH_FORM,
		OS.XmNresizable, 0,
	};
	handle = OS.XmCreateComboBox (formHandle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}
/**
* Deselects an item.
* <p>
* If the item at an index is selected, it is
* deselected.  If the item at an index is not
* selected, it remains deselected.  Indices
* that are out of range are ignored.  Indexing
* is zero based.
*
* @param index the index of the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void deselect (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == -1) return;
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	if (OS.XmListPosSelected (argList[3], index + 1)) {
		Display display = getDisplay ();
		boolean warnings = display.getWarnings ();
		display.setWarnings (false);
		OS.XmTextSetString (argList[1], new byte[1]);
		OS.XmTextSetInsertionPosition (argList[1], 0);
		display.setWarnings (warnings);	
		OS.XmListDeselectAllItems (argList[3]);
	}
}
/**
* Deselects all items.
* <p>
*
* If an item is selected, it is deselected.
* If an item is not selected, it remains unselected.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void deselectAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (argList[1], new byte[1]);
	OS.XmTextSetInsertionPosition (argList[1], 0);
	display.setWarnings(warnings);	
	OS.XmListDeselectAllItems (argList[3]);
}


/**
* Gets an item at an index.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be queried from
* the OS.
*
* @param index the index of the item
* @return the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_ITEM)
*	when the operation fails
*/
public String getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNitemCount, 0, OS.XmNitems, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (argList [3] == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	int ptr = argList [3] + (index * 4);
	int [] buffer1 = new int [1]; 
	OS.memmove (buffer1, ptr, 4);
	ptr = buffer1 [0];
	int address = OS.XmStringUnparse (
		ptr,
		null,
		OS.XmCHARSET_TEXT,
		OS.XmCHARSET_TEXT,
		null,
		0,
		OS.XmOUTPUT_ALL);
	if (address == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	int length = OS.strlen (address);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, address, length);
	OS.XtFree (address);
	return decodeString(new String (Converter.mbcsToWcs (null, buffer)));
}
/**
* Gets the number of items.
* <p>
* This operation will fail if the number of
* items could not be queried from the OS.
*
* @return the number of items in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_COUNT)
*	when the operation fails
*/
public int getItemCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
* Gets the height of one item.
* <p>
* This operation will fail if the height of
* one item could not be queried from the OS.
*
* @return the height of one item in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_ITEM_HEIGHT)
*	when the operation fails
*/
public int getItemHeight () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] listHandleArgs = {OS.XmNlist, 0};
	OS.XtGetValues (handle, listHandleArgs, listHandleArgs.length / 2);
	int [] argList = {OS.XmNlistSpacing, 0, OS.XmNhighlightThickness, 0};
	OS.XtGetValues (listHandleArgs[1], argList, argList.length / 2);
	int spacing = argList [1], highlight = argList [3];
	/* Result is from empirical analysis on Linux and AIX */
	return getFontHeight () + spacing + (2 * highlight);
}
/**
* Gets the items.
* <p>
* This operation will fail if the items cannot
* be queried from the OS.
*
* @return the items in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_ITEM)
*	when the operation fails
*/
public String [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNitems, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	int [] buffer1 = new int [1];
	String [] result = new String [itemCount];
	for (int i = 0; i < itemCount; i++) {
		OS.memmove (buffer1, items, 4);
		int ptr = buffer1 [0];
		int address = OS.XmStringUnparse (
			ptr,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			null,
			0,
			OS.XmOUTPUT_ALL);
		if (address == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
		int length = OS.strlen (address);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, address, length);
		OS.XtFree (address);
		result[i] = decodeString(new String (Converter.mbcsToWcs (null, buffer)));
		items += 4;
	}
	return result;
}
/**
* Gets the selection.
* <p>
* @return a point representing the selection start and end
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Point getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] start = new int [1], end = new int [1];
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextGetSelectionPosition (argList[1], start, end);
	if (start [0] == end [0]) {
		start [0] = end [0] = OS.XmTextGetInsertionPosition (argList[1]);
	}
	return new Point (start [0], end [0]);
}
/**
* Gets the index of the selected item.
* <p>
* Indexing is zero based.
* If no item is selected -1 is returned.
*
* @return the index of the selected item.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getSelectionIndex () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	int [] argList = {OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	int index = OS.XmListGetKbdItemPos (argList[1]);
	if (OS.XmListPosSelected (argList[1], index)) return index - 1;
	int [] count = new int [1], positions = new int [1];
	if (!OS.XmListGetSelectedPos (argList[1], positions, count)) return -1;
	if (count [0] == 0) return -1;
	int address = positions [0];
	int [] indices = new int [1];
	OS.memmove (indices, address, 4);
	OS.XtFree (address);
	return indices [0] - 1;
}
/**
* Gets the widget text.
* <p>
* If the widget has no text, an empty string is returned.
*
* @return the widget text
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	int ptr = OS.XmTextGetString (argList[1]);
	if (ptr == 0) return "";
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);
	OS.XtFree (ptr);
	return decodeString(new String (Converter.mbcsToWcs (null, buffer)));
}
/**
* Gets the height of the combo's text field.
* <p>
* The operation will fail if the height cannot 
* be queried from the OS.

* @return the height of the combo's text field.
* 
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_ERROR_CANNOT_GET_ITEM_HEIGHT)
*	when the operation fails
*/
public int getTextHeight () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	if ((style & SWT.DROP_DOWN) != 0) {
		/*
		* Bug in MOTIF.  For some reason, XtQueryGeometry ()
		* returns the wrong height when the combo is not realized.
		* The fix is to force the combo to be realized by forcing
		* the shell to be realized. 
		*/
		if (!OS.XtIsRealized (handle)) getShell ().realizeWidget ();
		XtWidgetGeometry result = new XtWidgetGeometry ();
		result.request_mode = OS.CWHeight;	
		OS.XtQueryGeometry (handle, null, result);
		return result.height;
	} else {
		/* Calculate text field height. */
		int [] argList = {OS.XmNtextField, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);	
		int [] argList2 = {OS.XmNmarginHeight, 0};
		OS.XtGetValues (argList[1], argList2, argList2.length / 2);	
		int height = getFontHeight ();
		XRectangle rect = new XRectangle ();
		OS.XmWidgetGetDisplayRect (argList[1], rect);
		height += (rect.y * 2) + (2 * argList2[1]);
		
		/* Add in combo box margins. */
		int [] argList3 = {OS.XmNmarginHeight, 0, OS.XmNshadowThickness, 0, OS.XmNhighlightThickness, 0};
		OS.XtGetValues(handle, argList3, argList3.length / 2);	
		height += (2 * argList3[1]) + (2 * argList3[3]) + (2 * argList3[5]);
		
		return height;
	}
}
/**
* Gets the text limit.
* <p>
* @return the text limit
* 
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getTextLimit () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return OS.XmTextGetMaxLength (argList[1]);
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNselectionCallback, windowProc, SWT.Selection);
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XtAddCallback (argList[1], OS.XmNactivateCallback, windowProc, SWT.DefaultSelection);
	OS.XtAddCallback (argList[1], OS.XmNvalueChangedCallback, windowProc, SWT.Modify);
}
/**
* Gets the index of an item.
* <p>
* The list is searched starting at 0 until an
* item is found that is equal to the search item.
* If no item is found, -1 is returned.  Indexing
* is zero based.
*
* @param string the search item
* @return the index of the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public int indexOf (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);

	byte [] buffer = Converter.wcsToMbcs (null, encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) return -1;
	
	int [] argList = {OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	int index = OS.XmListItemPos (argList[1], xmString);
	OS.XmStringFree (xmString);
	return index - 1;
}
/**
* Gets the index of an item.
* <p>
* The widget is searched starting at start including
* the end position until an item is found that
* is equal to the search itenm.  If no item is
* found, -1 is returned.  Indexing is zero based.
*
* @param string the search item
* @param index the starting position
* @return the index of the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public int indexOf (String string, int start) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] argList = {OS.XmNitems, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	if (!((0 <= start) && (start < itemCount))) return -1;
	byte [] buffer1 = Converter.wcsToMbcs (null, encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer1);
	if (xmString == 0) return -1;
	int index = start;
	items += start * 4;
	int [] buffer2 = new int [1];
	while (index < itemCount) {
		OS.memmove (buffer2, items, 4);
		if (OS.XmStringCompare (buffer2 [0], xmString)) break;
		items += 4;  index++;
	}
	OS.XmStringFree (xmString);
	if (index == itemCount) return -1;
	return index;
}
/**
* Removes an item at an index.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be removed from
* the OS.
*
* @param index the index of the item
* @return the selection state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_ITEM_NOT_REMOVED)
*	when the operation fails
*/
public void remove (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	OS.XmComboBoxDeletePos (handle, index + 1);
}
/**
* Removes a range of items.
* <p>
* Indexing is zero based.  The range of items
* is from the start index up to and including
* the end index.
*
* This operation will fail when the index is out
* of range or an item could not be removed from
* the OS.
*
* @param start the start of the range
* @param end the end of the range
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_ITEM_NOT_REMOVED)
*	when the operation fails
*/
public void remove (int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (start > end) return;
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= start && start < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int newEnd = Math.min (end, argList [1] - 1);
	for (int i = start; i <= newEnd; i++) {
		OS.XmComboBoxDeletePos (handle, start + 1);	
	}
	if (end >= argList [1]) error (SWT.ERROR_INVALID_RANGE);
}
void register () {
	super.register ();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	WidgetTable.put(argList[1], this);
}
/**
* Removes an item.
* <p>
* This operation will fail when the item
* could not be removed from the OS.
*
* @param string the search item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
* @exception SWTError(ERROR_ITEM_NOT_REMOVED)
*	when the operation fails
*/
public void remove (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);

	byte [] buffer = Converter.wcsToMbcs (null, encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	
	int [] argList = {OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int index = OS.XmListItemPos (argList[1], xmString);
	
	OS.XmStringFree (xmString);	
	if (index == 0) error (SWT.ERROR_INVALID_ARGUMENT);
	OS.XmComboBoxDeletePos (handle, index);
}
/**
* Removes all items.
* <p>
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void removeAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (argList[1], new byte[1]);
	OS.XmTextSetInsertionPosition (argList[1], 0);
	display.setWarnings(warnings);	
	OS.XmListDeselectAllItems (argList[3]);
	
	for (int i = 0; i < argList[5]; i++) {
		OS.XmComboBoxDeletePos(handle, 1);
	}
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeModifyListener (ModifyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}
/**
* Selects an item.
* <p>
* If the item at an index is not selected, it is
* selected. Indices that are out of
* range are ignored.  Indexing is zero based.
*
* @param index the index of the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void select (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == -1) {
		int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		Display display = getDisplay ();
		boolean warnings = display.getWarnings ();
		display.setWarnings (false);
		OS.XmTextSetString (argList[1], new byte[1]);
		OS.XmTextSetInsertionPosition (argList[1], 0);
		display.setWarnings (warnings);	
		OS.XmListDeselectAllItems (argList[3]);
	} else {
		int [] argList = {OS.XmNitemCount, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		if (!(0 <= index && index < argList [1])) {
			error (SWT.ERROR_INVALID_RANGE);
		}
		int [] argList2 = {OS.XmNselectedPosition, index};
		OS.XtSetValues(handle, argList2, argList2.length / 2);
	}
}
/**
* Sets the widget bounds.
*/
public void setBounds (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int newHeight = ((style & SWT.DROP_DOWN) != 0) ? getTextHeight() : height;
	super.setBounds (x, y, width, newHeight);
}
/**
* Sets the text of an item.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be changed in
* the OS.
*
* @param index the index for the item
* @param string the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when items is null
* @exception SWTError(ERROR_ITEM_NOT_MODIFIED)
*	when the operation fails
*/
public void setItem (int index, String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int [] argList = {OS.XmNlist, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [3])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (null, encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	boolean isSelected = OS.XmListPosSelected (argList[1], index + 1);
	OS.XmListReplaceItemsPosUnselected (argList[1], new int [] {xmString}, 1, index + 1);
	if (isSelected) OS.XmListSelectPos (argList[1], index + 1, false);
	OS.XmStringFree (xmString);
}
/**
* Sets all items.
* <p>
* @param items the array of items
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when items is null
* @exception SWTError(ERROR_ITEM_NOT_ADDED)
*	when the operation fails
*/
public void setItems (String [] items) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);

	if (items.length == 0) {
		removeAll();
		return;
	}
	
	int index = 0;
	int [] table = new int [items.length];
	while (index < items.length) {
		String string = items [index];
		if (string == null) break; 
		byte [] buffer = Converter.wcsToMbcs (null, encodeString(string), true);
		int xmString = OS.XmStringCreateLocalized (buffer);
		if (xmString == 0) break;
		table [index++] = xmString;
	}
	int ptr = OS.XtMalloc (index * 4);
	OS.memmove (ptr, table, index * 4);
	int [] argList = {OS.XmNitems, ptr, OS.XmNitemCount, index};
	OS.XtSetValues (handle, argList, argList.length / 2);
	for (int i=0; i<index; i++) OS.XmStringFree (table [i]);
	OS.XtFree (ptr);
	if (index < items.length) error (SWT.ERROR_ITEM_NOT_ADDED);
}
/**
* Sets the new selection.
* <p>
* @param selection point representing the start and the end of the new selection
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when selection is null
*/
public void setSelection (Point selection) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	/* Clear the highlight before setting the selection. */
	int position = OS.XmTextGetLastPosition (argList[1]);

	/*
	* Bug in MOTIF.  XmTextSetSelection () fails to set the
	* selection when the receiver is not realized.  The fix
	* is to force the receiver to be realized by forcing the
	* shell to be realized.  If the receiver is realized before
	* the shell, MOTIF fails to draw the text widget and issues
	* lots of X BadDrawable errors.
	*/
	if (!OS.XtIsRealized (handle)) getShell ().realizeWidget ();

	/* Set the selection. */
	int xDisplay = OS.XtDisplay (argList[1]);
	if (xDisplay == 0) return;
	int nStart = Math.min (Math.max (Math.min (selection.x, selection.y), 0), position);
	int nEnd = Math.min (Math.max (Math.max (selection.x, selection.y), 0), position);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetSelection (argList[1], nStart, nEnd, OS.XtLastTimestampProcessed (xDisplay));

	/* Force the i-beam to follow the highlight/selection. */
	OS.XmTextSetInsertionPosition (argList[1], nEnd);
	display.setWarnings(warnings);
}
/**
* Sets the widget size.
*/
public void setSize (int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int newHeight = ((style & SWT.DROP_DOWN) != 0) ? getTextHeight () : height;
	super.setSize (width, newHeight);
}
/**
* Sets the widget text
* <p>
* @param string the widget text
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);

	if ((style & SWT.READ_ONLY) == 0) {
		byte [] buffer = Converter.wcsToMbcs (null, string, true);
		int xmString = OS.XmStringCreateLocalized (buffer);
		if (xmString == 0) return;
		int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int index = OS.XmListItemPos (argList[3], xmString);
		if (index > 0) {
			/* The list contains the item. */
			OS.XmComboBoxSelectItem(handle, xmString);
		} else {
			/* The list does not contain the item. */
			Display display = getDisplay ();
			boolean warnings = display.getWarnings ();
			display.setWarnings (false);
			OS.XmTextSetString (argList[1], buffer);
			OS.XmTextSetInsertionPosition (argList[1], 0);
			display.setWarnings(warnings);
		}
		OS.XmStringFree (xmString);
		/*
		* Bug in Linux.  When the widget is multi-line
		* it does not send a Modify to notify the application
		* that the text has changed.  The fix is to send the event.
		*/
		if (IsLinux && (style & SWT.MULTI) != 0) sendEvent (SWT.Modify);
	}
}
/**
* Sets the text limit
* <p>
* @param limit new text limit
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_BE_ZERO)
*	when limit is 0
*/
public void setTextLimit (int limit) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextSetMaxLength (argList[1], limit);
}

void deregister () {
	super.deregister ();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	WidgetTable.remove (argList[1]);
}

/**
 * Bug in Motif.
 * Empty strings in the combo will cause GPFs if a) they
 * are the only items in the combo or b) if they are
 * included in an array which is set as the value of the
 * combo's items resource. To protect against these GPFs,
 * make sure that no empty strings are added to the combo.
 * The solution is to add a space to empty strings or 
 * strings which are all spaces. This space is removed
 * when answering the text of items which are all spaces.
 */
String encodeString(String string) {
	for (int i = 0; i < string.length(); i++) {
		if (string.charAt(i) != ' ') {
			return string;
		}
	}
	return string + ' ';
}
/**
 * Bug in Motif.
 * Empty strings in the combo will cause GPFs if a) they
 * are the only items in the combo or b) if they are
 * included in an array which is set as the value of the
 * combo's items resource. To protect against these GPFs,
 * make sure that no empty strings are added to the combo.
 * The solution is to add a space to empty strings or 
 * strings which include only spaces. A space is removed
 * when answering the text of items which are all spaces.
 */
String decodeString(String string) {
	if (string.length() == 0) return string;
	
	for (int i = 0; i < string.length(); i++) {
		if (string.charAt(i) != ' ') {
			return string;
		}
	}
	return string.substring(0, string.length() - 1);
}
}
