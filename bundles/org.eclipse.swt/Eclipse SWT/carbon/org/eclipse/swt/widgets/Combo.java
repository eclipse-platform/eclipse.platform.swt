package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.CGRect;

public class Combo extends Composite {

	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 */
	public static int LIMIT;
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
	}

public Combo (Composite parent, int style) {
	super (parent, checkStyle (style));
}

public void add (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);	
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	int result = OS.HIComboBoxAppendTextItem (handle, ptr, null);
	OS.CFRelease (ptr);
	if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_ADDED);
}

public void add (String string, int index) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int count = OS.HIComboBoxGetItemCount (handle);
	if (0 > index || index > count) error (SWT.ERROR_INVALID_RANGE);
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	int result = OS.HIComboBoxInsertTextItemAtIndex (handle, index, ptr);
	OS.CFRelease (ptr);
	if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_ADDED);
}

public void addModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget();
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

public void clearSelection () {
	checkWidget();
	// NEEDS WORK
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	// NEEDS WORK
	int width = 100;
	int height = 30;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

public void copy () {
	checkWidget ();
	// NEEDS WORK
}

void createHandle () {
	// NEEDS WORK
	int [] outControl = new int [1];
	//int window = OS.GetControlOwner (parent.handle);
	CGRect rect = new CGRect ();
	int kHIComboBoxAutoCompletionAttribute = (1 << 0);
	int inAttributes = kHIComboBoxAutoCompletionAttribute | OS.kHIComboBoxAutoSizeListAttribute;
	OS.HIComboBoxCreate(rect, 0, null, 0, inAttributes, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	OS.HIViewSetVisible (handle, true);
	OS.HIViewAddSubview (parent.handle, handle);
	OS.HIViewSetZOrder (handle, OS.kHIViewZOrderBelow, 0);
}

public void cut () {
	checkWidget ();
	// NEEDS WORK
}

public void deselect (int index) {
	checkWidget ();
	if (index == -1) return;
	// NEEDS WORK
}

public void deselectAll () {
	checkWidget ();
	// NEEDS WORK
}

public String getItem (int index) {
	checkWidget ();
	int count = OS.HIComboBoxGetItemCount (handle);
	if (0 > index || index >= count) error (SWT.ERROR_INVALID_RANGE);
	int[] ptr = new int[1];
	if (OS.HIComboBoxCopyTextItemAtIndex (handle, index, ptr) != OS.noErr) error(SWT.ERROR_CANNOT_GET_ITEM);
	int length = OS.CFStringGetLength (ptr [0]);
	char [] buffer= new char [length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (ptr [0], range, buffer);
	OS.CFRelease (ptr [0]);
	return new String (buffer);
}

public int getItemCount () {
	checkWidget ();
	return OS.HIComboBoxGetItemCount (handle);
}

public int getItemHeight () {
	checkWidget ();
	return 26; // NEEDS WORK
}

public String [] getItems () {
	checkWidget ();
	int count = OS.HIComboBoxGetItemCount (handle);
	String[] result = new String [count];
	for (int i=0; i<count; i++) result [i] = getItem (i);
	return result;
}

public Point getSelection () {
	checkWidget ();
	// NEEDS WORK
	return new Point(0, 0);
}

public int getSelectionIndex () {
	checkWidget ();
    return indexOf(getText());
}

public String getText () {
	checkWidget ();
	int [] ptr = new int [1];
	int [] actualSize = new int [1];
	if (OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, ptr, actualSize) != OS.noErr) {
		return "";
	}
	int length = OS.CFStringGetLength (ptr [0]);
	char [] buffer= new char [length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (ptr [0], range, buffer);
	OS.CFRelease (ptr [0]);
	return new String (buffer);
}

public int getTextHeight () {
	checkWidget();
	return 26; // NEEDS WORK
}

public int getTextLimit () {
	checkWidget();
    return LIMIT; // NEEDS WORK
}

public int indexOf (String string) {
	return indexOf (string, 0);
}

public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = OS.HIComboBoxGetItemCount (handle);
	for (int i=start; i<count; i++) {
		if (string.equals (getItem (i))) {
			return i;
		}
	}
	return -1;
}

public void paste () {
	checkWidget ();
	// NEEDS WORK
}

public void remove (int index) {
	checkWidget ();
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int count = OS.HIComboBoxGetItemCount (handle);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	OS.HIComboBoxRemoveItemAtIndex(handle, index);
}

public void remove (int start, int end) {
	checkWidget();
	if (start > end) return;
	int count = OS.HIComboBoxGetItemCount (handle);
	if (0 > start || start >= count) error (SWT.ERROR_INVALID_RANGE);
	int newEnd = Math.min (end, count - 1);
	for (int i=newEnd; i>=start; i--) {
		OS.HIComboBoxRemoveItemAtIndex(handle, i);
	}
}

public void remove (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = OS.HIComboBoxGetItemCount (handle);
	for (int i=0; i<count; i++) {
		String s = getItem (i);
		if (string.equals (s)) {
			remove (i);
			return;
		}
	}
	error (SWT.ERROR_INVALID_ARGUMENT);
}

public void removeAll () {
	checkWidget ();
	int count = OS.HIComboBoxGetItemCount (handle);
	if (count > 0) {
		for (int i=count-1; i>=0; i--) {
  			OS.HIComboBoxRemoveItemAtIndex (handle, i);
		}
	}
}

public void removeModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);
}

public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

public void select (int index) {
	checkWidget();
	int count = OS.HIComboBoxGetItemCount (handle);
	if (0 > index || index >= count) error (SWT.ERROR_INVALID_RANGE);
	// NEEDS WORK
}

public void setItem (int index, String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = OS.HIComboBoxGetItemCount (handle);
	if (0 > index || index >= count) error (SWT.ERROR_INVALID_RANGE);
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.HIComboBoxInsertTextItemAtIndex(handle, index, ptr);
	OS.HIComboBoxRemoveItemAtIndex(handle, index+1);
	OS.CFRelease(ptr);
}

public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	removeAll();
	if (items.length == 0) return;
	for (int i= 0; i < items.length; i++) {
		String string = items[i];
		if (string == null) continue;
		char [] buffer = new char [string.length ()];
		string.getChars (0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
		int [] outIndex = new int[1];
		int result = OS.HIComboBoxAppendTextItem (handle, ptr, outIndex);
		OS.CFRelease(ptr);
		if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_ADDED);
	}
}

public void setSelection (Point selection) {
	checkWidget ();
	// NEEDS WORK
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.READ_ONLY) != 0) {
		int index= indexOf (string);
		if (index != -1) select(index);
		return;
	}
	// NEEDS WORK
}

public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	// NEEDS WORK
}

}
