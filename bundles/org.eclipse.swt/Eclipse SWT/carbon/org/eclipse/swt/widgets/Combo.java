/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.Rect;

/**
 * Instances of this class are controls that allow the user
 * to choose an item from a list of items, or optionally 
 * enter a new value by typing it into an editable text
 * field. Often, <code>Combo</code>s are used in the same place
 * where a single selection <code>List</code> widget could
 * be used but space is limited. A <code>Combo</code> takes
 * less space than a <code>List</code> widget and shows
 * similar information.
 * <p>
 * Note: Since <code>Combo</code>s can contain both a list
 * and an editable text field, it is possible to confuse methods
 * which access one versus the other (compare for example,
 * <code>clearSelection()</code> and <code>deselectAll()</code>).
 * The API documentation is careful to indicate either "the
 * receiver's list" or the "the receiver's text field" to 
 * distinguish between the two cases.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DROP_DOWN, READ_ONLY, SIMPLE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DROP_DOWN and SIMPLE 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 */
public class Combo extends Composite {

	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 */
	public static final int LIMIT;
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
	}
	
	// NEEDS WORK - is this handle being leaked?
	int menuHandle;

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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DROP_DOWN
 * @see SWT#READ_ONLY
 * @see SWT#SIMPLE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Combo (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the argument to the end of the receiver's list.
 *
 * @param string the new item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);	
	
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	int result;
	if ((style & SWT.READ_ONLY) != 0) {
		result = OS.AppendMenuItemTextWithCFString (menuHandle, ptr, 0, 0, null);
	} else {
		result = OS.HIComboBoxAppendTextItem (handle, ptr, null);
	}
	OS.CFRelease (ptr);
	if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_ADDED);
}

/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p>
 *
 * @param string the new item
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 *
 * @see #add(String)
 */
public void add (String string, int index) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = getItemCount ();
	if (0 > index || index > count) error (SWT.ERROR_INVALID_RANGE);
	
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	int result;
	if ((style & SWT.READ_ONLY) != 0) {
		result = OS.InsertMenuItemTextWithCFString (menuHandle, ptr, (short)index, 0, 0);
	} else {
		result = OS.HIComboBoxInsertTextItemAtIndex (handle, index, ptr);
	}
	OS.CFRelease (ptr);
	if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_ADDED);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is modified, by sending
 * it one of the messages defined in the <code>ModifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #removeModifyListener
 */
public void addModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the combo's list selection changes.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed the combo's text area.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
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

/**
 * Sets the selection in the receiver's text field to an empty
 * selection starting just before the first character. If the
 * text field is editable, this has the effect of placing the
 * i-beam at the start of the text.
 * <p>
 * Note: To clear the selected items in the receiver's list, 
 * use <code>deselectAll()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #deselectAll
 */
public void clearSelection () {
	checkWidget();
	if ((style & SWT.READ_ONLY) != 0) {
		OS.SetControl32BitValue (handle, 0);
	} else {
		char [] buffer = new char [0];
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) return;	
		OS.SetControlData (handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, new int[] {ptr});
		OS.CFRelease (ptr);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	// NEEDS WORK
	int width = 100, height = 21;
	int [] ptr = new int [1];
	if ((style & SWT.READ_ONLY) != 0) {
		int index = OS.GetControlValue (handle) - 1;
		OS.CopyMenuItemTextAsCFString (menuHandle, (short)(index+1), ptr);
	} else {
		OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, ptr, null);
	}
	if (ptr [0] != 0) {
		org.eclipse.swt.internal.carbon.Point bounds = new org.eclipse.swt.internal.carbon.Point ();
		// NEEDS work - only works for default font
		short [] baseLine = new short [1];
		if (OS.GetThemeTextDimensions (ptr [0], (short)OS.kThemeSystemFont, OS.kThemeStateActive, false, bounds, baseLine) == OS.noErr) {
			width = Math.max (width, bounds.h);
			height = Math.max (height, bounds.v);
		}
		OS.CFRelease (ptr [0]);
	}
	Rect inset = getInset ();
	width += inset.left + inset.right;
	height += inset.top + inset.bottom;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

/**
 * Copies the selected text.
 * <p>
 * The current selection is copied to the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void copy () {
	checkWidget ();
	int [] str = new int [1];
	short start, end;
	if ((style & SWT.READ_ONLY) != 0) {
		// NEEDS WORK - getting whole text, not just selection
		int index = OS.GetControlValue (handle);
		if (OS.CopyMenuItemTextAsCFString(menuHandle, (short)index, str) != OS.noErr) return;
		start = 0; end = (short)OS.CFStringGetLength (str [0]);
		if (start >= end) {
			OS.CFRelease (str [0]);
			return;
		}
	} else {
		short [] s = new short [2];
		OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, 4, s, null);
		if (s [0] >= s [1]) return;
		start = s [0]; end = s [1];
		if (OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, str, null) != OS.noErr) return;
	}
	CFRange range = new CFRange ();
	range.location = start;
	range.length = end - start;
	int encoding = OS.CFStringGetSystemEncoding ();
	int [] size = new int [1];
	OS.CFStringGetBytes (str [0], range, encoding, (byte)'?', true, null, 0, size);
	byte [] buffer = new byte [size [0]];
	OS.CFStringGetBytes (str [0], range, encoding, (byte)'?', true, buffer, size [0], size);
	OS.CFRelease (str [0]);
	
	OS.ClearCurrentScrap();
	int[] scrap = new int [1];
	OS.GetCurrentScrap (scrap);
	OS.PutScrapFlavor(scrap [0], OS.kScrapFlavorTypeText, 0, buffer.length, buffer);
}

void createHandle () {
	// NEEDS WORK - SIMPLE
	if ((style & SWT.READ_ONLY) != 0) {
		int [] outControl = new int [1];
		int window = OS.GetControlOwner (parent.handle);
		/* From ControlDefinitions.h:
		 * 
		 * Passing in a menu ID of -12345 causes the popup not to try and get the menu from a
		 * resource. Instead, you can build the menu and later stuff the MenuRef field in
		 * the popup data information.                                                                         
		 */
		OS.CreatePopupButtonControl(window, null, 0, (short)-12345, false, (short)0, (short)0, 0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		int[] menuRef= new int[1];
		OS.CreateNewMenu ((short)0, 0, menuRef);
		if (menuRef [0] == 0) error (SWT.ERROR_NO_HANDLES);
		menuHandle = menuRef[0];
		OS.SetControlPopupMenuHandle(handle, menuHandle);
		OS.SetControl32BitMaximum(handle, 0x7FFF);
	} else {
		int [] outControl = new int [1];
		CGRect rect = new CGRect ();
		int inAttributes = OS.kHIComboBoxAutoSizeListAttribute;
		/*
		* The following code is intentionally commented.
		* Auto completion does not allow the user to change
		* case of the text in a combo box.
		*/
//		inAttributes |= OS.kHIComboBoxAutoCompletionAttribute;
		OS.HIComboBoxCreate(rect, 0, null, 0, inAttributes, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kTXNDrawCaretWhenInactiveTag, 4, new byte [ ]{0});
//		OS.HIViewSetVisible (handle, true);
		OS.SetControlVisibility (handle, true, false);
	}
}

/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void cut () {
	//NEEDS WORK - Modify/Verify
	checkWidget ();
	int [] str = new int [1];
	short start, end;
	if ((style & SWT.READ_ONLY) != 0) {
		// NEEDS WORK - getting whole text, not just selection
		int index = OS.GetControlValue (handle);
		if (OS.CopyMenuItemTextAsCFString(menuHandle, (short)index, str) != OS.noErr) return;
		start = 0; end = (short)OS.CFStringGetLength (str [0]);
		if (start >= end) {
			OS.CFRelease (str [0]);
			return;
		}
	} else {
		short [] s = new short [2];
		OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, 4, s, null);
		if (s [0] >= s [1]) return;
		start = s [0]; end = s [1];
		if (OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, str, null) != OS.noErr) return;
	}
	CFRange range = new CFRange ();
	range.location = start;
	range.length = end - start;
	int encoding = OS.CFStringGetSystemEncoding ();
	int [] size = new int [1];
	OS.CFStringGetBytes (str [0], range, encoding, (byte)'?', true, null, 0, size);
	byte [] buffer = new byte [size [0]];
	OS.CFStringGetBytes (str [0], range, encoding, (byte)'?', true, buffer, size [0], size);

	OS.ClearCurrentScrap();
	int[] scrap = new int [1];
	OS.GetCurrentScrap (scrap);
	OS.PutScrapFlavor (scrap [0], OS.kScrapFlavorTypeText, 0, buffer.length, buffer);
	
	// delete selection
	if ((style & SWT.READ_ONLY) != 0) {
		// NEEDS WORK
	} else {
		byte [] newBuffer;
		range.location = 0;
		range.length = start;
		size = new int [1];
		OS.CFStringGetBytes (str [0], range, encoding, (byte)'?', true, null, 0, size);
		byte [] preBuffer = new byte [size [0]];
		OS.CFStringGetBytes(str [0], range, encoding, (byte)'?', true, preBuffer, size [0], size);
		range.location = end;
		range.length = OS.CFStringGetLength (str [0]) - end;
		size = new int [1];
		OS.CFStringGetBytes (str [0], range, encoding, (byte)'?', true, null, 0, size);
		byte [] postBuffer = new byte [size [0]];
		OS.CFStringGetBytes (str [0], range, encoding, (byte)'?', true, postBuffer, size [0], size);
		newBuffer = new byte [preBuffer.length + postBuffer.length];
		System.arraycopy(preBuffer, 0, newBuffer, 0, preBuffer.length);
		System.arraycopy(postBuffer, 0, newBuffer, preBuffer.length, postBuffer.length);
		int ptr = OS.CFStringCreateWithBytes (OS.kCFAllocatorDefault, newBuffer, newBuffer.length, encoding, true);
		OS.SetControlData (handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, new int[] {ptr});
		OS.CFRelease (ptr);
		sendEvent (SWT.Modify);
	}
	
	OS.CFRelease (str [0]);
}

/**
 * Deselects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int index) {
	checkWidget ();
	if (index == -1) return;
	// NEEDS WORK
}

/**
 * Deselects all selected items in the receiver's list.
 * <p>
 * Note: To clear the selection in the receiver's text field,
 * use <code>clearSelection()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #clearSelection
 */
public void deselectAll () {
	checkWidget ();
	// NEEDS WORK
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver's list. Throws an exception if the index is out
 * of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public String getItem (int index) {
	checkWidget ();
	int count = getItemCount ();
	if (0 > index || index >= count) error (SWT.ERROR_INVALID_RANGE);
	int[] ptr = new int[1];
	int result;
	if ((style & SWT.READ_ONLY) != 0) {
		result = OS.CopyMenuItemTextAsCFString(menuHandle, (short)(index+1), ptr);
	} else {
		result = OS.HIComboBoxCopyTextItemAtIndex (handle, index, ptr);
	}
	if (result != OS.noErr) error(SWT.ERROR_CANNOT_GET_ITEM);
	int length = OS.CFStringGetLength (ptr [0]);
	char [] buffer= new char [length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (ptr [0], range, buffer);
	OS.CFRelease (ptr [0]);
	return new String (buffer);
}

/**
 * Returns the number of items contained in the receiver's list.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) {
		return OS.CountMenuItems (menuHandle);
	} else {
		return OS.HIComboBoxGetItemCount (handle);
	}
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver's list.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget ();
	return 26; // NEEDS WORK
}

/**
 * Returns an array of <code>String</code>s which are the items
 * in the receiver's list. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver's list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public String [] getItems () {
	checkWidget ();
	int count = getItemCount ();
	String [] result = new String [count];
	for (int i=0; i<count; i++) result [i] = getItem (i);
	return result;
}

/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation bit.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public int getOrientation () {
	checkWidget();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**
 * Returns a <code>Point</code> whose x coordinate is the start
 * of the selection in the receiver's text field, and whose y
 * coordinate is the end of the selection. The returned values
 * are zero-relative. An "empty" selection as indicated by
 * the the x and y coordinates having the same value.
 *
 * @return a point representing the selection start and end
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget ();
	Point selection;
	if ((style & SWT.READ_ONLY) != 0) {
		// NEEDS WORK
		selection = new Point(0, 0);
	} else {
		short [] s = new short [2];
		OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, 4, s, null);
		selection = new Point (s[0], s[1]);
	}
	return selection;
}

/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver's list, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget ();
	int index;
	if ((style & SWT.READ_ONLY) != 0) {
		index = OS.GetControlValue (handle) - 1;
	} else {
		// NEEDS WORK
    	index = indexOf(getText ());
	}
	return index;
}

/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	int [] ptr = new int [1];
	int result;
	if ((style & SWT.READ_ONLY) != 0) {
		int index = OS.GetControlValue (handle) - 1;
		result = OS.CopyMenuItemTextAsCFString(menuHandle, (short)(index+1), ptr);
	} else {
		int [] actualSize = new int [1];
		result = OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, ptr, actualSize);
	}
	if (result != OS.noErr) return "";
	int length = OS.CFStringGetLength (ptr [0]);
	char [] buffer= new char [length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (ptr [0], range, buffer);
	OS.CFRelease (ptr [0]);
	return new String (buffer);
}

/**
 * Returns the height of the receivers's text field.
 *
 * @return the text height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getTextHeight () {
	checkWidget();
	return 26; // NEEDS WORK
}

/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Combo.LIMIT</code>.
 * 
 * @return the text limit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTextLimit () {
	checkWidget();
    return LIMIT; // NEEDS WORK
}

void hookEvents () {
	super.hookEvents ();
	if ((style & SWT.READ_ONLY) != 0) {
		int commandProc = display.commandProc;
		int [] mask = new int [] {
			OS.kEventClassCommand, OS.kEventProcessCommand,
		};
		int menuTarget = OS.GetMenuEventTarget (menuHandle);
		OS.InstallEventHandler (menuTarget, commandProc, mask.length / 2, mask, handle, null);		
	}
}
	
/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string) {
	return indexOf (string, 0);
}

/**
 * Searches the receiver's list starting at the given, 
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = getItemCount ();
	if (!(0 <= start && start < count)) return -1;
	for (int i=start; i<count; i++) {
		if (string.equals (getItem (i))) {
			return i;
		}
	}
	return -1;
}

Rect getInset () {
	return display.comboInset;
}

int kEventControlActivate (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlActivate (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	/*
	* Feature in the Macintosh.  When a combo box gets
	* kEventControlActivate, it starts the caret blinking.
	* Because there is no clipping on the Macintosh, the
	* caret may blink through a widget that is obscurred.
	* The fix is to avoid running the default handler.
	*/
	return OS.noErr;
}

int kEventProcessCommand (int nextHandler, int theEvent, int userData) {
	int result = super.kEventProcessCommand (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the modify
	* event.  If this happens, end the processing of the
	* Windows message by returning zero as the result of
	* the window proc.
	* 
	* Note: this should be a send event, but selection is updated
	* right way.
	*/
	postEvent (SWT.Modify);
	if (isDisposed ()) return OS.eventNotHandledErr;
	postEvent (SWT.Selection);
	return OS.eventNotHandledErr;
}

int kEventRawKey (int nextHandler, int theEvent, int userData) {
	/*
	* Feature in the Macintosh. The combo box widget consumes the
	* kEventRawKeyDown event when the Return key is pressed causing
	* the kEventTextInputUnicodeForKeyEvent to be not sent.  The fix
	* is to handle the Return key in kEventRawKeyDown instead.
	*/
	int [] keyCode = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	if (keyCode [0] == 36 /* Return */) {
		if (translateTraversal (keyCode [0], theEvent)) return OS.noErr;
		if (!sendKeyEvent (SWT.KeyDown, theEvent)) return OS.noErr;
		postEvent(SWT.DefaultSelection);
	}
	return OS.eventNotHandledErr;
}

/**
 * Pastes text from clipboard.
 * <p>
 * The selected text is deleted from the widget
 * and new text inserted from the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void paste () {
	checkWidget ();
	//NEEDS WORK - Modify/Verify
	int[] scrap = new int [1];
	OS.GetCurrentScrap (scrap);
	int [] size = new int [1];
	if (OS.GetScrapFlavorSize (scrap [0], OS.kScrapFlavorTypeText, size) != OS.noErr || size [0] == 0) return;
	byte [] buffer = new byte[size [0]];
	if (OS.GetScrapFlavorData (scrap [0], OS.kScrapFlavorTypeText, size, buffer) != OS.noErr) return;
	if ((style & SWT.READ_ONLY) != 0) {
		String string = new String (buffer); //??
		int index = indexOf (string);
		if (index != -1) select(index);
	} else {
		byte [] newBuffer;
		int encoding = OS.CFStringGetSystemEncoding ();
		int[] ptrOld = new int [1];
		if (OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, ptrOld, null) == OS.noErr) {
			short [] s = new short [2];
			OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, 4, s, null);
			CFRange range = new CFRange ();
			range.location = 0;
			range.length = s [0];
			size = new int [1];
			OS.CFStringGetBytes (ptrOld [0], range, encoding, (byte)'?', true, null, 0, size);
			byte [] preBuffer = new byte [size [0]];
			OS.CFStringGetBytes(ptrOld [0], range, encoding, (byte)'?', true, preBuffer, size [0], size);
			range.location = s [1];
			range.length = OS.CFStringGetLength (ptrOld [0]) - s [1];
			size = new int [1];
			OS.CFStringGetBytes (ptrOld [0], range, encoding, (byte)'?', true, null, 0, size);
			byte [] postBuffer = new byte [size [0]];
			OS.CFStringGetBytes(ptrOld [0], range, encoding, (byte)'?', true, postBuffer, size [0], size);
			newBuffer = new byte [preBuffer.length + buffer.length + postBuffer.length];
			System.arraycopy(preBuffer, 0, newBuffer, 0, preBuffer.length);
			System.arraycopy(buffer, 0, newBuffer, preBuffer.length, buffer.length);
			System.arraycopy(postBuffer, 0, newBuffer, preBuffer.length + buffer.length, postBuffer.length);
			OS.CFRelease (ptrOld [0]);
		} else {
			newBuffer = buffer;
		}
		int ptr = OS.CFStringCreateWithBytes (OS.kCFAllocatorDefault, newBuffer, newBuffer.length, encoding, true);
		OS.SetControlData (handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, new int[] {ptr});
		OS.CFRelease (ptr);
		sendEvent (SWT.Modify);
	}
}

/**
 * Removes the item from the receiver's list at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget ();
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int count = getItemCount ();
	if (0 > index || index >= count) error (SWT.ERROR_INVALID_RANGE);
	if ((style & SWT.READ_ONLY) != 0) {
		OS.DeleteMenuItems (menuHandle, (short)(index+1), 1);
		if (index == OS.GetControlValue (handle) - 1) {
			OS.SetControl32BitValue (handle, 0);
		}
	} else {
		OS.HIComboBoxRemoveItemAtIndex (handle, index);
	}
}

/**
 * Removes the items from the receiver's list which are
 * between the given zero-relative start and end 
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget();
	if (start > end) return;
	int count = getItemCount ();
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int newEnd = Math.min (end, count - 1);
	if ((style & SWT.READ_ONLY) != 0) {
		OS.DeleteMenuItems (menuHandle, (short)(start+1), newEnd-start+1);
		int index = OS.GetControlValue (handle) - 1;
		if (start <= index && index <= end) {
			OS.SetControl32BitValue (handle, 0);
		}
	} else {
		// NEEDS WORK
		for (int i=newEnd; i>=start; i--) {
			OS.HIComboBoxRemoveItemAtIndex(handle, i);
		}
	}
}

/**
 * Searches the receiver's list starting at the first item
 * until an item is found that is equal to the argument, 
 * and removes that item from the list.
 *
 * @param string the item to remove
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	// NEEDS WORK
	int count = getItemCount ();
	for (int i=0; i<count; i++) {
		String s = getItem (i);
		if (string.equals (s)) {
			remove (i);
			return;
		}
	}
	error (SWT.ERROR_INVALID_ARGUMENT);
}

/**
 * Removes all of the items from the receiver's list.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget ();
	int count = getItemCount ();
	if ((style & SWT.READ_ONLY) != 0) {
		OS.DeleteMenuItems (menuHandle, (short)1, count);
		OS.SetControl32BitValue (handle, 0);
	} else {
		// NEEDS WORK
		if (count > 0) {
			for (int i=count-1; i>=0; i--) {
  				OS.HIComboBoxRemoveItemAtIndex (handle, i);
			}
		}
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's text is modified.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #addModifyListener
 */
public void removeModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's selection changes.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

/**
 * Selects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int index) {
	checkWidget ();
	//NEEDS WORK Modify/Verify
	int count = getItemCount ();
	if (0 <= index && index < count) {
		if ((style & SWT.READ_ONLY) != 0) {
			OS.SetControl32BitValue (handle, index+1);
		} else {
			int[] ptr = new int[1];
			if (OS.HIComboBoxCopyTextItemAtIndex (handle, index, ptr) != OS.noErr) return;
			OS.SetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, ptr);
			OS.CFRelease (ptr [0]);		
		}
		sendEvent (SWT.Modify);
	}	
}

boolean sendKeyEvent (int type, Event event) {
	if (!super.sendKeyEvent (type, event)) {
		return false;
	}
	if (type != SWT.KeyDown) return true;
	if (event.character == 0) return true;
	if ((style & SWT.READ_ONLY) != 0) return false;
	/*
	* Post the modify event so that the character will be inserted
	* into the widget when the modify event is delivered.  Normally,
	* modify events are sent but it is safe to post the event here
	* because this method is called from the event loop.
	*/
	postEvent (SWT.Modify);
	return true;
}
/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument. This is equivalent
 * to <code>remove</code>'ing the old item at the index, and then
 * <code>add</code>'ing the new item at that index.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the remove operation fails because of an operating system failure</li>
 *    <li>ERROR_ITEM_NOT_ADDED - if the add operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = getItemCount ();
	if (0 > index || index >= count) error (SWT.ERROR_INVALID_RANGE);
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	int result;
	if ((style & SWT.READ_ONLY) != 0) {
		result = OS.SetMenuItemTextWithCFString (menuHandle, (short)(index+1), ptr);
	} else {
		result = OS.HIComboBoxInsertTextItemAtIndex (handle, index, ptr);
		OS.HIComboBoxRemoveItemAtIndex (handle, index+1);
	}
	OS.CFRelease(ptr);
	if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_ADDED);
}

/**
 * Sets the receiver's list to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
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
		int result;
		if ((style & SWT.READ_ONLY) != 0) {
			result = OS.AppendMenuItemTextWithCFString (menuHandle, ptr, 0, 0, null);
		} else {
			int [] outIndex = new int[1];
			result = OS.HIComboBoxAppendTextItem (handle, ptr, outIndex);
		}
		OS.CFRelease(ptr);
		if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_ADDED);
	}
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.LEFT_TO_RIGHT</code>.
 * <p>
 *
 * @param orientation new orientation bit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public void setOrientation (int orientation) {
	checkWidget();
}

/**
 * Sets the selection in the receiver's text field to the
 * range specified by the argument whose x coordinate is the
 * start of the selection and whose y coordinate is the end
 * of the selection. 
 *
 * @param a point representing the new selection start and end
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (Point selection) {
	checkWidget ();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.READ_ONLY) != 0) {
		// NEEDS WORK
	} else {
		short [] s = new short [] {(short)selection.x, (short)selection.y };
		OS.SetControlData (handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, 4, s);
	}
}

/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to 
 * display incorrectly.
 * </p>
 *
 * @param text the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget ();
	//NEEDS WORK - Modify/Verify
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.READ_ONLY) != 0) {
		int index = indexOf (string);
		if (index != -1) select(index);
	} else {
		char [] buffer = new char [string.length ()];
		string.getChars (0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) return;	
		OS.SetControlData (handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, new int[] {ptr});
		OS.CFRelease (ptr);
		sendEvent (SWT.Modify);
	}
}

/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 *
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	// NEEDS WORK
}

}
