/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.ControlEditTextSelectionRec;
import org.eclipse.swt.internal.carbon.MenuTrackingData;
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
 * <dd>DefaultSelection, Modify, Selection, Verify</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DROP_DOWN and SIMPLE may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 * @see <a href="http://www.eclipse.org/swt/snippets/#combo">Combo snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Combo extends Composite {
	int menuHandle;
	int textLimit = LIMIT;
	String lastText = "";
	ControlEditTextSelectionRec selection;

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
	
	static final String [] AX_ATTRIBUTES = {
		OS.kAXValueAttribute,
		OS.kAXNumberOfCharactersAttribute,
		OS.kAXSelectedTextAttribute,
		OS.kAXSelectedTextRangeAttribute,
		OS.kAXStringForRangeParameterizedAttribute,
	};

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
		/*
		* Feature in the Macintosh.  Setting text that starts with "-" makes the
		* menu item a separator.  The fix is to clear the separator attribute. 
		*/
		if (string.startsWith ("-")) {
			OS.ChangeMenuItemAttributes (menuHandle, (short)OS.CountMenuItems (menuHandle), 0, OS.kMenuItemAttrSeparator);
		}
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
	int selectionIndex = -1;
	if ((style & SWT.READ_ONLY) != 0) {
		selectionIndex = OS.GetControlValue (handle) - 1;
		result = OS.InsertMenuItemTextWithCFString (menuHandle, ptr, (short)index, 0, 0);
		/*
		* Feature in the Macintosh.  Setting text that starts with "-" makes the
		* menu item a separator.  The fix is to clear the separator attribute. 
		*/
		if (string.startsWith ("-")) {
			OS.ChangeMenuItemAttributes (menuHandle, (short)(index + 1), 0, OS.kMenuItemAttrSeparator);
		}
	} else {
		result = OS.HIComboBoxInsertTextItemAtIndex (handle, index, ptr);
	}
	OS.CFRelease (ptr);
	if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_ADDED);
	/*
	* When inserting an item into a READ_ONLY combo at or above the selected
	* index neither the selection index nor the text get updated. Fix is to
	* update the selection index to be sure the displayed item matches the 
	* selected index.
	*/
	if (selectionIndex >= index) OS.SetControl32BitValue (handle, selectionIndex + 2);
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
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the combo's list selection.
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

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is verified, by sending
 * it one of the messages defined in the <code>VerifyListener</code>
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
 * @see VerifyListener
 * @see #removeVerifyListener
 * 
 * @since 3.1
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

int callFocusEventHandler (int nextHandler, int theEvent) {
	short [] part = new short [1];
	if ((style & SWT.READ_ONLY) == 0) {
		OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
		if (part [0] == OS.kControlFocusNoPart) {
			selection = new ControlEditTextSelectionRec ();
			OS.GetControlData (handle, (short) OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection, null);
		}
	}
	int result = super.callFocusEventHandler (nextHandler, theEvent);
	if ((style & SWT.READ_ONLY) == 0) {
		if (part [0] != OS.kControlFocusNoPart && selection != null) {
			OS.SetControlData (handle, (short) OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection);
			selection = null;
		}
	}
	return result;
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

void checkSelection () {
	if ((style & SWT.READ_ONLY) != 0) return;
	String newText = getText ();
	if (newText.equals (lastText)) return;
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		setText (lastText, false);
		newText = verifyText (newText, 0, lastText.length (), null);
		if (newText == null) return;
		setText (newText, false);
	} else {
		lastText = newText;
	}
	sendEvent (SWT.Modify);
	if (isDisposed ()) return;
	int index = indexOf (newText);
	if (index != -1) postEvent (SWT.Selection);
	
	/* Send value changed notification to accessible client. */
	String string = OS.kAXFocusedWindowChangedNotification;
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	OS.AXNotificationHIObjectNotify(stringRef, handle, 0);
	OS.CFRelease(stringRef);
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
	if ((style & SWT.READ_ONLY) == 0) {
		Point selection = getSelection ();
		selection.y = selection.x;
		setSelection (selection);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	int [] ptr = new int [1];
	if ((style & SWT.READ_ONLY) != 0) {
		int index = OS.GetControlValue (handle) - 1;
		OS.CopyMenuItemTextAsCFString (menuHandle, (short)(index+1), ptr);
	} else {
		OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, ptr, null);
	}
	Point size = textExtent (ptr [0], 0);
	if (ptr [0] != 0) OS.CFRelease (ptr [0]);
	width = Math.max (width, size.x);
	height = Math.max (height, size.y);
	int count;
	if ((style & SWT.READ_ONLY) != 0) {
		count = OS.CountMenuItems (menuHandle);
	} else {
		count = OS.HIComboBoxGetItemCount (handle);
	}
	for (int i=0; i<count; i++) {
		int result;
		if ((style & SWT.READ_ONLY) != 0) {
			result = OS.CopyMenuItemTextAsCFString(menuHandle, (short)(i+1), ptr);
		} else {
			result = OS.HIComboBoxCopyTextItemAtIndex (handle, i, ptr);
		}
		if (result == OS.noErr) {
			size = textExtent (ptr [0], 0);
			width = Math.max (width, size.x);
			OS.CFRelease (ptr [0]);
		}
	}
	int [] metric = new int [1];
	if ((style & SWT.READ_ONLY) != 0) {
		OS.GetThemeMetric (OS.kThemeMetricDisclosureButtonWidth, metric);
		width += metric [0];
		//TODO
		width += 13;
	} else {
		OS.GetThemeMetric (OS.kThemeMetricComboBoxLargeDisclosureWidth, metric);
		width += metric [0];
	}
	OS.GetThemeMetric (OS.kThemeMetricEditTextWhitespace, metric);
	width += metric [0] * 2;
	height += metric [0] * 2;
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
	Point selection = getSelection ();
	if (selection.x == selection.y) return;
	copyToClipboard (getText (selection.x, selection.y));
}

void createHandle () {
	if ((style & SWT.READ_ONLY) != 0) {
		int [] outControl = new int [1];
		int window = OS.GetControlOwner (parent.handle);
		/*
		* From ControlDefinitions.h:
		*
		* Passing in a menu ID of -12345 causes the popup not to try and get the menu from a
		* resource. Instead, you can build the menu and later stuff the MenuRef field in
		* the popup data information.                                                                         
		*/
		OS.CreatePopupButtonControl(window, null, 0, (short)-12345, false, (short)0, (short)0, 0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		int[] menuRef= new int [1];
		OS.CreateNewMenu ((short) 0, 0, menuRef);
		if (menuRef [0] == 0) error (SWT.ERROR_NO_HANDLES);
		menuHandle = menuRef[0];
		OS.SetControlPopupMenuHandle (handle, menuHandle);
		OS.SetControl32BitMaximum (handle, 0x7FFF);
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
		OS.HIViewSetVisible (handle, true);
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	Point selection = getSelection ();
	if (selection.x == selection.y) return;
	int start = selection.x, end = selection.y;
	String text = getText ();
	String leftText = text.substring (0, start);
	String rightText = text.substring (end, text.length ());
	String oldText = text.substring (start, end);
	String newText = "";
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		newText = verifyText (newText, start, end, null);
		if (newText == null) return;
	}
	char [] buffer = new char [oldText.length ()];
	oldText.getChars (0, buffer.length, buffer, 0);
	copyToClipboard (buffer);
	setText (leftText + newText + rightText, false);
	start += newText.length ();
	setSelection (new Point (start, start));
	sendEvent (SWT.Modify);
}

Color defaultBackground () {
    return display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
}

Color defaultForeground () {
    return display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
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
	if (index == getSelectionIndex ()) {
		if ((style & SWT.READ_ONLY) != 0) {
			OS.SetControl32BitValue (handle, 0);
			sendEvent (SWT.Modify);
		} else {
			setText ("");
		}
	}
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
	if ((style & SWT.READ_ONLY) != 0) {
		OS.SetControl32BitValue (handle, 0);
		sendEvent (SWT.Modify);
	} else {
		setText ("");
	}
}

void destroyWidget () {
	/*
	* Bug in the Macintosh.  Carbon segments fault if the combo box has
	* keyboard focus and it is disposed or its parent is disposed because
	* there is an outstanding timer that runs after the widget is dispoed.
	* The fix is to remove the combo box from its parent and dispose it when
	* the display is idle.
	* 
	* NOTE: The problem does not happen when the window is disposed.
	*/
	if ((getShell ().state & DISPOSE_SENT) != 0) {
		super.destroyWidget ();
	} else {
		releaseHandle ();
	}
}

String [] getAxAttributes () {
	return AX_ATTRIBUTES;
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
 */
public int getItemHeight () {
	checkWidget ();
	//TODO - not supported by the OS
	return 26;
}

/**
 * Returns a (possibly empty) array of <code>String</code>s which are
 * the items in the receiver's list. 
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
 */
public String [] getItems () {
	checkWidget ();
	int count = getItemCount ();
	String [] result = new String [count];
	for (int i=0; i<count; i++) result [i] = getItem (i);
	return result;
}

/**
 * Returns <code>true</code> if the receiver's list is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's list's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public boolean getListVisible () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) {
		MenuTrackingData outData = new MenuTrackingData ();
		return OS.GetMenuTrackingData (menuHandle, outData) == OS.noErr;
	} else {
 		if (OS.VERSION >= 0x1040) {
 			return OS.HIComboBoxIsListVisible (handle);
 		}
 		return false;
	}
}

int getMininumHeight () {
	return getTextHeight ();
}

String getNameText () {
	return getText ();
}

/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation style
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
 * Returns a <code>Point</code> whose x coordinate is the
 * character position representing the start of the selection
 * in the receiver's text field, and whose y coordinate is the
 * character position representing the end of the selection.
 * An "empty" selection is indicated by the x and y coordinates
 * having the same value.
 * <p>
 * Indexing is zero based.  The range of a selection is from
 * 0..N where N is the number of characters in the widget.
 * </p>
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
	if ((style & SWT.READ_ONLY) != 0) {
		return new Point (0, getCharCount ());
	} else {
		ControlEditTextSelectionRec selection;
		if (this.selection != null) {
			selection = this.selection;
		} else {
			selection = new ControlEditTextSelectionRec ();
			OS.GetControlData (handle, (short) OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, 4, selection, null);
		}
		return new Point (selection.selStart, selection.selEnd);
	}
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
	if ((style & SWT.READ_ONLY) != 0) {
		return OS.GetControlValue (handle) - 1;
	} else {
		return indexOf (getText ());
	}
}

/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field, or an empty string if there are no
 * contents.
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
	return new String (getText (0, -1));
}

char []  getText (int start, int end) {
	int result;
	int [] ptr = new int [1];
	if ((style & SWT.READ_ONLY) != 0) {
		int index = OS.GetControlValue (handle) - 1;
		result = OS.CopyMenuItemTextAsCFString (menuHandle, (short)(index+1), ptr);
	} else {
		int [] actualSize = new int [1];
		result = OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, ptr, actualSize);
	}
	if (result != OS.noErr) return new char [0];
	CFRange range = new CFRange ();
	range.location = start;
	if (end == -1) {
		int length = OS.CFStringGetLength (ptr [0]);
		range.length = length - start;
	} else {
		range.length = end - start;
	}
	char [] buffer= new char [range.length];
	OS.CFStringGetCharacters (ptr [0], range, buffer);
	OS.CFRelease (ptr [0]);
	return buffer;
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
 */
public int getTextHeight () {
	checkWidget();
	//TODO - not supported by the OS
	return 26;
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
 *
 * @see #LIMIT
 */
public int getTextLimit () {
	checkWidget();
    return textLimit;
}

/**
 * Gets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @return the number of items that are visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public int getVisibleItemCount () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) {
		return getItemCount ();
	} else {
		int [] buffer = new int [1];
		OS.GetControlData (handle, (short) OS.kControlEntireControl, OS.kHIComboBoxNumVisibleItemsTag, 4, buffer, null);
		return buffer [0];
	}
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
 * @param start the zero-relative index at which to begin the search
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

int getCharCount () {
//	checkWidget ();
	int [] ptr = new int [1];
	int result;
	if ((style & SWT.READ_ONLY) != 0) {
		int index = OS.GetControlValue (handle) - 1;
		result = OS.CopyMenuItemTextAsCFString(menuHandle, (short)(index+1), ptr);
	} else {
		int [] actualSize = new int [1];
		result = OS.GetControlData (handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, ptr, actualSize);
	}
	if (result != OS.noErr) return 0;
	int length = OS.CFStringGetLength (ptr [0]);
	OS.CFRelease (ptr [0]);
	return length;
}

Rect getInset () {
	return display.comboInset;
}

int kEventAccessibleGetNamedAttribute (int nextHandler, int theEvent, int userData) {
	int code = OS.eventNotHandledErr;
	if ((style & SWT.READ_ONLY) == 0) {
		int [] stringRef = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeName, OS.typeCFStringRef, null, 4, null, stringRef);
		int length = 0;
		if (stringRef [0] != 0) length = OS.CFStringGetLength (stringRef [0]);
		char [] buffer = new char [length];
		CFRange range = new CFRange ();
		range.length = length;
		OS.CFStringGetCharacters (stringRef [0], range, buffer);
		String attributeName = new String(buffer);
		if (attributeName.equals (OS.kAXValueAttribute)) {
			buffer = getText(0, -1);
			stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
			if (stringRef [0] != 0) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
				OS.CFRelease(stringRef [0]);
				code = OS.noErr;
			}
		} else if (attributeName.equals (OS.kAXNumberOfCharactersAttribute)) {
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {getCharCount()});
			code = OS.noErr;
		} else if (attributeName.equals (OS.kAXSelectedTextAttribute)) {
			Point sel = getSelection ();
			buffer = getText(sel.x, sel.y);
			stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
			if (stringRef [0] != 0) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
				OS.CFRelease(stringRef [0]);
				code = OS.noErr;
			}
		} else if (attributeName.equals (OS.kAXSelectedTextRangeAttribute)) {
			Point sel = getSelection ();
			range = new CFRange();
			range.location = sel.x;
			range.length = sel.y - sel.x;
			int valueRef = OS.AXValueCreate(OS.kAXValueCFRangeType, range);
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFTypeRef, 4, new int [] {valueRef});
			OS.CFRelease(valueRef);
			code = OS.noErr;
		} else if (attributeName.equals (OS.kAXStringForRangeParameterizedAttribute)) {
			int valueRef [] = new int [1];
			int status = OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeParameter, OS.typeCFTypeRef, null, 4, null, valueRef);
			if (status == OS.noErr) {
				range = new CFRange();
				boolean ok = OS.AXValueGetValue(valueRef[0], OS.kAXValueCFRangeType, range);
				if (ok) {
					buffer = getText (range.location, range.location + range.length);
					stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
					if (stringRef [0] != 0) {
						OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
						OS.CFRelease(stringRef [0]);
						code = OS.noErr;
					}
				}
			}
		}
	}
	if (accessible != null) {
		code = accessible.internal_kEventAccessibleGetNamedAttribute (nextHandler, theEvent, code);
	}
	return code;
}

int kEventControlActivate (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlActivate (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	/*
	* Feature in the Macintosh.  When a combo box gets
	* kEventControlActivate, it starts the caret blinking.
	* Because there is no clipping on the Macintosh, the
	* caret may blink through a widget that is obscured.
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

int kEventRawKeyPressed (int nextHandler, int theEvent, int userData) {
	/*
	* Feature in the Macintosh. The combo box widget consumes the
	* kEventRawKeyDown event when the up and down arrow keys are
	* pressed, causing kEventTextInputUnicodeForKeyEvent not
	* to be sent.  The fix is to handle these keys in kEventRawKeyDown.
	* 
	* NOTE:  This was fixed in OS X 10.4.
	*/
	if (OS.VERSION < 0x1040) {
		int [] keyCode = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
		switch (keyCode [0]) {
			case 126: /* Up arrow */
			case 125: /* Down arrow */
				if (!sendKeyEvent (SWT.KeyDown, theEvent)) return OS.noErr;
				break;
		}
	}
	return OS.eventNotHandledErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if (result == OS.noErr) {
		if ((style & SWT.READ_ONLY) == 0) {
			short [] part = new short [1];
			OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
			if (part [0] != OS.kControlFocusNoPart) display.focusCombo = this;
		}
	}
	return result;
}

int kEventUnicodeKeyPressed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventUnicodeKeyPressed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] keyboardEvent = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
	int [] keyCode = new int [1];
	OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	
	String string = OS.kAXValueChangedNotification;
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	OS.AXNotificationHIObjectNotify(stringRef, handle, 0);
	OS.CFRelease(stringRef);
	string = OS.kAXSelectedTextChangedNotification;
	buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	OS.AXNotificationHIObjectNotify(stringRef, handle, 0);
	OS.CFRelease(stringRef);

	if (hooks (SWT.Verify) || filters (SWT.Verify)
			|| hooks (SWT.Modify) || filters (SWT.Modify)) {
		int [] modifiers = new int [1];
		OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
		if (modifiers [0] == OS.cmdKey) {
			switch (keyCode [0]) {
				case 7: /* X */
					cut ();
					return OS.noErr;
				case 9: /* V */
					paste ();
					return OS.noErr;
			}
		}
	}
	switch (keyCode [0]) {
		case 76: /* KP Enter */
		case 36: { /* Return */
			postEvent (SWT.DefaultSelection);
			break;
		}
	}
	result = OS.CallNextEventHandler (nextHandler, theEvent);
	lastText = getText ();
	return result;
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
	if ((style & SWT.READ_ONLY) != 0) return;
	Point selection = getSelection ();
	int start = selection.x, end = selection.y;
	String text = getText ();
	String leftText = text.substring (0, start);
	String rightText = text.substring (end, text.length ());
	String newText = getClipboardText ();
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		newText = verifyText (newText, start, end, null);
		if (newText == null) return;
	}
	if (textLimit != LIMIT) {
		int charCount = text.length ();
		if (charCount - (end - start) + newText.length() > textLimit) {
			newText = newText.substring(0, textLimit - charCount + (end - start));
		}
	}
	setText (leftText + newText + rightText, false);
	start += newText.length ();
	setSelection (new Point (start, start));
	sendEvent (SWT.Modify);
}

boolean pollTrackEvent() {
	return true;
}

void releaseHandle () {
	/*
	* Bug in the Macintosh.  Carbon segments fault if the combo box has
	* keyboard focus and it is disposed or its parent is disposed because
	* there is an outstanding timer that runs after the widget is dispoed.
	* The fix is to remove the combo box from its parent and dispose it when
	* the display is idle.
	* 
	* NOTE: The problem does not happen when the window is disposed.
	*/
	if ((getShell ().state & DISPOSE_SENT) == 0) {
		display.addToDisposeWindow (handle);
	}
	super.releaseHandle ();
}

void releaseWidget () {
	super.releaseWidget ();
	if (display.focusCombo == this) display.focusCombo = null;
	if (menuHandle != 0) {
		OS.DisposeMenu (menuHandle);
	}
	menuHandle = 0;
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
		for (int i=newEnd; i>=start; i--) {
			OS.HIComboBoxRemoveItemAtIndex (handle, i);
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
 */
public void remove (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_INVALID_ARGUMENT);
	remove (index);
}

/**
 * Removes all of the items from the receiver's list and clear the
 * contents of receiver's text field.
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
		setText ("", true);
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
 * be notified when the user changes the receiver's selection.
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
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
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
 * @see VerifyListener
 * @see #addVerifyListener
 * 
 * @since 3.1
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);
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
	int count = getItemCount ();
	if (0 <= index && index < count) {
		if ((style & SWT.READ_ONLY) != 0) {
			OS.SetControl32BitValue (handle, index + 1);
			sendEvent (SWT.Modify);
		} else {
			setText (getItem (index), true);
		}
	}	
}

boolean sendKeyEvent (int type, Event event) {
	if (!super.sendKeyEvent (type, event)) {
		return false;
	}
	if (type != SWT.KeyDown) return true;
	if ((style & SWT.READ_ONLY) != 0) return true;
	if (event.character == 0) return true;
	if ((event.stateMask & SWT.COMMAND) != 0) return true;
	String oldText = "", newText = "";
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int charCount = getCharCount ();
		Point selection = getSelection ();
		int start = selection.x, end = selection.y;
		switch (event.character) {
			case SWT.BS:
				if (start == end) {
					if (start == 0) return true;
					start = Math.max (0, start - 1);
				}
				break;
			case SWT.DEL:
				if (start == end) {
					if (start == charCount) return true;
					end = Math.min (end + 1, charCount);
				}
				break;
			case SWT.CR:
				return true;
			default:
				if (event.character != '\t' && event.character < 0x20) return true;
				oldText = new String (new char [] {event.character});
		}
		newText = verifyText (oldText, start, end, event);
		if (newText == null) return false;
		if (charCount - (end - start) + newText.length () > textLimit) {
			return false;
		}
		if (newText != oldText) {
			String text = getText ();
			String leftText = text.substring (0, start);
			String rightText = text.substring (end, text.length ());
			setText (leftText + newText + rightText, false);
			start += newText.length ();
			setSelection (new Point (start, start));
		}
	}
	/*
	* Post the modify event so that the character will be inserted
	* into the widget when the modify event is delivered.  Normally,
	* modify events are sent but it is safe to post the event here
	* because this method is called from the event loop.
	*/
	postEvent (SWT.Modify);
	return newText == oldText;
}

void setBackground (int control, float [] color) {
	if ((style & SWT.READ_ONLY) == 0) {
		if (color == null) color = defaultBackground ().handle;
	}
	super.setBackground (control, color);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	if ((style & SWT.READ_ONLY) != 0) {
		return super.setBounds (x, y, width, height, move, resize, events);
	}
	/*
	* Bug in the Macintosh.  When the caret is moved,
	* the combo widget scrolls to show the new location.
	* This means that the combo widget may be scrolled
	* to the left in order to show the caret when the
	* widget is not large enough to show both the caret
	* location and all the text.  Unfortunately, when
	* the widget is resized such that all the text and
	* the caret could be visible, the Macintosh does not
	* scroll the widget back.  The fix is to save the 
	* current selection, reset the text and then restore
	* the selection.  This will cause the widget
	* to recompute the left scroll position.
	*/
	Rect inset = getInset ();
	Rectangle rect = getBounds ();
	int oldWidth = rect.width - inset.left - inset.right;
	int result = super.setBounds (x, y, width, height, move, resize, events);
	if (oldWidth == 0 && width > 0) {
		Point selection = getSelection ();
		setText (getText (), false);
		setSelection (selection);
	}
	return result;
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
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
		/*
		* Feature in the Macintosh.  Setting text that starts with "-" makes the
		* menu item a separator.  The fix is to clear the separator attribute. 
		*/
		if (string.startsWith ("-")) {
			OS.ChangeMenuItemAttributes (menuHandle, (short)(index+1), 0, OS.kMenuItemAttrSeparator);
		}
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if an item in the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	removeAll();
	if (items.length == 0) return;
	for (int i= 0; i < items.length; i++) {
		String string = items[i];
		char [] buffer = new char [string.length ()];
		string.getChars (0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
		int result;
		if ((style & SWT.READ_ONLY) != 0) {
			result = OS.AppendMenuItemTextWithCFString (menuHandle, ptr, 0, 0, null);
			/*
			* Feature in the Macintosh.  Setting text that starts with "-" makes the
			* menu item a separator.  The fix is to clear the separator attribute. 
			*/
			if (string.startsWith ("-")) {
				OS.ChangeMenuItemAttributes (menuHandle, (short)(i + 1), 0, OS.kMenuItemAttrSeparator);
			}
		} else {
			int [] outIndex = new int[1];
			result = OS.HIComboBoxAppendTextItem (handle, ptr, outIndex);
		}
		OS.CFRelease(ptr);
		if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_ADDED);
	}
}

/**
 * Marks the receiver's list as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public void setListVisible (boolean visible) {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) {
		if (visible) {
			OS.HIViewSimulateClick (handle, (short) 0, 0, null);
		} else {
			OS.CancelMenuTracking (menuHandle, true, 0);
		}
	} else {
 		if (OS.VERSION >= 0x1040) {
 			OS.HIComboBoxSetListVisible (handle, visible);
 		}
	}
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 *
 * @param orientation new orientation style
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
 * @param selection a point representing the new selection start and end
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
	if ((style & SWT.READ_ONLY) == 0) {
		int length = getCharCount ();
		int start = selection.x, end = selection.y;
		ControlEditTextSelectionRec sel = new ControlEditTextSelectionRec ();
		sel.selStart = (short) Math.min (Math.max (Math.min (start, end), 0), length);
		sel.selEnd = (short) Math.min (Math.max (Math.max (start, end), 0), length);
		if (hasFocus ()) {
			OS.SetControlData (handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, 4, sel);
		} else {
			this.selection = sel;
		}
	}
}

/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * This call is ignored when the receiver is read only and 
 * the given string is not in the receiver's list.
 * </p>
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to 
 * display incorrectly.
 * </p>
 *
 * @param string the new text
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
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	setText (string, true);
}

void setText (String string, boolean notify) {
	if (notify) {
		if (hooks (SWT.Verify) || filters (SWT.Verify)) {
			string = verifyText (string, 0, getCharCount (), null);
			if (string == null) return;
		}
	}
	if ((style & SWT.READ_ONLY) != 0) {
		int index = indexOf (string);
		if (index != -1 && index != getSelectionIndex ()) {
			select (index);
			if (notify) sendEvent (SWT.Modify);
		}
	} else {
		char [] buffer = new char [Math.min(string.length (), textLimit)];
		string.getChars (0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		lastText = string;
		OS.SetControlData (handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, new int[] {ptr});
		OS.CFRelease (ptr);
		selection = null;
		if (notify) sendEvent (SWT.Modify);
	}
}

/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 * <p>
 * To reset this value to the default, use <code>setTextLimit(Combo.LIMIT)</code>.
 * Specifying a limit value larger than <code>Combo.LIMIT</code> sets the
 * receiver's limit to <code>Combo.LIMIT</code>.
 * </p>
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #LIMIT
 */
public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	textLimit = limit;
}

/**
 * Sets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @param count the new number of items to be visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setVisibleItemCount (int count) {
	checkWidget ();
	if (count < 0) return;
	if ((style & SWT.READ_ONLY) != 0) {
		//TODO 
	} else {
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kHIComboBoxNumVisibleItemsTag, 4, new int[] {count});
	}
}

String verifyText (String string, int start, int end, Event keyEvent) {
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	if (keyEvent != null) {
		event.character = keyEvent.character;
		event.keyCode = keyEvent.keyCode;
		event.stateMask = keyEvent.stateMask;
	}
	/*
	 * It is possible (but unlikely), that application
	 * code could have disposed the widget in the verify
	 * event.  If this happens, answer null to cancel
	 * the operation.
	 */
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

}
