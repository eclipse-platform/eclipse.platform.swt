package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.OS;
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
public /*final*/ class Combo extends Composite {

	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 */
	public static int LIMIT;
	private static int fgCommandID= 6000;
	private static final int FOCUS_BORDER= 3;
	private static final int MARGIN= 2;

	private int menuHandle;
	private int textLimit= LIMIT;
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
	}

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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	int sHandle= 0;
	try {
		sHandle= OS.CFStringCreateWithCharacters(string);
		if (menuHandle != 0) {
			if (OS.AppendMenuItemTextWithCFString(menuHandle, sHandle, 0, fgCommandID++, null) != OS.noErr)
				error (SWT.ERROR_ITEM_NOT_ADDED);
			OS.SetControl32BitMaximum(handle, OS.CountMenuItems(menuHandle));	
		} else {
			if (OS.HIComboBoxAppendTextItem(handle, sHandle, null) != OS.noErr)
				error (SWT.ERROR_ITEM_NOT_ADDED);
		}
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);

	int sHandle= 0;
	try {
		sHandle= OS.CFStringCreateWithCharacters(string);
		if (menuHandle != 0) {
			if (OS.InsertMenuItemTextWithCFString(menuHandle, sHandle, (short)index, 0, fgCommandID++) != OS.noErr)
				error (SWT.ERROR_ITEM_NOT_ADDED);
			OS.SetControl32BitMaximum(handle, OS.CountMenuItems(menuHandle));	
		} else {
			OS.HIComboBoxInsertTextItemAtIndex(handle, index, sHandle);
		}
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
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

	// AW only READ_ONLY is implemented
	style &= ~SWT.DROP_DOWN;
	style &= ~SWT.SIMPLE;
	// AW
	
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
	if (menuHandle == 0)
		OS.SetControlData(handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, 4, new short[] { 0, 0 });
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
    /* AW
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
    */
		
	int width = wHint;
	int height = hHint;
	
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		
		Point e= MacUtil.computeSize(handle);
		if (wHint == SWT.DEFAULT)
			width= e.x;
		if (hHint == SWT.DEFAULT)
			height= e.y;
	}
	
	width= 150;
	height--;
	
//	width += 2*MARGIN;
//	height += 2*MARGIN;
//	if ((style & SWT.BORDER) != 0) {
		width += 2*FOCUS_BORDER;
		height += 2*FOCUS_BORDER;
//	}
	
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
	selectionToClipboard();
}
void createHandle (int index) {
	state |= HANDLE;
	if ((style & SWT.READ_ONLY) != 0) {
		handle= OS.NewControl(0, new Rect(), null, false, (short)0, (short)-12345, (short)-1, (short)(OS.kControlPopupButtonProc+1), 0);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		MacUtil.insertControl(handle, parent.handle, -1);
		int[] menuRef= new int[1];
		OS.CreateNewMenu(20000, 0, menuRef);
		menuHandle= menuRef[0];
		if (menuHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.SetControlPopupMenuHandle(handle, menuHandle);
	} else {
	    int[] outComboBox= new int[1];
	    OS.HIComboBoxCreate(new CGRect(), 0, null, 0, OS.kHIComboBoxAutoSizeListAttribute, outComboBox);
		handle= outComboBox[0];
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		MacUtil.insertControl(handle, parent.handle, -1);
	}
	OS.HIViewSetVisible(handle, true);
}
/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void cut () {
	checkWidget ();
	selectionToClipboard();
	_replaceTextSelection("");
}
/* AW
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	int [] argList = {
		OS.XmNlist, 0,
		OS.XmNtextField, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	enableHandle (enabled, argList [1]);
	enableHandle (enabled, argList [3]);
}
*/
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
	checkWidget();
	if (index == -1) return;
    /* AW
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
    */
	System.out.println("Combo.deselect: nyi");
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
	checkWidget();
    /* AW
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (argList[1], new byte[1]);
	OS.XmTextSetInsertionPosition (argList[1], 0);
	display.setWarnings(warnings);
	OS.XmListDeselectAllItems (argList[3]);
    */
	System.out.println("Combo.deselectAll: nyi");
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
	checkWidget();
	return _getItem(index);
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
	checkWidget();
	return _getItemCount();
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
	checkWidget();
    /* AW
	int [] listHandleArgs = {OS.XmNlist, 0};
	OS.XtGetValues (handle, listHandleArgs, listHandleArgs.length / 2);
	int [] argList = {OS.XmNlistSpacing, 0, OS.XmNhighlightThickness, 0};
	OS.XtGetValues (listHandleArgs[1], argList, argList.length / 2);
	int spacing = argList [1], highlight = argList [3];
    */
	/* Result is from empirical analysis on Linux and AIX */
    /* AW
	return getFontHeight () + spacing + (2 * highlight);
    */
    return MacUtil.computeSize(handle).y;
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
	checkWidget();
	int itemCount= _getItemCount();
	String[] result= new String [itemCount];
	for (int i= 0; i < itemCount; i++)
		result[i]= _getItem(i);
	return result;
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
	checkWidget();
 	Point selection= new Point(0, 0);
	if (menuHandle == 0) {
		short[] s= new short[2];
		OS.GetControlData(handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, s.length*2, s, null);
		selection.x= (short) s[0];
		selection.y= (short) s[1];
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
	checkWidget();
	if (menuHandle != 0)
    	return OS.GetControlValue(handle)-1;
    return indexOf(getText());
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
	checkWidget();
	if (menuHandle != 0) {
		int index= getSelectionIndex();
		if (index >= 0)
			return _getItem(index);
		return "";
	}
	int[] t= new int[1];
	OS.GetControlData(handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, t.length*4, t, null);
	return MacUtil.getStringAndRelease(t[0]);
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
	if ((style & SWT.DROP_DOWN) != 0) {
		/*
		* Bug in MOTIF.  For some reason, XtQueryGeometry ()
		* returns the wrong height when the combo is not realized.
		* The fix is to force the combo to be realized by forcing
		* the shell to be realized.
		*/
        /* AW
		if (!OS.XtIsRealized (handle)) getShell ().realizeWidget ();
		XtWidgetGeometry result = new XtWidgetGeometry ();
		result.request_mode = OS.CWHeight;
		OS.XtQueryGeometry (handle, null, result);
		return result.height;
        */
        return 26;
	} else {
		/* Calculate text field height. */
        /* AW
		int [] argList = {OS.XmNtextField, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int [] argList2 = {OS.XmNmarginHeight, 0};
		OS.XtGetValues (argList[1], argList2, argList2.length / 2);
		int height = getFontHeight ();
		XRectangle rect = new XRectangle ();
		OS.XmWidgetGetDisplayRect (argList[1], rect);
		height += (rect.y * 2) + (2 * argList2[1]);
        */

		/* Add in combo box margins. */
        /* AW
		int [] argList3 = {OS.XmNmarginHeight, 0, OS.XmNshadowThickness, 0, OS.XmNhighlightThickness, 0};
		OS.XtGetValues(handle, argList3, argList3.length / 2);
		height += (2 * argList3[1]) + (2 * argList3[3]) + (2 * argList3[5]);

		return height;
        */
        return 26;
	}
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
    return textLimit;
}
void hookEvents () {
	super.hookEvents ();
    /* AW
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNselectionCallback, windowProc, SWT.Selection);
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XtAddCallback (argList[1], OS.XmNactivateCallback, windowProc, SWT.DefaultSelection);
	OS.XtAddCallback (argList[1], OS.XmNvalueChangedCallback, windowProc, SWT.Modify);
    */
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int itemCount= _getItemCount();
	for (int i= 0; i < itemCount; i++) {
		String s= _getItem(i);
		if (s != null && string.equals(s))
			return i;
	}
	return -1;
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
	int itemCount= _getItemCount();
	if (!((0 <= start) && (start < itemCount))) return -1;
	for (int i= start; i < itemCount; i++) {
		String s= _getItem(i);
		if (string.equals(s))
			return i;
	}
	return -1;
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
	if (menuHandle == 0) {
		Clipboard clipboard= new Clipboard(getDisplay());
		TextTransfer textTransfer= TextTransfer.getInstance();
		String clipBoard= (String)clipboard.getContents(textTransfer);
		clipboard.dispose();
		_replaceTextSelection(clipBoard);
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
	checkWidget();
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int itemCount= _getItemCount();
	if (!(0 <= index && index < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
   	if (menuHandle != 0) {
		OS.DeleteMenuItems(menuHandle, (short)(index+1), 1);
		OS.SetControl32BitMaximum(handle, OS.CountMenuItems(menuHandle));
   	} else {
   		OS.HIComboBoxRemoveItemAtIndex(handle, index);
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
	int itemCount= _getItemCount();
	if (!(0 <= start && start < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int newEnd = Math.min (end, itemCount - 1);
	if (menuHandle != 0) {
		OS.DeleteMenuItems(menuHandle, (short)(start+1), newEnd-start+1);
		OS.SetControl32BitMaximum(handle, OS.CountMenuItems(menuHandle));
	} else {
		for (int i= end; i >= start; i--)
  			OS.HIComboBoxRemoveItemAtIndex(handle, i);
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int itemCount= _getItemCount();
	for (int i= 0; i < itemCount; i++) {
		String s= _getItem(i);
		if (s != null && string.equals(s)) {
			remove(i);
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
	checkWidget();
	int itemCount= _getItemCount();
	if (itemCount > 0) {
		if (menuHandle != 0) {
			OS.DeleteMenuItems(menuHandle, (short)1, itemCount);
			OS.SetControl32BitMaximum(handle, OS.CountMenuItems(menuHandle));
		} else {
			for (int i= itemCount-1; i >= 0; i--)
  				OS.HIComboBoxRemoveItemAtIndex(handle, i);
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
	checkWidget();
	
	int itemCount= _getItemCount();
	if (!(0 <= index && index < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	
	if (menuHandle != 0)
		OS.SetControl32BitValue(handle, index+1);
	else {
		String string= _getItem(index);
		_setText(string);
		_selectAll();
		//sendEvent(SWT.Modify);
	}
}
/**
* Sets the widget bounds.
*/
public void setBounds (int x, int y, int width, int height) {
	checkWidget();
	int newHeight = ((style & SWT.DROP_DOWN) != 0) ? getTextHeight() : height;
	super.setBounds (x, y, width, newHeight);
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
    /* AW
	int [] argList = {OS.XmNlist, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [3])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	boolean isSelected = OS.XmListPosSelected (argList[1], index + 1);
	OS.XmListReplaceItemsPosUnselected (argList[1], new int [] {xmString}, 1, index + 1);
	if (isSelected) OS.XmListSelectPos (argList[1], index + 1, false);
	OS.XmStringFree (xmString);
    */
	int sHandle= 0;
	try {
		sHandle= OS.CFStringCreateWithCharacters(string);
		if (menuHandle != 0) {
			if (OS.SetMenuItemTextWithCFString(menuHandle, (short)(index+1), sHandle) != OS.noErr)
				error (SWT.ERROR_ITEM_NOT_ADDED);
		} else {
			OS.HIComboBoxInsertTextItemAtIndex(handle, index, sHandle);
			OS.HIComboBoxRemoveItemAtIndex(handle, index+1);
		}
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
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

	if (items.length == 0) {
		removeAll();
		return;
	}

    /* AW
	int index = 0;
	int [] table = new int [items.length];
	String codePage = getCodePage ();
	while (index < items.length) {
		String string = items [index];
		if (string == null) break;
		byte [] buffer = Converter.wcsToMbcs (codePage, encodeString(string), true);
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
    */
	
	if (menuHandle != 0) {
		for (int i= 0; i < items.length; i++) {
			String string= items[i];
			if (string == null)
				break;
			int sHandle= 0;
			try {
				sHandle= OS.CFStringCreateWithCharacters(string);
				if (OS.AppendMenuItemTextWithCFString(menuHandle, sHandle, 0, fgCommandID++, null) != OS.noErr)
					error (SWT.ERROR_ITEM_NOT_ADDED);
			} finally {
				if (sHandle != 0)
					OS.CFRelease(sHandle);
			}
		}
		OS.SetControl32BitMaximum(handle, items.length);
	} else {
		removeAll();
		for (int i= 0; i < items.length; i++) {
			String string= items[i];
			if (string == null)
				break;
			int sHandle= 0;
			try {
				sHandle= OS.CFStringCreateWithCharacters(string);
				if (OS.HIComboBoxAppendTextItem(handle, sHandle, null) != OS.noErr)
					error (SWT.ERROR_ITEM_NOT_ADDED);
			} finally {
				if (sHandle != 0)
					OS.CFRelease(sHandle);
			}
		}
	}
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
	checkWidget();
	if (menuHandle == 0) {
		short[] s= new short[] { (short)selection.x, (short)selection.y };
		OS.SetControlData(handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, s.length*2, s);
	}
}
/**
* Sets the widget size.
*/
public void setSize (int width, int height) {
	checkWidget();
	int newHeight = ((style & SWT.DROP_DOWN) != 0) ? getTextHeight () : height;
	super.setSize (width, newHeight);
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);

	int index= indexOf (string);
	if (index != -1) {
		select(index);
	} else {
		if ((style & SWT.READ_ONLY) == 0) {
			int sHandle= 0;
			try {
				sHandle= OS.CFStringCreateWithCharacters(string);
				OS.SetControlData(handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, new int[]{sHandle});
			} finally {
				if (sHandle != 0)
					OS.CFRelease(sHandle);
			}
			sendEvent(SWT.Modify);
		}
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
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	textLimit= limit;
}

////////////////////////////////////////////////////////
// Mac stuff
////////////////////////////////////////////////////////

	private void _setText (String string) {
		if ((style & SWT.READ_ONLY) == 0) {
			int sHandle= 0;
			try {
				sHandle= OS.CFStringCreateWithCharacters(string);
				OS.SetControlData(handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, new int[]{sHandle});
			} finally {
				if (sHandle != 0)
					OS.CFRelease(sHandle);
			}
			sendEvent(SWT.Modify);
		}
	}

	private int _getItemCount () {
		if (menuHandle != 0)
			return OS.CountMenuItems(menuHandle);
		return OS.HIComboBoxGetItemCount(handle);
	}
	
	private String _getItem (int index) {
		int itemCount= _getItemCount();
		if (!(0 <= index && index < itemCount)) {
			error (SWT.ERROR_INVALID_RANGE);
		}
		int[] sHandle= new int[1];
		int rc;
		if (menuHandle != 0)
			rc= OS.CopyMenuItemTextAsCFString(menuHandle, (short)(index+1), sHandle);
		else
			rc= OS.HIComboBoxCopyTextItemAtIndex(handle, index, sHandle);
		if (rc != OS.noErr)
			error(SWT.ERROR_CANNOT_GET_ITEM);
		return MacUtil.getStringAndRelease(sHandle[0]);
	}

	/**
	 * Overridden from Control.
	 * x and y are relative to window!
	 */
	void handleResize(int hndl, Rect bounds) {
		bounds.left+= FOCUS_BORDER;
		bounds.top+= FOCUS_BORDER;
		bounds.right-= FOCUS_BORDER;
		bounds.bottom-= FOCUS_BORDER;
		super.handleResize(hndl, bounds);
	}
	
	void internalGetControlBounds(int hndl, Rect bounds) {
		super.internalGetControlBounds(hndl, bounds);
		bounds.left+= -FOCUS_BORDER;
		bounds.top+= -FOCUS_BORDER;
		bounds.right-= -FOCUS_BORDER;
		bounds.bottom-= -FOCUS_BORDER;
	}

	private void _selectAll() {
		String s= getText();
		short[] selection= new short[] { 0, (short) s.length() };
		OS.SetControlData(handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, selection.length*2, selection);
	}
	
	int sendKeyEvent (int type, MacEvent mEvent, Event event) {
		
		/* AW: other platforms call super
		LRESULT result = super.WM_CHAR (wParam, lParam);
		if (result != null) return result;
		*/
		
//		if (translateTraversal(mEvent))
//			return 0;
			
		int kind= mEvent.getKind();
		int mcc= mEvent.getMacCharCodes();
		int code= mEvent.getKeyCode();

		// return key -> DefaultSelection
		if (mcc == SWT.CR) {
			if (kind == OS.kEventRawKeyDown)
				postEvent (SWT.DefaultSelection);
			return OS.noErr;
		}
				
		if ((mEvent.getModifiers() & OS.cmdKey) != 0) {
			switch (code) {
			case 0:	// select all
				if (kind == OS.kEventRawKeyDown)
					_selectAll();
				return OS.noErr;
			case 7:
				if (kind == OS.kEventRawKeyDown)
					cut();
				return OS.noErr;
			case 8:
				if (kind == OS.kEventRawKeyDown)
					copy();
				return OS.noErr;
			case 9:
				if (kind == OS.kEventRawKeyDown || kind == OS.kEventRawKeyRepeat)
					paste();
				return OS.noErr;
			default:
				break;
			}
		}

		String oldText= getText();

		int status= OS.CallNextEventHandler(mEvent.getNextHandler(), mEvent.getEventRef());
		
		if (kind == OS.kEventRawKeyDown) {
			String newText= getText();
			if (!oldText.equals(newText))
				sendEvent (SWT.Modify);
		}
		
		return status;
	}
	
	private void selectionToClipboard() {
		short[] s= new short[2];
		OS.GetControlData(handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, s.length*2, s, null);
		if (s[0] != s[1]) {
			int[] t= new int[1];
			OS.GetControlData(handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, t.length*4, t, null);
			String txt= MacUtil.getStringAndRelease(t[0]);
			txt= txt.substring(s[0], s[1]);
	
			Clipboard clipboard= new Clipboard(getDisplay());
			clipboard.setContents(new Object[] { txt }, new Transfer[]{ TextTransfer.getInstance() });
			clipboard.dispose();
		}
	}
	
	/**
	 * Replace current text selection with given string.
	 * If selection is empty, inserts string.
	 * If string is empty, selection is deleted.
	 */
	private void _replaceTextSelection(String newText) {
		
		short[] s= new short[2];
		OS.GetControlData(handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, s.length*2, s, null);
		
		boolean selEmpty= s[0] == s[1];
		if (newText.length() == 0 && selEmpty)
			return;
		
		int[] t= new int[1];
		OS.GetControlData(handle, (short)OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, t.length*4, t, null);
		String txt= MacUtil.getStringAndRelease(t[0]);
		
		String pre= "";
		if (selEmpty)
			pre= txt.substring(0, s[0]);
		else if (s[0] > 0)
			pre= txt.substring(0, s[0]-1);
			
		String post= txt.substring(s[1]);
		
		int sHandle= 0;
		try {
			sHandle= OS.CFStringCreateWithCharacters(pre + newText + post);
			OS.SetControlData(handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextCFStringTag, 4, new int[]{sHandle});
		} finally {
			if (sHandle != 0)
				OS.CFRelease(sHandle);
		}
		
		s[0]= s[1]= (short)(pre.length() + newText.length());
		OS.SetControlData(handle, OS.kHIComboBoxEditTextPart, OS.kControlEditTextSelectionTag, s.length*2, s);
	
		sendEvent(SWT.Modify);
	}
}
